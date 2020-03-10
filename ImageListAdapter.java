package uk.ac.kent.hn92.imageviewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import uk.ac.kent.hn92.imageviewer.db.FavouritesDBHelper;
import uk.ac.kent.hn92.imageviewer.model.Photo;
import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView imageTitle;
        ImageView imageThumb;
        ImageView favButton;
        ImageView favButton2;
        ImageView shareButton;



        private View.OnClickListener onClick = new View.OnClickListener() {
            public void onClick(View view) {
                int position = ViewHolder.this.getLayoutPosition();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("PHOTO_POSITION", position);
                //Toast.makeText(context,"position vh"+position,Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        };

        private View.OnClickListener favouritesListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //button shown when the picture is not favourited
                int position = ViewHolder.this.getLayoutPosition();
                Photo newPhoto = PhotoRepository.getInstance().photoList.get(position);
                FavouritesDBHelper dbHelper = new FavouritesDBHelper(context);
                dbHelper.insert(newPhoto);
                favButton.setVisibility(View.INVISIBLE);
                favButton2.setVisibility(View.VISIBLE);


            }
        };

        private View.OnClickListener favouritesListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ViewHolder.this.getLayoutPosition();
                Photo newPhoto = PhotoRepository.getInstance().photoList.get(position);
                FavouritesDBHelper dbHelper = new FavouritesDBHelper(context);
                dbHelper.delete(newPhoto);
                favButton2.setVisibility(View.INVISIBLE);
                favButton.setVisibility(View.VISIBLE);

            }
        };

        private View.OnClickListener shareListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");

            }
        };


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTitle = itemView.findViewById(R.id.imageTitle);
            imageThumb = itemView.findViewById(R.id.thumbImage);
            favButton = itemView.findViewById(R.id.favouriteBtn);
            favButton2 = itemView.findViewById(R.id.favouriteBtn2);
            shareButton = itemView.findViewById(R.id.shareImageBtn);
            favButton2.setVisibility(View.INVISIBLE);



            favButton.setOnClickListener(favouritesListener);
            favButton2.setOnClickListener(favouritesListener2);
            this.itemView.setOnClickListener(onClick);

            shareButton.setOnClickListener(shareListener);
        }



    }


    private Context context;


    public ImageListAdapter(Context context) {
        super();
        this.context = context;
    }

    //clear elements in Repository
    public void clearList() {
        PhotoRepository.getInstance().photoList.clear();
        notifyDataSetChanged();
    }

    //add new elements in Repository
    public void refreshList(ArrayList<Photo> newPhotos) {
        PhotoRepository.getInstance().photoList.addAll(newPhotos);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View cellLayout = LayoutInflater.from(context).inflate(R.layout.cell_image_card, parent, false);

        ViewHolder vh = new ViewHolder(cellLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        Photo item = PhotoRepository.getInstance().photoList.get(position);


        holder.imageTitle.setText(item.title);


        Picasso.get()
                .load(item.url_m).into(holder.imageThumb);

    }

    @Override
    public int getItemCount() {
        return PhotoRepository.getInstance().photoList.size();
    }
}
