package com.marco.turistafelicetest.ui.around;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class AroundViewModel extends ViewModel {

    private MutableLiveData<LatLng> mPos;

    public AroundViewModel() {
        mPos = new MutableLiveData<>();
        mPos.setValue(new LatLng(41.9102415,12.3959117));
    }

    public LiveData<LatLng> getPos() {
        return mPos;
    }

    public void setPos(LatLng nPos){
        this.mPos.setValue(nPos);
    }
}