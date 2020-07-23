package com.amz.internshipproject.ui.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amz.internshipproject.BloodPost;
import com.amz.internshipproject.NgoPost;
import com.amz.internshipproject.OldAgePosts;
import com.amz.internshipproject.Orphans;
import com.amz.internshipproject.OtherPost;
import com.amz.internshipproject.R;

public class SearchFragment extends Fragment {
CardView orphan,old,ngo,blood,other;
    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
      //  final TextView textView = root.findViewById(R.id.text_home);
        orphan=root.findViewById(R.id.orphanID);
        old=root.findViewById(R.id.OldageID);
        ngo=root.findViewById(R.id.ngoId);
        blood=root.findViewById(R.id.BloodbankID);
        other=root.findViewById(R.id.OtherID);

        orphan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Orphans.class));
            }
        });
        old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(getContext(), OldAgePosts.class));
            }
        });
        ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(getContext(), NgoPost.class));
            }
        });
        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getContext(), BloodPost.class));
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OtherPost.class));

            }
        });

        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setText(s);
            }
        });
        return root;
    }
}
