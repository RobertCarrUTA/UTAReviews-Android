package com.example.cse3311project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class AccountActivity extends AppCompatActivity implements UsernameDialog.ExampleDialogListener
{
    Button resendCode;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    private TextView editTextTextEmailAddress;
    private TextView editTextTextUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Declaration for variable
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Button returnHomeButton_AccountPage = (Button) findViewById(R.id.returnHomeButton_AccountPage);
        editTextTextEmailAddress = (TextView) findViewById(R.id.editTextTextEmailAddress);
        editTextTextUsername = (TextView) findViewById(R.id.editTextTextUsername);
        Button Credentials = (Button) findViewById(R.id.ChangingAccountCredentials);
        Button Avatar = (Button) findViewById(R.id.changeAvatarButton_AccountPage);

        editTextTextEmailAddress.setEnabled(false);
        editTextTextUsername.setEnabled(false);

        // Import Database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //declare variables for resending verified Code for the email
        resendCode = findViewById(R.id.resendCode);
        userID = fAuth.getCurrentUser().getEmail();
        FirebaseUser user = fAuth.getCurrentUser();

        // this if statement is verified the email is not empty and it is an email address
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            editTextTextEmailAddress.setText(userEmail);

            //String username = editTextTextEmailAddress.toString();
            /*
            assert userEmail != null;
            String[] splits = userEmail.split("@");
            String usernameString = "@" + splits[0];
            editTextTextUsername.setText(usernameString);
             */
        }

        Credentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        if(!user.isEmailVerified())
        {
            //setting Verify Now invisible for email that is not verified
            resendCode.setVisibility(View.VISIBLE);
            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(), "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure: Email not sent "+e.getMessage());
                        }
                    });
                }
            });
        }
        returnHomeButton_AccountPage.setOnClickListener(v -> openHomeActivity());
    }

    // this function calls the dialog and merge it into the accountactivity
    public void showDialog() {
        UsernameDialog usernameDialog = new UsernameDialog();
        usernameDialog.show(getSupportFragmentManager(),"Username Changing Dialog");
    }

    // Use this function to open the home (Main) activity

    public void openHomeActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void applyTexts(String username) {
        editTextTextUsername.setText(username);
    }
}