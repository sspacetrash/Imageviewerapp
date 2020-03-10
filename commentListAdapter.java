package uk.ac.kent.hn92.imageviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.kent.hn92.imageviewer.model.Comment;
import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class commentListAdapter extends RecyclerView.Adapter<commentListAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView commentAuthor;
        TextView commentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.authorName);
            commentContent = itemView.findViewById(R.id.commentText);
        }
    }

    private Context context;

    public commentListAdapter(Context context){
        super();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellLayout = LayoutInflater.from(context)
                .inflate(R.layout.cell_comment_card, parent, false);
        ViewHolder vh = new ViewHolder(cellLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment item = PhotoRepository.getInstance().commentList.get(position);
        holder.commentAuthor.setText(item.name);
        holder.commentContent.setText(item.content);
    }

    @Override
    public int getItemCount() {
        return PhotoRepository.getInstance().commentList.size();
    }
}
