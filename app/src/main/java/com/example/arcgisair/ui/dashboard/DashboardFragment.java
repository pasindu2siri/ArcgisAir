package com.example.arcgisair.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.arcgisair.R;
import com.example.arcgisair.ui.location.LocationFragment;
import com.example.arcgisair.ui.location.LocationViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment{

    private DashboardViewModel dashboardViewModel;

    ListView listView;
    Button addButton;
    EditText getValue;
    JSONObject d1;

    long lon;
    long lat;
    View root;
    String[] ListElements = new String[] {
          "Los Angeles"
    };



    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        listView = root.findViewById(R.id.cities);
        addButton = root.findViewById(R.id.add);
        getValue = root.findViewById(R.id.addText);

        final List<String> cities = new ArrayList<String>();
        cities.addAll(Arrays.asList("Agoura Hills", "Alhambra", "Arcadia", "Artesia", "Avalon", "Azusa", "Baldwin Park", "Bell", "Bell Gardens", "Bellflower", "Beverly Hills", "Bradbury", "Burbank", "Calabasas", "Carson", "Cerritos", "Claremont", "Commerce", "Compton", "Covina", "Cudahy", "Culver City", "Diamond Bar", "Downey", "Duarte", "El Monte", "El Segundo", "Gardena", "Glendale", "Glendora", "Hawaiian Gardens", "Hawthorne", "Hermosa Beach", "Hidden Hills", "Huntington Park" ,"Industry" ,"Inglewood", "Irwindale", "La Cañada" , "Flintridge" , "La Habra Heights", "La Mirada", "La Puente" ,"La Verne", "Lakewood", "Lancaster", "Lawndale" ,"Lomita", "Long Beach" ,"Los Angeles", "Lynwood", "Malibu", "Manhattan" , "BeachMaywood", "Monrovia", "Montebello", "Monterey Park", "Norwalk",  "Palmdale", "Palos Verdes Estates", "Paramount", "Pasadena", "Pico Rivera", "Pomona", "Rancho Palos Verdes", "Redondo Beach", "Rolling Hills", "Rolling Hills Estates", "Rosemead", "San Dimas", "San Fernando", "San Gabriel",  "San Marino", "Santa Clarita", "Santa Fe Springs", "Santa Monica", "Sierra Madre", "Signal Hill", "South El Monte", "South Gate" ,"South Pasadena", "Temple City" ,"Torrance" , "Vernon", "Walnut", "West Covina",  "West Hollywood", "Westlake Village", "Whittier"));
        final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList);
        listView.setAdapter(adapter);




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cities.contains(getValue.getText().toString()) && !(ListElementsArrayList.contains(getValue.getText().toString()))){
                    ListElementsArrayList.add(getValue.getText().toString());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Oh no", 2);
                }

            }
        });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    getJSON(adapterView.getItemAtPosition(i).toString());
                    Log.i("Info", adapterView.getItemAtPosition(i).toString());
                    //startActivity(new Intent(getActivity(), LocationFragment.class));

//                    Fragment location = new DashboardFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.navigation_dashboard, location);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                }
            }
        );

        return root;
    }



    

    @SuppressLint("StaticFieldLeak")
    public void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city +"&units=imperial" + "&APPID=680ce2ab20370c3d0afbd3ad789183e4");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    d1 = new JSONObject(json.toString());

                    if (d1.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if (d1 != null) {
                    Log.d("my weather received", d1.toString());
                    try {
                        JSONObject main = d1.getJSONObject("main");
                        int C = main.getInt("temp");

                        JSONObject loc = d1.getJSONObject("coord");

                        lon = loc.getLong("lon");
                        lat = loc.getLong("lat");
                        System.out.println(lat + "|" + lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute();

    }
//
//    @SuppressLint("StaticFieldLeak")
//    public void getAP(long lon, long lat){
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//
//                    List<String> val = new ArrayList<String>();
//
//
//                    URL url = new URL("https://www.airnowapi.org/aq/forecast/latLong/?format=text/csv&latitude=33.8923&longitude=-118.2924&date=2021-01-07&distance=25&API_KEY=80B64FF3-2AA2-4DF0-95A0-73AB2C285BEE");
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                    BufferedReader reader =
//                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                    StringBuffer json = new StringBuffer(1024);
//                    String tmp = "";
//
//                    while ((tmp = reader.readLine()) != null) {
//                        json.append(tmp).append("\n");
//                        val.add(tmp);
//                    }
//                    int size = val.size();
//                    val = val.subList(size-5,size-1);
//
//                    for(String line: val){
//                        System.out.print(line);
//                    }
//                    reader.close();
//
//                    d2 = new JSONObject(json.toString());
//
//                    JSONObject date = d2.getJSONObject("DateIssue");
//
//
//
//
//                } catch (Exception e) {
//
//                    //System.out.println("Exception " + e.getMessage());
//                    return null;
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void Void) {
//                if (d2 != null) {
//                    Log.d("my weather received", d2.toString());
//
//                }
//
//            }
//        }.execute();
//    }


}