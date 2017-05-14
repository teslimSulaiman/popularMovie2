package com.example.user.popularmovies;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by USER on 5/4/2017.
 */

public class MyApplication extends Application {
    final String POPULAR_MOVIE = "popular";
    final String TOP_RATED_MOVIE = "top_rated";
    @Override
    public void onCreate() {
        super.onCreate();
        updateMovieList(POPULAR_MOVIE);
        updateMovieList(TOP_RATED_MOVIE);
    }
    private void updateMovieList(String type) {
        FetchMovieTask movieTask = new FetchMovieTask(getApplicationContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String movieType = prefs.getString(type,
                getString(R.string.pref_movie_default));
        movieTask.execute(movieType);
    }
}
