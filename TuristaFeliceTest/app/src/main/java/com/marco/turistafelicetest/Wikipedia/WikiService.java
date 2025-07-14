package com.marco.turistafelicetest.Wikipedia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiService {
    @GET("?format=json&action=query&prop=extracts&exintro=&explaintext=")
    Call<Result> search (@Query("titles") String search);
}
