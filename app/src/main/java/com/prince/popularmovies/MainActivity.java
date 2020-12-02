package com.prince.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.app.LoaderManager;
//import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.prince.popularmovies.FavourateMoviesDatabase.MovieEntity;
import com.prince.popularmovies.FavourateMoviesDatabase.MoviesDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements MyOwnAdapter.ListItemClickListener {
    RecyclerView movieGridRecyclerView;
    MyOwnAdapter myOwnAdapter;
    TextView tvErrorMessage;
    Parameters paramitersToInflateMoviesList;
    ProgressBar progressBar;
    public String gSearchResult;
    public static final String SEARCH_QUERY_RESULT_KEY = "search-result";
    private MoviesDatabase sInstance;
    public Boolean FAVOURITE_FLAG = false;
    public static String MOVIE_INDEX ="movie-index";
    public static String SEARCH_RESULT = "search-result";
    public static String MOVIE_ID = "favourite-movie-id";
    List<String> movieId = new ArrayList<>();


    private final String stringBaseAddressTopRated = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private final String stringBaseAddressPopular = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private final String api = "41ec4d909d71953ad8ffa75eb3157315";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paramitersToInflateMoviesList = new Parameters();

        movieGridRecyclerView = findViewById(R.id.movie_grid_recycler_view);
        tvErrorMessage = findViewById(R.id.errorMessage);
        progressBar = findViewById(R.id.progressBar);
        sInstance = MoviesDatabase.getInstance(this);
        if(savedInstanceState != null && savedInstanceState.getString(SEARCH_QUERY_RESULT_KEY)!=null){
            String searchResult = savedInstanceState.getString(SEARCH_QUERY_RESULT_KEY);
            gSearchResult = searchResult;
            if(searchResult!=null) {
                displayRecyclerView();
                Parameters parameters = getGridDataForMainActivity(searchResult);
                inflateMainActivity(parameters);
            }else{
                displayErrorMessage();
            }
        }else{
            new FetchDataFronInternet().execute(stringBaseAddressPopular+api);
        }


    }

    void inflateMainActivity(Parameters paramitersToInflateMoviesList){
        myOwnAdapter = new MyOwnAdapter(this,paramitersToInflateMoviesList,this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        movieGridRecyclerView.setLayoutManager(layoutManager);
        movieGridRecyclerView.setHasFixedSize(true);
        movieGridRecyclerView.setAdapter(myOwnAdapter);
    }

    @Override
    public void onListItemClick(int listItemIndex) {
        Intent intentToStartDetailActivity = new Intent(MainActivity.this,DetailActivity.class);

        if(FAVOURITE_FLAG){

            intentToStartDetailActivity.putExtra(MOVIE_ID,movieId.get(listItemIndex));

        }else{

            intentToStartDetailActivity.putExtra(MOVIE_INDEX,listItemIndex);
            intentToStartDetailActivity.putExtra(SEARCH_RESULT,gSearchResult);
        }
        startActivity(intentToStartDetailActivity);

    }


    public class FetchDataFronInternet extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            tvErrorMessage.setVisibility(View.INVISIBLE);
            movieGridRecyclerView.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            URL urlToFetchData = getUrlFromString(stringUrl);
            String searchResult = null;
            try {
                searchResult = getResponseFromHttpUrl(urlToFetchData);
                gSearchResult = searchResult;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return searchResult;


        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);
            if(s!=null) {
                displayRecyclerView();
                Parameters parameters = getGridDataForMainActivity(s);
                inflateMainActivity(parameters);
            }else{
                displayErrorMessage();
            }
        }
    }
    void displayErrorMessage(){
        movieGridRecyclerView.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
    void displayRecyclerView(){
        movieGridRecyclerView.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
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

    private Parameters getGridDataForMainActivity(String searchResult){
        Parameters parameters = new Parameters();
        List<String> movieName = new ArrayList<>();
        List<Double> movieRating = new ArrayList<>();
        List<String> movieImage = new ArrayList<>();
        try {
            JSONObject movieData = new JSONObject(searchResult);
            JSONArray movieDetails = movieData.getJSONArray("results");
            int length = movieDetails.length();
           for(int i=0; i<length;i++){
               JSONObject tempObject = new JSONObject(movieDetails.getString(i));
               movieName.add(tempObject.getString("title"));
               movieRating.add(tempObject.getDouble("vote_average"));
               movieImage.add(tempObject.getString("poster_path"));
           }
           parameters.setMovieImage(movieImage);
           parameters.setMovieName(movieName);
           parameters.setMovieRating(movieRating);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parameters;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.listByTop_rating){
            FAVOURITE_FLAG =false;
            new FetchDataFronInternet().execute(stringBaseAddressTopRated+api);
        }
        else if(itemId == R.id.listByPopularity){
            FAVOURITE_FLAG =false;
            new FetchDataFronInternet().execute(stringBaseAddressPopular+api);
        }else if(itemId == R.id.favouriteMovieList){
            inflateFavouriteMovieList();
            FAVOURITE_FLAG =true;
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_RESULT_KEY,gSearchResult);
    }

    void inflateFavouriteMovieList(){

        AppExecutor.getInstance().discIo().execute(new Runnable() {
            @Override
            public void run() {
                List<MovieEntity>  favouriteMovies = sInstance.moviesDao().getFavouriteMovies();
                int length = favouriteMovies.size();
                List<String> movieNames = new ArrayList<>();
                List<String> movieImage = new ArrayList<>();
                List<Double> movieRating = new ArrayList<>();

                for(int i=0;i<length;i++){
                    MovieEntity tempMovieEntity;
                    tempMovieEntity = favouriteMovies.get(i);
                    movieNames.add(tempMovieEntity.getMovieTitle());
                    movieImage.add(tempMovieEntity.getPosterUrl());
                    movieRating.add(tempMovieEntity.getMovieRating());
                    movieId.add(tempMovieEntity.getMovieId());
                }
                final Parameters parameters = new Parameters();
                parameters.setMovieName(movieNames);
                parameters.setMovieImage(movieImage);
                parameters.setMovieRating(movieRating);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inflateMainActivity(parameters);
                    }
                });
            }
        });

    }
}
