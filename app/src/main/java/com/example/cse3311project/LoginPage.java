package com.example.cse3311project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity
{

    EditText Email, Password;
    Button Login, signupButton, forgotPasswordButton;

    protected void onCreate(Bundle savedInstanceSate)
    {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_login_page);
        Email = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.buttonLogin);
        signupButton = findViewById(R.id.signupButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordPage.class));
            }
        });
    }
}