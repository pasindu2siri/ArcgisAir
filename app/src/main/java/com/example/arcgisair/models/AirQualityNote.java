package com.example.arcgisair.models;

public class AirQualityNote {
    String city;
    int AQI;
    double longitude;
    double latitude;
   // int imageId;
    int id;


    public AirQualityNote(String city, int AQI, double longitude, double latitude,  int id){
        this.city = city;
        this.AQI = AQI;
        this.longitude = longitude;
        this.latitude = latitude;
        //this.imageId = imageId;
    }

    public String getCity()
    {
        return city;
    }

    public int getAQI()
    {
        return AQI;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

//    public int getImageId()
//    {
//        return imageId;
//    }

    public int getID()
    {
        return id;
    }





}
