package com.prince.popularmovies.FavourateMoviesDatabase;

import androidx.arch.core.executor.TaskExecutor;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MoviesDao {
    @Query("SELECT * FROM favourites")
    List<MovieEntity> getFavouriteMovies();

    @Insert
    void insertTask(MovieEntity movieEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(MovieEntity movieEntity);

    @Delete
    void deleteTask(MovieEntity movieEntity);

    @Query("SELECT * FROM favourites WHERE movieId = :movieId")
    MovieEntity getFavouriteMovieWithId(String movieId);
}
