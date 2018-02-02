package com.app.incidentreporter.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.incidentreporter.R;
import com.app.incidentreporter.utils.AppUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, addressInput, stateInput,
            dobInput, occupationInput, emailInput, passwordInput;
    private TextInputLayout nameWrapper, addressWrapper, stateWrapper,
            dobWrapper, occupationWrapper, emailWrapper, passwordWrapper;
    private String nameValue, addressValue, stateValue,
            dobValue, occupationValue, emailValue, passwordValue;
    private AppCompatButton registerButton;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("User");

        progressDialog = new ProgressDialog(this);

        nameWrapper = findViewById(R.id.name_wrapper);
        addressWrapper = findViewById(R.id.address_wrapper);
        stateWrapper = findViewById(R.id.state_wrapper);
        dobWrapper = findViewById(R.id.dob_wrapper);
        occupationWrapper = findViewById(R.id.occupation_wrapper);
        emailWrapper = findViewById(R.id.email_wrapper);
        passwordWrapper = findViewById(R.id.password_wrapper);

        nameInput = findViewById(R.id.name_input);
        addressInput = findViewById(R.id.address_input);
        stateInput = findViewById(R.id.state_input);
        dobInput = findViewById(R.id.dob_input);
        occupationInput = findViewById(R.id.occupation_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSubmission();
            }
        });
    }

    private void validateSubmission(){
        boolean valid = true;

        nameValue = nameInput.getText().toString().trim();
        nameWrapper.setError(null);
        if (nameValue.length() == 0) {
            nameWrapper.setError("Name is required.");
            valid = false;
        }

        addressValue = addressInput.getText().toString().trim();
        addressWrapper.setError(null);
        if (addressValue.length() == 0) {
            addressWrapper.setError("Address is required.");
            valid = false;
        }

        stateValue = stateInput.getText().toString().trim();
        stateWrapper.setError(null);
        if (stateValue.length() == 0) {
            stateWrapper.setError("State is required.");
            valid = false;
        }

        dobValue = dobInput.getText().toString().trim();
        dobWrapper.setError(null);
        if (dobValue.length() == 0) {
            dobWrapper.setError("Date of birth is required.");
            valid = false;
        }

        occupationValue = occupationInput.getText().toString().trim();
        occupationWrapper.setError(null);
        if (occupationValue.length() == 0) {
            occupationWrapper.setError("Occupation is required.");
            valid = false;
        }

        emailValue = emailInput.getText().toString().trim();
        emailWrapper.setError(null);
        if (emailValue.length() == 0) {
            emailWrapper.setError("Email is required.");
            valid = false;
        }

        passwordValue = passwordInput.getText().toString().trim();
        passwordWrapper.setError(null);
        if (passwordValue.length() == 0) {
            passwordWrapper.setError("Password is required.");
            valid = false;
        }

        if (valid){
            submitForm();
        }
    }

    private void submitForm() {
        if (AppUtility.isNetworkAvailable(this)) {
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String userId = auth.getCurrentUser().getUid();

                        DatabaseReference currentUser = databaseUser.child(userId);
                        currentUser.child("Name").setValue(nameValue);
                        currentUser.child("Address").setValue(addressValue);
                        currentUser.child("State").setValue(stateValue);
                        currentUser.child("DateOfBirth").setValue(dobValue);
                        currentUser.child("Occupation").setValue(occupationValue);
                        progressDialog.dismiss();

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to complete registration.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
