package com.example.android.myproject.dimension;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dimension.db";
    private static final int SCHEMA = 1;

    static final String TABLE = "dimension";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_VALUE = "value";

    // Constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Создание базы данных
        db.execSQL("CREATE TABLE dimension (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " + COLUMN_VALUE + " INTEGER );");
//
//        // Внесение в базу информации для отладки
//        db.execSQL(" INSERT INTO " + TABLE + "(" + COLUMN_DATE + ", " + COLUMN_VALUE
//                + ")  VALUES ('01.01.2022', '3.3'), ('10.01.2022', '6.6');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
