package com.example.mynews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // GNews 'search' is more reliable than 'top-headlines' for some countries
    @GET("search")
    Call<DataHolder> searchNews(
            @Query("q") String query,
            @Query("country") String country,
            @Query("lang") String lang,
            @Query("apikey") String apiKey
    );

    @GET("top-headlines")
    Call<DataHolder> getNews(
            @Query("country") String country,
            @Query("lang") String lang,
            @Query("apikey") String apiKey
    );
}
