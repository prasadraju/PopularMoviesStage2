package com.prasad.poupularmoviesstage1.popularmoviesstage1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.MainActivity;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.R;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.adapters.MoviesDataAdapter;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.helpers.ConnectionDetector;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.helpers.Constants;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.helpers.Storage;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.interfaces.MovieDatabaseApi;
import com.prasad.poupularmoviesstage1.popularmoviesstage1.models.MoviesPostersData;
;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MoviePostersFragment extends Fragment implements AdapterView.OnItemClickListener {


    @Bind(R.id.movies_grid)
    GridView gridView;

    MovieDatabaseApi moviesDatabaseApi;

    private Communicator communicator;

    Activity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movieposters, container, false);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);


        //Creating a rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Constants.THEMOVIEDB_URL).build();

        //Creating an object of our api interface
        moviesDatabaseApi = restAdapter.create(MovieDatabaseApi.class);


        gridView.setOnItemClickListener(this);

        if (ConnectionDetector.isConnected(getActivity())) {
            moviePostersPopular();
        } else {
            showToastMsg(Constants.NO_INTERNET);
        }

        return view;
    }


    private void moviePostersPopular() {

        //Defining the method
        moviesDatabaseApi.getPosters(new Callback<MoviesPostersData>() {
            @Override
            public void success(MoviesPostersData moviesPostersData, Response response) {

                List<MoviesPostersData.Inner> moviePosterList = moviesPostersData.getResults();
                MoviesDataAdapter adapter = new MoviesDataAdapter(getActivity(), moviePosterList);
                gridView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                showToastMsg(Constants.ERROR);
            }
        });
    }


    private void moviePostersToprated() {

        //Defining the method
        moviesDatabaseApi.getPostersTopRated(new Callback<MoviesPostersData>() {
            @Override
            public void success(MoviesPostersData moviesPostersData, Response response) {

                List<MoviesPostersData.Inner> moviePosterList = moviesPostersData.getResults();
                MoviesDataAdapter adapter = new MoviesDataAdapter(getActivity(), moviePosterList);
                gridView.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                showToastMsg(Constants.ERROR);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.toprated) {

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("TopRated Movies");

            if (ConnectionDetector.isConnected(getActivity())) {
                moviePostersToprated();
            } else {
                showToastMsg(Constants.NO_INTERNET);
            }

        } else if (id == R.id.popularity) {

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Pop Movies");
            if (ConnectionDetector.isConnected(getActivity())) {
                moviePostersPopular();
            } else {
                showToastMsg(Constants.NO_INTERNET);
            }

        } else if (id == R.id.favourite) {

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Favourite");
            Storage storage = new Storage(getActivity());
            MoviesDataAdapter adapter = new MoviesDataAdapter(getActivity(), storage.getFavMoviesData());
            gridView.setAdapter(adapter);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MoviesPostersData.Inner data = (MoviesPostersData.Inner) parent.getItemAtPosition(position);
        updateDetailFragment(data);
    }

    private void updateDetailFragment(MoviesPostersData.Inner data) {

        communicator.Message(data);
    }

    //Create Interface
    public interface Communicator {
        public void Message(MoviesPostersData.Inner data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }


        if (mActivity instanceof Communicator) {
            communicator = (Communicator) mActivity;

        } else {
            throw new ClassCastException(mActivity.toString()
                    + " must implemenet MoviePostersFragment.Communicator");
        }
    }


    private void showToastMsg(String msg) {

        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();

    }
}
