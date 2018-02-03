package com.prasad.poupularmoviesstage1.popularmoviesstage1.interfaces;

import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MovieReviews;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MovieTeaser;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MoviesPostersData;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Welcome on 5/18/2016.
 */
public interface MovieDatabaseApi {

    @GET("/popular?api_key=<api_key>")
    public void getPosters(Callback<MoviesPostersData> response);

    @GET("/top_rated?api_key=<api_key>")
    public void getPostersTopRated(Callback<MoviesPostersData> response);

    @GET("/reviews?api_key=<api_key>")
    public void getReviews(Callback<MovieReviews> response);

    @GET("/videos?api_key=<api_key>")
    public void getTeasers(Callback<MovieTeaser> response);





}
