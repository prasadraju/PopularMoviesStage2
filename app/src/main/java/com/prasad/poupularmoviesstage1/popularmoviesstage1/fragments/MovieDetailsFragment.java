package com.prasad.poupularmoviesstage1.popularmoviesstage1.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.R;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.helpers.Constants;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.helpers.Logger;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.helpers.Storage;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.interfaces.MovieDatabaseApi;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MovieReviews;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MovieTeaser;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MoviesPostersData;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailsFragment extends Fragment {


    @Bind(R.id.trailorll)
    LinearLayout trailorll;

    @Bind(R.id.reviewsll)
    LinearLayout reviewsll;

    @Bind(R.id.movieTitle)
    TextView movieTitle;

    @Bind(R.id.poster_icon)
    ImageView poster_icon;//iv
    @Bind(R.id.releaseDate)
    TextView releaseDate;
    @Bind(R.id.popularity)
    TextView popularity;
    @Bind(R.id.voteAvarage)
    TextView voteAvarage;
    @Bind(R.id.markAsFav)
    ImageView markAsFav;
    @Bind(R.id.overview)
    TextView overview;

    Activity mActivity;

    static MoviesPostersData.Inner moviesData;

    MovieDatabaseApi moviesApi;


    public static final MovieDetailsFragment newInstance(MoviesPostersData.Inner data) {
        moviesData = data;
        return new MovieDetailsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviedetails, container, false);

        ButterKnife.bind(this, view);

        setDataToWidgets(moviesData);

        Storage storage = new Storage(getActivity());

        String storedMovieData = storage.getMovieData(moviesData.getId());

        MoviesPostersData.Inner storedMovieInnerObj = new Gson().fromJson(storedMovieData, MoviesPostersData.Inner.class);


        if (storedMovieInnerObj == null) {
            markAsFav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.star));
        } else {
            markAsFav.setTag(storedMovieInnerObj.getId());
            markAsFav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.star_favourite));
        }

        setListenerToFavImage();


        //Creating a rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.THEMOVIEDB_URL + moviesData.getId())
                .build();

        //Creating an object of our api interface
        moviesApi = restAdapter.create(MovieDatabaseApi.class);

        loadMovieTeasors();

        loadMovieReviews();

        return view;
    }


    private void loadMovieReviews() {

        //Defining the method
        moviesApi.getReviews(new Callback<MovieReviews>() {
            @Override
            public void success(MovieReviews movieReviews, Response response) {
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                lparams.setMargins(8, 0, 0, 8);

                List<MovieReviews.Inner> movieReviewList = movieReviews.getResults();

                for (int index = 0; index < movieReviewList.size(); index++) {

                    MovieReviews.Inner inner = movieReviewList.get(index);
                    TextView tv = new TextView(mActivity);
                    tv.setLayoutParams(lparams);
                    tv.setText(inner.getContent());
                    tv.setPadding(4, 8, 4, 0);
//                    tv.setTextColor(Color.BLACK);
                    reviewsll.addView(tv);
                }
            }

            @Override
            public void failure(RetrofitError error) {


                showToastMsg(Constants.ERROR);

            }
        });
    }

    private void loadMovieTeasors() {

        //Defining the method
        moviesApi.getTeasers(new Callback<MovieTeaser>() {
            @Override
            public void success(MovieTeaser movieTeaser, Response response) {

                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lparams.setMargins(48, 0, 0, 8);

                List<MovieTeaser.Inner> movieTeaserList = movieTeaser.getResults();

                Logger.log("click data::success::" + moviesData.getOriginal_title());

                for (int index = 0; index < movieTeaserList.size(); index++) {

                    int teaserCount = index + 1;

                    MovieTeaser.Inner inner = movieTeaserList.get(index);

                    Logger.log("retroit url::::success" + inner.getkey());

                    TextView tv = new TextView(mActivity);
                    tv.setLayoutParams(lparams);
                    tv.setText("Trailor " + teaserCount);
                    tv.setPadding(0, 8, 0, 0);

                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play, 0, 0, 0);
                    tv.setCompoundDrawablePadding(48);
                    tv.setId(index);
                    tv.setTag(inner.getkey());
                    tv.setOnClickListener(trailorClick);
                    trailorll.addView(tv);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                showToastMsg(Constants.ERROR);
                Logger.log("click data::fail::" + error.getMessage());
            }
        });

    }


    OnClickListener markAsFavClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Storage storage = new Storage(getActivity());

            if (markAsFav.getTag() != null) {

                markAsFav.setTag(null);
                storage.clearMovieData(moviesData.getId());
                markAsFav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.star));

            } else {

                String movieData = new Gson().toJson(moviesData);
                markAsFav.setTag(true);
                storage.setMovieData(moviesData.getId(), movieData);
                markAsFav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.star_favourite));
            }


        }
    };

    private void setListenerToFavImage() {
        markAsFav.setOnClickListener(markAsFavClick);
    }


    private void showToastMsg(String msg) {

        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();

    }

    private void setDataToWidgets(MoviesPostersData.Inner moviesData) {

        Picasso.with(getActivity()).load(Constants.POSTERMAIN_PATH + moviesData.getPoster_path())
                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(poster_icon);

        movieTitle.setText(moviesData.getOriginal_title());
        releaseDate.setText(moviesData.getRelease_date());
        popularity.setText("" + moviesData.getPopularity());
        voteAvarage.setText("" + moviesData.getVote_average() + "/10");
        overview.setText(moviesData.getOverview());

    }

    OnClickListener trailorClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) getActivity().findViewById(v.getId());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + (String) textView.getTag())));

        }
    };
}
