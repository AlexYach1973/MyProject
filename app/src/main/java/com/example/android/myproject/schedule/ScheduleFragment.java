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
import androidx.databinding.DataBindingUtil;
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

        // Инициализируем BroadcastReceiver
        receiver = new ScheduleReceiver();

        // Владелец жизненого цикла
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

        // Привязка schedule_fragment.XML с ViewModel
        binding.setViewModel(scheduleViewModel);

        RecyclerView recyclerView = binding.recyclerviewSchedule;

        /** Adapter */

        // Определяем слушателя нажатия элемента в списке
        ScheduleAdapter.OnStateClickListener stateClickListener =
                new ScheduleAdapter.OnStateClickListener() {
                    @Override
                    public void onStateClick(ScheduleEntity scheduleEntity, int position) {

//                        Toast.makeText(application, "Был выбран пункт "
//                                + scheduleEntity.getId(), Toast.LENGTH_SHORT).show();

                        // Делаем видимой кнопку удаления текущего элемента
                        scheduleViewModel.upDate(scheduleEntity);

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

            // Проверяем: есть ли у кого то booleanValue- переменная true,
            // т.е. кого надо удалить?
            isBooleanValueTrue(list);

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

        // Наблюдаем за изменением Boolean- переменной вывода Тостов
        scheduleViewModel.getToastShow().observe(getViewLifecycleOwner(), (num) -> {
            switch (num) {
                case 1:
                    Toast.makeText(application, "В полях ВРЕМЯ должны быть только цифры",
                            Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    Toast.makeText(application, "Время указано неправильно",
                            Toast.LENGTH_SHORT).show();
                    break;

                case 3:
                    Toast.makeText(application, "Заполните все поля",
                            Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    Toast.makeText(application, "Нет выбранных элементов",
                            Toast.LENGTH_SHORT).show();
                    break;

                case 5:
                    Toast.makeText(application, "Не забудьте перезапустить уведомления",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

            // Обнулили - получили Бесконечній цикл
//            scheduleViewModel.setToastShow(0);
        });


        // Разрешает использовать свое меню
        setHasOptionsMenu(true);

        return view;
    }

    // Переводим время из строк в целые числа для Alarm
    private void listStringToInteger(List<String> timeList) {
        for (String str : timeList) {

            timeListHour.add(Integer.parseInt(str.substring(0, str.indexOf(":"))));
            timeListMinute.add(Integer.parseInt(str.substring(str.indexOf(":") + 1)));
        }
    }

    /**
     * BroadcastReceiver
     */
    private void startReceiver() {
        receiver.cancelAlarm(application);

        receiver.setAlarm(application, timeListHour, timeListMinute, typeList);
    }

    /**
     * Menu
     */

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

                // Удалить выбранные эл-ты
            case R.id.menu_delete_one:
                scheduleViewModel.deleteOneElement();
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

        if (scheduleViewModel.getVisibleInsert().getValue() != null) {

            if (scheduleViewModel.getVisibleInsert().getValue()) {
                item.setIcon(R.drawable.insert_true);
                // Меняем расположение кнопки "Перезапустить"
                item_restart.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            } else {
                item.setIcon(R.drawable.insert_24);
                // Меняем расположение кнопки "Перезапустить"
                item_restart.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }
        }
    }

    // Ищем записи со boolean-значением true, которые для удаления
    private void isBooleanValueTrue(List<ScheduleEntity> list){

        // Нашли пункт меню "Удаление элементов"
        MenuItem item_delete_one = menu.findItem(R.id.menu_delete_one);

        for (ScheduleEntity sch : list) {
            if (sch.getBooleanValue()) {
                // Выводим кнопку "Удаление элементов" из меню на панель
                item_delete_one.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

                // Достаточно одного элемента с sch.getBooleanValue() == true,
                // выходим из функции
                return;

            } else {
                // Прячем кнопку "Удаление элементов" назад в меню
                item_delete_one.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }

        }
    }


}

