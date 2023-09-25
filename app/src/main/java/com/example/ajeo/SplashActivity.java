package com.example.ajeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        mAuth = FirebaseAuth.getInstance();
//        if (currentUser != null) {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            SystemClock.sleep(3000);
//
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(SplashActivity.this, Home.class);
//            SystemClock.sleep(3000);
//
//            startActivity(intent);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // progressBar.setVisibility(View.VISIBLE);
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
            Intent intent = new Intent(SplashActivity.this, Inicio.class);
//            SystemClock.sleep(3000);
            startActivity(intent);
            finish();
        } else {
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

//            SystemClock.sleep(3000);
            startActivity(intent);
            finish();
        }

//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
            }
        },3000);



    }//OnCreate
}//Splash