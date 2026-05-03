package com.example.mynews;

import com.google.gson.annotations.SerializedName;

public class DataNews {
    private String title;
    private String description;
    @SerializedName("image")
    private String urlToImage;
    private String url;
    private String publishedAt;
    private Source source;

    public static class Source {
        private String name;
        public String getName() { return name; }
    }

    public DataNews(String title, String description, String urlToImage, String url, String publishedAt, Source source) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.url = url;
        this.publishedAt = publishedAt;
        this.source = source;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrlToImage() { return urlToImage; }
    public String getUrl() { return url; }
    public String getPublishedAt() { return publishedAt; }
    public Source getSource() { return source; }

    public String getSourceName() {
        return (source != null) ? source.getName() : "Unknown Source";
    }

    public String getFormattedDate() {
        if (publishedAt != null && publishedAt.length() > 10) {
            // Converts "2023-10-27T12:00:00Z" to "Oct 27, 2023" roughly or just "2023-10-27"
            return publishedAt.substring(0, 10);
        }
        return publishedAt;
    }
}
