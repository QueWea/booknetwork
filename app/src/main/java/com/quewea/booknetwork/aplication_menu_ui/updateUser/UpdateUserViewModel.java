package com.quewea.booknetwork.aplication_menu_ui.updateUser;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quewea.booknetwork.R;

public class UpdateUserViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.menu_update_user));
    }

    public UpdateUserViewModel(){
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() {
        return titleText;
    }
}