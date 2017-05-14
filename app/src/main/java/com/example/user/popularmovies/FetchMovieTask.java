package com.example.user.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.example.user.popularmovies.data.MovieContract;
import com.example.user.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by USER on 4/30/2017.
 */

public class FetchMovieTask extends AsyncTask<String, Void, Void> {
    private  final String LOG_TAG = FetchMovieTask.class.getName() ;
    private final Context mContext;

    public FetchMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
        final String APPID_PARAM = "api_key";

        if (strings.length == 0) {
            return null ;
        }

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(strings[0])
                .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                .build();
        Log.v(LOG_TAG, "Built URI " + builtUri.toString());
        String jsonResponse = "";
        try {
            URL url = new URL(builtUri.toString());
            jsonResponse = Utility.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to the server", e);
        }
          deletePreviousMovie(strings[0]);
          extractFeatureFromJson(jsonResponse, strings[0]);

        return null;

    }
    private void deletePreviousMovie(String movieType){
        int amountDeleted =0;
        amountDeleted  = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_ORDER_BY + "='" + movieType + "'",
                null);
    }
    private void extractFeatureFromJson(String movieJson, String orderBy) {
        final String results = "results";
        final String title = "title";
        final String move_id = "id";
        final String movieImage = "poster_path";
        final String overview = "overview";
        final String vote_average = "vote_average";
        final String release_date = "release_date";
        //if json string is empty or null return early
        if (TextUtils.isEmpty(movieJson)) {
            return ;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(movieJson);
            JSONArray itemsArray = baseJsonResponse.getJSONArray(results);

            //Vector<ContentValues> cVVector = null;
            Vector<ContentValues> cVVector = new Vector<ContentValues>(itemsArray.length());
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject movieData = itemsArray.getJSONObject(i);
                // Extract out the title, url, overview, release date and average vote of movie
                String movieId = movieData.getString(move_id);
                String movieTitle = movieData.getString(title);
                String unFormatMovieImageUrl = movieData.getString(movieImage);
                String imageUrl = formatMovieImagePath(unFormatMovieImageUrl);
                String movieOverview = movieData.getString(overview);
                String movieReleaseDate = movieData.getString(release_date);
                double movieAverageVote = movieData.getDouble(vote_average);

                ContentValues weatherValues = new ContentValues();

                weatherValues.put(MovieEntry.COLUMN_ID, movieId);
                weatherValues.put(MovieEntry.COLUMN_TITLE, movieTitle);
                weatherValues.put(MovieEntry.COLUMN_POSTER_PATH, imageUrl);
                weatherValues.put(MovieEntry.COLUMN_RATING, movieAverageVote);
                weatherValues.put(MovieEntry.COLUMN_DATE, movieReleaseDate);
                weatherValues.put(MovieEntry.COLUMN_SYNOPSIS, movieOverview);
                weatherValues.put(MovieEntry.COLUMN_ORDER_BY, orderBy);

                cVVector.add(weatherValues);
            }
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the movie JSON results", e);
        }
    }

    private String formatMovieImagePath(String pathFragment) {
        final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String MOVIE_IMAGE_SIZE_OPTION = "w185/";
        return MOVIE_IMAGE_BASE_URL + MOVIE_IMAGE_SIZE_OPTION + pathFragment;
    }

}
