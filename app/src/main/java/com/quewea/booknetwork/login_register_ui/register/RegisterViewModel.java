package com.quewea.booknetwork.login_register_ui.register;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quewea.booknetwork.R;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.register));
    }

    public RegisterViewModel() {
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() { return titleText; }
}