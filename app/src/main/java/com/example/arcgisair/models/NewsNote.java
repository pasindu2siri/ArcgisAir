package com.example.arcgisair.models;

public class NewsNote {
    String id;
    String description;
    String imageUrl;
    String date;
    String title;
    String webUrl;
    String provider;


    public NewsNote(String id, String description, String imageUrl, String date, String title, String webUrl, String provider) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.title = title;
        this.webUrl = webUrl;
        this.provider = provider;
    }


    public String getDescription() { return title; }

    public String getImageUrl() { return imageUrl; }

    public String getDate() { return date; }

    public String getTitle() { return title; }

    public String getWebUrl() { return webUrl; }

    public String getId() { return id; }

    public String getProvider() { return provider; }

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("description: " + description);
        System.out.println("image: " + imageUrl);
        System.out.println("title: " + title);
        System.out.println("webUrl: " + webUrl);
        System.out.println("provider: " + provider);
    }







}
