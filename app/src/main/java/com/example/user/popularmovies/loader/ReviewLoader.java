package com.example.user.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.example.user.popularmovies.Utility;
import com.example.user.popularmovies.model.Review;
import com.example.user.popularmovies.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 5/4/2017.
 */

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    private static final String LOG_TAG = "errors";
    private URL REQUEST_URL;

    public ReviewLoader(Context context, URL url) {
        super(context);
        REQUEST_URL = url;
    }

    @Override
    public List<Review> loadInBackground() {
        URL url = (REQUEST_URL);
        Log.d(LOG_TAG, REQUEST_URL.toString()+" got here");
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = Utility.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to the server", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Review} object
        return extractFeatureFromJson(jsonResponse);
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private List<Review> extractFeatureFromJson(String reviewJSON) {

        //if json string is empty or null return early
        if (TextUtils.isEmpty(reviewJSON)) {
            return null;
        }
        List reviewList = new ArrayList();
        try {
            JSONObject baseJsonResponse = new JSONObject(reviewJSON);

            JSONArray trailersArray = baseJsonResponse.getJSONArray("results");

            // If there are results in the features array
            for (int i = 0; i < trailersArray.length(); i++) {
                // Extract out the first feature
                JSONObject firstObjectFeature = trailersArray.getJSONObject(i);

                // Extract out the  id, content of review
                String id = firstObjectFeature.getString("id");
                String content = firstObjectFeature.getString("content");

                // Create a traler {@link Trailer} object
                reviewList.add(new Review(id, content));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return reviewList;
    }
}
