package com.example.android_second_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_second_assignment.DB.MovieData;
import com.example.android_second_assignment.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.android_second_assignment.DB.Constants.MOVIE_ACTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_DIRECTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_FAV;
import static com.example.android_second_assignment.DB.Constants.MOVIE_ID;
import static com.example.android_second_assignment.DB.Constants.MOVIE_RATE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_REVIEW;
import static com.example.android_second_assignment.DB.Constants.MOVIE_TITLE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_YEAR;
import static com.example.android_second_assignment.DB.Constants.TABLE_NAME;

public class ActSearch extends AppCompatActivity {

    private MovieData data;

    List<Item> items;
    ListView listView;
    ItemsListAdapter movieListAdapter;
    EditText etSearchText;
    Button searchBtn;
    String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);

        data = new MovieData(this);

        listView = (ListView)findViewById(R.id.listview);
        etSearchText = (EditText)findViewById(R.id.etSearchText);
        searchBtn = (Button)findViewById(R.id.searchBtn);

    }

    private Cursor getDataByTitle() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select * from "+TABLE_NAME+" WHERE "+MOVIE_TITLE+" LIKE ?",new String[] { "%" + keyword + "%" });
        return  cursor;
    }

    private Cursor getDataByDirector() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select * from "+TABLE_NAME+" WHERE "+MOVIE_DIRECTOR+" LIKE ?",new String[] { "%" + keyword + "%" });
        return  cursor;
    }

    private Cursor getDataByActors() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select * from "+TABLE_NAME+" WHERE "+MOVIE_ACTOR+" LIKE ?",new String[] { "%" + keyword + "%" });
        return  cursor;
    }

    private void getMovies(Cursor cursor) {

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

    public void searchMovie(View view) {
        keyword = etSearchText.getText().toString().toUpperCase();
        items = new ArrayList<Item>();
        try {
            Cursor cursor1 = getDataByTitle();
            getMovies(cursor1);
            Cursor cursor2 = getDataByDirector();
            getMovies(cursor2);
            Cursor cursor3 = getDataByActors();
            getMovies(cursor3);
        }finally {
            data.close();
        }

        movieListAdapter = new ActSearch.ItemsListAdapter(this, items);
        listView.setAdapter(movieListAdapter);
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