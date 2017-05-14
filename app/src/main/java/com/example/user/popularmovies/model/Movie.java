package com.example.user.popularmovies.model;

import java.io.Serializable;

/**
 * Created by USER on 2/15/2017.
 */

public class Movie implements Serializable {

    private String title;
    private String imageUrl;
    private String overview;
    private double rating;
    private String releaseDate;
    private String id;

    public Movie(String title, String imageUrl, String overview, double rating, String releaseDate, String id) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public String getMovieId() {
        return id;
    }

    public void setMovieId(String title) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
