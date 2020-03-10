package uk.ac.kent.hn92.imageviewer.model;

import java.util.ArrayList;
import java.util.Date;

public class Photo {
    public String id;
    public String owner;
    public String ownername;
    public String title;
    public Date datetaken;
    public String url_m;
    public String url_l;
    public String url_o;
    public PhotoDescription description;


    public String checkImageSize(){
        if (url_l != null){
            return url_l;
        }else {
            return url_m;
        }
    }



}
