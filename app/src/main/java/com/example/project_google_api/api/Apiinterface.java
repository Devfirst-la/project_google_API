package com.example.project_google_api.api;

import com.example.project_google_api.model.news;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apiinterface {

    @GET("top-headlines")
    Call<news> getNews(
            @Query("country") String country ,
            @Query("category") String category ,
            @Query("apiKey") String apiKey

    );
    @GET("everything")
    Call<news> getnewsSearch(
            @Query("q") String keyword,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apikey
    );

}
