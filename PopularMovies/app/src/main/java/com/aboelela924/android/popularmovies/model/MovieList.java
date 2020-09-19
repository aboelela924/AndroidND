package com.aboelela924.android.popularmovies.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieList {
    private Integer page;
    private List<Movie> mMovies = null;
    private Integer totalResults;
    private Integer totalPages;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
