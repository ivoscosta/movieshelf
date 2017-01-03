package br.com.zup.movieshelf.controller;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.movieshelf.R;
import br.com.zup.movieshelf.adapter.MovieGridAdapter;
import br.com.zup.movieshelf.adapter.MovieListAdapter;
import br.com.zup.movieshelf.dao.MovieDbHandler;
import br.com.zup.movieshelf.model.Movie;
import br.com.zup.movieshelf.model.MoviesResponse;
import br.com.zup.movieshelf.rest.RetrofitClient;
import br.com.zup.movieshelf.rest.RetrofitInterface;
import br.com.zup.movieshelf.util.CustomFont;
import br.com.zup.movieshelf.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Intent mIntent;
    private GridView mGridView;
    private MovieGridAdapter mMoviesGridAdapter;
    private ArrayList<Movie> mMoviesGrid;
    private TextView mTitleNoItens, mSubtitleNoItens;
    private RelativeLayout mNoItens, mNoItensFound, mNoInternet, mProgressBar;
    private MovieDbHandler mDb;

    private ListView mListView;
    private MovieListAdapter mMoviesListAdapter;
    private List<Movie> mMoviesList;
    private RetrofitInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Database instance
        mDb = new MovieDbHandler(this);

        //region gridview
        mMoviesGrid = new ArrayList<>();
        mGridView = (GridView)findViewById(R.id.gridview);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Movie movie = mMoviesGrid.get(position);
                goToMoviePage(movie);
            }
        });

        mNoItens = (RelativeLayout) findViewById(R.id.no_itens);
        mProgressBar = (RelativeLayout) findViewById(R.id.progressbar);

        mTitleNoItens = (TextView) findViewById(R.id.titleNoItens);
        mTitleNoItens.setTypeface(CustomFont.getTypeface(4));

        mSubtitleNoItens = (TextView) findViewById(R.id.subtitleNoItens);
        mSubtitleNoItens.setTypeface(CustomFont.getTypeface(Typeface.NORMAL));
        //endregion

        //region Search listview
        mNoItensFound = (RelativeLayout) findViewById(R.id.no_movies_found);
        mNoInternet = (RelativeLayout) findViewById(R.id.no_internet_connection);
        mListView = (ListView) findViewById(R.id.listView);
        mMoviesList = new ArrayList<>();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mMoviesList.get(position);
                goToMoviePage(movie);
            }
        });

        mMoviesListAdapter = new MovieListAdapter(mMoviesList, this);
        mListView.setAdapter(mMoviesListAdapter);
        //endregion

        apiService = RetrofitClient.getClient().create(RetrofitInterface.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedMovies();

        //if searching
        if(mListView.getVisibility() == View.GONE) {
            if(mMoviesGrid.size() != 0)
                Util.crossfade(mNoItens, mGridView);
            else
                Util.crossfade(mGridView, mNoItens);
        }
    }

    private void goToMoviePage(Movie movie) {
        Gson gson = new Gson();
        mIntent = new Intent(getBaseContext(), MovieActivity.class);
        mIntent.putExtra("movie", gson.toJson(movie));
        startActivity(mIntent);
    }

    private void getSavedMovies() {
        // Reading all
        Log.d("Reading: ", "Reading all movies...");
        mMoviesGrid = mDb.getAllMovies();

        mMoviesGridAdapter = new MovieGridAdapter(this, mMoviesGrid);
        mGridView.setAdapter(mMoviesGridAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getMoviesApi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
//                Log.d("######", ""+searchQuery);
                mListView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                mNoInternet.setVisibility(View.GONE);

                View v = mListView;
                if(mNoItensFound.getVisibility() == View.VISIBLE)
                    v = mNoItensFound;
                else if(mNoInternet.getVisibility() == View.VISIBLE)
                    v = mNoInternet;

                if(mMoviesGrid.size() > 0)
                    Util.crossfade(v, mGridView);
                else
                    Util.crossfade(v, mNoItens);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                if(mNoItens.getVisibility() == View.VISIBLE)
                    Util.crossfade(mNoItens, mListView);
                else
                    Util.crossfade(mGridView, mListView);
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMoviesApi(String query) {
        mNoItensFound.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mNoInternet.setVisibility(View.GONE);
        if(Util.isConnectInternet(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<MoviesResponse> call = apiService.getMovies(query);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                    if(response.errorBody() == null) {
                        if (response.body().getSearch() != null && response.body().getSearch().size() > 0) {
                            mMoviesList.clear();
                            mMoviesList.addAll(response.body().getSearch());
                            mMoviesListAdapter.notifyDataSetChanged();
                            Util.crossfade(mProgressBar, mListView);
                        } else
                            Util.crossfade(mProgressBar, mNoItensFound);
                    }
                    else
                        Log.e("###", response.errorBody().toString());
                }

                @Override
                public void onFailure(Call<MoviesResponse>call, Throwable t) {
                    // Log error here since request failed
                    Log.e("###", t.toString());
                }
            });
        }
        else {
            mNoInternet.setVisibility(View.VISIBLE);
        }
    }
}
