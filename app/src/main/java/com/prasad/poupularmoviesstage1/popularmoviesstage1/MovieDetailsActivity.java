package com.prasad.poupularmoviesstage1.popularmoviesstage1;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.fragments.MovieDetailsFragment;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MoviesPostersData;

/**
 * Created by Welcome on 5/1/2016.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    MoviesPostersData.Inner data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_moviedetails);


        if(savedInstanceState==null){
            data= (MoviesPostersData.Inner) getIntent().getSerializableExtra("movieData");
            loadMovieDetailsFragment();
        }

    }

    private void loadMovieDetailsFragment(){
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        MovieDetailsFragment myFrag =MovieDetailsFragment. newInstance(data);
        fragTransaction.replace(R.id.movieDetailFragId, myFrag);
        fragTransaction.commit();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movieData", data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        data= (MoviesPostersData.Inner) savedInstanceState.getSerializable("movieData");
        loadMovieDetailsFragment();
    }


}
