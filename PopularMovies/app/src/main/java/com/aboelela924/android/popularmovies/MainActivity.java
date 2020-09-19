package com.aboelela924.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aboelela924.android.popularmovies.model.Movie;
import com.aboelela924.android.popularmovies.model.MovieList;
import com.aboelela924.android.popularmovies.utils.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{

    private static final String TAG = "MainActivity";
    private static final String ADAPTER = "adapter";


    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mMovieAdapter;
    private AVLoadingIndicatorView mLoadingIndicator;

    public enum TypeOfMovies{
        TOP_RATED, POPULAR
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ADAPTER, mMovieAdapter);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieRecyclerView = findViewById(R.id.rv_movies);
        mLoadingIndicator = findViewById(R.id.avi);

        final GridLayoutManager manager = new GridLayoutManager(this,
                2,
                RecyclerView.VERTICAL,
                false);

        mMovieRecyclerView.setLayoutManager(manager);

        if(savedInstanceState == null){
            mMovieAdapter = new MovieAdapter(this);
            new RetrieveMovies().execute(TypeOfMovies.TOP_RATED);
        }else{
            mMovieAdapter = savedInstanceState.getParcelable(ADAPTER);
            mMovieAdapter.setMovieClickListener(this);
        }

        mMovieRecyclerView.setAdapter(mMovieAdapter);

        mMovieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)
                        && mMovieAdapter.getCurrentPage() < mMovieAdapter.getTotalPages()) {

                    new RetrieveMovies().execute(TypeOfMovies.TOP_RATED);

                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_by_popularity:
                mMovieAdapter.clear();
                new RetrieveMovies().execute(TypeOfMovies.POPULAR);
                return true;
            case R.id.sort_by_rate:
                mMovieAdapter.clear();
                new RetrieveMovies().execute(TypeOfMovies.TOP_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieClick(Movie movie) {
        startActivity(DetailActivity.createIntent(this, movie));
    }

    class RetrieveMovies extends AsyncTask<TypeOfMovies, Void, MovieList>{

        private String page;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
            if(mMovieAdapter.getCurrentPage() == -1){
                page = "1";
            }else{
                page = String.valueOf(mMovieAdapter.getCurrentPage()+1);
            }
        }

        @Override
        protected MovieList doInBackground(TypeOfMovies... typeOfMovies) {
            TypeOfMovies type = typeOfMovies[0];
            MovieList list;
            switch (type){
                case POPULAR:
                    MovieList popular = NetworkUtils.getPopularMovies(page);
                    return popular;
                case TOP_RATED:
                    MovieList topRated = NetworkUtils.getTopRatedMovies(page);
                    return topRated;
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieList list) {
            super.onPostExecute(list);
            hideLoading();
            mMovieAdapter.addMovies(list.getMovies());
            mMovieAdapter.setCurrentPage(list.getPage());
            mMovieAdapter.setTotalPages(list.getTotalPages());
        }
    }
}