package com.example.arcgisair.ui.home;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.example.arcgisair.R;


import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View root;
    MapView mMapView;
    SearchView search;
    String address;
    private LocatorTask locator = new LocatorTask("https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");
    List<GeocodeResult> result;

    public void findAddress(String address) {
        GeocodeParameters parameters = new GeocodeParameters();
        parameters.getResultAttributeNames().add("*");
        parameters.setMaxResults(1);

        ListenableFuture<List<GeocodeResult>> geocodeResultFuture = locator.geocodeAsync(address, parameters);

        geocodeResultFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try{
                    result = geocodeResultFuture.get();
                    double latitude = result.get(0).getDisplayLocation().getY();
                    double longitude = result.get(0).getDisplayLocation().getX();
                    Point area = new Point(-14093.0, 6711377.0,0);
                    mMapView.setViewpoint(new Viewpoint(latitude,longitude,7500));

                }
                catch (Exception e){
                    Log.e("ERROR", "Error getting result" + e );
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        mMapView = (MapView)root.findViewById(R.id.mapView);


        String itemId = "1311ed6d637546c792f175f6f0b34699";
        String url = "https://www.arcgis.com/sharing/rest/content/items/" + itemId + "/data";
        ArcGISMap map = new ArcGISMap(url);
        mMapView.setMap(map);

        requestPermissions();

        search = root.findViewById(R.id.SearchAddress);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                address = search.getQuery().toString();
                findAddress(address);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud1048958025,none,ZZ0RJAY3FPGEGTJ89137");

        return root;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 100);
    }


}