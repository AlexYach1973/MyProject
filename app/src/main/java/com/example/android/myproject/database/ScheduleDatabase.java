package com.example.android.myproject.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ScheduleEntity.class}, version = 1, exportSchema = false)
public abstract class ScheduleDatabase extends RoomDatabase {

    public abstract ScheduleDao scheduleDao();

    private static volatile ScheduleDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ScheduleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ScheduleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ScheduleDatabase.class, "schedule_database")
                            .addCallback(mDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback mDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ScheduleDao dao = INSTANCE.scheduleDao();
//                dao.deleteAll();
            });

        }
    };

}



