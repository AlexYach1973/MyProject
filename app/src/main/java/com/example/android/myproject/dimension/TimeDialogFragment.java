package com.example.android.myproject.dimension;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class TimeDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Используем текущую дату по умолчанию
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int mouth = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Создаем и возвращаем новый экземпляр DatePickerDialog
        return new DatePickerDialog(getActivity(), this, year, mouth, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        /**
         * Получение результатов с помощью Fragment Result API
         */
        Bundle result = new Bundle();
        result.putInt("day", dayOfMonth);
        result.putInt("month", monthOfYear);
        result.putInt("year", year);

        // Отправляем в DimensionFragment с тем-же requestKey
        requireActivity().getSupportFragmentManager().setFragmentResult("Key", result);

        /** ***************** */
    }


}
