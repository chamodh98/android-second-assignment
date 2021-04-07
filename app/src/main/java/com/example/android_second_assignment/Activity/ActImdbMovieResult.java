package com.example.android_second_assignment.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android_second_assignment.DB.MovieData;
import com.example.android_second_assignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActImdbMovieResult extends AppCompatActivity {

    private MovieData data;
    List<Item> items;
    ListView listView;
    ItemsListAdapter movieListAdapter;
    String movieTitle = "";
    LinearLayout lytImgView;
    ImageView imgView;
    RelativeLayout lytShowData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_imdb_movie_result);

        listView = (ListView)findViewById(R.id.listview);
        lytImgView = (LinearLayout) findViewById(R.id.lytImgView);
        imgView = (ImageView)findViewById(R.id.imgView);
        lytShowData = (RelativeLayout)findViewById(R.id.lytShowData);
        Intent intent = getIntent();
        movieTitle = intent.getStringExtra("movieTitle");
        setData();
    }

    private void setData(){
        items = new ArrayList<Item>();
        data = new MovieData(this);
        checkMovie();

        movieListAdapter = new ItemsListAdapter(this, items);
        listView.setAdapter(movieListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lytShowData.setVisibility(View.GONE);
                lytImgView.setVisibility(View.VISIBLE);

                LoadImage loadImage = new LoadImage(imgView);
                loadImage.execute(items.get(position).movieUrl);
            }
        });
    }

    public void checkMovie() {

        String myUrl = "https://imdb-api.com/en/API/SearchTitle/k_492n8ej5/"+movieTitle;
        new DownloadTask().execute(myUrl);
        Log.i("URL :", myUrl);
    }

    private class DownloadTask extends AsyncTask<String, Void, String>{


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder("");
            StringBuilder stringBuilderParseResults = new StringBuilder("");
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                if (stringBuilder.length() == 0) {
                    return null;
                }
                Log.i("StringBuffer Contains :", stringBuilder.toString());

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray movieResultArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < movieResultArray.length(); i++){
                    JSONObject movieResult = movieResultArray.getJSONObject(i);
                    String title = movieResult.getString("title") + " "+movieResult.getString("description");
                    String id = movieResult.getString("id");
                    String movieUrl = movieResult.getString("image");
                    Item item = new Item(title,id,movieUrl);
                    items.add(item);
                }



            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return stringBuilderParseResults.toString();
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            movieListAdapter.notifyDataSetChanged();
        }


    }

    private class LoadImage extends AsyncTask<String,Void, Bitmap>{
        ImageView imageView;
        public LoadImage(ImageView imgView){
            this.imageView = imgView;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }


    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_row, null);

                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }


            final String itemStr = list.get(position).movieTitle;
            viewHolder.text.setText(itemStr);


            return rowView;
        }
    }

    public class Item {
        String movieTitle;
        String movieId;
        String movieUrl;
        Item( String t, String mi, String mu){
            movieTitle = t;
            movieId = mi;
            movieUrl = mu;
        }

    }

    static class ViewHolder {
        TextView text;
    }


}