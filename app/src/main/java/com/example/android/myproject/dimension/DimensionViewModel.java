package com.example.android.myproject.dimension;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DimensionViewModel extends ViewModel {

    // Дата, которая установлена в TimeDialogFragment
    // selectedDate
    private String selectedDate = "??.??.??";
    public String getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(String strValue) {
        selectedDate = strValue;
    }

    // Id выбраного элемента
    private long currentId = 0;
    public long getCurrentId() { return currentId; } // getter
    public void setCurrentId(long currentId) { this.currentId = currentId; } // setter

    /**
     * LiveData- переменные
     */
    // Весь список
//    private LiveData<Cursor> allList = new MutableLiveData<>();
//    public LiveData<Cursor> getAllList() { return allList; }

//    private MutableLiveData<Integer> selectedDay = new MutableLiveData<>(1);
//    public LiveData<Integer> getSelectedDay() { return selectedDay; } // getter
//    public void setSelectedDay(int day) {                              // setter
//        selectedDay.setValue(day);
//    }

    // Boolean переменная показ/скрыть LinearLayout вставки
    private final MutableLiveData<Boolean> visibleAdd = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> getVisibleAdd() {
        return visibleAdd;
    }

    // Boolean переменная доступности кнопки удаления элемента
    private final MutableLiveData<Boolean> visibleDelete = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> getVisibleDelete() {
        return visibleDelete;
    }


    // Изменение Boolean переменной видимости LinearLayout вставки
    public void inverseVisiblyAdd() {

        if (visibleAdd.getValue() != null) {
            visibleAdd.setValue(!visibleAdd.getValue());
        }
    }

    // Изменение Boolean- переменной доступности кнопки удаления элемента
    public void inverseVisiblyDelete() {

        if (visibleDelete.getValue() != null) {
            visibleDelete.setValue(!visibleDelete.getValue());
        }
    }

    /**
     * Работа с базой данных
     */
    // Чтение всего списка
    public Cursor readAllList(SQLiteDatabase db) {
        return db.rawQuery("select * from " + DataBaseHelper.TABLE, null);
    }

    // Удалить по id
    public int deleteById(SQLiteDatabase db, String id) {

        return db.delete(DataBaseHelper.TABLE,
                "_id = ?",
                new String[] {id});
    }

    // Удалить ВСЕ
    public void deleteAll(SQLiteDatabase db) {
        db.delete(DataBaseHelper.TABLE, null, null);
    }


}