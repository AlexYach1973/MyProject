package com.example.android.myproject.schedule;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myproject.R;
import com.example.android.myproject.database.ScheduleDao;
import com.example.android.myproject.database.ScheduleDatabase;
import com.example.android.myproject.database.ScheduleEntity;
import com.example.android.myproject.databinding.ScheduleFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {
    // binding
    private ScheduleFragmentBinding binding;
    // Context
    private Application application;
    // ViewModel
    private ScheduleViewModel scheduleViewModel;
    // DataBase
    ScheduleDatabase db;

    // Время из списка - Integer
    List<Integer> timeListHour = new ArrayList<>();
    List<Integer> timeListMinute = new ArrayList<>();
    // Тип из списка
    List<String> typeList = new ArrayList<>();

    // BroadcastReceiver
    ScheduleReceiver receiver;

    // Для изменений кнопок меню (из never -> ifRoom)
    Menu menu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // binding
        binding = ScheduleFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // BroadcastReceiver
        receiver = new ScheduleReceiver();

        binding.setLifecycleOwner(this);

        // Get Application
        application = requireActivity().getApplication();
        // Get database
        db = ScheduleDatabase.getDatabase(application);
        // Get DAO
        ScheduleDao scheduleDao = db.scheduleDao();

        // initialize ViewModel через ViewModelFactory
        scheduleViewModel = new ViewModelProvider(this,
                new ScheduleViewModelFactory(scheduleDao)).get(ScheduleViewModel.class);

        // Привязка *.XML с ViewModel
        binding.setViewModel(scheduleViewModel);

        RecyclerView recyclerView = binding.recyclerviewSchedule;

        // Определяем слушателя нажатия элемента в списке
        ScheduleAdapter.OnStateClickListener stateClickListener =
                new ScheduleAdapter.OnStateClickListener() {
                    @Override
                    public void onStateClick(ScheduleEntity scheduleEntity, int position) {

                        Toast.makeText(application, "Был выбран пункт "
                                        + scheduleEntity.getId(),Toast.LENGTH_SHORT).show();

                    }
                };

        // Adapter
        ScheduleAdapter adapter = new ScheduleAdapter(new ScheduleAdapter.ScheduleDiff(),
                stateClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(application));


        /** Наблюдения */

        // Наблюдаем весь список
        scheduleViewModel.getAllScheduleEntity().observe(getViewLifecycleOwner(), (list) ->
        {
//            Log.d("myLogs", "getAllScheduleEntity().observe. Заметил изменения");

            adapter.submitList(list); // Обновили

            // Записали время
            List<String> timeList = new ArrayList<>();

            for (ScheduleEntity sch : list) {
                timeList.add(sch.getTime());
                // и тип (type) укола
                typeList.add(sch.getType());
            }

            // Переводим время из String в Integer
            listStringToInteger(timeList);

        });

        // Наблюдаем за изменением Boolean- переменной видимости полей вставки
        /*scheduleViewModel.getVisibleInsert().observe(getViewLifecycleOwner(), aBoolean -> {

            if (aBoolean) {
                binding.linearLayoutInsert.setVisibility(View.VISIBLE);

            } else {
                binding.linearLayoutInsert.setVisibility(View.GONE);
            }
        });*/

        // Кнопка Записи новых данных
        binding.buttonSaveSchedule.setOnClickListener(v -> {
            // Запись данных из полей
            addNewInjection();
        });


        // Разрешает использовать свое меню
        setHasOptionsMenu(true);

        return view;
    }

    // Переводим время из строк в целые числа для Alarm
    private void listStringToInteger(List<String> timeList) {
        for (String str : timeList) {
            timeListHour.add(Integer.parseInt(str.substring(0, str.indexOf(":"))));

//            Log.d("myLogs", " time Hour= " +
//                    Integer.parseInt(str.substring(0, str.indexOf(":"))));

            timeListMinute.add(Integer.parseInt(str.substring(str.indexOf(":") + 1)));

//            Log.d("myLogs", " time Minute= " +
//                    Integer.parseInt(str.substring(str.indexOf(":")+1)));
        }

    }

    // Функция Записи новых данных
    private void addNewInjection() {
        // Проверка полей ввода
        if (isEntryValid() && isTimeValid()) {

            // формируем строку Время из часов и минут
            String strTimeBuild = binding.scheduleInsertTimeHour.getText().toString() +
                    ":" + binding.scheduleInsertTimeMinute.getText().toString();

            // Формируем экземпляр ScheduleEntity
            ScheduleEntity schedule = new ScheduleEntity(
                    binding.scheduleInsertType.getText().toString(),
                    strTimeBuild,
                    binding.scheduleInsertAmount.getText().toString(),
                    binding.scheduleInsertDescription.getText().toString()
            );

            // Отправляем в  ViewModel для вставки в БД
            scheduleViewModel.insert(schedule);

            Toast.makeText(application, "Необходимо перезапустить уведомления",
                    Toast.LENGTH_SHORT).show();

            // Очищаем поля вставки
            binding.scheduleInsertType.setText("");
            binding.scheduleInsertTimeHour.setText("");
            binding.scheduleInsertTimeMinute.setText("");
            binding.scheduleInsertAmount.setText("");
            binding.scheduleInsertDescription.setText("");
        }
    }

    // Проверка установки времени: HH:MM
    private boolean isTimeValid() {
        String strTimeHour = binding.scheduleInsertTimeHour.getText().toString();
        String strTimeMinute = binding.scheduleInsertTimeMinute.getText().toString();

        return (isHourAnfMinuteValid(strTimeHour, 4) &&
                isHourAnfMinuteValid(strTimeMinute, 10));
    }
    // Вспомогательная функция установки времени
    private boolean isHourAnfMinuteValid(String strTime, int count) {
        if (strTime.contains(".") || strTime.contains(",") ||
                strTime.contains("-") || strTime.contains(" ")) {

            Toast.makeText(application, "В полях должны быть только цифры",
                    Toast.LENGTH_SHORT).show();
            return false;

        } else if (Integer.parseInt(strTime) < 0 || Integer.parseInt(strTime) > count * 6) {

            Toast.makeText(application, "Неправельные данные",
                    Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    // Проверка на не нулевые поля Записи новых данных (кроме description)
    private Boolean isEntryValid() {
        if (binding.scheduleInsertType.getText().toString().isEmpty() ||
                binding.scheduleInsertTimeHour.getText().toString().isEmpty() ||
                binding.scheduleInsertTimeMinute.getText().toString().isEmpty() ||
                binding.scheduleInsertAmount.getText().toString().isEmpty()
        ) {
            Toast.makeText(application, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /** BroadcastReceiver */
    private void startReceiver() {
        receiver.cancelAlarm(application);

        receiver.setAlarm(application, timeListHour, timeListMinute, typeList);
    }

    /** Menu */

    // Create menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // передаём ссылку на наш объект
        this.menu = menu;
        inflater.inflate(R.menu.schedule_menu, menu);
    }

    // Option menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // Удалить все
            case R.id.menu_delete_all:
                scheduleViewModel.deleteAll();

                // Удалить Базу
//                db.close();

                // Очистить список pendingIntent
                receiver.cancelAlarm(application);
                break;

            // Вставить
            case R.id.menu_insert:
                // Меняем Boolean- значение
                scheduleViewModel.inverseVisiblyInsert();
                // Изменение значка
                iconSwitch(item);
                break;

            // Перезапустить Уведомления
            case R.id.menu_restart:
                startReceiver();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Функция изменения значка
    private void iconSwitch(MenuItem item) {
        // Находим пункт меню RESTART, чтобы отобразить его
        MenuItem item_restart = menu.findItem(R.id.menu_restart);

        if (scheduleViewModel.getVisibleInsert().getValue()) {
            item.setIcon(R.drawable.insert_true);
            item_restart.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            item.setIcon(R.drawable.insert_24);
            item_restart.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
    }

}

