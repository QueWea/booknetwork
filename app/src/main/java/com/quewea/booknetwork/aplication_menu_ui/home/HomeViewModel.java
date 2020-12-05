package com.quewea.booknetwork.aplication_menu_ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;

import com.quewea.booknetwork.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.menu_home));
    }

    public HomeViewModel() {
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() { return titleText; }
}