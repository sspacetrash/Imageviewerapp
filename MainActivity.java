package uk.ac.kent.hn92.imageviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;



import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.kent.hn92.imageviewer.model.PhotosResponse;
import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ImageListAdapter adapter;
    private PhotoRepository repository;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private String theme;
    private int act = 0; //main activity = 0

    private static final int RESULT_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_top);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photos");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle tog1 = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                double randomDouble = Math.random();
                randomDouble = randomDouble * 20 + 1;
                int randomInt = (int) randomDouble;

                repository = PhotoRepository.getInstance();
                Call<PhotosResponse> call = repository.getInterestingPhotos(randomInt);
                call.enqueue(photosResponseCallback);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);




        recyclerView = findViewById(R.id.photoListView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation((LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageListAdapter(this);
        recyclerView.setAdapter(adapter);


        //get the call from the repository
        repository = PhotoRepository.getInstance();
        Call<PhotosResponse> call = repository.getInterestingPhotos(1);


        //Start the network activity
        call.enqueue(photosResponseCallback);

    }



    //apply preferences when changed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SETTINGS:
                setTheme();
                break;
        }

    }

    public void setTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        theme = prefs.getString("APP_THEME", "purpleTheme");

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_photos) {
            // Handle the selection

        }
        else if (id == R.id.nav_preferences) {
            // Handle the selection
            Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
            startActivityForResult(intent, RESULT_SETTINGS); //use forResult to trigger onActivity
            return false;
        }
        else if (id == R.id.nav_favourites) {
            // Handle the selection
            Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
            startActivity(intent);
            return false;
        }else if (id == R.id.nav_about) {
            // Handle the selection
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return false;
        }
        else if (id == R.id.nav_search) {
            // Handle the selection
            Intent intent = new Intent (MainActivity.this, SearchActivity.class);
            act = 1;
            intent.putExtra("ACTIVITYCODE",act);
            startActivity(intent);
            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //method for debugging
    private Callback<PhotosResponse> photosResponseCallback = new Callback<PhotosResponse>() {
        @Override
        public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
            PhotosResponse resp = response.body();
            if (resp != null) {
                repository.photoList.clear();
                repository.photoList.addAll(resp.getPhotoList());
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onFailure(Call<PhotosResponse> call, Throwable t) {
            final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this);
            dlgBuilder.setTitle("Network error");
            dlgBuilder.setMessage("Image could not be downloaded this time, please try again later.");

            dlgBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    repository = PhotoRepository.getInstance();
                    Call<PhotosResponse> call = repository.getInterestingPhotos(1);


                    //Start the network activity
                    call.enqueue(photosResponseCallback);
                }
            });

            dlgBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //closes dialog
                    System.exit(0);
                }
            });
            AlertDialog dialog = dlgBuilder.create();
            dialog.show();

        }
    };

}
