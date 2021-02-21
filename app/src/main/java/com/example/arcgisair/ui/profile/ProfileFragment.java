package com.example.arcgisair.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import com.example.arcgisair.BuildConfig;
import com.example.arcgisair.R;
import com.example.arcgisair.models.AirQualityNote;
import com.example.arcgisair.ui.dashboard.DashboardFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    FusedLocationProviderClient mFusedLocationClient;


    View root;
    TextView textCity;
    TextView mainPollutant;
    TextView textDescription;
    TextView textAQI;
    CardView cardView;
    ImageView imageView;
    LinearLayout linearLayout;
    TextView textWeather;
    TextView textWind;
    TextView textHumidity;
    TextView textPressure;
    AirQualityNote newNote;
    MapView mMapView;





    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_profile, container, false);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        mMapView = root.findViewById(R.id.pmapView);
        getLastLocation();



        textCity = root.findViewById(R.id.city);
        imageView = root.findViewById(R.id.card_view_image);
        textAQI = root.findViewById(R.id.AQI);
        textDescription = root.findViewById(R.id.description);
        mainPollutant = root.findViewById(R.id.mainPollutant);
        linearLayout =  root.findViewById(R.id.card_layout);
        cardView = root.findViewById(R.id.card_view);
        textWeather = root.findViewById(R.id.weather);
        textWind = root.findViewById(R.id.wind);
        textHumidity = root.findViewById(R.id.humidity);
        textPressure = root.findViewById(R.id.pressure);




        return root;
    }



    @SuppressLint("StaticFieldLeak")
    public void getJSON(double latitude, double longitude) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://api.airvisual.com/v2/nearest_city?lat="+latitude+"&lon="+longitude+"&key=afe5e23e-0c0a-4932-afbb-1af1e415413c");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    System.out.println(json.toString());

                    JSONObject jsonObject = new JSONObject(json.toString());
                    JSONObject values = jsonObject.getJSONObject("data");

                    String city = values.getString("city");
                    int AQI = values.getJSONObject("current").getJSONObject("pollution").getInt("aqius");
                    String MainPollutant = values.getJSONObject("current").getJSONObject("pollution").getString("mainus");
                    String pollutant;
                    if(MainPollutant.equals("p1")){
                        pollutant = "PM10";
                    } else if(MainPollutant.equals("p2")) {
                        pollutant = "PM2.5";
                    } else if(MainPollutant.equals("o3")) {
                        pollutant = "Ozone (O3)";
                    } else if(MainPollutant.equals("n2")) {
                        pollutant = "Nitrogen Dioxide (NO2)";
                    } else if(MainPollutant.equals("s2")) {
                        pollutant = "Sulfur Dioxide (SO2)";
                    } else {
                        pollutant = "Carbon Monoxide (CO)";
                    }

                    double longitude = values.getJSONObject("location").getJSONArray("coordinates").getDouble(0);
                    double latitude= values.getJSONObject("location").getJSONArray("coordinates").getDouble(1);
                    int Celsius = values.getJSONObject("current").getJSONObject("weather").getInt("tp");

                    int weather = Celsius * (9/ 5) + 32;
                    String windSpeed = String.valueOf(values.getJSONObject("current").getJSONObject("weather").getInt("ws"));
                    int wind = values.getJSONObject("current").getJSONObject("weather").getInt("wd");
                    String totalWind;
                    if(wind >  270){
                        totalWind = "NW " + wind;
                    } else if( wind > 180){
                        totalWind = "SW " + wind;
                    }else if( wind > 180){
                        totalWind = "SE " + wind;
                    } else {
                        totalWind = "NE " + wind;
                    }

                    int humidity = values.getJSONObject("current").getJSONObject("weather").getInt("hu");
                    int pressure = values.getJSONObject("current").getJSONObject("weather").getInt("pr");

                   newNote = new AirQualityNote(city, AQI, longitude, latitude, pollutant, weather, totalWind, humidity, pressure,  999);






                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                populate();
            }
        }.execute();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            getJSON(location.getLatitude(), location.getLongitude());
                            String itemId = "1311ed6d637546c792f175f6f0b34699";
                            String url = "https://www.arcgis.com/sharing/rest/content/items/" + itemId + "/data";
                            ArcGISMap map = new ArcGISMap(url);
                            mMapView.setMap(map);
                            mMapView.setViewpoint(new Viewpoint(location.getLatitude(), location.getLongitude(), 500000));

                            ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud1048958025,none,ZZ0RJAY3FPGEGTJ89137");



                        }
                    }
                });
            } else {
                //Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }


    public void populate(){
        textCity.setText(newNote.getCity());
        textAQI.setText("AQI: " + (newNote.getAQI()));
        textWeather.setText(String.valueOf(newNote.getWeather()));
        textWind.setText(String.valueOf(newNote.getWind()));
        textHumidity.setText((newNote.getHumidity()) + " %");
        textPressure.setText((newNote.getPressure()) + " hpa");
        mainPollutant.setText("Main Pollutant: "  + newNote.getPollutant());


        if(newNote.getAQI() < 50 ){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.healthy));
            textDescription.setText("Good");
            imageView.setImageResource(R.drawable.face0);
        } else if(newNote.getAQI() >= 50 || newNote.getAQI() < 100 ){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.unhealthy));
            textDescription.setText("Fair");
            imageView.setImageResource(R.drawable.face50);
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.danger));
            textDescription.setText("Bad");
            imageView.setImageResource(R.drawable.face100);
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
//            latitude.setText("Latitude: " + mLastLocation.getLatitude() + "");
//            longitude.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 100);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

}
