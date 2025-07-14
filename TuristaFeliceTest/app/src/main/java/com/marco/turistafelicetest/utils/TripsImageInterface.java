package com.marco.turistafelicetest.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TripsImageInterface {

    @GET("images")
    Call<Object> getCityImage(@Query("q") String cityName,
                              @Query("count") int count,
                              @Query("t") String type,
                              @Query("safesearch") int safeSearch,
                              @Query("uiv") int uiv);

}
