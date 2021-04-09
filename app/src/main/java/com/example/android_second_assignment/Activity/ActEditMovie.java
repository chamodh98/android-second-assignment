package com.example.android_second_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
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

public class ActEditMovie extends AppCompatActivity {
    private MovieData data;
    int movieId = 0;
    int rate = 0;
    boolean isFav = false;

    private EditText txtTitle,txtYear,txtDirector,txtActor,txtReview;
    private RadioButton radioFav,radioNFav;
    private ImageView star1,star2,star3,star4,star5,star6,star7,star8,star9,star10;

    List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_movie);
        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId",0);

        data = new MovieData(this);

        txtTitle = (EditText) findViewById(R.id.title);
        txtYear = (EditText) findViewById(R.id.year);
        txtDirector = (EditText) findViewById(R.id.director);
        txtActor = (EditText) findViewById(R.id.actor);
        txtReview = (EditText) findViewById(R.id.review);
        radioFav = (RadioButton) findViewById(R.id.radioFav);
        radioNFav = (RadioButton) findViewById(R.id.radioNFav);

        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        star6 = (ImageView) findViewById(R.id.star6);
        star7 = (ImageView) findViewById(R.id.star7);
        star8 = (ImageView) findViewById(R.id.star8);
        star9 = (ImageView) findViewById(R.id.star9);
        star10 = (ImageView) findViewById(R.id.star10);


        try {
            Cursor cursor = getData();
            getMovies(cursor);
        }finally {
            data.close();
        }

        setData();
    }

    //get selected movie by movie id
    private Cursor getData() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select * from "+TABLE_NAME+" WHERE "+MOVIE_ID+" = "+movieId, null );
        return  cursor;
    }

    private void getMovies(Cursor cursor) {
        //set data
        items = new ArrayList<Item>();
        while (cursor.moveToNext()) {
            String mt = cursor.getString(1);
            int mi = cursor.getInt(0);
            String my = cursor.getString(2);
            String md = cursor.getString(3);
            String ma = cursor.getString(4);
            String mr = cursor.getString(6);
            String mRate = cursor.getString(5);
            String fav = cursor.getString(7);
            boolean b = false;
            if (fav!=null && fav.equals("TRUE")){
                b = true;
            }

            Item item = new Item(mi,b,mt,my,md,ma,mr,mRate);
            items.add(item);
        }
        cursor.close();


    }

    private void setData(){
        //set data to ui
        if (items.size()>0){
            txtTitle.setText(items.get(0).title);
            txtActor.setText(items.get(0).actor);
            txtDirector.setText(items.get(0).director);
            txtReview.setText(items.get(0).review);
            txtYear.setText(items.get(0).year);
            //set favourite movie status
            if (items.get(0).checked){
                radioFav.setChecked(true);
                isFav = true;
            }else {
                radioNFav.setChecked(true);
            }
        }
        //set rating view
        if (Integer.parseInt(items.get(0).rate) > 0){
            rate = Integer.parseInt(items.get(0).rate);
            if (rate>=1){
                star1.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=2){
                star2.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=3){
                star3.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=4){
                star4.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=5){
                star5.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=6){
                star6.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=7){
                star7.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=8){
                star8.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=9){
                star9.setImageResource(R.drawable.yellow_star);
            }
            if (rate>=10){
                star10.setImageResource(R.drawable.yellow_star);
            }
        }
    }

    public void update(View view) {
        int year = 0;
        if  (txtYear.getText().toString().length() > 0){
            year = Integer.parseInt(txtYear.getText().toString());
        }

        //validate user inputs
        if (txtTitle.getText().toString().length() <= 0){
            txtTitle.setError("Enter Movie Title.");
            txtTitle.requestFocus();
        }else if (txtYear.getText().toString().length() <= 0){
            txtYear.setError("Enter Year.");
            txtYear.requestFocus();
        }else if (year < 1895){
            txtYear.setError("Accept only values which are greater than 1895");
            txtYear.requestFocus();
        }else if (txtDirector.getText().toString().length() <= 0){
            txtDirector.setError("Enter Director.");
            txtDirector.requestFocus();
        }else if (txtActor.getText().toString().length() <= 0){
            txtActor.setError("Enter actors/actresses.");
            txtActor.requestFocus();
        }else if (txtReview.getText().toString().length() <= 0){
            txtReview.setError("Enter Review.");
            txtReview.requestFocus();
        }else {
            updateData();
            Toast toast = Toast.makeText(getApplicationContext(), "Movie data updated.", Toast.LENGTH_SHORT);
            toast.show();
            //back to main UI
            finish();
        }

    }

    //update selected movie details
    private void updateData(){
        String movieFav = "FALSE";
        if (isFav){
            movieFav = "TRUE";
        }

        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOVIE_TITLE, txtTitle.getText().toString());
        values.put(MOVIE_YEAR, txtYear.getText().toString());
        values.put(MOVIE_DIRECTOR, txtDirector.getText().toString());
        values.put(MOVIE_ACTOR, txtActor.getText().toString());
        values.put(MOVIE_RATE, rate);
        values.put(MOVIE_REVIEW, txtReview.getText().toString());
        values.put(MOVIE_FAV,movieFav);
        db.update(TABLE_NAME, values, MOVIE_ID+" = ?", new String[]{String.valueOf(movieId)});
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioFav:
                if (checked)
                    isFav = true;
                    break;
            case R.id.radioNFav:
                if (checked)
                    isFav = false;
                    break;
        }
    }

    public void star1Click(View view) {
        rate = 1;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_unselect_star);
        star3.setImageResource(R.drawable.yellow_unselect_star);
        star4.setImageResource(R.drawable.yellow_unselect_star);
        star5.setImageResource(R.drawable.yellow_unselect_star);
        star6.setImageResource(R.drawable.yellow_unselect_star);
        star7.setImageResource(R.drawable.yellow_unselect_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star2Click(View view) {
        rate = 2;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_unselect_star);
        star4.setImageResource(R.drawable.yellow_unselect_star);
        star5.setImageResource(R.drawable.yellow_unselect_star);
        star6.setImageResource(R.drawable.yellow_unselect_star);
        star7.setImageResource(R.drawable.yellow_unselect_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star3Click(View view) {
        rate = 3;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_unselect_star);
        star5.setImageResource(R.drawable.yellow_unselect_star);
        star6.setImageResource(R.drawable.yellow_unselect_star);
        star7.setImageResource(R.drawable.yellow_unselect_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star4Click(View view) {
        rate = 4;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_unselect_star);
        star6.setImageResource(R.drawable.yellow_unselect_star);
        star7.setImageResource(R.drawable.yellow_unselect_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star5Click(View view) {
        rate = 5;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_star);
        star6.setImageResource(R.drawable.yellow_unselect_star);
        star7.setImageResource(R.drawable.yellow_unselect_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star6Click(View view) {
        rate = 6;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_star);
        star6.setImageResource(R.drawable.yellow_star);
        star7.setImageResource(R.drawable.yellow_unselect_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star7Click(View view) {
        rate = 7;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_star);
        star6.setImageResource(R.drawable.yellow_star);
        star7.setImageResource(R.drawable.yellow_star);
        star8.setImageResource(R.drawable.yellow_unselect_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star8Click(View view) {
        rate = 8;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_star);
        star6.setImageResource(R.drawable.yellow_star);
        star7.setImageResource(R.drawable.yellow_star);
        star8.setImageResource(R.drawable.yellow_star);
        star9.setImageResource(R.drawable.yellow_unselect_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star9Click(View view) {
        rate = 9;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_star);
        star6.setImageResource(R.drawable.yellow_star);
        star7.setImageResource(R.drawable.yellow_star);
        star8.setImageResource(R.drawable.yellow_star);
        star9.setImageResource(R.drawable.yellow_star);
        star10.setImageResource(R.drawable.yellow_unselect_star);
    }

    public void star10Click(View view) {
        rate = 10;
        star1.setImageResource(R.drawable.yellow_star);
        star2.setImageResource(R.drawable.yellow_star);
        star3.setImageResource(R.drawable.yellow_star);
        star4.setImageResource(R.drawable.yellow_star);
        star5.setImageResource(R.drawable.yellow_star);
        star6.setImageResource(R.drawable.yellow_star);
        star7.setImageResource(R.drawable.yellow_star);
        star8.setImageResource(R.drawable.yellow_star);
        star9.setImageResource(R.drawable.yellow_star);
        star10.setImageResource(R.drawable.yellow_star);
    }


    public class Item {
        int movieId;
        boolean checked;
        String title;
        String year;
        String director;
        String actor;
        String review;
        String rate;

        Item(int mi,boolean movieFav, String mt,String my,String md,String ma,String mr,String mRate){
            checked = movieFav;
            movieId = mi;
            title = mt;
            year = my;
            director = md;
            actor = ma;
            review = mr;
            rate = mRate;
        }

    }
}
