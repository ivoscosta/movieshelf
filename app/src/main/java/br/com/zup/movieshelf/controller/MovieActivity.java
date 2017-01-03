package br.com.zup.movieshelf.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import br.com.zup.movieshelf.R;
import br.com.zup.movieshelf.dao.MovieDbHandler;
import br.com.zup.movieshelf.model.Movie;
import br.com.zup.movieshelf.rest.RetrofitClient;
import br.com.zup.movieshelf.rest.RetrofitInterface;
import br.com.zup.movieshelf.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private Movie mMovie;
    private RetrofitInterface apiService;
    private ImageView mVideo, mPoster;
    private TextView mTitle, mYear, mGenre, mType, mRuntime, mRating, mPlot, mActors, mDirectors, mAwards;
    private Button mBtnAdd, mBtnRemove;
    private RelativeLayout mContainer;
    private Intent mIntent;
    private MovieDbHandler mDb;
    private boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        apiService = RetrofitClient.getClient().create(RetrofitInterface.class);
        mDb = new MovieDbHandler(this);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("movie");
        mMovie = gson.fromJson(strObj, Movie.class);

        Movie movieDb = mDb.getMovie(mMovie.getImdbID());
        if(movieDb != null) {
            mMovie = movieDb;
            saved = true;
        }

        loadViews();
        setFieldsMovie();
        Util.crossfade(null, mContainer);

        if(Util.isConnectInternet(this)) {
            Call<Movie> call = apiService.getMovieById(mMovie.getImdbID(), "full", "json");
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie>call, Response<Movie> response) {
                    if(response.errorBody() == null) {
                        mMovie = response.body();
                        setFieldsMovie();
                    }
                    else
                        Log.e("###", response.errorBody().toString());
                }

                @Override
                public void onFailure(Call<Movie>call, Throwable t) {
                    // Log error here since request failed
                    Log.e("###", t.toString());
                }
            });
        }
        else
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
    }

    private void loadViews() {
        mContainer = (RelativeLayout) findViewById(R.id.activity_movie);

        mVideo = (ImageView) findViewById(R.id.video);
        mPoster = (ImageView) findViewById(R.id.poster);

        mTitle = (TextView) findViewById(R.id.title);
        mYear = (TextView) findViewById(R.id.year);
        mGenre = (TextView) findViewById(R.id.genre);
        mType = (TextView) findViewById(R.id.type);
        mRuntime = (TextView) findViewById(R.id.runtime);
        mRating = (TextView) findViewById(R.id.rating);
        mPlot = (TextView) findViewById(R.id.plot);
        mActors = (TextView) findViewById(R.id.actors);
        mDirectors = (TextView) findViewById(R.id.directors);
        mAwards = (TextView) findViewById(R.id.awards);

        mBtnAdd = (Button) findViewById(R.id.add);
        mBtnRemove = (Button) findViewById(R.id.remove);

        if(saved) {
            mBtnAdd.setVisibility(View.GONE);
            mBtnRemove.setVisibility(View.VISIBLE);
        }
    }

    private void setFieldsMovie() {
        Util.setPicture(mVideo, mMovie.getPoster());
        Util.setPicture(mPoster, mMovie.getPoster());

        mTitle.setText(mMovie.getTitle());
        mYear.setText(mMovie.getYear());
        mGenre.setText(mMovie.getGenre());
        mType.setText(mMovie.getType());
        mRuntime.setText(mMovie.getRuntime());
        mRating.setText(mMovie.getImdbRating());
        mPlot.setText(mMovie.getPlot());
        mActors.setText(mMovie.getActors());
        mDirectors.setText(mMovie.getDirector());
        mAwards.setText(mMovie.getAwards());
    }

    public void back(View v) {
        onBackPressed();
    }

    public void share(View v) {
        mIntent = new Intent(android.content.Intent.ACTION_SEND);
        mIntent.setType("text/plain");
        mIntent.putExtra(Intent.EXTRA_TEXT, "http://www.imdb.com/title/" + mMovie.getImdbID());
        startActivity(Intent.createChooser(mIntent, getResources().getString(R.string.share_via)));
    }

    public void searchTrailerYoutube(View v) {
        mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/results?search_query=" + mMovie.getTitle() + " trailer"));
        startActivity(mIntent);
    }

    public void addToDb(View v) {
        mDb.addMovie(mMovie);
        mBtnAdd.setVisibility(View.GONE);
        mBtnRemove.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.movie_added, Toast.LENGTH_SHORT).show();
    }

    public void removeToDb(View v) {
        mDb.deleteMovie(mMovie);
        mBtnAdd.setVisibility(View.VISIBLE);
        mBtnRemove.setVisibility(View.GONE);
        Toast.makeText(this, R.string.movie_removed, Toast.LENGTH_SHORT).show();
    }
}
