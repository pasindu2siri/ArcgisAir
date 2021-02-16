package com.example.arcgisair.ui.location;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.app.ActionBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgisair.MainActivity;
import com.example.arcgisair.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Double lon;
    Double lat;
    String city;
    TextView AQI;
    TextView Place;
    TextView Date;
    TextView Status;
    GifImageView Pic;
    String Type;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationViewModel =
                new ViewModelProvider(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_location, container, false);




        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNav();


        lon = Double.valueOf(df.format(getArguments().getDouble("Longitude")));
        lat = Double.valueOf(df.format(getArguments().getDouble("Latitude")));

       // Log.i("Lon", String.valueOf(lon));
       // Log.i("Lat", String.valueOf(lat));


        city = getArguments().getString("City");
        Log.i("City", city);
        AQI = root.findViewById(R.id.airQuality);
        Place  = root.findViewById(R.id.city);
        Date = root.findViewById(R.id.date);
        Status = root.findViewById(R.id.status);
        Pic = root.findViewById(R.id.background);




        //root.setBackgroundColor(R.drawable.bg_gradient);

        getWeather(city);
        getAP(lon, lat);

        return root;
    }

    @SuppressLint("StaticFieldLeak")
    public void getAP(double lon, double lat){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {


                    URL url = new URL("https://www.airnowapi.org/aq/forecast/latLong/?format=application/json&latitude="+ lat+ "&longitude=" + lon +"&date=2021-01-14&distance=10&API_KEY=80B64FF3-2AA2-4DF0-95A0-73AB2C285BEE");


                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((tmp = reader.readLine()) != null) {
                        json.append(tmp).append("\n");
                    }






                    reader.close();
                    jsonArray = new JSONArray(json.toString());
                    //System.out.println(jsonArray.toString());


                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if (jsonArray != null) {
                    applyValues(jsonArray);
                }
            }
        }.execute();
    }

    public void applyValues(JSONArray vals){
        Place.setText(city);



        try {
            AQI.setText(String.valueOf(vals.getJSONObject(0).get("AQI")));

            Date.setText(String.valueOf(vals.getJSONObject(0).get("DateIssue")));
            JSONObject category = (JSONObject) vals.getJSONObject(0).get("Category");
            Status.setText(String.valueOf(category.get("Name")));

            Log.i("Type", Type);


            if(Type.equals("rainy")){
                Pic.setImageResource(R.drawable.rain);
            } else if(Type.equals("clouds")){
                Pic.setImageResource(R.drawable.clouds);
            } else {
                    Pic.setImageResource(R.drawable.sun);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getWeather(String city){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {


                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city+ "&appid=680ce2ab20370c3d0afbd3ad789183e4");


                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while ((tmp = reader.readLine()) != null) {
                        json.append(tmp).append("\n");
                    }


                    reader.close();
                    JSONObject o1 = new JSONObject(json.toString()).getJSONArray("weather").getJSONObject(0);
                    Type = o1.getString("main").toString().toLowerCase();


                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if (jsonArray != null) {
                    applyValues(jsonArray);
                }
            }
        }.execute();
    }



}