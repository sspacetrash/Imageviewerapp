package uk.ac.kent.hn92.imageviewer.model;

import java.util.ArrayList;

public class CommentsResponse {
    public CommentSet comments;
    public String stat;

    public ArrayList<Comment> getCommentList(){
        if (comments!=null)
            return comments.comment;
        else
            return new ArrayList<Comment>();
    }
}
