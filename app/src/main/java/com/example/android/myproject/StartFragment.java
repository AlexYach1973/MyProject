package com.example.android.myproject;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.myproject.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {

    // bining
    FragmentStartBinding binding;
    // Context
    private Application application;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get Application
        application = requireActivity().getApplication();

        /** Слушатели */

        //*****************************//
        // Кнопка График уколов
        binding.buttonStartToSchedule.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_startFragment_to_scheduleFragment));


        //*****************************//
        // Кнопка Измерения сахара
        binding.buttonStartToDimension.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_startFragment_to_dimensionFragment));

        //*****************************//
        // Кнопка Установки
        binding.buttonStartToTools.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_startFragment_to_settingFragment));


        // Разрешает использовать свое меню
//        setHasOptionsMenu(true); // Пока не используем
        return view;
    }
}