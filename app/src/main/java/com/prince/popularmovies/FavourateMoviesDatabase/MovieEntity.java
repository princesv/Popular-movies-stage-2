package com.prince.popularmovies.FavourateMoviesDatabase;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourites")
public class MovieEntity {
    @PrimaryKey(autoGenerate = false)
    @NonNull String movieId;
    String isAdult;
    String movieTitle;
    String originalLanguage;
    String movieOverview;
    int movieTotalVotes;
    String dateOfRelease;
    Double movieRating;
    String posterUrl;
    String backdropUrl;
    String trailersJson;
    String reviewsJson;


    public MovieEntity(String movieId, String isAdult, String movieTitle, String originalLanguage, String movieOverview, int movieTotalVotes, String dateOfRelease, Double movieRating,String posterUrl, String backdropUrl, String trailersJson, String reviewsJson) {
        this.movieId = movieId;
        this.isAdult = isAdult;
        this.movieTitle = movieTitle;
        this.originalLanguage = originalLanguage;
        this.movieOverview = movieOverview;
        this.movieTotalVotes = movieTotalVotes;
        this.dateOfRelease = dateOfRelease;
        this.movieRating = movieRating;
        this.backdropUrl = backdropUrl;
        this.posterUrl = posterUrl;
        this.trailersJson = trailersJson;
        this.reviewsJson = reviewsJson;
    }

    @Ignore
    public MovieEntity(String isAdult, String movieTitle, String originalLanguage, String movieOverview, int movieTotalVotes, String dateOfRelease, Double movieRating,String posterUrl, String backdropUrl, String trailersJson, String reviewsJson) {
        this.isAdult = isAdult;
        this.movieTitle = movieTitle;
        this.originalLanguage = originalLanguage;
        this.movieOverview = movieOverview;
        this.movieTotalVotes = movieTotalVotes;
        this.dateOfRelease = dateOfRelease;
        this.movieRating = movieRating;
        this.backdropUrl = backdropUrl;
        this.posterUrl = posterUrl;
        this.trailersJson = trailersJson;
        this.reviewsJson = reviewsJson;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getAdult() {
        return isAdult;
    }

    public void setAdult(String adult) {
        isAdult = adult;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public int getMovieTotalVotes() {
        return movieTotalVotes;
    }

    public void setMovieTotalVotes(int movieTotalVotes) {
        this.movieTotalVotes = movieTotalVotes;
    }

    public String getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(String dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public Double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(Double movieRating) {
        this.movieRating = movieRating;
    }

    public String getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(String isAdult) {
        this.isAdult = isAdult;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getTrailersJson() {
        return trailersJson;
    }

    public void setTrailersJson(String trailersJson) {
        this.trailersJson = trailersJson;
    }

    public String getReviewsJson() {
        return reviewsJson;
    }

    public void setReviewsJson(String reviewsJson) {
        this.reviewsJson = reviewsJson;
    }
}
