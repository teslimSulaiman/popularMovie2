package com.example.user.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.user.popularmovies.adapter.ReviewAdapter;
import com.example.user.popularmovies.adapter.TrailerAdapter;
import com.example.user.popularmovies.loader.ReviewLoader;
import com.example.user.popularmovies.loader.TrailerLoader;
import com.example.user.popularmovies.model.Review;
import com.example.user.popularmovies.model.Video;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Review>> {

    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String APPID_PARAM = "api_key";
    final String REVIEW_PATH = "reviews";
    private static final int REVIEW_LOADER_ID = 1;
    private ReviewAdapter reviewAdapter;
    private RecyclerView recyclerView;
    String movieId;
    public static final String LOG_TAG = ReviewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = (RecyclerView) findViewById(R.id.reviews);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reviewAdapter = new ReviewAdapter(new ArrayList<Review>(),this);
        recyclerView.setAdapter(reviewAdapter);
        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);

    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getExtras().getString("movieId");
            Log.v(LOG_TAG, "Built URI " + movieId);

        }
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEW_PATH)
                .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                .build();
        Log.v(LOG_TAG, "Built URI " + builtUri.toString());
        String url = builtUri.toString();
        reviewAdapter.notifyDataSetChanged();
        return new ReviewLoader(this, Utility.createUrl(url));
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            reviewAdapter.setReviews(reviews);
        }
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {

    }
}
