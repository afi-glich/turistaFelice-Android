package com.marco.turistafelicetest.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.marco.turistafelicetest.models.Resource;
import com.google.android.gms.maps.model.LatLng;
import com.marco.turistafelicetest.models.Place;
import com.marco.turistafelicetest.models.PlacesApiResponse;
import com.marco.turistafelicetest.utils.Constants;
import com.marco.turistafelicetest.utils.PlacesInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesRepository {
    private static final String TAG = "PlacesRepository";

    private static PlacesRepository instance;
    private PlacesInterface placesService;

    private PlacesRepository(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.PLACES_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        placesService = retrofit.create(PlacesInterface.class);
    }

    public static synchronized PlacesRepository getInstance() {
        if (instance == null) {
            instance = new PlacesRepository();
        }
        return instance;
    }

    public void getPlacesByLocation(final MutableLiveData<Resource<List<Place>>> placesResource, LatLng location, String type, String lang) {
        Call<PlacesApiResponse> call = placesService.getPlacesAround(location.latitude+","+location.longitude, "1000", type, lang,Constants.PLACES_API_KEY_PLANNING);
        Log.i("PLACEREPOSITORY","Lanciato places with type");
        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<PlacesApiResponse>() {
            @Override
            public void onResponse(Call<PlacesApiResponse> call, Response<PlacesApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("PLACEREPOSITORY", response.body().toString());
                    Resource<List<Place>> resource = new Resource<>();

                    if (placesResource.getValue() != null && placesResource.getValue().getData() != null) {
                        List<Place> currentPlacesList = placesResource.getValue().getData();
                        currentPlacesList.addAll(response.body().getResults());
                        resource.setData(currentPlacesList);
                    } else {
                        resource.setData(response.body().getResults());
                    }

                    resource.setStatusCode(response.code());
                    resource.setNextPageToken(response.body().getNext_page_token());
                    resource.setLoading(false);
                    placesResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<Place>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    placesResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<PlacesApiResponse> call, Throwable t) {
                Resource<List<Place>> resource = new Resource<>();
                placesResource.postValue(resource);
            }
        });
    }
    public void getPlacesByToken(final MutableLiveData<Resource<List<Place>>> placesResource, String token) {
        Call<PlacesApiResponse> call = placesService.getPlacesToken(Constants.PLACES_API_KEY, token);
        Log.i("PLACEREPOSITORY","Lanciato places by token");
        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<PlacesApiResponse>() {
            @Override
            public void onResponse(Call<PlacesApiResponse> call, Response<PlacesApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("PLACEREPOSITORY", response.body().toString());
                    Resource<List<Place>> resource = new Resource<>();

                    if (placesResource.getValue() != null && placesResource.getValue().getData() != null) {
                        List<Place> currentPlacesList = placesResource.getValue().getData();
                        currentPlacesList.addAll(response.body().getResults());
                        resource.setData(currentPlacesList);
                    } else {
                        resource.setData(response.body().getResults());
                    }

                    resource.setStatusCode(response.code());
                    resource.setNextPageToken(response.body().getNext_page_token());
                    resource.setLoading(false);
                    placesResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<Place>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    placesResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<PlacesApiResponse> call, Throwable t) {
                Resource<List<Place>> resource = new Resource<>();
                placesResource.postValue(resource);
            }
        });
    }
    public void getPlacesByLocationWithoutType(final MutableLiveData<Resource<List<Place>>> placesResource, LatLng location) {
        Call<PlacesApiResponse> call = placesService.getPlacesWithoutType(Constants.PLACES_API_KEY_PLANNING, location.latitude + "," + location.longitude, "1000");
        Log.i("PLACEREPOSITORY","Lanciato places by location without type");
        // It shows the use of method enqueue to do the HTTP request asynchronously.
        call.enqueue(new Callback<PlacesApiResponse>() {
            @Override
            public void onResponse(Call<PlacesApiResponse> call, Response<PlacesApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("PLACEREPOSITORY", response.body().toString());
                    Resource<List<Place>> resource = new Resource<>();

                    if (placesResource.getValue() != null && placesResource.getValue().getData() != null) {
                        List<Place> currentPlacesList = placesResource.getValue().getData();
                        currentPlacesList.addAll(response.body().getResults());
                        resource.setData(currentPlacesList);
                    } else {
                        resource.setData(response.body().getResults());
                    }

                    resource.setStatusCode(response.code());
                    resource.setNextPageToken(response.body().getNext_page_token());
                    resource.setLoading(false);
                    placesResource.postValue(resource);
                } else if (response.errorBody() != null) {
                    Resource<List<Place>> resource = new Resource<>();
                    resource.setStatusCode(response.code());
                    placesResource.postValue(resource);
                }
            }

            @Override
            public void onFailure(Call<PlacesApiResponse> call, Throwable t) {
                Log.i("PLACEREPOSITORY", t.getMessage());
                Resource<List<Place>> resource = new Resource<>();
                placesResource.postValue(resource);
            }
        });
    }
}
