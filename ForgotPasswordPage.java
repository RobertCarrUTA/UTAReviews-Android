package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

        Email = findViewById(R.id.Email);
        ResetPassword = findViewById(R.id.buttonSendingCode);
        auth = FirebaseAuth.getInstance();

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                String[] emailSplit = email.split("@");
                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is Required.");
                    return;
                }

                if (!email.contains("@")) {
                    Email.setError("Please enter an UTA email");
                    return;
                }

                if (emailSplit[1].equals("mavs.uta.edu") || emailSplit[1].equals("uta.edu")) {
                }
                else
                {
                    Email.setError("Must be UTA email");
                    return;
                }
                auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>(){

                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(ForgotPasswordPage.this, "Password Reset sent to Email", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPasswordPage.this, "This email has not been registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                    }
                });
            }
        });
    }
}