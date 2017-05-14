package com.example.user.popularmovies.model;

/**
 * Created by USER on 5/2/2017.
 */

public class Review {

    private String id;
    private String content;

    public Review(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
