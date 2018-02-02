package com.app.incidentreporter;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mishael on 1/31/2018.
 */

public class IncidentReporter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
