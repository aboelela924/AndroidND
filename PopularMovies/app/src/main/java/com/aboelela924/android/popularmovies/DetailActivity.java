package com.aboelela924.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aboelela924.android.popularmovies.model.Movie;
import com.aboelela924.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String MOVIE = "movie";

    public static Intent createIntent(Context ctx, Movie movie){
        Intent i = new Intent(ctx, DetailActivity.class);
        i.putExtra(MOVIE, movie);
        return i;
    }

    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mRateTextView;
    private TextView mOverviewTextView;
    private ImageView mPosterImageView;

    private Movie mMovie;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE, mMovie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(getResources().getString(R.string.detail_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mTitleTextView = findViewById(R.id.rv_title);
        mReleaseDateTextView = findViewById(R.id.rv_release_year);
        mRateTextView = findViewById(R.id.rv_rate);
        mOverviewTextView = findViewById(R.id.rv_overview);
        mPosterImageView = findViewById(R.id.iv_poster);

        if (savedInstanceState != null){
            mMovie = savedInstanceState.getParcelable(MOVIE);
        }

        Intent i = getIntent();
        if(i.hasExtra(MOVIE) || mMovie != null){
            if(i != null){
                mMovie = i.getParcelableExtra(MOVIE);
            }

            Picasso.get()
                    .load(NetworkUtils.getImageUrl(mMovie.getPosterPath().substring(1)))
                    .into(mPosterImageView);

            mTitleTextView.setText(mMovie.getTitle());
            mOverviewTextView.setText(mMovie.getOverview());
            mRateTextView.setText(String.valueOf(mMovie.getVoteAverage())+"/10");
            mReleaseDateTextView.setText(mMovie.getReleaseDate());
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}