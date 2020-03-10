package uk.ac.kent.hn92.imageviewer.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.kent.hn92.imageviewer.model.Comment;
import uk.ac.kent.hn92.imageviewer.model.CommentsResponse;
import uk.ac.kent.hn92.imageviewer.model.FavouritesResponse;
import uk.ac.kent.hn92.imageviewer.model.Photo;
import uk.ac.kent.hn92.imageviewer.model.PhotosResponse;

public class PhotoRepository {

    private static PhotoRepository instance;

    public static PhotoRepository getInstance(){
        if (instance==null){
            instance = new PhotoRepository();
        }
        return instance;
    }

    private Retrofit retrofit;
    private FlickrService flickrService;
    public ArrayList<Photo> photoList = new ArrayList<>();
    public ArrayList<Comment> commentList = new ArrayList<>();
   public ArrayList<Photo> favouriteList = new ArrayList<>();

    public PhotoRepository(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

   retrofit = new Retrofit.Builder()
           .baseUrl(FlickrService.BASE_URL)
           .addConverterFactory(GsonConverterFactory.create(gson))
           .build();

                flickrService =retrofit.create(FlickrService.class);
    }


    public Call<PhotosResponse> getInterestingPhotos(int page){

        return flickrService.getInterestingPhotos(page);
    }

    public Call<CommentsResponse> getComments(String id){

        return flickrService.getComments(id);
    }

    public Call<PhotosResponse> getSearch(String text){

        return flickrService.getSearch(text);
    }




}
