package com.aboelela924.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.aboelela924.android.popularmovies.Confidential;
import com.aboelela924.android.popularmovies.model.Movie;
import com.aboelela924.android.popularmovies.model.MovieList;

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

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static Uri getImageUrl(String path){
        return new Uri.Builder()
                .scheme(Constants.HTTPS_SCHEME)
                .authority(Constants.BASE_IMAGE_URL)
                .appendPath(Constants.FIRST_PATH)
                .appendPath(Constants.SECOND_PATH)
                .appendPath(Constants.POSTER_SIZE[5])
                .appendPath(path).build();
    }

    public static MovieList getPopularMovies(String page){
        String method = "popular";
        Uri popularMoviesUri = uriCreator(Constants.HTTPS_SCHEME,
                Constants.API_BASE_URL,
                Constants.API_VERSION,
                Constants.API_MOVIE_PATH,
                method,
                Confidential.API_KEY,
                page);

        try {
            URL popularMoviesURL = new URL(popularMoviesUri.toString());
            String jsonPopularMovies = readFromAPI(popularMoviesURL);
            Log.d(TAG, "getPopularMovies: \n"+jsonPopularMovies);
            MovieList movieList = parseMovieList(jsonPopularMovies);
            return movieList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public  static MovieList getTopRatedMovies(String page){
        String method = "top_rated";
        Uri topRatedMoviesUri = uriCreator(Constants.HTTPS_SCHEME,
                Constants.API_BASE_URL,
                Constants.API_VERSION,
                Constants.API_MOVIE_PATH,
                method,
                Confidential.API_KEY,
                page);
        try {
            URL topRatedMoviesURL = new URL(topRatedMoviesUri.toString());
            String jsonTopRatedMovies = readFromAPI(topRatedMoviesURL);
            Log.d(TAG, "getTopRatedMovies: \n"+jsonTopRatedMovies);
            MovieList movieList = parseMovieList(jsonTopRatedMovies);
            return movieList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri uriCreator(String scheme,
                                  String base,
                                  String version,
                                  String type,
                                  String method,
                                  String apiKey,
                                  String page){
        Uri.Builder uri = new Uri.Builder()
                .scheme(scheme)
                .authority(base)
                .appendPath(version)
                .appendPath(type)
                .appendPath(method)
                .appendQueryParameter("api_key", apiKey);
        if(page != null && !page.equals("")){
            uri.appendQueryParameter("page", page);
        }
        return uri.build();
    }

    private static String readFromAPI(URL url) throws IOException {
       HttpURLConnection connection = (HttpURLConnection) url.openConnection();
       try{
           InputStream in = connection.getInputStream();
           Scanner scanner = new Scanner(in);
           scanner.useDelimiter("//A");
           boolean hasInput = scanner.hasNext();
           if(hasInput){
               return scanner.next();
           }else{
               return null;
           }
       }finally {
           connection.disconnect();
       }
    }


    private static MovieList parseMovieList(String jsonString){
        try {
            JSONObject allResult = new JSONObject(jsonString);
            JSONArray movies = allResult.getJSONArray("results");
            int page = allResult.getInt("page");
            int totalPages = allResult.getInt("total_pages");
            int totalResults = allResult.getInt("total_results");


            MovieList list = new MovieList();
            list.setPage(page);
            list.setTotalPages(totalPages);
            list.setTotalResults(totalResults);
            List<Movie> moviesList = new ArrayList<>();
            for(int i = 0; i < movies.length(); i++){
                Movie movie = new Movie();
                JSONObject nMovie = movies.getJSONObject(i);
                movie.setPopularity(nMovie.getDouble("popularity"));
                movie.setVoteCount(nMovie.getInt("vote_count"));
                movie.setVideo(nMovie.getBoolean("video"));
                movie.setPosterPath(nMovie.getString("poster_path"));
                movie.setId(nMovie.getInt("id"));
                movie.setAdult(nMovie.getBoolean("adult"));
                movie.setBackdropPath(nMovie.getString("backdrop_path"));
                movie.setOriginalLanguage(nMovie.getString("original_language"));
                movie.setOriginalTitle(nMovie.getString("original_title"));

                JSONArray genreIdsJsonArray = nMovie.getJSONArray("genre_ids");
                List<Integer> genreIds = new ArrayList<>();
                for(int j = 0; j < genreIdsJsonArray.length(); j++){
                    genreIds.add(genreIdsJsonArray.getInt(j));
                }
                movie.setGenreIds(genreIds);

                movie.setTitle(nMovie.getString("title"));
                movie.setVoteAverage(nMovie.getDouble("vote_average"));
                movie.setOverview(nMovie.getString("overview"));
                movie.setReleaseDate(nMovie.getString("release_date"));
                moviesList.add(movie);
            }
            list.setMovies(moviesList);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
