package com.example.arcgisair.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
import androidx.navigation.Navigation;

import com.example.arcgisair.R;
import com.example.arcgisair.ui.home.HomeFragment;
import com.example.arcgisair.ui.location.LocationFragment;
import com.example.arcgisair.ui.location.LocationViewModel;

import org.json.JSONArray;
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
    JSONObject jsonObject;

    View root;
    String[] ListElements = new String[] {
          "Los Angeles"
    };
    List<Integer> ZipCodes = new ArrayList<>();
    List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));


    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        for(String city: ListElements){
            getJSON(city);
        }

        listView = root.findViewById(R.id.cities);
        addButton = root.findViewById(R.id.add);
        getValue = root.findViewById(R.id.addText);

        final List<String> cities = new ArrayList<String>();
        cities.addAll(Arrays.asList("Agoura Hills", "Alhambra", "Arcadia", "Artesia", "Avalon", "Azusa", "Baldwin Park", "Bell", "Bell Gardens", "Bellflower", "Beverly Hills", "Bradbury", "Burbank", "Calabasas", "Carson", "Cerritos", "Claremont", "Commerce", "Compton", "Covina", "Cudahy", "Culver City", "Diamond Bar", "Downey", "Duarte", "El Monte", "El Segundo", "Gardena", "Glendale", "Glendora", "Hawaiian Gardens", "Hawthorne", "Hermosa Beach", "Hidden Hills", "Huntington Park" ,"Industry" ,"Inglewood", "Irwindale", "La Cañada" , "Flintridge" , "La Habra Heights", "La Mirada", "La Puente" ,"La Verne", "Lakewood", "Lancaster", "Lawndale" ,"Lomita", "Long Beach" ,"Los Angeles", "Lynwood", "Malibu", "Manhattan" , "BeachMaywood", "Monrovia", "Montebello", "Monterey Park", "Norwalk",  "Palmdale", "Palos Verdes Estates", "Paramount", "Pasadena", "Pico Rivera", "Pomona", "Rancho Palos Verdes", "Redondo Beach", "Rolling Hills", "Rolling Hills Estates", "Rosemead", "San Dimas", "San Fernando", "San Gabriel",  "San Marino", "Santa Clarita", "Santa Fe Springs", "Santa Monica", "Sierra Madre", "Signal Hill", "South El Monte", "South Gate" ,"South Pasadena", "Temple City" ,"Torrance" , "Vernon", "Walnut", "West Covina",  "West Hollywood", "Westlake Village", "Whittier"));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, ListElementsArrayList);
        listView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cities.contains(getValue.getText().toString()) && !(ListElementsArrayList.contains(getValue.getText().toString()))){
                    ListElementsArrayList.add(getValue.getText().toString());
                    adapter.notifyDataSetChanged();
                    getJSON(getValue.getText().toString());
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
                    Bundle bundle  = new Bundle();
                    bundle.putInt("ZipCode", ZipCodes.get(i));
                    bundle.putString("City", ListElementsArrayList.get(i));

                    Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_navigation_location, bundle );
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

                    URL url = new URL("https://www.zipcodeapi.com/rest/jWGWEWfqhc16FJdff3f6ddTq2SE9lqjvrAQHnLtf4VnwIEVw4iVkkEYo2t9jt5Ar/city-zips.json/" + city +"/CA");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    jsonObject = new JSONObject(json.toString());

                    JSONArray main = jsonObject.getJSONArray("zip_codes");
                    ZipCodes.add(main.getInt(0));

                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
            }
        }.execute();

    }
}