package com.example.rt1.ui.myself;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyselfViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyselfViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myself fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}