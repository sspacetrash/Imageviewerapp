package uk.ac.kent.hn92.imageviewer.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Comment {
    @SerializedName("authorname")
    public String name;
    @SerializedName("_content")
    public String content;



}
