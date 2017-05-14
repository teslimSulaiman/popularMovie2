package com.example.user.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.popularmovies.adapter.MovieAdapter;
import com.example.user.popularmovies.data.MovieContract;

import java.util.ArrayList;

import com.example.user.popularmovies.data.MovieContract.MovieEntry;
import com.example.user.popularmovies.model.Movie;

/**
 * Created by USER on 2/15/2017.
 */

public class MovieFragment extends Fragment  implements
        LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.ListItemClickListener {

    private MovieAdapter  movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private RecyclerView recyclerView;
    static final int NUM_IN_ROW = 2;
    private static final int TASK_LOADER_ID = 45;
    Movie movie;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.grid);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),NUM_IN_ROW);
        recyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(getContext(),this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
       getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
                Cursor cursor = movieAdapter.getItem(clickedItemIndex);
                 cursor.moveToPosition(clickedItemIndex);

                int stringId = cursor.getColumnIndex(MovieEntry._ID);
                String id = cursor.getString(stringId);
                int titleId = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
                String title = cursor.getString(titleId);
                int imageUrlId = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);
                String imageUrl = cursor.getString(imageUrlId);
                int overviewId = cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS);
                String overview = cursor.getString(overviewId);
                int voteId = cursor.getColumnIndex(MovieEntry.COLUMN_RATING);
                double averageVote = cursor.getDouble(voteId);
                int releaseDateId = cursor.getColumnIndex(MovieEntry.COLUMN_DATE);
                String releaseDate = cursor.getString(releaseDateId);
                int moveIdId = cursor.getColumnIndex(MovieEntry.COLUMN_ID);
                String  moveId = cursor.getString(moveIdId);

         movie =  new Movie(title, imageUrl,overview,averageVote,releaseDate,moveId);

        Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("sampleObject", movie);
                intent.putExtra("currentMovie", id);
                startActivity(intent);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieType = prefs.getString(getString(R.string.pref_movie_key),
                getString(R.string.pref_movie_default));
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_ORDER_BY + "='" + movieType + "'",
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }
}
