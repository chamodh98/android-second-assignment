package com.example.android_second_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ActDisplayMovie extends AppCompatActivity {

    private static String[] Details = {MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_DIRECTOR, MOVIE_ACTOR, MOVIE_RATE, MOVIE_REVIEW};
    private MovieData data;

    List<Item> items;
    ListView listView;
    Button save;
    ItemsListAdapter movieListAdapter;
    List<String> movieIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_movie);

        data = new MovieData(this);

        listView = (ListView)findViewById(R.id.listview);
        save = (Button)findViewById(R.id.lookup);

        try {
            Cursor cursor = getData();
            getMovies(cursor);
        }finally {
            data.close();
        }

        movieListAdapter = new ItemsListAdapter(this, items);
        listView.setAdapter(movieListAdapter);

        movieIds = new ArrayList<String>();
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
            boolean b = false;
            int mi = movieId;
            Item item = new Item( s, b, mi);
            items.add(item);
        }
        cursor.close();


    }

    public void update(View view) {
        // update favourite movies

        for (int i = 0 ; i < movieIds.size(); i++ ){
            String id = movieIds.get(i);
            SQLiteDatabase db = data.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MOVIE_FAV,"TRUE");

            db.update(TABLE_NAME, values, MOVIE_ID+" = ?", new String[]{id});
        }
        finish();
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

        public boolean isChecked(int position) {
            return list.get(position).isChecked();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).movieTitle;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);


            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;

                    //add favourite movie
                    String id = String.valueOf(list.get(position).movieId);

                    if (newState == true){
                        //add favourite movie
                        movieIds.add(id);
                    }else{
                        //remove movie from favourite list by unpicking
                        if (movieIds.size()>0){
                            for (int i = 0 ; i < movieIds.size(); i++ ){
                                if (id == movieIds.get(i)){
                                    movieIds.remove( new String(id));
                                }
                            }
                        }
                    }
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    public class Item {
        boolean checked;
        String movieTitle;
        int movieId;
        Item( String t, boolean b, int mi){
            movieTitle = t;
            checked = b;
            movieId = mi;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }
}