package com.app.incidentreporter.activites;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.incidentreporter.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exitSplashScreen();
            }
        }, 3000);
    }

    private void exitSplashScreen(){
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    launchLogin();
                } else {
                    launchHome();
                }
            }
        };
        auth.addAuthStateListener(authListener);
    }

    public void launchLogin(){
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    public void launchHome(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
