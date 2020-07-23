package com.amz.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private String email, password, uid;
    private FirebaseAuth auth;
    private DatabaseReference userTypeRef;
    private String[] rolesList = {"GeneralUser", "NGO","Orphanage","Old age home","BloodBank","Restaurant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        etEmail = findViewById(R.id.mail);
        etPassword = findViewById(R.id.pass);
        auth= FirebaseAuth.getInstance();
        userTypeRef = FirebaseDatabase.getInstance().getReference("roles");
    }

    public void onSignInTapped(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        authenticateUser();
    }

    private void authenticateUser() {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                switchToUserHome();
                Toast.makeText(SignIn.this, "SignIn Success....", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignIn.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void switchToUserHome() {
        userTypeRef.child(auth.getCurrentUser().getUid()).child("userType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              //  String userType = snapshot.getValue(String.class);
                //if (userType.equals(rolesList[0])){
                    navigateToUserActivity();
              //  }
             //   else {
               //     navigateToOrgainsationActivity();
                //}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignIn.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToOrgainsationActivity() {

    }

    private void navigateToUserActivity() {
        //startActivity(new Intent(SignIn.this,OrgActivity.class));
        startActivity(new Intent(SignIn.this,UserActivity.class));
    }

    public void onSignUpTapped(View view) {
        startActivity(new Intent(SignIn.this,SignUpActivity.class));
    }
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            // finish();
            FirebaseUser currentUser=auth.getCurrentUser();
            updateUI(currentUser);
            startActivity(new Intent(this, UserActivity.class));
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            startActivity(new Intent(this,UserActivity.class));

        }
        else{
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }
    }