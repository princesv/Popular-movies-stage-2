package com.prince.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prince.popularmovies.FavourateMoviesDatabase.MovieEntity;
import com.prince.popularmovies.FavourateMoviesDatabase.MoviesDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

public class DetailActivity extends AppCompatActivity implements TrailerListAdapter.TrailerListItemClickListener {
    TextView tvMovieTitle;
    TextView tvMovieOverview;
    TextView tvMovieRating;
    TextView tvMovieTotalVotes;
    TextView tvMovieOriginalLangudge;
    TextView tvOriginalTitle;
    TextView tvMovieIsAdult;
    TextView tvDateOfRelease;
    ImageView ivBackdropImage;
    ImageView ivposterImage;
    RecyclerView trailerListRV;
    RecyclerView revirewListRV;
    int movieIndex;
    String searchResult;
    public String gTrailerJson;
    public String gReviewJson;
    public String movieId;
   // private final String api = "41ec4d909d71953ad8ffa75eb3157315";
    TrailerListAdapter trailerListAdapter;
    List<String> gTrailerKeys;
    MoviesDatabase moviesDatabase;
    MoviesDatabase mDatabase;
    ImageView favouriteButton;
    Boolean isFavouriteFlag;
    Boolean isFavouriteActivity = false;
    MovieEntity favouriteActivityMovieEntitity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentThatStartedActivity =getIntent();
        tvMovieTitle = findViewById(R.id.movieTitle);
        tvMovieOverview =findViewById(R.id.overview);
        tvMovieRating = findViewById(R.id.movieRating);
        tvDateOfRelease = findViewById(R.id.dateOfRelease);
        tvMovieTotalVotes = findViewById(R.id.voteCount);
        tvOriginalTitle = findViewById(R.id.originalTitle);
        tvMovieOriginalLangudge= findViewById(R.id.originalLanguage);
        tvMovieIsAdult = findViewById(R.id.isAdult);
        ivBackdropImage = findViewById(R.id.backdropImage);
        ivposterImage = findViewById(R.id.posterImage);
        trailerListRV = findViewById(R.id.trailerListRecyclerView);
        revirewListRV = findViewById(R.id.reviewListRecyclerView);
        moviesDatabase = MoviesDatabase.getInstance(this);
        mDatabase = MoviesDatabase.getInstance(this);
        isFavouriteFlag = false;
        favouriteButton = findViewById(R.id.favouriteButton);




