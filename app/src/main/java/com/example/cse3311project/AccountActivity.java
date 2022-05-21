package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity implements UsernameDialog.ExampleDialogListener
{
    Button resendCode;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    private TextView editTextTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Documentation for setting up a username?:
        // https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile

        //Declaration for variable
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Button returnHomeButton_AccountPage = findViewById(R.id.returnHomeButton_AccountPage);
        TextView editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextUsername = findViewById(R.id.editTextTextUsername);
        Button Credentials = findViewById(R.id.ChangingAccountCredentials);
        // Leave the below code as a comment for now until a way to use avatars is found
        //Button Avatar = findViewById(R.id.changeAvatarButton_AccountPage);

        editTextTextEmailAddress.setEnabled(false);
        editTextTextUsername.setEnabled(false);

        // Import Database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //declare variables for resending verified Code for the email
        resendCode = findViewById(R.id.resendCode);
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getEmail();
        FirebaseUser user = fAuth.getCurrentUser();

        // this if statement is verified the email is not empty and it is an email address
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            editTextTextEmailAddress.setText(userEmail);
        }

        Credentials.setOnClickListener(view -> showDialog());

        if(!user.isEmailVerified())
        {
            //setting Verify Now invisible for email that is not verified
            resendCode.setVisibility(View.VISIBLE);
            resendCode.setOnClickListener(view -> user.sendEmailVerification().addOnSuccessListener(
                    aVoid -> Toast.makeText(view.getContext(), "Verification Email has been sent.",
                            Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                                Log.d("tag","onFailure: Email not sent "+e.getMessage())));
        }
        returnHomeButton_AccountPage.setOnClickListener(v -> openHomeActivity());
    }

    // this function calls the dialog and merge it into the accountactivity
    public void showDialog()
    {
        UsernameDialog usernameDialog = new UsernameDialog();
        usernameDialog.show(getSupportFragmentManager(),"Change Username");
    }

    // Use this function to open the home (Main) activity
    public void openHomeActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void applyTexts(String username)
    {
        editTextTextUsername.setText(username);
    }
}