package com.example.user.popularmovies.model;

/**
 * Created by USER on 5/2/2017.
 */

public class Video {

    private String id;
    private String key;

    public Video(String id, String key) {
        this.id = id;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