        if(intentThatStartedActivity!=null) {

            if(intentThatStartedActivity.hasExtra(MainActivity.MOVIE_INDEX) && intentThatStartedActivity.hasExtra(MainActivity.SEARCH_RESULT)) {
                isFavouriteActivity = false;
                movieIndex = intentThatStartedActivity.getIntExtra(MainActivity.MOVIE_INDEX,-1);
                searchResult = intentThatStartedActivity.getStringExtra(MainActivity.SEARCH_RESULT);
                JSONObject resultJSON = new JSONObject();
                JSONArray moviesData =new JSONArray();
                JSONObject movieData = new JSONObject();
                if(movieIndex != -1) {

                    try {
                        resultJSON = new JSONObject(searchResult);
                        moviesData = resultJSON.getJSONArray("results");
                        movieData = moviesData.getJSONObject(movieIndex);
                        movieId = movieData.getString("id");
                        tvMovieIsAdult.setText(""+movieData.getBoolean("adult"));
                        tvMovieTitle.setText(movieData.getString("title"));
                        tvMovieOriginalLangudge.setText(movieData.getString("original_language"));
                        tvOriginalTitle.setText(movieData.getString("original_title"));
                        tvMovieOverview.setText(movieData.getString("overview"));
                        tvMovieTotalVotes.setText(""+movieData.getInt("vote_count"));
                        tvDateOfRelease.setText(""+movieData.getString("release_date"));
                        tvMovieRating.setText(""+movieData.getDouble("vote_average"));
                        Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movieData.getString("poster_path")).into(ivposterImage);
                        Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movieData.getString("backdrop_path")).into(ivBackdropImage);
                        new LoadTrailerLinkFromInternet().execute("http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key="+MainActivity.api);
                        new LoadReviewFromInternet().execute("https://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key="+MainActivity.api);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }else if(intentThatStartedActivity.hasExtra(MainActivity.MOVIE_ID)){
                isFavouriteActivity = true;
                final String movieId = intentThatStartedActivity.getStringExtra(MainActivity.MOVIE_ID);
                this.movieId = movieId;
                final MovieEntity favouriteMovie;
                AppExecutor.getInstance().discIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        final MovieEntity tempMovieEntry = mDatabase.moviesDao().getFavouriteMovieWithId(movieId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                inflateActivityWithDataFromDatabase(tempMovieEntry);
                            }
                        });
                    }
                });


            }
            AppExecutor.getInstance().discIo().execute(new Runnable() {
                @Override
                public void run() {
                    MovieEntity movieEntity = mDatabase.moviesDao().getFavouriteMovieWithId(movieId);
                    if(movieEntity != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                favouriteButton.setImageResource(R.drawable.staronn);
                                isFavouriteFlag = true;
                            }
                        });

                    }else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                favouriteButton.setImageResource(R.drawable.staroff);
                                isFavouriteFlag = false;
                            }
                        });

                    }
                }
            });
        }

    }

    void inflateActivityWithDataFromDatabase(MovieEntity movieEntity){
        favouriteActivityMovieEntitity = movieEntity;
        String tempid = favouriteActivityMovieEntitity.getMovieId();
        tvMovieIsAdult.setText(""+movieEntity.getIsAdult());
        tvMovieTitle.setText(movieEntity.getMovieTitle());
        tvMovieOriginalLangudge.setText(movieEntity.getOriginalLanguage());
        tvOriginalTitle.setText(movieEntity.getMovieTitle());
        tvMovieOverview.setText(movieEntity.getMovieOverview());
        tvMovieTotalVotes.setText(""+movieEntity.getMovieTotalVotes());
        tvDateOfRelease.setText(""+movieEntity.getDateOfRelease());
        tvMovieRating.setText(""+movieEntity.getMovieRating());
        Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movieEntity.getPosterUrl()).into(ivposterImage);
        Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movieEntity.getBackdropUrl()).into(ivBackdropImage);
       // new LoadTrailerLinkFromInternet().execute("http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key="+api);
       // new LoadReviewFromInternet().execute("https://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key="+api);
        getTrailersObjectFromTrailersStringAndInflateList(movieEntity.getTrailersJson());
        getReviewsObjectFromStringJsonAndInflateList(movieEntity.getReviewsJson());

    }

    @Override
    public void onTrailerListItemClick(int listItemIndex) {
        openActivityForTrailer("https://www.youtube.com/watch?v="+gTrailerKeys.get(listItemIndex));
    }

    public class LoadTrailerLinkFromInternet extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            URL urlToFetchData = getUrlFromString(stringUrl);
            String searchResult = null;
            try {
                searchResult = getResponseFromHttpUrl(urlToFetchData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return searchResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            gTrailerJson = s;
            getTrailersObjectFromTrailersStringAndInflateList(gTrailerJson);
        }
    }
    void getTrailersObjectFromTrailersStringAndInflateList(String trailerSearchResult){
        try {
            JSONObject trailerUrlObject = new JSONObject(trailerSearchResult);
            JSONArray results = trailerUrlObject.getJSONArray("results");
            List<String> titles = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            gTrailerKeys = keys;
            int length = results.length();
            for(int i=0;i<length;i++){
                JSONObject tempObj = results.getJSONObject(i);
                titles.add(tempObj.getString("name"));
                keys.add(tempObj.getString("key"));
            }
            Trailers trailers = new Trailers(titles,keys);
            displayTrailerListView(trailers);
            // openActivityForTrailer("https://www.youtube.com/watch?v="+key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class LoadReviewFromInternet extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            URL urlToFetchData = getUrlFromString(stringUrl);
            String searchResult = null;
            try {
                searchResult = getResponseFromHttpUrl(urlToFetchData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return searchResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            gReviewJson = s;
            getReviewsObjectFromStringJsonAndInflateList(gReviewJson);
        }
    }
    void getReviewsObjectFromStringJsonAndInflateList(String reviewsStringJson){
        if(reviewsStringJson == null){
            return;
        }
        try {
            JSONObject reviewUrlObject = new JSONObject(reviewsStringJson);
            JSONArray results = reviewUrlObject.getJSONArray("results");
            List<String> name= new ArrayList<>();
            List<String> userName = new ArrayList<>();
            List<String> avatar = new ArrayList<>();
            List<Double> rating = new ArrayList<>();
            List<String> reviews = new ArrayList<>();
            int length = results.length();
            for(int i=0;i<length;i++){
                JSONObject tempObj = results.getJSONObject(i);
                reviews.add(tempObj.getString("content"));
                JSONObject authorDetailObject = tempObj.getJSONObject("author_details");
                name.add(authorDetailObject.getString("name"));
                userName.add(authorDetailObject.getString("username"));
                avatar.add(authorDetailObject.getString("avatar_path"));
                rating.add(authorDetailObject.getDouble("rating"));
            }
            Reviews reviewsObject = new Reviews(name,userName,avatar,rating,reviews);
            displayReviewListView(reviewsObject);
            // openActivityForTrailer("https://www.youtube.com/watch?v="+key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    URL getUrlFromString(String stringUrl){
        try {
            URL urlToFetchData = new URL(stringUrl);
            return urlToFetchData;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public void openNewActivityForTrailer(View view){
        new LoadTrailerLinkFromInternet().execute("http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key="+MainActivity.api);


    }

    public void openActivityForTrailer(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void displayTrailerListView(Trailers trailers){
        trailerListAdapter = new TrailerListAdapter(this,trailers,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        trailerListRV.setLayoutManager(layoutManager);
        trailerListRV.setAdapter(trailerListAdapter);
    }
    public void displayReviewListView(Reviews reviews){
        ReviewListAdapter adapter = new ReviewListAdapter(this,reviews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        revirewListRV.setLayoutManager(layoutManager);
        revirewListRV.setAdapter(adapter);

    }


    public void onFavouriteButtonClick(View view) {
        if (!isFavouriteActivity){
            if (isFavouriteFlag == false) {

                final MovieEntity movieEntity = getDetailActivityInfoObject();
                AppExecutor.getInstance().discIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        moviesDatabase.moviesDao().insertTask(movieEntity);
                    }
                });
                favouriteButton.setImageResource(R.drawable.staronn);
                if (movieEntity != null) {
                    Toast.makeText(this, "movie added to favourite", Toast.LENGTH_SHORT).show();
                }
                isFavouriteFlag = true;
            } else if (isFavouriteFlag == true) {
                final MovieEntity movieEntity = getDetailActivityInfoObject();
                AppExecutor.getInstance().discIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        moviesDatabase.moviesDao().deleteTask(movieEntity);
                    }
                });
                if (movieEntity != null) {
                    Toast.makeText(this, "movie removed from favourite", Toast.LENGTH_SHORT).show();
                }
                favouriteButton.setImageResource(R.drawable.staroff);
                isFavouriteFlag = false;
            }
         }else if(isFavouriteActivity){
            if (isFavouriteFlag == false) {

                final MovieEntity movieEntity = favouriteActivityMovieEntitity;
                AppExecutor.getInstance().discIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        moviesDatabase.moviesDao().insertTask(movieEntity);
                    }
                });
                favouriteButton.setImageResource(R.drawable.staronn);
                if (movieEntity != null) {
                    Toast.makeText(this, "movie added to favourite", Toast.LENGTH_SHORT).show();
                }
                isFavouriteFlag = true;
            } else if (isFavouriteFlag == true) {
                final MovieEntity movieEntity = favouriteActivityMovieEntitity;
                AppExecutor.getInstance().discIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        moviesDatabase.moviesDao().deleteTask(movieEntity);
                    }
                });
                if (movieEntity != null) {
                    Toast.makeText(this, "movie removed from favourite", Toast.LENGTH_SHORT).show();
                }
                favouriteButton.setImageResource(R.drawable.staroff);
                isFavouriteFlag = false;
            }
        }


    }

    MovieEntity getDetailActivityInfoObject(){
        try {
            JSONObject resultJSON = new JSONObject(searchResult);
            JSONArray moviesData = resultJSON.getJSONArray("results");
            JSONObject movieData = moviesData.getJSONObject(movieIndex);
            //movieId = movieData.getString("id");
           // tvMovieIsAdult.setText(""+movieData.getBoolean("adult"));
            String movieIsAdult = ""+movieData.getBoolean("adult");
           // tvMovieTitle.setText(movieData.getString("title"));
            String movieTitle = movieData.getString("title");
           // tvMovieOriginalLangudge.setText(movieData.getString("original_language"));
            String originalLanguage = movieData.getString("original_language");
           // tvOriginalTitle.setText(movieData.getString("original_title"));
            String originalTitle = movieData.getString("original_title");
           // tvMovieOverview.setText(movieData.getString("overview"));
            String movieOverview = movieData.getString("overview");
           // tvMovieTotalVotes.setText(""+movieData.getInt("vote_count"));
            int totalVote = movieData.getInt("vote_count");
           // tvDateOfRelease.setText(""+movieData.getString("release_date"));
            String dateOfRelease = ""+movieData.getString("release_date");
           // tvMovieRating.setText(""+movieData.getDouble("vote_average"));
            Double movieRating = movieData.getDouble("vote_average");

           // Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movieData.getString("poster_path")).into(ivposterImage);
            String posterUrl = movieData.getString("poster_path");
            String backdropUrl = movieData.getString("backdrop_path");
           // Picasso.get().load("http://image.tmdb.org/t/p/w185//"+movieData.getString("backdrop_path")).into(ivBackdropImage);
            MovieEntity movieEntity = new MovieEntity(movieId,movieIsAdult,movieTitle,originalLanguage,movieOverview,totalVote,dateOfRelease,movieRating,posterUrl,backdropUrl,gTrailerJson,gReviewJson);
            return movieEntity;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
