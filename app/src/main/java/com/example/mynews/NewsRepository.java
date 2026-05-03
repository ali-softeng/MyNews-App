package com.example.mynews;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private ApiService apiService;

    public NewsRepository(){
        apiService = MyRetrofit.getApiService();
    }

    public LiveData<Resource<List<DataNews>>> getTopHeadlines(String country, String key){
        MutableLiveData<Resource<List<DataNews>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        apiService.searchNews("news", country, "en", key).enqueue(new Callback<DataHolder>() {
            @Override
            public void onResponse(Call<DataHolder> call, Response<DataHolder> response) {
                if(response.isSuccessful() && response.body() != null){
                    data.setValue(Resource.success(response.body().getArticles()));
                } else {
                    int errorCode = response.code();
                    String errorMsg = "Something went wrong";
                    if (errorCode == 403) {
                        errorMsg = "Daily Limit Reached";
                    }
                    data.setValue(Resource.error(errorMsg, null, errorCode));
                }
            }

            @Override
            public void onFailure(Call<DataHolder> call, Throwable t) {
                data.setValue(Resource.error("Network Failure", null, 0));
            }
        });

        return data;
    }
}
