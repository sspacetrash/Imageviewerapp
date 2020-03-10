package uk.ac.kent.hn92.imageviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import uk.ac.kent.hn92.imageviewer.model.Photo;

import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class DetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TextView discText;
    private FloatingActionButton downloadButton;
    private FloatingActionButton shareButton;
    private Button commentButton;
    public Photo photo;
    TextView imageTitle;
    ImageView imageThumb;
    TextView authorTitle;
    public int photoPosition;


    private PhotoRepository repository;

    private static final int RESULT_SETTINGS = 1;


    private int requestCode;
    private int grantResults[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setTheme();

        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();
        photoPosition = intent.getIntExtra("PHOTO_POSITION", 0);

        photo = PhotoRepository.getInstance().photoList.get(photoPosition);

        repository = PhotoRepository.getInstance();


        imageTitle = findViewById(R.id.imageTitle);
        imageTitle.setText(photo.title);

        imageThumb = findViewById(R.id.mainImage);
        Picasso.get()
                .load(photo.url_l).into(imageThumb);

        discText = findViewById(R.id.discriptionText);
        discText.setText(photo.description.content);

        authorTitle = findViewById(R.id.authorName);
        authorTitle.setText(photo.ownername);

        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(commentsListener);

        downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(downloadListener);

        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(shareListener);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);


        onRequestPermissionsResult(requestCode, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);

        Log.e("code", "req code" + requestCode);
        Log.e("code", " gran" + grantResults);


    }

    private View.OnClickListener commentsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), CommentsActivity.class);
            intent.putExtra("PHOTO_POSITION", photoPosition);
            startActivity(intent);
        }
    };

    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            intent.putExtra(intent.EXTRA_TEXT, photo.url_l);
            startActivity(Intent.createChooser(intent, "Share image URL"));
        }
    };


    private View.OnClickListener downloadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(DetailsActivity.this);
            dlgBuilder.setTitle("Download");
            dlgBuilder.setMessage("Do you want to download this photo?");

            dlgBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    imageDownload(photo.url_l);


                }
            });

            dlgBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //closes dialog
                }
            });
            AlertDialog dialog = dlgBuilder.create();
            dialog.show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted.
                    imageDownload(photo.url_l);
                    Toast.makeText(this, "yes granted", Toast.LENGTH_SHORT).show();

                } else {
                    //Denied.
                    Toast.makeText(this, "NOOOO", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void imageDownload(String url) {

        Picasso.get()
                .load(url)
                .into(getTarget(url));

    }

    private static Target getTarget(final String url) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "photo.jpg");
                        String f = String.valueOf(file);
                        try {
                            Log.e("path", f);
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();

                        } catch (Exception e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_photos) {
            // Handle the selection

        }
        else if (id == R.id.nav_preferences) {
            // Handle the selection
            Intent intent = new Intent(DetailsActivity.this, PreferenceActivity.class);
            startActivityForResult(intent, RESULT_SETTINGS); //use forResult to trigger onActivity
            return false;
        }
        else if (id == R.id.nav_favourites) {
            // Handle the selection
            Intent intent = new Intent(DetailsActivity.this, FavouritesActivity.class);
            startActivity(intent);
            return false;
        }
        else if (id == R.id.nav_search) {
            // Handle the selection
            Intent intent = new Intent (DetailsActivity.this, SearchActivity.class);
            int act = 1;
            intent.putExtra("ACTIVITYCODE",act);
            startActivity(intent);
            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


}



