package br.com.zup.movieshelf.rest;

import br.com.zup.movieshelf.R;
import br.com.zup.movieshelf.controller.MovieShelfApplication;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ivo.costa on 23/08/2016.
 */
public class RetrofitClient {

    public static final String BASE_URL = MovieShelfApplication.getAppContext().getResources().getString(R.string.omdbapi_url);
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
