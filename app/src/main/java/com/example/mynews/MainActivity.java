package com.example.mynews;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mynews.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NewsViewModel viewModel;
    NewsAdapter adapter;

    // Use a fresh API key if possible. The one below might have reached its limit.
    private static final String API_KEY = "8acd5d27cf242ec25bfcfd9b57641903";
    private String currentCountryCode = "pk";

    private static class Country {
        String name;
        String code;
        Country(String name, String code) {
            this.name = name;
            this.code = code;
        }
        @Override
        public String toString() {
            return code.toUpperCase();
        }
    }

    private final List<Country> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null) {
            currentCountryCode = savedInstanceState.getString("selected_country", "pk");
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        setupCountryPicker();
        setupRecyclerView();
        setupViewModel();

        fetchNews();

        binding.swipeRefresh.setOnRefreshListener(this::fetchNews);
        
        binding.retryButton.setOnClickListener(v -> {
            binding.retryButton.setEnabled(false);
            binding.retryProgress.setVisibility(View.VISIBLE);
            binding.errorTitle.setText("Retrying...");
            fetchNews();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selected_country", currentCountryCode);
    }

    private void setupRecyclerView() {
        adapter = new NewsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel.getNewsLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case SUCCESS:
                    binding.swipeRefresh.setRefreshing(false);
                    binding.retryButton.setEnabled(true);
                    binding.retryProgress.setVisibility(View.GONE);
                    
                    if (resource.data != null && !resource.data.isEmpty()) {
                        adapter.setList(resource.data);
                        binding.errorLayout.setVisibility(View.GONE);
                        binding.recycler.setVisibility(View.VISIBLE);
                    } else {
                        showError("No news available", "No articles found for this region.");
                    }
                    break;

                case ERROR:
                    binding.swipeRefresh.setRefreshing(false);
                    binding.retryButton.setEnabled(true);
                    binding.retryProgress.setVisibility(View.GONE);
                    
                    String errorTitle = "Connection Issue";
                    if (resource.errorCode == 403) {
                        errorTitle = "Daily Limit Reached";
                    }
                    
                    showError(errorTitle, resource.message != null ? resource.message : "Please try again later.");
                    Toast.makeText(this, "Error: " + resource.message, Toast.LENGTH_SHORT).show();
                    break;

                case LOADING:
                    // Only show refreshing spinner if it's not already showing
                    if (!binding.swipeRefresh.isRefreshing()) {
                        binding.swipeRefresh.setRefreshing(true);
                    }
                    break;
            }
        });
    }

    private void setupCountryPicker() {
        countryList.clear();
        countryList.add(new Country("Pakistan", "pk"));
        countryList.add(new Country("United States", "us"));
        countryList.add(new Country("India", "in"));
        countryList.add(new Country("United Kingdom", "gb"));
        countryList.add(new Country("Australia", "au"));
        countryList.add(new Country("Canada", "ca"));
        countryList.add(new Country("Saudi Arabia", "sa"));
        countryList.add(new Country("Japan", "jp"));
        countryList.add(new Country("France", "fr"));
        countryList.add(new Country("Germany", "de"));

        ArrayAdapter<Country> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryList);
        binding.countrySelector.setAdapter(arrayAdapter);
        // Important: false ensures the dropdown list isn't filtered to just the current selection
        binding.countrySelector.setText(currentCountryCode.toUpperCase(), false);

        binding.countrySelector.setOnItemClickListener((parent, view, position, id) -> {
            Country selected = (Country) parent.getItemAtPosition(position);
            currentCountryCode = selected.code;
            fetchNews();
        });
    }

    private void fetchNews() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            binding.swipeRefresh.setRefreshing(false);
            binding.retryButton.setEnabled(true);
            binding.retryProgress.setVisibility(View.GONE);
            showError("No Internet Connection", "Please check your network settings and try again.");
            return;
        }

        binding.swipeRefresh.setRefreshing(true);
        // We call fetchNews on the ViewModel, which uses the search endpoint internally
        viewModel.fetchNews(currentCountryCode, API_KEY);
    }

    private void showError(String title, String message) {
        binding.recycler.setVisibility(View.GONE);
        binding.errorLayout.setVisibility(View.VISIBLE);
        binding.errorTitle.setText(title);
        binding.errorMessage.setText(message);
    }
}
