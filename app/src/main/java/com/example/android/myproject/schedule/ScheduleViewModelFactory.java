package com.example.android.myproject.schedule;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.myproject.database.ScheduleDao;

class ScheduleViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ScheduleDao scheduleDao;


    ScheduleViewModelFactory(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    @NonNull
    public <T extends ViewModel> T create (@NonNull Class<T> modelClass) {
        if (modelClass == ScheduleViewModel.class) {
            return (T) new ScheduleViewModel(scheduleDao);
        }
        return null;
    }

}
