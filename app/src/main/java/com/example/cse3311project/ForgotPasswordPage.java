package com.example.cse3311project;;

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


public class ForgotPasswordPage extends AppCompatActivity {
    EditText Email;
    Button ResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        Email = findViewById(R.id.RecoveringEmail);
        ResetPassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(ForgotPasswordPage.this, "Password Reset sent to Email", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
            }
        });
    }
}