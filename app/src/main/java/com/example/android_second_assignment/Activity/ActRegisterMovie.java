package com.example.android_second_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android_second_assignment.DB.MovieData;
import com.example.android_second_assignment.R;

import static com.example.android_second_assignment.DB.Constants.MOVIE_ACTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_DIRECTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_ID;
import static com.example.android_second_assignment.DB.Constants.MOVIE_RATE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_REVIEW;
import static com.example.android_second_assignment.DB.Constants.MOVIE_TITLE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_YEAR;
import static com.example.android_second_assignment.DB.Constants.TABLE_NAME;

public class ActRegisterMovie extends AppCompatActivity {

    private EditText txtTitle,txtYear,txtDirector,txtActor,txtRate,txtReview;
    private static String[] Details = {MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR, MOVIE_DIRECTOR, MOVIE_ACTOR, MOVIE_RATE, MOVIE_REVIEW};
    private MovieData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register_movie);

        data = new MovieData(this);

        txtTitle = (EditText) findViewById(R.id.title);
        txtYear = (EditText) findViewById(R.id.year);
        txtDirector = (EditText) findViewById(R.id.director);
        txtActor = (EditText) findViewById(R.id.actor);
        txtRate = (EditText) findViewById(R.id.rate);
        txtReview = (EditText) findViewById(R.id.review);

    }

    public void save(View view) {
        int year = 0;
        int rate = 0;
        if  (txtYear.getText().toString().length() > 0){
            year = Integer.parseInt(txtYear.getText().toString());
        }
        if  (txtRate.getText().toString().length() > 0){
            rate = Integer.parseInt(txtRate.getText().toString());
        }

        Log.d("year" , String.valueOf(year));
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
        }else if (txtRate.getText().toString().length() <= 0){
            txtRate.setError("Enter Rate.");
            txtRate.requestFocus();
        }else if (rate > 10 || rate < 1){
            txtRate.setError("Accept only values corresponding to the range of 1−10");
            txtRate.requestFocus();
        }else if (txtReview.getText().toString().length() <= 0){
            txtReview.setError("Enter Review.");
            txtReview.requestFocus();
        }else {
            addData();

            finish();
        }
    }

    public void addData(){
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOVIE_TITLE, txtTitle.getText().toString());
        values.put(MOVIE_YEAR, txtYear.getText().toString());
        values.put(MOVIE_DIRECTOR, txtDirector.getText().toString());
        values.put(MOVIE_ACTOR, txtActor.getText().toString());
        values.put(MOVIE_RATE, txtRate.getText().toString());
        values.put(MOVIE_REVIEW, txtReview.getText().toString());

        db.insertOrThrow(TABLE_NAME,null,values);
    }
}