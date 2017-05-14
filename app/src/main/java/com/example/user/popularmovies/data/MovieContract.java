package com.example.user.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 4/27/2017.
 */

public class MovieContract {

    private MovieContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.user.popularmovies";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        /**
         * Name of database table for movies
         */
        public final static String TABLE_NAME = "movies";

        /**
         * Unique ID number for the movies (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;


        /**
         * The MIME type of the {@link #CONTENT_URI} for a single movie.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_SYNOPSIS = "overview";
        public static final String COLUMN_RATING = "vote_average";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_ORDER_BY = "order_by";

    }
}
