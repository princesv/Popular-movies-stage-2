package com.prince.popularmovies.FavourateMoviesDatabase;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MovieEntity.class}, version = 1 , exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    final static String LOG_TAG = MoviesDatabase.class.getSimpleName();
    final static Object LOCK = new Object();
    private static final String DATABASE_NAME = "favourites";
    private static MoviesDatabase sInstance;
    public static MoviesDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),MoviesDatabase.class,MoviesDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG,"Getting the database instance");
        return sInstance;
    }
  public abstract MoviesDao moviesDao();
}
