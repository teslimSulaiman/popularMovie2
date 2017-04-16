package com.example.user.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 2/15/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private List<Movie> movie = new ArrayList<Movie>();
    private Context context;
    private static class ViewHolder {
        ImageView movieImage;

    }
    public MovieAdapter(Context context, List<Movie> movie) {
        super(context, R.layout.list_item_movies, movie);
        this.context = context;
        this.movie = movie;
    }
    @Override
    public int getCount() {
        return movie.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View gridItemView = convertView;
        if (gridItemView == null) {
            holder = new ViewHolder();
            gridItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_movies, parent, false);
            holder.movieImage = (ImageView) gridItemView.findViewById(R.id.image);
            gridItemView.setTag(holder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            holder = (ViewHolder) gridItemView.getTag();
        }
        Movie currentMovie = getItem(position);

        Picasso.with(context).load(currentMovie.getImageUrl()).into(holder.movieImage);
        return gridItemView;
    }
}
