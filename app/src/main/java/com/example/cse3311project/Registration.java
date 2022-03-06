package com.example.cse3311project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity
{
    EditText lastNameInput, emailInput, idInput;
    Button submitButton, cancelButton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        idInput = findViewById(R.id.idInput);


        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        fAuth = FirebaseAuth.getInstance();

        // If a user is already signed in, they should not be on the registration page
        // They should instead be redirected to the homepage
        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //String firstName = firstNameInput.getText().toString().trim();
                String password = idInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();

                if(TextUtils.isEmpty(lastName))
                {
                    idInput.setError("Last name must not be empty");
                }

                if(TextUtils.isEmpty(password))
                {
                    idInput.setError("A password is required");
                }

                if(password.length() < 8)
                {
                    idInput.setError("Password must be 8 characters or greater");
                }

                // Registration of the user in the database. If the user is successfully registered, show a toast and redirect to login page
                // if a user is not successfully registered, show the error that occurred
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Registration.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginPage.class));
                        }
                        else
                        {
                            Toast.makeText(Registration.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });
    }
}