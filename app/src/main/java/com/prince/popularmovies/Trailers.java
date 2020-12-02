package com.prince.popularmovies;

import java.util.List;

public class Trailers {
    public Trailers(List<String> title, List<String> key) {
        this.title = title;
        this.key = key;
    }

    List<String> title;
    List<String> key;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }
}
