package com.example.arcgisair.ui.location;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgisair.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;
    JSONArray jsonArray;
    Integer zipcode;
    String city;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationViewModel =
                new ViewModelProvider(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_location, container, false);

        zipcode = getArguments().getInt("ZipCode");
        city = getArguments().getString("City");




        getAP(zipcode);





        return root;
    }

    @SuppressLint("StaticFieldLeak")
    public void getAP(int zipcode){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    List<String> val = new ArrayList<String>();

                    URL url = new URL("https://www.airnowapi.org/aq/forecast/zipCode/?format=application/json&zipCode=" + zipcode + "&date=2021-01-12&distance=25&API_KEY=80B64FF3-2AA2-4DF0-95A0-73AB2C285BEE");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while ((tmp = reader.readLine()) != null) {
                        json.append(tmp).append("\n");
                    }
                    reader.close();

                    jsonArray = new JSONArray(json.toString());
                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if (jsonArray != null) {
                    try {
                        Log.d("my weather received", jsonArray.get(1).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }.execute();
    }
}