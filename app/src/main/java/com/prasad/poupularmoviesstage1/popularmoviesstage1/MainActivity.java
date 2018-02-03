package com.prasad.poupularmoviesstage1.popularmoviesstage1;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.fragments.MovieDetailsFragment;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.fragments.MoviePostersFragment;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MoviesPostersData;
import java.io.Serializable;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Welcome on 5/1/2016.
 */
public class MainActivity extends AppCompatActivity implements MoviePostersFragment.Communicator{


    @Bind(R.id.moviePosterFragId)
    FrameLayout moviePosterFragId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        MoviePostersFragment myFrag = new MoviePostersFragment();
        fragTransaction.replace(R.id.moviePosterFragId, myFrag);
        fragTransaction.commit();

    }


    @Override
    public void Message(MoviesPostersData.Inner data) {

        if (findViewById(R.id.movieDetailFragId)!=null) {

            FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction fragTransaction = fragMan.beginTransaction();
            MovieDetailsFragment myFrag = MovieDetailsFragment.newInstance(data);
            fragTransaction.replace(R.id.movieDetailFragId, myFrag);
            fragTransaction.commit();
        }else {

            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra("movieData", (Serializable) data);
            startActivity(intent);

        }

    }

}
