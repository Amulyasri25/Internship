package com.amz.internshipproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class InitialSplash extends AppCompatActivity {
    private static int splash_time_out =2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(InitialSplash.this,SignIn.class);
                startActivity(in);
                finish();
            }
        },splash_time_out);
    }
}

