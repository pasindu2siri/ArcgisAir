package com.example.arcgisair.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcgisair.R;
import com.example.arcgisair.models.NewsNote;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private GridLayoutManager gridLayoutManager;

    private RecyclerView mRecyclerView;
    private static RecyclerView.Adapter adapter;

    ArrayList data;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(gridLayoutManager);

        data = new ArrayList<NewsNote>();
        adapter = new NewsFragment.CustomAdapter(data);

        getJSON();

        return root;
    }

    public class CustomAdapter extends RecyclerView.Adapter<NewsFragment.CustomAdapter.ViewHolder> {
        private ArrayList<NewsNote> dataList;


        public CustomAdapter(ArrayList<NewsNote> data) {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView description;
            TextView date;
            TextView provider;
            ImageView image;


            public ViewHolder(View itemView) {

                super(itemView);
                this.title = itemView.findViewById(R.id.title);
                this.image = itemView.findViewById(R.id.image);
                this.description = itemView.findViewById(R.id.description);
                this.date = itemView.findViewById(R.id.date);
                this.provider = itemView.findViewById(R.id.provider);


            }
        }


        @Override
        public NewsFragment.CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);

            NewsFragment.CustomAdapter.ViewHolder viewHolder = new NewsFragment.CustomAdapter.ViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(NewsFragment.CustomAdapter.ViewHolder holder, final int position) {
            holder.title.setText(dataList.get(position).getTitle());
            holder.provider.setText(dataList.get(position).getProvider());
            //holder.description.setText(dataList.get(position).getDescription());
            holder.date.setText(dataList.get(position).getDate());


            Picasso.get().load(dataList.get(position).getImageUrl()).into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Check", "HELL YEAH");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataList.get(position).getWebUrl()));
                    startActivity(browserIntent);
                }
                });
            }


        @Override
        public int getItemCount() {
            return dataList.size();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void getJSON() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/search/NewsSearchAPI?q=air%20pollution%2C%20air%20quality%2C%20US&pageNumber=1&pageSize=30&autoCorrect=true&withThumbnails=true&fromPublishedDate=2021-03-10&toPublishedDate=null")
                            .get()
                            .addHeader("x-rapidapi-key", "a0b2b57f19mshc9cd08ccc8e0847p1807aejsnf5bdb34bc5e4")
                            .addHeader("x-rapidapi-host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();


                    JSONObject jsonObject = new JSONObject(responseData);
                    int length = jsonObject.getJSONArray("value").length();

                    for (int index = 0; index < length; index++) {
                        String id = (String) jsonObject.getJSONArray("value").getJSONObject(index).get("id");
                        String title = (String) jsonObject.getJSONArray("value").getJSONObject(index).get("title");
                        String description = (String) jsonObject.getJSONArray("value").getJSONObject(index).get("description");
                        String unformatted = (String) jsonObject.getJSONArray("value").getJSONObject(index).get("datePublished");
                        String date = unformatted.substring(0, 10);
                        String imageUrl = (String) jsonObject.getJSONArray("value").getJSONObject(index).getJSONObject("image").get("url");
                        String webUrl = (String) jsonObject.getJSONArray("value").getJSONObject(index).get("url");
                        String unformatProvider = (String) jsonObject.getJSONArray("value").getJSONObject(index).getJSONObject("provider").get("name");
                        String provider = unformatProvider.substring(0,1).toUpperCase() + unformatProvider.substring(1, unformatProvider.length());

                        if (imageUrl.substring(imageUrl.length() - 3, imageUrl.length()).equals("jpg")) {
                            NewsNote newsNote = new NewsNote(id, description, imageUrl, date, title, webUrl, provider);
                            //newsNote.printAll();
                            data.add(newsNote);
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                adapter = new NewsFragment.CustomAdapter(data);
                mRecyclerView.setAdapter(adapter);
            }
        }.execute();
    }


}