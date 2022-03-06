package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AccountActivity extends AppCompatActivity
{
    private Button returnHomeButton_AccountPage;
    private EditText editTextTextEmailAddress, editTextTextUsername;

    FirebaseAuth fAuth;

    private DatabaseReference ProfessorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        returnHomeButton_AccountPage = (Button) findViewById(R.id.returnHomeButton_AccountPage);
        editTextTextEmailAddress = (EditText)  findViewById(R.id.editTextTextEmailAddress);
        editTextTextUsername = (EditText) findViewById(R.id.editTextTextUsername);

        editTextTextEmailAddress.setEnabled(false);
        editTextTextUsername.setEnabled(false);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            editTextTextEmailAddress.setText(userEmail);

            //String username = editTextTextEmailAddress.toString();
            String[] splits = userEmail.split("@");
            editTextTextUsername.setText("@" + splits[0]);
        }

        returnHomeButton_AccountPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });
    }

    // Use this function to open the home (Main) activity
    public void openHomeActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}