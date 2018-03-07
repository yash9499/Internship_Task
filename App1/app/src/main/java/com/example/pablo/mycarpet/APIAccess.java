package com.example.pablo.mycarpet;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by pablo on 3/5/18.
 */

public class APIAccess {

    private static final String Url = "http://www.androidbegin.com/tutorial/";
    public static APIService apiService = null;

    public static APIService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

    public interface APIService {
        @GET("jsonparsetutorial.txt")
        Call<CountryPopulationRank> getCountryList();

    }

}
