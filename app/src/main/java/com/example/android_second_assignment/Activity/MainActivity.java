package com.example.android_second_assignment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android_second_assignment.Activity.ActRegisterMovie;
import com.example.android_second_assignment.R;

public class MainActivity extends AppCompatActivity {

    Button btnRegisterMovie,btnDisplayMovie,btnFavourite,btnEditMovie,btnSearch,btnRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegisterMovie = (Button)findViewById(R.id.btnRegisterMovie);
        btnDisplayMovie = (Button)findViewById(R.id.btnDisplayMovie);
        btnFavourite = (Button)findViewById(R.id.btnFavourite);
        btnEditMovie = (Button)findViewById(R.id.btnEditMovie);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnRating = (Button)findViewById(R.id.btnRating);
    }

    public void registerMovie(View view) {
        Intent intent = new Intent(this, ActRegisterMovie.class);
        startActivity(intent);
    }

    public void displayMovie(View view) {
        Intent intent = new Intent(this, ActDisplayMovie.class);
        startActivity(intent);
    }

    public void favourite(View view) {
    }

    public void editMovie(View view) {
    }

    public void search(View view) {
    }

    public void rating(View view) {
    }
}