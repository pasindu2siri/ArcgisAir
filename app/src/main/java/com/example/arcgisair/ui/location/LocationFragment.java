package com.example.arcgisair.ui.location;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;
    JSONArray jsonArray;
    Double lon;
    Double lat;
    String city;
    TextView AQI;
    TextView Place;
    TextView Date;
    TextView Status;




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

        Log.i("Lon", String.valueOf(lon));
        Log.i("Lat", String.valueOf(lat));

        city = getArguments().getString("City");

        AQI = root.findViewById(R.id.airQuality);
        Place  = root.findViewById(R.id.city);
        Date = root.findViewById(R.id.date);
        Status = root.findViewById(R.id.status);


        //root.setBackgroundColor(R.drawable.bg_gradient);


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
                    System.out.println(jsonArray.toString());


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
            AQI.setText("AQI: " +  String.valueOf(vals.getJSONObject(1).get("AQI")));

            Date.setText(String.valueOf(vals.getJSONObject(1).get("DateIssue")));
            JSONObject category = (JSONObject) vals.getJSONObject(1).get("Category");
            Status.setText("Status: " + String.valueOf(category.get("Name")));
            Status.setText("Hello");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}