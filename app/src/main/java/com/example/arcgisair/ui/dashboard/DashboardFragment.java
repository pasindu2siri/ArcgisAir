package com.example.arcgisair.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgisair.MainActivity;
import com.example.arcgisair.R;
import com.example.arcgisair.models.AirQualityNote;
import com.example.arcgisair.models.DummyDataReader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {


    private DashboardViewModel dashboardViewModel;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView mRecyclerView;
    private static RecyclerView.Adapter adapter;

    private FirebaseFirestore db;


    View root;

    List<String> cities;
    JSONObject jsonObject;
    ImageView addButton;


    ArrayList data;

    List<String> ListElementsArrayList = new ArrayList<>();

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference citiesDocument = firebaseFirestore.collection("Cities").document(user.getUid());


        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showBottomNav();

        addButton = root.findViewById(R.id.addButton);
//        addButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//
//                final String[] m_Text = {""};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
//                builder.setTitle("Add City");
//
//                // Set up the input
//                final EditText input = new EditText(root.getContext());
//                input.setInputType(InputType.TYPE_CLASS_TEXT);
//                builder.setView(input);
//
//                // Set up the buttons
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        m_Text[0] = input.getText().toString();
//                        if (cities.contains(m_Text[0]) && !(ListElementsArrayList.contains(m_Text[0]))) {
//                            ListElementsArrayList.add(m_Text[0]);
//                            adapter.notifyDataSetChanged();
//                            getJSON(ListElementsArrayList.size() - 1);
//                            citiesDocument.update("Cities", FieldValue.arrayUnion(m_Text[0]));
//
//                        }
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                builder.show();
//            }
//
//
//        });

        citiesDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                    if(doc.contains("Cities")) {
                        List<String> cities = (List<String>) doc.get("Cities");
                        for (String city : cities) {
                            ListElementsArrayList.add(city);
                            Collections.sort(ListElementsArrayList);

                        }
                    }




                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
//        String citiesList = new DummyDataReader(getActivity()).ReadTextFromFile("cities.txt");
//
//
//
//        ListElements = (citiesList.split(","));
//        ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));


        cities = new ArrayList<String>();
        cities.addAll(Arrays.asList("Agoura Hills", "Alhambra", "Arcadia", "Artesia", "Avalon", "Azusa", "Baldwin Park", "Bell", "Bell Gardens", "Bellflower", "Beverly Hills", "Bradbury", "Burbank", "Calabasas", "Carson", "Cerritos", "Claremont", "Commerce", "Compton", "Covina", "Cudahy", "Culver City", "Diamond Bar", "Downey", "Duarte", "El Monte", "El Segundo", "Gardena", "Glendale", "Glendora", "Hawaiian Gardens", "Hawthorne", "Hermosa Beach", "Hidden Hills", "Huntington Park", "Industry", "Inglewood", "Irwindale", "La Cañada", "Flintridge", "La Habra Heights", "La Mirada", "La Puente", "La Verne", "Lakewood", "Lancaster", "Lawndale", "Lomita", "Long Beach", "Los Angeles", "Lynwood", "Malibu", "Manhattan", "BeachMaywood", "Monrovia", "Montebello", "Monterey Park", "Norwalk", "Palmdale", "Palos Verdes Estates", "Paramount", "Pasadena", "Pico Rivera", "Pomona", "Rancho Palos Verdes", "Redondo Beach", "Rolling Hills", "Rolling Hills Estates", "Rosemead", "San Dimas", "San Fernando", "San Gabriel", "San Marino", "Santa Clarita", "Santa Fe Springs", "Santa Monica", "Sierra Madre", "Signal Hill", "South El Monte", "South Gate", "South Pasadena", "Temple City", "Torrance", "Vernon", "Walnut", "West Covina", "West Hollywood", "Westlake Village", "Whittier"));

        return root;
    }

    public class CustomAdapter extends RecyclerView.Adapter<DashboardFragment.CustomAdapter.ViewHolder> {
        private ArrayList<AirQualityNote> dataList;


        public CustomAdapter(ArrayList<AirQualityNote> data) {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
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

            public ViewHolder(View itemView) {
                super(itemView);
                this.textCity = itemView.findViewById(R.id.city);
                this.imageView = itemView.findViewById(R.id.card_view_image);
                this.textAQI = itemView.findViewById(R.id.AQI);
                this.textDescription = itemView.findViewById(R.id.description);
                this.mainPollutant = itemView.findViewById(R.id.mainPollutant);
                this.linearLayout = itemView.findViewById(R.id.card_layout);
                this.cardView = itemView.findViewById(R.id.card_view);
                this.textWeather = itemView.findViewById(R.id.weather);
                this.textWind = itemView.findViewById(R.id.wind);
                this.textHumidity = itemView.findViewById(R.id.humidity);
                this.textPressure = itemView.findViewById(R.id.pressure);
            }
        }

        @Override
        public DashboardFragment.CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aqi_card, parent, false);

            DashboardFragment.CustomAdapter.ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(DashboardFragment.CustomAdapter.ViewHolder holder, final int position) {
            holder.textCity.setText(dataList.get(position).getCity());
            holder.textAQI.setText("AQI: " + (dataList.get(position).getAQI()));
            holder.textWeather.setText(String.valueOf(dataList.get(position).getWeather()));
            holder.textWind.setText(String.valueOf(dataList.get(position).getWind()));
            holder.textHumidity.setText((dataList.get(position).getHumidity()) + " %");
            holder.textPressure.setText((dataList.get(position).getPressure()) + " hpa");
            holder.mainPollutant.setText("Main Pollutant: " + dataList.get(position).getPollutant());


            if (dataList.get(position).getAQI() < 50) {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.healthy));
                holder.textDescription.setText("Good");
                holder.imageView.setImageResource(R.drawable.face0);
            } else if (dataList.get(position).getAQI() >= 50 || dataList.get(position).getAQI() < 100) {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.unhealthy));
                holder.textDescription.setText("Fair");
                holder.imageView.setImageResource(R.drawable.face50);
            } else {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.danger));
                holder.textDescription.setText("Bad");
                holder.imageView.setImageResource(R.drawable.face100);
            }


