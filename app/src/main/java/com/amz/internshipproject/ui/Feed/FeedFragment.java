package com.amz.internshipproject.ui.Feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amz.internshipproject.ImageAdapter;
import com.amz.internshipproject.Post;
import com.amz.internshipproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedFragment extends Fragment implements ImageAdapter.OnCardListener {
    private RecyclerView recyclerView;
    private ArrayList<Post> stories;
    private DatabaseReference feedRef;
    private ImageButton donate;
private String name,id;
private CardView c;
private ImageAdapter imageAdapter;
    private FeedViewModel feedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feedViewModel =
                ViewModelProviders.of(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
             //donate=root.findViewById(R.id.contact);
        recyclerView=root.findViewById(R.id.recyclerView_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stories = new ArrayList<Post>();
        c=root.findViewById(R.id.post);
        feedRef = FirebaseDatabase.getInstance().getReference("Posts");
        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    Post feed = data.getValue(Post.class);
                    stories.add(feed);
                }
                //name=snapshot.child("uid").getValue().toString();
                ImageAdapter adapter = new ImageAdapter(getContext(),stories, FeedFragment.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        feedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });


        return root;
    }

    @Override
    public void onCardClick(int position) {



    }

}
