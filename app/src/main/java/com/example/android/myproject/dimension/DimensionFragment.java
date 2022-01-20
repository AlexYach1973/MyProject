package com.example.android.myproject.dimension;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.myproject.databinding.DimensionFragmentBinding;

public class DimensionFragment extends Fragment {
    // binding
    private DimensionFragmentBinding binding;
    // Context
    private Application application;
    // ViewModel
    private DimensionViewModel dimensionViewModel;


    public static DimensionFragment newInstance() {
        return new DimensionFragment();
    }

    /**
     * Получение результатов с помощью Fragment Result API
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getSupportFragmentManager().setFragmentResultListener("Key",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                        // Извлекаем данные
                        int day = result.getInt("day");
                        int month = result.getInt("month");
                        int year = result.getInt("year");

                        // Че-то делаем с данными

                        // Формируем дату- типа String
                        String strDate = formDateToString(day, (month + 1), year);

                        // Назначаем дату TextView через ViewModel
                        // Срабатывает только после переворота
                        dimensionViewModel.setSelectedDate(strDate);

                        // Назначаем дату непосредственно в TextView
                        // Что бы сразу видеть
                        binding.textViewDataDimen.setText(strDate);
                    }
                });
    }
    /** ****************************** */


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // binding
        binding = DimensionFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Владелец жизненого цикла
        binding.setLifecycleOwner(this);

        // Get Application
        application = requireActivity().getApplication();

        // ViewModel
        dimensionViewModel = new ViewModelProvider(this).get(DimensionViewModel.class);

        // Привязка dimension_fragment.XML с ViewModel
        binding.setViewModel(dimensionViewModel);

        // Button Add
        binding.buttonAddDimen.setOnClickListener(v -> {
//            showTimePickerDialog();
        });

        // Слушатель поля TextView DATA
        binding.textViewDataDimen.setOnClickListener(v -> {

            // Запускает TimeDialogFragment
            showTimePickerDialog();

        });

        //

        /**
         * Наблюдения
         */
        // Наблюдаем



        // Разрешает использовать свое меню
//        setHasOptionsMenu(true); // Пока не используем
        return view;
    }

    // Запускает TimeDialogFragment
    public void showTimePickerDialog() {
        DialogFragment dataFragment = new TimeDialogFragment();
        dataFragment.show(requireActivity().getSupportFragmentManager(),"Data");
    }

    // Формируем дату- типа String
    private String formDateToString(int day, int month, int year) {
       String dayStr = (day < 10)? "0" + day : String.valueOf(day);
       String monthStr = (month < 10)? "0" + month : String.valueOf(month);

       return dayStr + "." + monthStr + "." + year;
    }

}