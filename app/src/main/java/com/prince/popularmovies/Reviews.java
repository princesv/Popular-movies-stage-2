package com.prince.popularmovies;

import java.util.ArrayList;
import java.util.List;

public class Reviews {
    List<String> name;
    List<String> userName;
    List<String> avatar;
    List<Double> rating;
    List<String> reviews;

    public Reviews(List<String> name, List<String> userName, List<String> avatar, List<Double> rating, List<String> reviews) {
        this.name = name;
        this.userName = userName;
        this.avatar = avatar;
        this.rating = rating;
        this.reviews = reviews;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getUserName() {
        return userName;
    }

    public void setUserName(List<String> userName) {
        this.userName = userName;
    }

    public List<String> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<String> avatar) {
        this.avatar = avatar;
    }

    public List<Double> getRating() {
        return rating;
    }

    public void setRating(List<Double> rating) {
        this.rating = rating;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }
}
