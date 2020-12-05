package com.quewea.booknetwork.book_management_ui.updatePublication;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quewea.booknetwork.R;

public class UpdatePublicationViewModel extends ViewModel {

    private MutableLiveData<String> titleText;

    public void resourceTexts(Context c){
        titleText.setValue(c.getString(R.string.book_management_update_publication));
    }

    public UpdatePublicationViewModel() {
        titleText = new MutableLiveData<>();
    }

    public LiveData<String> getTitle() { return titleText; }
}