package uk.ac.kent.hn92.imageviewer.model;

import java.util.ArrayList;

public class FavouritesResponse {
    public Photo photo;
    public String stat;

    public Photo getPhoto(){
        if (photo!=null)
            return photo;
        else
            return new Photo();
    }
}
