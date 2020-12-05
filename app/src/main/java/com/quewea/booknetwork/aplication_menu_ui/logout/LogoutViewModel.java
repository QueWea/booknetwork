package com.quewea.booknetwork.aplication_menu_ui.logout;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quewea.booknetwork.R;
import com.quewea.booknetwork.login_register;
import com.quewea.booknetwork.book_management;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.menu_log_out));
    }

    public LogoutViewModel(){
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() {
        return titleText;
    }
}