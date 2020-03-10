package uk.ac.kent.hn92.imageviewer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import uk.ac.kent.hn92.imageviewer.model.Photo;
import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton favourtitesBtn;
        ImageView favThumb;

        private View.OnClickListener onClick = new View.OnClickListener() {
            public void onClick(View view) {
                int position = FavouritesListAdapter.ViewHolder.this.getLayoutPosition();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("PHOTO_POSITION", position);
                //Toast.makeText(context,"position vh"+position,Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favThumb = itemView.findViewById(R.id.favImage);
            this.itemView.setOnClickListener(onClick);
        }
    }


    private Context context;

    public FavouritesListAdapter(Context context) {
        super();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cellLayout = LayoutInflater.from(context).inflate(R.layout.cell_favourites_card, parent, false);
        ViewHolder vh = new ViewHolder(cellLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesListAdapter.ViewHolder holder, int position) {
        Photo item = PhotoRepository.getInstance().favouriteList.get(position);
        Picasso.get()
                .load(item.url_m).into(holder.favThumb);
    }

    @Override
    public int getItemCount() {
        return PhotoRepository.getInstance().favouriteList.size();
    }


}
