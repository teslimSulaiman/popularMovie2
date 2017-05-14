package com.example.user.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.popularmovies.R;
import com.example.user.popularmovies.model.Review;
import com.example.user.popularmovies.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 5/4/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyReviewViewHolder> {

    private List<Review> reviews = new ArrayList<Review>();
    private Context context;

    public ReviewAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }
    @Override
    public MyReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyReviewViewHolder viewHolder = new MyReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
    }

    public class MyReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewTextView;

        public MyReviewViewHolder(View itemView) {
            super(itemView);
            reviewTextView = (TextView) itemView.findViewById(R.id.reviewId);
        }
    }
}
