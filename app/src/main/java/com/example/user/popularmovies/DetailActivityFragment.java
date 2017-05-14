package com.example.user.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.popularmovies.adapter.MovieAdapter;
import com.example.user.popularmovies.adapter.TrailerAdapter;
import com.example.user.popularmovies.data.MovieContract;
import com.example.user.popularmovies.loader.TrailerLoader;
import com.example.user.popularmovies.model.Movie;
import com.example.user.popularmovies.model.Video;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.user.popularmovies.data.MovieContract.MovieEntry;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private CheckBox checkBox;
    public static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public final String FAVORITE = "favorite";
    String id;
    Movie movie ;
    Button trailerButton, reviewButton;
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        checkBox = (CheckBox) rootView.findViewById(R.id.favorite);
        trailerButton = (Button) rootView.findViewById(R.id.trailerButton);
        reviewButton = (Button) rootView.findViewById(R.id.reviewButton);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movie = (Movie) intent.getSerializableExtra("sampleObject");
            Bundle extras = intent.getExtras();
            id = extras.getString("currentMovie");
            ((TextView) rootView.findViewById(R.id.movie_name))
                    .setText(movie.getTitle());
            Picasso.with( getContext() )
                    .load( movie.getImageUrl() )
                    .into((ImageView) rootView.findViewById(R.id.image));
            String releasedDateWithMonthAndDate = movie.getReleaseDate();
            String releasedYear = releasedDateWithMonthAndDate.substring(0, Math.min(releasedDateWithMonthAndDate.length(), 4));
            ((TextView) rootView.findViewById(R.id.year_of_release))
                    .setText((releasedYear));
            ((TextView) rootView.findViewById(R.id.rating))
                    .setText((movie.getRating()+ "/10"));
            ((TextView) rootView.findViewById(R.id.overview))
                    .setText(movie.getOverview());

            getCheckboxStatus(movie);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkBox.isChecked()){
                    System.out.println( movie.getMovieId());
                    insertFavorite(movie);

                }else{
                    System.out.println(movie.getRating());
                    deleteFavorite(movie);
                }

            }
        });
        trailerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), TrailerActivity.class);
                intent.putExtra("movieId",  movie.getMovieId());
                startActivity(intent);

            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("movieId",  movie.getMovieId());
                intent.putExtra("movieTitle",  movie.getTitle());
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getCheckboxStatus(Movie movie){
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_ID + "='" + movie.getMovieId() + "' and "
                        + MovieContract.MovieEntry.COLUMN_ORDER_BY + "='" + FAVORITE + "'",
                null,
                null);

        if (cursor.getCount() > 0){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }

    }

    private void deleteFavorite(Movie movie){
        String movieId = movie.getMovieId();
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_ID + "='" + movie.getMovieId() + "' and "
                        + MovieContract.MovieEntry.COLUMN_ORDER_BY + "='" + FAVORITE + "'",
                null,
                null);
        cursor.moveToFirst();
        int id = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
        String stringId = cursor.getString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
        getActivity().getContentResolver().delete(uri, null, null);
    }

    private void insertFavorite(Movie movie){
        String movieId = movie.getMovieId();

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieEntry.COLUMN_ID, movieId);
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getImageUrl());
        contentValues.put(MovieEntry.COLUMN_RATING, movie.getRating());
        contentValues.put(MovieEntry.COLUMN_DATE, movie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_ORDER_BY, FAVORITE);

        // Insert the content values via a ContentResolver
        Uri uri = getActivity().getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        Log.d(TAG, "insertFavorite: "+ uri);
    }
}
