package com.example.android.myproject;

import android.widget.Button;

import androidx.databinding.BindingAdapter;

public class DataBindingAdapter {

    @BindingAdapter({"app:visibleDelete", "app:currentId"})
    public static void setTextButton(Button view, Boolean bool, long id) {
        if (bool && id != 0) {
            String str = "Удалить id= " + id;
            view.setText(str);
        } else {
            view.setText(R.string.button_delete);
        }
    }

}
