package uk.ac.kent.hn92.imageviewer.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uk.ac.kent.hn92.imageviewer.model.Comment;
import uk.ac.kent.hn92.imageviewer.model.CommentsResponse;
import uk.ac.kent.hn92.imageviewer.model.FavouritesResponse;
import uk.ac.kent.hn92.imageviewer.model.PhotosResponse;

public interface FlickrService {
    static final String BASE_URL = "https://api.flickr.com/services/rest/";
    static final String API_KEY = "&api_key=9ea9b22636750761697091f4dc057993";
    static final String JSON_PARAMS = "&format=json&nojsoncallback=?";
    static final String PHOTO_LIST_PARAMS ="&extras=description,owner_name,url_m,url_l,url_o,date_taken&per_page=10";
    static final String SAFE_SEARCH ="&safe_search=1";



    static final String GET_INTERESTING_PHOTOS = "?method=flickr.interestingness.getList" + API_KEY + JSON_PARAMS + PHOTO_LIST_PARAMS;
    static final String GET_COMMENTS = "?method=flickr.photos.comments.getList" + API_KEY + JSON_PARAMS;
    static final String GET_SEARCH = "?method=flickr.photos.search" + API_KEY + JSON_PARAMS + PHOTO_LIST_PARAMS + SAFE_SEARCH;


    @GET(GET_INTERESTING_PHOTOS)
    Call<PhotosResponse> getInterestingPhotos(@Query("page") Integer page);

    @GET(GET_COMMENTS)
    Call<CommentsResponse> getComments(@Query("photo_id") String commentID);

    @GET(GET_SEARCH)
    Call<PhotosResponse> getSearch(@Query("text") String search);


}
