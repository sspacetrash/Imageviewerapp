package uk.ac.kent.hn92.imageviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import uk.ac.kent.hn92.imageviewer.db.FavouritesDBHelper;

public class AboutActivity extends AppCompatActivity {

    ImageView aboutThumb;
    TextView aboutName;
    TextView aboutEmail;
    TextView aboutFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        FavouritesDBHelper dbHelper = new FavouritesDBHelper(this);
        String name = prefs.getString("USER_NAME","EMPTY");
        String email = prefs.getString("USER_EMAIL","EMPTY");
        String favNo = String.valueOf(dbHelper.getSize());

        aboutThumb = findViewById(R.id.aboutThumb);

        aboutName = findViewById(R.id.aboutName);
        aboutName.setText(name);


        aboutEmail = findViewById(R.id.aboutEmail);
        aboutEmail.setText(email);

        aboutFav = findViewById(R.id.aboutFav);

        aboutFav.setText(favNo);
    }
}
