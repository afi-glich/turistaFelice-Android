package com.marco.turistafelicetest.ui.around;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.marco.turistafelicetest.models.Place;
import com.marco.turistafelicetest.models.Resource;
import com.marco.turistafelicetest.repositories.PlacesRepository;

import java.util.List;

public class PlaceViewModel extends ViewModel {
    private static final String TAG = "PlaceViewModel";

    private MutableLiveData<Resource<List<Place>>> places;
    private boolean isLoading;

    public PlaceViewModel(){
        places = new MutableLiveData<>();
    }

    public LiveData<Resource<List<Place>>> getPlacesResource(LatLng location, String type, String lang) {
        if (places == null) {
            places = new MutableLiveData<>();
            Log.d(TAG, "getPlaces: Download the places from Internet");
            PlacesRepository.getInstance().getPlacesByLocation(places, location, type, lang);
        }else{
            PlacesRepository.getInstance().getPlacesByLocation(places, location, type, lang);
        }

        return places;
    }
    public LiveData<Resource<List<Place>>> getPlacesResourceWithoutType(LatLng location) {
        if (places == null) {
            places = new MutableLiveData<>();
            Log.d(TAG, "getPlaces: Download the places from Internet");
            PlacesRepository.getInstance().getPlacesByLocationWithoutType(places, location);
        }else{
            PlacesRepository.getInstance().getPlacesByLocationWithoutType(places, location);
        }

        return places;
    }
    public LiveData<Resource<List<Place>>> getPlacesResourceByToken(String token) {
        if (places == null) {
            places = new MutableLiveData<>();
            Log.d(TAG, "getPlaces: Download the places from Internet");
            PlacesRepository.getInstance().getPlacesByToken(places, token);
        }else{
            Log.d(TAG, "getPlaces: Download the places from Internet");
            PlacesRepository.getInstance().getPlacesByToken(places, token);
        }

        return places;
    }

    public MutableLiveData<Resource<List<Place>>> getPlacesLiveData() {
        return places;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
