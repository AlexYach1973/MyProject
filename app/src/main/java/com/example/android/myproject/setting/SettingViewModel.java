package com.example.android.myproject.setting;

import androidx.lifecycle.ViewModel;

public class SettingViewModel extends ViewModel {

    // EditText полe - сообщениe
    private String editTextNotification = "";
    public String getEditTextNotification() {
        return editTextNotification;
    }
    public void setEditTextNotification(String str) { editTextNotification = str; }



}