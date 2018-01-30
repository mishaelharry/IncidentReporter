package com.app.incidentreporter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.incidentreporter.R;

public class IncidentHistoryFragment extends Fragment {

    public static final String TITLE = "Incident History";

    public IncidentHistoryFragment() {
        // Required empty public constructor
    }

    public static IncidentHistoryFragment newInstance() {
        IncidentHistoryFragment fragment = new IncidentHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_incident_history, container, false);
    }

}
