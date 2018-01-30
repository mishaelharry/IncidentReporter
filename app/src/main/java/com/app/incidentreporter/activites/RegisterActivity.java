package com.app.incidentreporter.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.incidentreporter.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

    }
}
