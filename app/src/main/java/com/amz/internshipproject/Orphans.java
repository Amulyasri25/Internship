package com.amz.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.amz.internshipproject.ui.Feed.FeedFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Orphans extends AppCompatActivity implements ImageAdapter.OnCardListener{
    private RecyclerView recyclerView;
    private ArrayList<Post> stories;
    private DatabaseReference feedRef,userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphans);
        recyclerView=findViewById(R.id.recyclerView_orphans_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stories = new ArrayList<Post>();
        feedRef = FirebaseDatabase.getInstance().getReference("Posts");
        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    Post feed = data.getValue(Post.class);
                    if(feed.getSelectedType().equals("Orphan")) {
                        stories.add(feed);
                    }
                }
                ImageAdapter adapter = new ImageAdapter(getApplicationContext(),stories, Orphans.this);
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
