package uk.ac.kent.hn92.imageviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;


import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class PreferenceActivity extends android.preference.PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setTheme();

        addPreferencesFromResource(R.xml.root_preferences);
        Toolbar toolbar =findViewById(R.id.toolbar);




    }

    void storeUserInfo(String fullName, String email, String theme){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor prefEditor = prefs.edit();

        prefEditor.putString("USER_NAME", fullName);
        prefEditor.putString("USER_EMAIL", email);
        prefEditor.putString("APP_THEME", theme);
        prefEditor.apply();
    }

    public void setTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme;
        theme = prefs.getString("APP_THEME", "purpleTheme");

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }
}