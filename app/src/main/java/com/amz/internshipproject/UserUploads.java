package com.amz.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserUploads extends AppCompatActivity implements ImageAdapter.OnCardListener{
    private RecyclerView recyclerView;
    private ArrayList<Post> stories;
    private DatabaseReference feedRef;
    private Button donate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploads);
        recyclerView=findViewById(R.id.recyclerView_user_feed);
        donate=findViewById(R.id.donate);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stories = new ArrayList<Post>();
        feedRef = FirebaseDatabase.getInstance().getReference("Posts");
        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    Post feed = data.getValue(Post.class);
                    if (feed.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        stories.add(feed);
                       // donate.setVisibility(View.INVISIBLE);
                    }

                }
                System.out.println("sdadas "+stories.size());
                ImageAdapter adapter = new ImageAdapter(getApplicationContext(),stories,UserUploads.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onCardClick(int position) {
        Toast.makeText(this, ""+stories.get(position).getUid(), Toast.LENGTH_SHORT).show();

    }
}
