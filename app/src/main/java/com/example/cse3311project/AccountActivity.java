package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Button returnHomeButton_AccountPage = (Button) findViewById(R.id.returnHomeButton_AccountPage);
        EditText editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText editTextTextUsername = (EditText) findViewById(R.id.editTextTextUsername);

        editTextTextEmailAddress.setEnabled(false);
        editTextTextUsername.setEnabled(false);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            editTextTextEmailAddress.setText(userEmail);

            //String username = editTextTextEmailAddress.toString();
            assert userEmail != null;
            String[] splits = userEmail.split("@");
            String usernameString = "@" + splits[0];
            editTextTextUsername.setText(usernameString);
        }

        returnHomeButton_AccountPage.setOnClickListener(v -> openHomeActivity());
    }

    // Use this function to open the home (Main) activity
    public void openHomeActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}