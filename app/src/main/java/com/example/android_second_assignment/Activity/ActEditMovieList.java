package com.example.android_second_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android_second_assignment.DB.MovieData;
import com.example.android_second_assignment.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.android_second_assignment.DB.Constants.MOVIE_ACTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_DIRECTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_ID;
import static com.example.android_second_assignment.DB.Constants.MOVIE_RATE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_REVIEW;
import static com.example.android_second_assignment.DB.Constants.MOVIE_TITLE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_YEAR;
import static com.example.android_second_assignment.DB.Constants.TABLE_NAME;

public class ActEditMovieList extends AppCompatActivity {

    private static String[] Details = {MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_DIRECTOR, MOVIE_ACTOR, MOVIE_RATE, MOVIE_REVIEW};
    private MovieData data;



    List<Item> items;
    ListView listView;
    ItemsListAdapter movieListAdapter;
    List<String> movieIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_movie_list);


        listView = (ListView)findViewById(R.id.listview);
        movieIds = new ArrayList<String>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        data = new MovieData(this);
        try {
            Cursor cursor = getData();
            getMovies(cursor);
        }finally {
            data.close();
        }

        movieListAdapter = new ItemsListAdapter(this, items);
        listView.setAdapter(movieListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ActEditMovie.class);
                intent.putExtra("movieId", items.get(position).movieId);
                startActivity(intent);
            }
        });
    }

    private Cursor getData() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, Details, null, null,null,null,MOVIE_TITLE + " ASC");
        return  cursor;
    }

    private void getMovies(Cursor cursor) {

        items = new ArrayList<Item>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            int movieId = cursor.getInt(0);

            String s = name;
            int mi = movieId;
            Item item = new Item( s, mi);
            items.add(item);
        }
        cursor.close();


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
        int movieId;
        Item( String t, int mi){
            movieTitle = t;
            movieId = mi;
        }

    }

    static class ViewHolder {
        TextView text;
    }
}