package com.example.arcgisair.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgisair.MainActivity;
import com.example.arcgisair.R;
import com.example.arcgisair.models.AirQualityNote;

import org.json.JSONArray;
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
    private TextView mTextViewEmpty;
    private ProgressBar mProgressBarLoading;
    private ImageView mImageViewEmpty;
    private RecyclerView mRecyclerView;
    private DashboardFragment.ListAdapter mListadapter;

    View root;
    Button newPopup;

    List<String> cities;
    JSONObject jsonObject;

    ArrayList data;
    String[] ListElements = new String[] {
            "Torrance",
            "Gardena"
    };
    List<String> ListElementsArrayList;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mTextViewEmpty = (TextView)root.findViewById(R.id.textViewEmpty);
        mImageViewEmpty = (ImageView)root.findViewById(R.id.imageViewEmpty);
        mProgressBarLoading = (ProgressBar)root.findViewById(R.id.progressBarLoading);

        newPopup = root.findViewById(R.menu.add_button);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);


        ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));
        data = new ArrayList<AirQualityNote>();


        for (int i = 0; i < ListElementsArrayList.size(); i++)
        {
            getJSON(i);
        }

        cities = new ArrayList<String>();
        cities.addAll(Arrays.asList("Agoura Hills", "Alhambra", "Arcadia", "Artesia", "Avalon", "Azusa", "Baldwin Park", "Bell", "Bell Gardens", "Bellflower", "Beverly Hills", "Bradbury", "Burbank", "Calabasas", "Carson", "Cerritos", "Claremont", "Commerce", "Compton", "Covina", "Cudahy", "Culver City", "Diamond Bar", "Downey", "Duarte", "El Monte", "El Segundo", "Gardena", "Glendale", "Glendora", "Hawaiian Gardens", "Hawthorne", "Hermosa Beach", "Hidden Hills", "Huntington Park" ,"Industry" ,"Inglewood", "Irwindale", "La Cañada" , "Flintridge" , "La Habra Heights", "La Mirada", "La Puente" ,"La Verne", "Lakewood", "Lancaster", "Lawndale" ,"Lomita", "Long Beach" ,"Los Angeles", "Lynwood", "Malibu", "Manhattan" , "BeachMaywood", "Monrovia", "Montebello", "Monterey Park", "Norwalk",  "Palmdale", "Palos Verdes Estates", "Paramount", "Pasadena", "Pico Rivera", "Pomona", "Rancho Palos Verdes", "Redondo Beach", "Rolling Hills", "Rolling Hills Estates", "Rosemead", "San Dimas", "San Fernando", "San Gabriel",  "San Marino", "Santa Clarita", "Santa Fe Springs", "Santa Monica", "Sierra Madre", "Signal Hill", "South El Monte", "South Gate" ,"South Pasadena", "Temple City" ,"Torrance" , "Vernon", "Walnut", "West Covina",  "West Hollywood", "Westlake Village", "Whittier"));

        return root;
    }




    public class ListAdapter extends RecyclerView.Adapter<DashboardFragment.ListAdapter.ViewHolder>
    {
        private ArrayList<AirQualityNote> dataList;

        ImageView imageView = root.findViewById(R.id.card_view_image);

        public ListAdapter(ArrayList<AirQualityNote> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textCity;
            TextView textAQI;
            ImageView imageAQI;


            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textCity = (TextView) itemView.findViewById(R.id.text);
                this.textAQI = (TextView) itemView.findViewById(R.id.comment);
                this.imageAQI = (ImageView) itemView.findViewById(R.id.card_view_image);
            }
        }

        @Override
        public DashboardFragment.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aqi_card, parent, false);

            DashboardFragment.ListAdapter.ViewHolder viewHolder = new DashboardFragment.ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(DashboardFragment.ListAdapter.ViewHolder holder, final int position)
        {
            holder.textCity.setText(dataList.get(position).getCity());
            holder.textAQI.setText(String.valueOf(dataList.get(position).getAQI()));
            holder.imageAQI.setImageResource(R.drawable.face50);


            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(), "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Longitude", dataList.get(position).getLongitude());
                    bundle.putDouble("Latitude", dataList.get(position).getLatitude());
                    bundle.putString("City", dataList.get(position).getCity());

                    Navigation.findNavController(root).navigate(R.id.action_navigation_dashboard_to_navigation_location, bundle );

                }
            });
        }



        @Override
        public int getItemCount()
        {
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

                    URL url = new URL("https://api.airvisual.com/v2/city?city=" + ListElementsArrayList.get(index) + "&state=California&country=USA&key=19990c1e-be6d-44ac-9faf-641e5b6038ec");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    jsonObject = new JSONObject(json.toString());
                    JSONObject values = jsonObject.getJSONObject("data");

                    String city = values.getString("city");
                    int AQI = values.getJSONObject("current").getJSONObject("pollution").getInt("aqius");
                    double longitude = values.getJSONObject("location").getJSONArray("coordinates").getDouble(0);
                    double latitude= values.getJSONObject("location").getJSONArray("coordinates").getDouble(1);


                    AirQualityNote newNote = new AirQualityNote(city, AQI, longitude, latitude, index);
                    data.add(newNote);
                    System.out.println(data.size());

                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                mListadapter = new DashboardFragment.ListAdapter(data);
                mRecyclerView.setAdapter(mListadapter);
            }
        }.execute();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.add_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        //This is the layout that you are going to use in your alertdialog
        final View addView = getLayoutInflater().inflate(R.layout.popup, null);

        final String[] m_Text = {""};

        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(root.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                if(cities.contains(m_Text[0]) && !(ListElementsArrayList.contains(m_Text[0]))) {
                    ListElementsArrayList.add(m_Text[0]);
                    mListadapter.notifyDataSetChanged();
                    getJSON(ListElementsArrayList.size()-1);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        return (super.onOptionsItemSelected(item));
    }
}