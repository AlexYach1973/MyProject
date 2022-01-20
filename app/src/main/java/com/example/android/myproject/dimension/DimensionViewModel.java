package com.example.android.myproject.dimension;

import android.util.Log;

import androidx.lifecycle.LiveData;
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

    /**
     * LiveData- переменные
//     */
//    private MutableLiveData<Integer> selectedDay = new MutableLiveData<>(1);
//    public LiveData<Integer> getSelectedDay() { return selectedDay; } // getter
//    public void setSelectedDay(int day) {                              // setter
//        selectedDay.setValue(day);
//    }




    //    private LiveData<Integer> selectedMinute = new MutableLiveData<>();
//    public LiveData<Integer> getSelectedMinute() {
//        return selectedMinute;
//    }



}