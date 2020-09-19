package com.aboelela924.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aboelela924.android.popularmovies.model.Movie;
import com.aboelela924.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieVH> implements Parcelable {

    interface MovieClickListener{
        void onMovieClick(Movie movie);
    }

    private List<Movie> mMovies;
    private MovieClickListener mMovieClickListener;
    private int mCurrentPage = -1;
    private int mTotalPages = -1;

    public void setMovieClickListener(MovieClickListener movieClickListener) {
        mMovieClickListener = movieClickListener;
    }

    public MovieAdapter(MovieClickListener movieClickListener) {
        mMovies = new ArrayList<>();
        mMovieClickListener = movieClickListener;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        this.mTotalPages = totalPages;
    }

    public void addMovies(List<Movie> movies) {
        mMovies.addAll(movies);
        this.notifyDataSetChanged();
    }

    public void sortMoviesBy(MainActivity.TypeOfMovies type){
        if (type == MainActivity.TypeOfMovies.POPULAR){
            Collections.sort(mMovies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie, Movie t1) {
                    return -1*movie.getPopularity().compareTo(t1.getPopularity());
                }


            });
        }else if (type == MainActivity.TypeOfMovies.TOP_RATED){
            Collections.sort(mMovies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie, Movie t1) {
                    return -1*movie.getVoteAverage().compareTo(t1.getVoteAverage());
                }
            });
        }
        this.notifyDataSetChanged();
    }

    public void clear(){
        mMovies.clear();
        mCurrentPage = -1;
        mTotalPages = -1;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie, parent, false);
        return new MovieVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    class MovieVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMovieImageView;

        public MovieVH(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mMovieImageView = itemView.findViewById(R.id.iv_movie);
        }

        public void bind(Movie movie){
            Picasso.get()
                    .load(NetworkUtils.getImageUrl(movie.getPosterPath().substring(1)))
                    .into(mMovieImageView);
        }

        @Override
        public void onClick(View view) {
            mMovieClickListener.onMovieClick(mMovies.get(this.getAbsoluteAdapterPosition()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mMovies);
        dest.writeInt(this.mCurrentPage);
        dest.writeInt(this.mTotalPages);
    }

    protected MovieAdapter(Parcel in) {
        this.mMovies = in.createTypedArrayList(Movie.CREATOR);
        this.mCurrentPage = in.readInt();
        this.mTotalPages = in.readInt();
    }

    public static final Parcelable.Creator<MovieAdapter> CREATOR = new Parcelable.Creator<MovieAdapter>() {
        @Override
        public MovieAdapter createFromParcel(Parcel source) {
            return new MovieAdapter(source);
        }

        @Override
        public MovieAdapter[] newArray(int size) {
            return new MovieAdapter[size];
        }
    };
}
