package com.example.user.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.user.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by USER on 4/28/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "movies.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 45;

    /**
     * Constructs a new instance of {@link MovieDbHelper}.
     *
     * @param context of the app
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String LOG_TAG = MovieContract.class.getSimpleName();


    final String SQL_CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_ID + " INTEGER NO NULL," +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_ORDER_BY + " TEXT NOT NULL " + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
