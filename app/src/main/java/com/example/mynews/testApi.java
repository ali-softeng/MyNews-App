package com.example.mynews;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class testApi {
    public static void main(String[] args) {
        ApiService apiService = MyRetrofit.getApiService();
        
        // YOUR GNEWS KEY
        String key = "8acd5d27cf242ec25bfcfd9b57641903";
        
        // Added "en" as language
        Call<DataHolder> call = apiService.getNews("us", "en", key);

        try {
            Response<DataHolder> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                List<DataNews> articles = response.body().getArticles();
                System.out.println("✅ SUCCESS! GNews returned " + (articles != null ? articles.size() : 0) + " articles.");
            } else {
                System.out.println("❌ GNEWS API ERROR");
                System.out.println("Code: " + response.code());
                if (response.errorBody() != null) {
                    // THIS WILL PRINT THE EXACT ERROR (e.g., "Daily limit reached")
                    System.out.println("Server Message: " + response.errorBody().string());
                }
            }
        } catch (IOException e) {
            System.out.println("🌐 NETWORK ERROR");
            e.printStackTrace();
        }
    }
}
