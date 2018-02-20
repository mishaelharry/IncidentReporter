package com.app.incidentreporter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.incidentreporter.R;
import com.app.incidentreporter.models.Incident;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Collection;

public class IncidentHistoryFragment extends Fragment {

    public static final String TITLE = "Incident History";

    private RecyclerView incidentList;

    private DatabaseReference database;
    private FirebaseAuth auth;
    private Query query;


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
        View rootView = inflater.inflate(R.layout.fragment_incident_history, container, false);
        auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference().child("Incidents");
        query = database.orderByChild("uid").equalTo(uid);

        incidentList = rootView.findViewById(R.id.incident_list);
        incidentList.setHasFixedSize(true);
        incidentList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Incident, IncidentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Incident, IncidentViewHolder>(
                Incident.class,
                R.layout.incident_item,
                IncidentViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(IncidentViewHolder viewHolder, Incident model, int position) {
                viewHolder.setLocation(model.getLocation());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setImage(getContext(), model.getImage());
            }
        };
        incidentList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class IncidentViewHolder extends RecyclerView.ViewHolder{
        public View mView;

        public IncidentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDescription(String title){
            TextView descriptionInput = (TextView) mView.findViewById(R.id.description_input);
            descriptionInput.setText(title);
        }

        public void setLocation(String location){
            TextView locationInput = (TextView) mView.findViewById(R.id.location_input);
            locationInput.setText(location);
        }

        public void setImage(Context context, String image){
            ImageView imageInput = (ImageView) mView.findViewById(R.id.image_input);
            Glide.with(context).load(image).into(imageInput);
        }
    }

}
