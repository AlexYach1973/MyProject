package com.example.android.myproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

    // Обновить 1 элемент
    @Update
    void updateInjection(ScheduleEntity scheduleEntity);

    // Удалить несколько элементов по Id
    @Query("DELETE from injection_table WHERE id IN (:idList)")
    void deleteByIdList(List<Integer> idList);

    /*// Читать по id
    @Query("SELECT * FROM injection_table WHERE id = :currentId")
    LiveData<ScheduleEntity> getById(long currentId);

    // Читать по
    @Query("SELECT * FROM injection_table WHERE type = :str" )
    LiveData<ScheduleEntity> getByTime(String str);*/


}
