package com.quewea.booknetwork.aplication_menu_ui.myPublications;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quewea.booknetwork.R;

public class MyPublicationsViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.menu_my_publications));
    }

    public MyPublicationsViewModel(){
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() {
        return titleText;
    }
}