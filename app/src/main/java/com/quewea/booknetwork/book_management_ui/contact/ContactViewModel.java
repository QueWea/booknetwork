package com.quewea.booknetwork.book_management_ui.contact;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quewea.booknetwork.R;

public class ContactViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.book_management_contact));
    }

    public ContactViewModel() {
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() { return titleText; }
}