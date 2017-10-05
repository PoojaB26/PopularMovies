package poojab26.popularmovies;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import poojab26.popularmovies.Adapter.MoviesAdapter;
import poojab26.popularmovies.Model.Movie;
import poojab26.popularmovies.Model.PopularMoviesList;
import poojab26.popularmovies.Utilities.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MoviesAdapter adapter;
    ApiInterface apiInterface;
    RecyclerView recyclerView;
    ProgressBar sortProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sortProgress = (ProgressBar)findViewById(R.id.sortProgress);


         recyclerView = (RecyclerView) findViewById(R.id.rvMovies);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        //loadPopularMoviesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> menuArrayAadapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array_spinner, android.R.layout.simple_list_item_1);
        menuArrayAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(menuArrayAadapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(parent.getSelectedItemPosition()==0) {
                    sortProgress.setVisibility(View.VISIBLE);
                    loadPopularMoviesList();
                }

                else if (parent.getSelectedItemPosition()==1) {
                    sortProgress.setVisibility(View.VISIBLE);
                    loadTopRatedMoviesList();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        return true;
    }
    private void loadPopularMoviesList() {
        apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<PopularMoviesList> call = apiInterface.getPopularMovies(getString(R.string.API_KEY));
        call.enqueue(new Callback<PopularMoviesList>() {
            @Override
            public void onResponse(Call<PopularMoviesList> call, Response<PopularMoviesList> response) {

                List<Movie> movies = response.body().getMovies();
                Log.d("TAG", movies.get(0).getOriginalLanguage());
                adapter = new MoviesAdapter(movies, R.layout.movie_recycler_view_item, MainActivity.this);
                sortProgress.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
              //  adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PopularMoviesList> call, Throwable t) {
                Log.d("Error", t.getMessage());
                setContentView(R.layout.layout_no_network);


            }
        });
    }

    private void loadTopRatedMoviesList() {

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<PopularMoviesList> call = apiInterface.getTopRatedMovies(getString(R.string.API_KEY));
        call.enqueue(new Callback<PopularMoviesList>() {
            @Override
            public void onResponse(Call<PopularMoviesList> call, Response<PopularMoviesList> response) {

                List<Movie> movies = response.body().getMovies();
                Log.d("TAG", movies.get(0).getOriginalLanguage());
                sortProgress.setVisibility(View.GONE);

                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.movie_recycler_view_item, MainActivity.this));
             //   adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PopularMoviesList> call, Throwable t) {
                Log.d("Error", t.getMessage());
                setContentView(R.layout.layout_no_network);


            }
        });
    }


}
