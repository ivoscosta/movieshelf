package br.com.zup.movieshelf.rest;

import br.com.zup.movieshelf.model.Movie;
import br.com.zup.movieshelf.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ivo.costa on 23/08/2016.
 */

public interface RetrofitInterface {
    @GET("/")
    Call<MoviesResponse> getMovies(@Query("s") String s);

    @GET("/")
    Call<Movie> getMovieById(@Query("i") String id, @Query("plot") String plot, @Query("r") String r);
}