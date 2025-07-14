package com.marco.turistafelicetest.Wikipedia;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.marco.turistafelicetest.utils.Constants;

import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WikiRepository {

    private static WikiRepository instance;
    private WikiService mWikiService;


    private WikiRepository(){
        String language = Locale.getDefault().getDisplayLanguage();
        Retrofit retrofit = null;
        if(language.equals("English"))
        {
            retrofit = new Retrofit.Builder().baseUrl(Constants.WIKI_API_BASE_URL_EN).addConverterFactory(GsonConverterFactory.create()).build();

        }
        else if (language.equals("italiano"))
        {
            retrofit = new Retrofit.Builder().baseUrl(Constants.WIKI_API_BASE_URL_IT).addConverterFactory(GsonConverterFactory.create()).build();
        }
        mWikiService = retrofit.create(WikiService.class);
    }

    public static synchronized WikiRepository getInstance() {
        if (instance == null) {
            instance = new WikiRepository();
        }
        return instance;
    }

    //da implementare
    public void getInformation (MutableLiveData<Page> info, String name)
    {
        Call<Result> call = mWikiService.search(name);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Map<String, Page> pages =  response.body().getQuery().getPages();
                for (String key: pages.keySet()) {
                    Log.d("PlanningActivity", key);
                    Page page = pages.get(key);
                    info.postValue(page);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("PlanningActivity",t.getMessage());
            }
        });
    }

}
