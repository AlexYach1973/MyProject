package com.example.android.myproject.setting;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.myproject.databinding.SettingFragmentBinding;
import com.example.android.myproject.schedule.ScheduleReceiver;

public class SettingFragment extends Fragment {

    // binding
    private SettingFragmentBinding binding;
    // Context
    private Application application;
    // ViewModel
    private SettingViewModel settingViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Инициализируем BroadcastReceiver
        ScheduleReceiver receiver = new ScheduleReceiver();

        // binding
        binding = SettingFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get Application
        application = requireActivity().getApplication();

        // Владелец жизненого цикла
        binding.setLifecycleOwner(this);

        // ViewModel
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        // Привязка setting_fragment.XML с ViewModel
        binding.setViewModel(settingViewModel);


        /** Наблюдения */
        // Пока нет наблюдений


        // Нажатие на кнопку "ТЕСТ уведомления"
        binding.buttonTestNotification.setOnClickListener(v -> {

            // Передаем создание уведомлений в ScheduleReceiver
            receiver.testTextNotification(application,
                    settingViewModel.getEditTextNotification());
        });

        // Разрешает использовать свое меню
//        setHasOptionsMenu(true); // Пока не используем

        return view;
    }

}