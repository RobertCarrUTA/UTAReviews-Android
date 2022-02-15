package com.example.utareviewing;

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

public class LoginPage extends AppCompatActivity {

    EditText Email, Password;
    Button Login;
    TextView CreateButton, ForgotPasswordButton;

    protected void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.Login);
        Email = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.buttonLogin);
        CreateButton = findViewById(R.id.Register);
        ForgotPasswordButton = findViewById(R.id.ForgotPassword);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginPage.this, "Login Sucessful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),HomePage.class));
            }
        });

        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterPage.class));
            }
        });

        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordPage.class));
            }
        });
    }
}