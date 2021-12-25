package com.example.android.myproject.schedule;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.myproject.database.ScheduleDao;
import com.example.android.myproject.database.ScheduleDatabase;
import com.example.android.myproject.database.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleViewModel extends ViewModel {

    private ScheduleDao scheduleDao;


    // Список
    private LiveData<List<ScheduleEntity>> allScheduleEntity = new MutableLiveData<>();
    public LiveData<List<ScheduleEntity>> getAllScheduleEntity() {
        return allScheduleEntity;
    }

    // Boolean переменная показ/скрыть поля вставки
    private MutableLiveData<Boolean> visibleInsert = new MutableLiveData<>();
    public LiveData<Boolean> getVisibleInsert() {
        return visibleInsert;
    }


    // Коонструктор
    public ScheduleViewModel(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
        // Прочитали все
        allScheduleEntity = scheduleDao.getAll();

        // Установили Boolean переменную
        visibleInsert.setValue(false);
    }

    // Изменение видимости поля вставки
    public void inverseVisiblyInsert() {

        if (visibleInsert.getValue() != null) {
            if (visibleInsert.getValue())
                visibleInsert.setValue(false);
            else
                visibleInsert.setValue(true);
        }
    }

    // Вставка в фоновом потоке
    public void insert(ScheduleEntity scheduleEntity) {
        ScheduleDatabase.databaseWriteExecutor.execute(() ->
                scheduleDao.insertInjection(scheduleEntity));
    }

    // Удаление всего в фоновом потоке
    public void deleteAll() {
        ScheduleDatabase.databaseWriteExecutor.execute(() ->
                scheduleDao.deleteAll());
    }


}