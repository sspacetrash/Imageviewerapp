package uk.ac.kent.hn92.imageviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.kent.hn92.imageviewer.model.PhotosResponse;
import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class SearchActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ImageListAdapter adapter;
    private PhotoRepository repository;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();

        setContentView(R.layout.activity_search);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.searchListView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation((LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageListAdapter(this);
        recyclerView.setAdapter(adapter);

        repository = PhotoRepository.getInstance();
        Call<PhotosResponse> call = repository.getInterestingPhotos(1);


        //Start the network activity
        call.enqueue(photosResponseCallback);

//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);


        //Get the menu item for search button
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);


        //Retrieve the SearchView widget
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);

        searchView.setIconified(false);


        SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //The user has submitted a search string
                repository = PhotoRepository.getInstance();
                Call<PhotosResponse> call = repository.getSearch(query);
                call.enqueue(photosResponseCallback);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // The search string is changing as the user types
                return false;
            }
        };

        searchView.setOnQueryTextListener(textListener);

        return super.onCreateOptionsMenu(menu);
    }

    //method for debugging
    private Callback<PhotosResponse> photosResponseCallback = new Callback<PhotosResponse>() {
        @Override
        public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
            PhotosResponse resp = response.body();
            if (resp != null) {

                adapter.clearList();
                repository.photoList.addAll(resp.getPhotoList());
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onFailure(Call<PhotosResponse> call, Throwable t) {
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_photos) {
            // Handle the selection

        }
        else if (id == R.id.nav_preferences) {
            // Handle the selection
            Intent intent = new Intent(SearchActivity.this, PreferenceActivity.class);
            startActivityForResult(intent, 1); //use forResult to trigger onActivity
            return false;
        }
        else if (id == R.id.nav_favourites) {
            // Handle the selection
            Intent intent = new Intent(SearchActivity.this, FavouritesActivity.class);
            startActivity(intent);
            return false;
        }
        else if (id == R.id.nav_search) {
            // Handle the selection
            Intent intent = new Intent (SearchActivity.this, SearchActivity.class);
            int act = 1;
            intent.putExtra("ACTIVITYCODE",act);
            startActivity(intent);
            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString("APP_THEME", "purpleTheme");

        if (theme.equals("appTheme")) {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        if (theme.equals("purpleTheme")) {
            setTheme((R.style.purpleTheme));
        }
        if (theme.equals("orangeTheme")) {
            setTheme((R.style.orangeTheme));
        }
    }
}
