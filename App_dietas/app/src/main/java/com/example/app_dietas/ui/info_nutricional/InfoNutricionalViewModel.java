package com.example.app_dietas.ui.info_nutricional;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoNutricionalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfoNutricionalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Informacion Nutricional fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}