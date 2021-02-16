package com.example.arcgisair.models;

public class AirQualityNote {
    String city;
    int AQI;
    double longitude;
    double latitude;
    int weather;
    String pollutant;
    String wind;
    int humidity;
    int pressure;
    int id;


    public AirQualityNote(String city, int AQI, double longitude, double latitude, String pollutant, int weather, String wind, int humidity, int pressure, int id){
        this.city = city;
        this.AQI = AQI;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pollutant = pollutant;
        this.weather = weather;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;

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

    public int getWeather()
    {
        return weather;
    }

    public String getPollutant() { return pollutant; }

    public String getWind()
    {
        return wind;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public int getPressure()
    {
        return pressure;
    }

    public int getID()
    {
        return id;
    }





}
