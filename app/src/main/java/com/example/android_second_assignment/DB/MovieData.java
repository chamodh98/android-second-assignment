package com.example.android_second_assignment.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android_second_assignment.DB.Constants.MOVIE_ACTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_DIRECTOR;
import static com.example.android_second_assignment.DB.Constants.MOVIE_FAV;
import static com.example.android_second_assignment.DB.Constants.MOVIE_ID;
import static com.example.android_second_assignment.DB.Constants.MOVIE_RATE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_REVIEW;
import static com.example.android_second_assignment.DB.Constants.MOVIE_TITLE;
import static com.example.android_second_assignment.DB.Constants.MOVIE_YEAR;
import static com.example.android_second_assignment.DB.Constants.TABLE_NAME;


public class MovieData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieData.db";
    private static final int DATABASE_VERSION = 1;

    public MovieData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + MOVIE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_TITLE + " TEXT NOT NULL,"
                + MOVIE_YEAR + " TEXT NOT NULL,"
                + MOVIE_DIRECTOR + " TEXT NOT NULL,"
                + MOVIE_ACTOR + " TEXT NOT NULL,"
                + MOVIE_RATE + " TEXT NOT NULL,"
                + MOVIE_REVIEW + " TEXT NOT NULL,"
                + MOVIE_FAV + " TEXT  );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
