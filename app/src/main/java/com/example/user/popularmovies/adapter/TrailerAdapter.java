package com.example.user.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.popularmovies.R;
import com.example.user.popularmovies.model.Movie;
import com.example.user.popularmovies.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 5/3/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private List<Video> videos = new ArrayList<Video>();

    private Context context;
    final private ListItemClickListener mOnClickListener;
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailerAdapter(ArrayList<Video> videos, Context context, ListItemClickListener listener) {
        this.videos = videos;
        this.context = context;

        mOnClickListener = listener;
    }
    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_video;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerAdapter.MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
    public void setTrailer(List<Video> video){
        this.videos = video;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, int position) {
        Video video = videos.get(position);
        int realPosition = position + 1;
        holder.trailerCounter.setText(context.getResources().getString(R.string.trailer_label) +" "+ realPosition);
        holder.playIcon.setImageResource(R.drawable.myplay);

    }
    public Video getItem(int id) {
        return videos.get(id);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView playIcon;
        TextView trailerCounter;

        public MyViewHolder(View itemView) {
            super(itemView);
            playIcon = (ImageView) itemView.findViewById(R.id.myPlayId);
            trailerCounter = (TextView) itemView.findViewById(R.id.trailerText);
            playIcon.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
