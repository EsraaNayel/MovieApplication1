package com.example.nayle.movieapplication.DataMovie;

import java.io.Serializable;

/**
 * Created by Nayle on 1/9/2016.
 */
public class Review implements Serializable {
    private String author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
    public Review(){}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

