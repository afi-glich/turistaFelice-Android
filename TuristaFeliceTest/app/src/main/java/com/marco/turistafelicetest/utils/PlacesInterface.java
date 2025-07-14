package com.marco.turistafelicetest.utils;

import com.marco.turistafelicetest.models.PlacesApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PlacesInterface {
    @GET("json")
    Call<PlacesApiResponse> getPlacesAround(@Query("location") String location,
                                      @Query("radius") String radius,
                                      @Query("type") String type,
                                      @Query("language") String lang,
                                      @Query("key") String key);
    @GET("json")
    Call<PlacesApiResponse> getPlacesToken(@Query("key") String key,
                                            @Query("pagetoken") String token);
    @GET("json")
    Call<PlacesApiResponse> getPlacesWithoutType(@Query("key") String key,
                                            @Query("location") String location,
                                            @Query("radius") String radius);
}
