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
import android.widget.Toast;

import com.app.incidentreporter.R;
import com.app.incidentreporter.utils.AppUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private TextInputLayout emailWrapper, passwordWrapper;
    private String emailValue, passwordValue;
    private AppCompatButton loginButton, registerButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);

        emailWrapper = findViewById(R.id.email_wrapper);
        passwordWrapper = findViewById(R.id.password_wrapper);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSubmission();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void validateSubmission(){
        boolean valid = true;

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
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            auth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed to login.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    /*private void checkUserExist(){
        final String userId = auth.getCurrentUser().getUid();

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)){

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })
    }*/
}
