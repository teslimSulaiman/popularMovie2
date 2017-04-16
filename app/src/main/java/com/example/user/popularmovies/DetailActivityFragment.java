package com.example.user.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         Movie movie;
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
             movie = (Movie) intent.getSerializableExtra("sampleObject");
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
        }

        return rootView;
    }
}
