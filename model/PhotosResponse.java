package uk.ac.kent.hn92.imageviewer.model;

import java.util.ArrayList;

public class PhotosResponse {
    public PhotoSet photos;
    public String stat;

    public ArrayList<Photo> getPhotoList(){
        if (photos!=null)
            return photos.photo;
        else
            return new ArrayList<Photo>();
    }
}
