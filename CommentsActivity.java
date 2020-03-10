package uk.ac.kent.hn92.imageviewer;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.kent.hn92.imageviewer.model.CommentsResponse;
import uk.ac.kent.hn92.imageviewer.model.Photo;
import uk.ac.kent.hn92.imageviewer.network.PhotoRepository;

public class CommentsActivity extends AppCompatActivity {

    private Photo photo;
    private PhotoRepository repository;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private commentListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        int photoPosition = intent.getIntExtra("PHOTO_POSITION", 0);
        photo = PhotoRepository.getInstance().photoList.get(photoPosition);


        repository = PhotoRepository.getInstance();
        Call<CommentsResponse> call = repository.getComments(photo.id);
        call.enqueue(commentsResponseCallback);

        //comment = PhotoRepository.getInstance().commentList.get(photoPosition);

        recyclerView = findViewById(R.id.commentsRecycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager((layoutManager));

        adapter = new commentListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private Callback<CommentsResponse> commentsResponseCallback = new Callback<CommentsResponse>() {
        @Override
        public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {
            CommentsResponse resp = response.body();

            if (resp !=null){
                repository.commentList.addAll(resp.getCommentList());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<CommentsResponse> call, Throwable t) {
            Log.d("DetailsActivity", "Error comments");
        }
    };

}
