package com.amz.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
String id=" ";
private String user_name,user_loc,user_mail,user_phone,addr1;
private TextView name1,loc1,mail1,phone1,addr;
private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name1=findViewById(R.id.name);
        loc1=findViewById(R.id.address);
        mail1=findViewById(R.id.mail);
        phone1=findViewById(R.id.phone);
        addr=findViewById(R.id.taddress1);


        if(getIntent().getExtras()!=null){
          id=getIntent().getExtras().getString("user_id");
          userRef= FirebaseDatabase.getInstance().getReference().child("users").child(id);
          userRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  user_name=snapshot.child("name").getValue().toString();
                  user_loc=snapshot.child("state").getValue().toString();
                  user_mail=snapshot.child("email").getValue().toString();
                  user_phone=snapshot.child("mobileNumber").getValue().toString();
                  addr1=snapshot.child("address").getValue().toString();


                  name1.setText(user_name);
                  loc1.setText(user_loc);
                  mail1.setText(user_mail);
                  phone1.setText(user_phone);
                  addr.setText(addr1);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });


        }
    }
}
