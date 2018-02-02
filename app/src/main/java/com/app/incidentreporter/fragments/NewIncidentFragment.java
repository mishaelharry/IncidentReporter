package com.app.incidentreporter.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.incidentreporter.R;
import com.app.incidentreporter.models.Incident;
import com.app.incidentreporter.utils.AppUtility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;


import static android.app.Activity.RESULT_OK;

public class NewIncidentFragment extends Fragment {

    public static final String TITLE = "New Incident";
    private static final int CAMERA = 0, GALLERY = 1 , GEO_LOCATION = 3;
    private AppCompatButton cameraButton, galleryButton, cancelButton, submitButton;
    private EditText descriptionInput, locationInput;
    private ImageView imageInput;
    private TextInputLayout descriptionWrapper, imageWrapper, locationWrapper;
    private String descriptionValue, locationValue;
    private Uri imageValue;
    private View rootView, modalView;
    private BottomSheetDialog dialog = null;
    private ProgressDialog progressDialog;

    private StorageReference storageReference;
    private DatabaseReference database;
    private FirebaseAuth auth;
    public NewIncidentFragment() {
        // Required empty public constructor
    }

    public static NewIncidentFragment newInstance() {
        NewIncidentFragment fragment = new NewIncidentFragment();
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
        rootView = inflater.inflate(R.layout.fragment_new_incident, container, false);

        progressDialog = new ProgressDialog(getActivity());

        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Incident");
        auth = FirebaseAuth.getInstance();

        descriptionInput =  rootView.findViewById(R.id.description_input);
        imageInput = rootView.findViewById(R.id.image_input);
        locationInput = rootView.findViewById(R.id.location_input);

        descriptionWrapper = rootView.findViewById(R.id.description_wrapper);
        imageWrapper = rootView.findViewById(R.id.image_wrapper);
        locationWrapper = rootView.findViewById(R.id.location_wrapper);

        submitButton = rootView.findViewById(R.id.button_submit);

        modalView = getLayoutInflater().inflate(R.layout.add_photo_bottomsheet, null);
        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(modalView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        cameraButton = modalView.findViewById(R.id.button_camera);
        galleryButton = modalView.findViewById(R.id.button_gallery);
        cancelButton = modalView.findViewById(R.id.button_cancel);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraClicked();
                dialog.dismiss();
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryClicked();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSubmission();
            }
        });
        return rootView;
    }

    private void validateSubmission() {
        boolean valid = true;

        descriptionValue = descriptionInput.getText().toString().trim();
        descriptionWrapper.setError(null);
        if (descriptionValue.length() == 0) {
            descriptionWrapper.setError("Incident description is required.");
            valid = false;
        }

        imageWrapper.setError(null);
        if (imageValue == null) {
            imageWrapper.setError("Incident photo is required.");
            valid = false;
        }

        locationValue = locationInput.getText().toString().trim();
        locationWrapper.setError(null);
        if (locationValue.length() == 0) {
            locationWrapper.setError("Location is required.");
            valid = false;
        }

        if (valid){
            submitForm();
        }
    }

    android.app.AlertDialog alertDialog;
    public void setSingleMessage(String title, String message, Context context){
        //creates a message dialog
        alertDialog = new android.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int which) {
                // here you can add functions
                descriptionInput.setText(null);
                locationInput.setText(null);
                imageInput.setImageResource(R.drawable.ic_add_a_photo_black_12dp);
                descriptionValue = null;
                locationValue = null;
                imageValue = null;
            }
        });
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.show();
    }

    private void submitForm() {
        if (AppUtility.isNetworkAvailable(getActivity())) {
            progressDialog.setMessage("Reporting Incident...");
            progressDialog.show();

            StorageReference filePath = storageReference.child("Incident_images").child(imageValue.getLastPathSegment());
            filePath.putFile(imageValue).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri incidentUrl = taskSnapshot.getDownloadUrl();
                    String uid = auth.getCurrentUser().getUid();
                    Incident incident = new Incident(uid, descriptionValue, incidentUrl, locationValue);

                    DatabaseReference newIncident = database.push();
                    newIncident.child("uid").setValue(incident.getUid());
                    newIncident.child("description").setValue(incident.getDescription());
                    newIncident.child("image").setValue(incident.getUri().toString());
                    newIncident.child("location").setValue(incident.getLocation());

                    progressDialog.dismiss();
                    setSingleMessage("Sent", "Incident report was sent successfully", getContext());
                }
            });

        } else {
            Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void onCameraClicked() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA);
        }
        else {
            Intent takePicture = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(takePicture, CAMERA);
        }
    }

    public void onGalleryClicked() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onCameraClicked();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(),"Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case GEO_LOCATION :

                return;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case CAMERA:
                if(resultCode == RESULT_OK){
                    setImageUri(imageReturnedIntent, false);
                }
                break;
            case GALLERY:
                if(resultCode == RESULT_OK){
                    setImageUri(imageReturnedIntent, true);
                }
                break;
        }
    }

    public void setImageUri(Intent imageReturnedIntent, boolean type) {
        try {
            Bitmap imageBitmap = null;
            if (type){
                Uri imageUri = imageReturnedIntent.getData();
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } else {
                Bundle extras = imageReturnedIntent.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
            }

            ContextWrapper cw = new ContextWrapper(getActivity());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File file = new File(directory, System.currentTimeMillis() + ".png");// System.currentTimeMillis() + ".png");
            imageValue = Uri.fromFile(file);
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            // Use the compress method on the BitMap object to write image to the OutputStream
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            imageInput.setImageBitmap(imageBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
