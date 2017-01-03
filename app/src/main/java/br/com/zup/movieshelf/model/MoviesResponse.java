package br.com.zup.movieshelf.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ivo on 02/01/17.
 */

public class MoviesResponse {

    @SerializedName("Search")
    private List<Movie> search;

    @SerializedName("totalResults")
    private String totalResults;

    public List<Movie> getSearch() {
        return search;
    }

    public void setSearch(List<Movie> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