//            holder.itemView.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
//                    Bundle bundle = new Bundle();
//                    bundle.putDouble("Longitude", dataList.get(position).getLongitude());
//                    bundle.putDouble("Latitude", dataList.get(position).getLatitude());
//                    bundle.putString("City", dataList.get(position).getCity());
//
//                    Navigation.findNavController(root).navigate(R.id.action_navigation_dashboard_to_navigation_location, bundle );
//
//                }
//            });
        }


        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getJSON(final int index) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    URL url = new URL("https://api.airvisual.com/v2/city?city=" + ListElementsArrayList.get(index) + "&state=California&country=USA&key=afe5e23e-0c0a-4932-afbb-1af1e415413c");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    //System.out.println(json.toString());

                    jsonObject = new JSONObject(json.toString());
                    JSONObject values = jsonObject.getJSONObject("data");

                    String city = values.getString("city");
                    int AQI = values.getJSONObject("current").getJSONObject("pollution").getInt("aqius");
                    String mainPollutant = values.getJSONObject("current").getJSONObject("pollution").getString("mainus");
                    String pollutant;
                    if (mainPollutant.equals("p1")) {
                        pollutant = "PM10";
                    } else if (mainPollutant.equals("p2")) {
                        pollutant = "PM2.5";
                    } else if (mainPollutant.equals("o3")) {
                        pollutant = "Ozone (O3)";
                    } else if (mainPollutant.equals("n2")) {
                        pollutant = "Nitrogen Dioxide (NO2)";
                    } else if (mainPollutant.equals("s2")) {
                        pollutant = "Sulfur Dioxide (SO2)";
                    } else {
                        pollutant = "Carbon Monoxide (CO)";
                    }

                    double longitude = values.getJSONObject("location").getJSONArray("coordinates").getDouble(0);
                    double latitude = values.getJSONObject("location").getJSONArray("coordinates").getDouble(1);
                    int Celsius = values.getJSONObject("current").getJSONObject("weather").getInt("tp");

                    int weather = Celsius * (9 / 5) + 32;
                    String windSpeed = String.valueOf(values.getJSONObject("current").getJSONObject("weather").getInt("ws"));
                    int wind = values.getJSONObject("current").getJSONObject("weather").getInt("wd");
                    String totalWind;
                    if (wind > 270) {
                        totalWind = "NW " + wind;
                    } else if (wind > 180) {
                        totalWind = "SW " + wind;
                    } else if (wind > 180) {
                        totalWind = "SE " + wind;
                    } else {
                        totalWind = "NE " + wind;
                    }

                    int humidity = values.getJSONObject("current").getJSONObject("weather").getInt("hu");
                    int pressure = values.getJSONObject("current").getJSONObject("weather").getInt("pr");


                    AirQualityNote newNote = new AirQualityNote(city, AQI, longitude, latitude, pollutant, weather, totalWind, humidity, pressure, index);


                    data.add(newNote);

                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {

                adapter = new CustomAdapter(data);
                mRecyclerView.setAdapter(adapter);
            }
        }.execute();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference citiesDocument = firebaseFirestore.collection("Cities").document(user.getUid());
        data = new ArrayList<AirQualityNote>();

        for (int i = 0; i < ListElementsArrayList.size(); i++) {
            getJSON(i);
        }

        setHasOptionsMenu(false);
    }
}