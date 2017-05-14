package com.example.user.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.user.popularmovies.adapter.TrailerAdapter;
import com.example.user.popularmovies.loader.TrailerLoader;
import com.example.user.popularmovies.model.Movie;
import com.example.user.popularmovies.model.Video;

import java.util.ArrayList;
import java.util.List;

public class TrailerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Video>>,
        TrailerAdapter.ListItemClickListener {

    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";
    final String APPID_PARAM = "api_key";
    final String TRAILER_PATH = "videos";
    final String WATCH_PATH = "watch";
    final String KEY_PARAM = "v";
    private static final int TRAILER_LOADER_ID = 1;
    private TrailerAdapter trailerAdapter;
    private RecyclerView trailerRecyclerView;
    private TrailerAdapter.ListItemClickListener mOnClickListener;
    public static final String LOG_TAG = TrailerActivity.class.getSimpleName();
    private  String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailer);
        trailerRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(layoutManager);

        trailerAdapter = new TrailerAdapter(new ArrayList<Video>(),this,this);
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerRecyclerView.setHasFixedSize(true);
        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getExtras().getString("movieId");
            Log.v(LOG_TAG, "Built URI " + movieId);
        }
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(TRAILER_PATH)
                .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                .build();
        Log.v(LOG_TAG, "Built URI " + builtUri.toString());
        String url = builtUri.toString();
        trailerAdapter.notifyDataSetChanged();
        return new TrailerLoader(this, Utility.createUrl(url));
    }

    @Override
    public void onLoadFinished(Loader<List<Video>> loader, List<Video> videos) {
        if (videos != null && !videos.isEmpty()) {
            trailerAdapter.setTrailer(videos);
        }
        trailerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Video>> loader) {

    }
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Video video =  trailerAdapter.getItem(clickedItemIndex);
        String trailerKey = video.getKey();
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(WATCH_PATH)
                .appendQueryParameter(KEY_PARAM, trailerKey)
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(builtUri.toString())));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trailer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
