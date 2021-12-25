package com.example.android.myproject.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "injection_table")
public class ScheduleEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "amount")
    private int amount;
    @ColumnInfo(name = "description")
    private String description;

    // Constructor
    public ScheduleEntity(String type, String time, int amount, String description) {
        this.type = type;
        this.time = time;
        this.amount = amount;
        this.description = description;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
