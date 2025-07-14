package com.marco.turistafelicetest.Wikipedia;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WikiViewModel extends ViewModel {

    private MutableLiveData<Page> info;

    public MutableLiveData<Page> getInfo(String name) {
        info = new MutableLiveData<>();
        WikiRepository.getInstance().getInformation(info,name);
        return info;
    }

    public void setInfo(MutableLiveData<Page> info) {
        this.info = info;
    }
}

