package uk.ac.kent.hn92.imageviewer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import uk.ac.kent.hn92.imageviewer.model.Photo;
import uk.ac.kent.hn92.imageviewer.model.PhotoDescription;


import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class FavouritesDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "favourites_db";
    String[] db_column= {"id", "title", "author", "description","url"};
    String[] db_id = {"id"};


    public FavouritesDBHelper(Context context){
        super(context.getApplicationContext(),DB_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String createQuery = "CREATE TABLE favourites (" +
            "id TEXT PRIMARY KEY,"
            + "title TEXT,"
            + "author TEXT,"
            //+ "date_added DATE,"
            + "description TEXT,"
            + "url TEXT"
            + ")";

    db.execSQL(createQuery);
    }

    public void insert(Photo photo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id",photo.id);
        values.put("title", photo.title);
        values.put("author", photo.ownername);
        values.put("description", photo.description.content);
        values.put("url", photo.url_m);

        db.insertWithOnConflict("favourites", null,values,CONFLICT_REPLACE);
        db.close();

    }

    public int getSize(){
        SQLiteDatabase db = getWritableDatabase();
        String countQuery = "SELECT  * FROM " + "favourites";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }


    public void delete(Photo photo){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("favourites","id"+ "=" + photo.id,null);
        db.close();

    }

    public ArrayList<Photo> loadAll(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Photo> list = new ArrayList<>();

        Cursor cursor =db.query("favourites",db_column,null,null,null,null,null);
        for(int i=0; i < cursor.getCount();i++){
            Photo photo = new Photo();
            photo.description = new PhotoDescription();
            cursor.moveToNext();
            photo.id = cursor.getString(0);
            photo.title = cursor.getString(1);
            photo.ownername = cursor.getString(2);
            photo.description.content = cursor.getString(3);
            photo.url_m = cursor.getString(4);

            list.add(photo);
        }
        db.close();
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
