package com.example.mynews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NewsViewModel extends ViewModel {
    private final NewsRepository newsRepository;
    private final MediatorLiveData<Resource<List<DataNews>>> newsLiveData = new MediatorLiveData<>();

    public NewsViewModel() {
        newsRepository = new NewsRepository();
    }

    public LiveData<Resource<List<DataNews>>> getNewsLiveData() {
        return newsLiveData;
    }

    public void fetchNews(String country, String key) {
        LiveData<Resource<List<DataNews>>> repositorySource = newsRepository.getTopHeadlines(country, key);
        
        newsLiveData.addSource(repositorySource, resource -> {
            newsLiveData.setValue(resource);
            if (resource.status != Resource.Status.LOADING) {
                newsLiveData.removeSource(repositorySource);
            }
        });
    }
}
