package com.example.user.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.example.user.popularmovies.FetchMovieTask;
import com.example.user.popularmovies.Utility;
import com.example.user.popularmovies.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 5/3/2017.
 */

public class TrailerLoader extends AsyncTaskLoader<List<Video>> {
    private static final String LOG_TAG = "errors";
    private URL REQUEST_URL;

    public TrailerLoader(Context context, URL url) {
        super(context);
        REQUEST_URL = url;
        Log.d(LOG_TAG, REQUEST_URL.toString()+" got here");
    }

    @Override
    public List<Video> loadInBackground() {
        URL url = (REQUEST_URL);
        Log.d(LOG_TAG, REQUEST_URL.toString()+" got here");
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = Utility.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to the server", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Trailer} object
        return extractFeatureFromJson(jsonResponse);
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    private List<Video> extractFeatureFromJson(String trailerJSON) {

        //if json string is empty or null return early
        if (TextUtils.isEmpty(trailerJSON)) {
            return null;
        }
        List trailerList = new ArrayList();
        try {
            JSONObject baseJsonResponse = new JSONObject(trailerJSON);

            JSONArray trailersArray = baseJsonResponse.getJSONArray("results");

            // If there are results in the features array
            for (int i = 0; i < trailersArray.length(); i++) {
                // Extract out the first feature
                JSONObject firstObjectFeature = trailersArray.getJSONObject(i);

                // Extract out the  id, key of trailer
                String id = firstObjectFeature.getString("id");
                String key = firstObjectFeature.getString("key");

                // Create a trailer {@link Trailer} object
                trailerList.add(new Video(id, key));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return trailerList;
    }

}
