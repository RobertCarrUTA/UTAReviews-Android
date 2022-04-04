package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordPage extends AppCompatActivity
{
    EditText Email;
    Button ResetPassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        Email = findViewById(R.id.RecoveringEmail);
        ResetPassword = findViewById(R.id.buttonSendingCode);
        auth = FirebaseAuth.getInstance();

        ResetPassword.setOnClickListener(v ->
        {
            String email = Email.getText().toString().trim();
            String[] emailSplit = email.split("@");
            if (TextUtils.isEmpty(email))
            {
                Email.setError("Email is Required.");
                return;
            }

            if (!email.contains("@"))
            {
                Email.setError("Please enter a valid email");
                return;
            }

            if (emailSplit[1].equals("mavs.uta.edu") || emailSplit[1].equals("uta.edu"))
            {
                Email.setError("Must be a UTA email");
            }
            else
            {
                Email.setError("Must be UTA email");
                return;
            }

            auth.sendPasswordResetEmail(email).addOnSuccessListener(unused ->
            {
                Toast.makeText(ForgotPasswordPage.this, "Password Reset sent to Email", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }).addOnFailureListener(e ->
            {
                Toast.makeText(ForgotPasswordPage.this, "This email has not been registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            });
        });
    }
}