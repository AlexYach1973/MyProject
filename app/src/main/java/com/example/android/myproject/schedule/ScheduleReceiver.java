package com.example.android.myproject.schedule;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.android.myproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleReceiver extends BroadcastReceiver {
    // Для записи сообщения
    private static SharedPreferences sPref;
    // Key
    private static final String TEXT_SAVE_LOAD = "textSaveLoad";
    private static String NAME_FILE = "mySetting";

    // Константа для putExtra.
    private static final String KEY_TIME = "time";
    private static final String KEY_TYPE = "type";

    // Название канала для Notification
    private static final String CHANNEL_ID = "injection";
    // ID уведомления
    private static final int NOTIFY_ID = 1;

    NotificationManager notificationManager;

    // Собираем в список все запущенные уведомления - для отмены
    ArrayList<PendingIntent> intentArray = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {

        // Читаем переданные данные из intent
        String time = intent.getStringExtra(KEY_TIME);
        String type = intent.getStringExtra(KEY_TYPE);

        Toast.makeText(context, "Время " + time + ", Пора делать укол", Toast.LENGTH_SHORT).show();
        Log.d("myLogs", "Время " + time + ", Пора делать укол");

        /**
         * Делаем Notification
         */
        // Регистрируем канал уведомлений- это надо для Android 8.0 и выше
        createNotificationChannel(context);

        // Большая картинка - setLargeIcon()
        Bitmap notifyImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.injection);

        // Читаем текст уведомления из SharedPreferences
        String longText = loadPreference();

        //  Создаем pendingIntent для отработки нажатия на Уведомление
        Intent intentPush = new Intent(); // Пустой интент- ничего не делаем
        PendingIntent pendingIntentPush = PendingIntent.getActivity(context,
                0, intentPush, 0);


        // Создаем уведомление - Builder
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.syringe_r) // маленькая иконка
                        .setLargeIcon(notifyImage) // Большая картинка
                        .setColor(Color.parseColor("#FFE74C3C"))
                        .setContentTitle("Время: " + time + ". Тип иньекции: " + type)
                        .setAutoCancel(true) // Исчезает при нажатии
//                        .setContentText("Время: " + time + ". Тип иньекции: " + type)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
                        .setContentIntent(pendingIntentPush);

        // Создали...
        Notification notification = builder.build();
        // Запустили
        notificationManager.notify(NOTIFY_ID, notification);

    }

    private String loadPreference() {
        return sPref.getString(TEXT_SAVE_LOAD, "");
    }

    // Регистрируем канал уведомлений для Android 8.0 и выше
    private void createNotificationChannel(Context context) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "injection",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager =
                    getSystemService(context, NotificationManager.class);

            assert notificationManager != null; // Не должно быть null
            // Создали канал
            notificationManager.createNotificationChannel(channel);

        }
    }

    /**
     * Методы, которые вызываются из ScheduleFragment
     */
    // Создаем периодические напоминания по Графику.
    // Этод метод вызывается из ScheduleFragment.
    // Передаем сюда часы и минуты, которые записаны в SQLite- базе
    // А также тип укола для вывода разных сообщений
    public void setAlarm(Context context, List<Integer> hours, List<Integer> minutes,
                         List<String> type) {

        AlarmManager alarmMng = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // ПРИМЕР для отладки
       /* Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.putExtra(KEY_TIME, "Сейчас");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMng.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 5000,
                pendingIntent);*/

        // Запускаем серию Напоминаний
        for (int i = 0; i < hours.size(); i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hours.get(i));
            calendar.set(Calendar.MINUTE, minutes.get(i));

            Intent intent = new Intent(context, ScheduleReceiver.class);

            // Переводим минуты в читабельный вид
            String strMinute = minutes.get(i) < 10 ? "0" + minutes.get(i) : "" + minutes.get(i);
            // Передаем время и тип
            intent.putExtra(KEY_TIME, hours.get(i) + ":" + strMinute);
            intent.putExtra(KEY_TYPE, type.get(i));

            Log.d("myLogs", "setAlarm, Time : " + hours.get(i) + ":" + minutes.get(i));

            // Разные PendingIntent - pазные requestCode: "i"
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent,
                    0);
            // Запускаем pазные  алярмы
            alarmMng.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            // Собираем pendingIntent для метода отмены (см. ниже)
            intentArray.add(pendingIntent);
        }

        Log.d("myLogs", " setAlarm. Размер intentArray: " + intentArray.size());

        Toast.makeText(context, "Уведомления перезапущены", Toast.LENGTH_SHORT).show();


    }

    // Отменяем все напоминания
    // Этод метод вызывается из ScheduleFragment.
    public void cancelAlarm(Context context) {

        AlarmManager alarmMng = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (!intentArray.isEmpty()) {

            for (int i = 0; i < intentArray.size(); i++) {
                alarmMng.cancel(intentArray.get(i));

                Log.d("myLogs", "Удален " + i + "-тый pendingIntent");

            }
        } else {
//            Toast.makeText(context, "Alarm пустой", Toast.LENGTH_LONG).show();

            Log.d("myLogs", "cancelAlarm. Alarm пустой");

        }
        intentArray.clear(); // Обнулили
    }

    /**
     * Метод, который вызывается из SettingFragment
     */

    public void testTextNotification(Context context, String textNotification) {
        // Создаем Тестовое уведомление
        testNotification(context);

        // Записываем текст уведомления
        saveTextNotification(context, textNotification);
    }

    // Создаем тестовое уведомление
    private void testNotification(Context context) {
        AlarmManager alarmMng = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Тестовое уведомление
        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.putExtra("time", "Сейчас");
        intent.putExtra("type", "Тест");
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, 0);

        // Запускаем прям сейчас
        alarmMng.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 3000, // через 3 сек.
                pendingIntent);
    }

    // Записываем текст уведомления
    private void saveTextNotification(Context context, String textNotification) {

        // Use SharedPreferences
        sPref = context.getSharedPreferences(NAME_FILE,
                Activity.MODE_PRIVATE);

        // Проверяем поле на пустоту
        if (textNotification.isEmpty()) {
            Toast.makeText(context, "Записываем уведомление по умолчанию",
                    Toast.LENGTH_SHORT).show();
            // Текст по умолчанию
            savePreferences("Через полчаса после укола коротким" +
                    " инсулином необходимо покушать");
        } else {

            // Записываем введенный текст
            savePreferences(textNotification);
        }
    }

    // SharedPreferences
    private void savePreferences(String editTextNotification) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(TEXT_SAVE_LOAD, editTextNotification);
        editor.apply();
    }

}