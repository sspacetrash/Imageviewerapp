package uk.ac.kent.hn92.imageviewer;


import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.kent.hn92.imageviewer.db.FavouritesDBHelper;
import uk.ac.kent.hn92.imageviewer.model.FavouritesResponse;
import uk.ac.kent.hn92.imageviewer.model.Photo;

import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class FavouritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private FavouritesListAdapter adapter;
    private PhotoRepository repository;


    public ArrayList<Photo> favPhotoList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        FavouritesDBHelper dbHelper = new FavouritesDBHelper(this);

        recyclerView = findViewById(R.id.favouritesListView);
        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new FavouritesListAdapter(this);
        recyclerView.setAdapter(adapter);

        favPhotoList = dbHelper.loadAll();
        repository = PhotoRepository.getInstance();
        repository.favouriteList.clear();

        repository.favouriteList.addAll(favPhotoList);
        adapter.notifyDataSetChanged();

    }

    public void setTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString("APP_THEME", "purpleTheme");

        if (theme.equals("appTheme")) {
            setTheme(R.style.AppTheme);
        }
        if (theme.equals("purpleTheme")) {
            setTheme((R.style.purpleTheme));
        }
        if (theme.equals("orangeTheme")) {
            setTheme((R.style.orangeTheme));
        }
    }

}
