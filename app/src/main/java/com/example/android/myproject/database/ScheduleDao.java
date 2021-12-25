package com.example.android.myproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScheduleDao {

    // Читать все
    @Query("SELECT * FROM injection_table")
    LiveData<List<ScheduleEntity>> getAll();

    // Удалить ВСЕ
    @Query("DELETE FROM injection_table")
    void deleteAll();

    // Вставить 1 элемент
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInjection(ScheduleEntity scheduleEntity);

    /*// Читать по id
    @Query("SELECT * FROM injection_table WHERE id = :currentId")
    LiveData<ScheduleEntity> getById(int currentId);

    // Читать по
    @Query("SELECT * FROM injection_table WHERE type = :str" )
    LiveData<ScheduleEntity> getByTime(String str);



    // Обновить 1 элемент
    @Update
    void updateInjection(ScheduleEntity scheduleEntity);

    // Удалить 1 элемент
    @Delete
    void deleteInjection(ScheduleEntity scheduleEntity);*/

}
