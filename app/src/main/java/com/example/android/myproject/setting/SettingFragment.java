package com.example.android.myproject.setting;

import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.myproject.R;
import com.example.android.myproject.databinding.SettingFragmentBinding;

public class SettingFragment extends Fragment {

    private SettingFragmentBinding binding;

    private Application application;

    private SettingViewModel settingViewModel;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // binding
        binding = SettingFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get Application
        application = requireActivity().getApplication();

        binding.setLifecycleOwner(this);

        // ViewModel
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        // Привязка *.XML с ViewModel
        binding.setViewModel(settingViewModel);





        // Разрешает использовать свое меню
//        setHasOptionsMenu(true);

        return view;
    }

}