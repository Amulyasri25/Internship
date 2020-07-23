package com.amz.internshipproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amz.internshipproject.ImageAdapter;
import com.amz.internshipproject.R;
import com.amz.internshipproject.UserUploads;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProfileFragment extends Fragment  {
private TextView name,location,email,phone,add;
private FirebaseAuth fAuth;
private DatabaseReference userRef;
    private ProfileViewModel profileViewModel;
    private Button uploads;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
     // final TextView textView = root.findViewById(R.id.text_home);
        name=root.findViewById(R.id.tv_name);
        location=root.findViewById(R.id.tv_loc);
        email=root.findViewById(R.id.user_mail);
        phone=root.findViewById(R.id.user_ph);
        uploads=root.findViewById(R.id.user_uploads);
        add=root.findViewById(R.id.tv_address);
        fAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = fAuth.getCurrentUser();
        if(user!=null){
            userRef= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String name1 = snapshot.child("name").getValue().toString();
                        String email1 = snapshot.child("email").getValue().toString();
                        String phone1 = snapshot.child("mobileNumber").getValue().toString();
                        String loca1=snapshot.child("state").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();
                        name.setText(name1);
                        location.setText(loca1);
                        email.setText(email1);
                        phone.setText(phone1);
                        add.setText(address);
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), (CharSequence) e,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), UserUploads.class));
            }
        });
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            //    textView.setText(s);
            }
        });
        return root;
    }
}
