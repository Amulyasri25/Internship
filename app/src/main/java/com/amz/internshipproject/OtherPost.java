package com.amz.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherPost extends AppCompatActivity implements ImageAdapter.OnCardListener{
    private RecyclerView recyclerView;
    private ArrayList<Post> stories;
    private DatabaseReference feedRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_post);
        recyclerView = findViewById(R.id.recyclerView_other_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stories = new ArrayList<Post>();
        feedRef = FirebaseDatabase.getInstance().getReference("Posts");
        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Post feed = data.getValue(Post.class);
                    if (feed.getSelectedType().equals("Food Required") ){
                        stories.add(feed);
                    }
                    if(feed.getSelectedType().equals("Food Available")) {
                        stories.add(feed);
                    }
                }
                ImageAdapter adapter = new ImageAdapter(getApplicationContext(), stories, OtherPost.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCardClick(int position) {

    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
