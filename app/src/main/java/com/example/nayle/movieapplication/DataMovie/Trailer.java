package com.example.nayle.movieapplication.DataMovie;

import java.io.Serializable;

/**
 * Created by Nayle on 1/9/2016.
 */
public class Trailer implements Serializable {

    private String name;
    private String url;

    public Trailer() {

    }

    public String getName() {
        return name;
    }
    public void setName(String key) {
        this.name = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
