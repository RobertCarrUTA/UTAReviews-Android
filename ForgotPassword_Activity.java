package com.example.newutareviewing;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForgotPassword_Activity extends AppCompatActivity {
    EditText Email;
    Button ResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        Email = findViewById(R.id.RecoveringEmail);
        ResetPassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(ForgotPassword_Activity.this, "Password Reset sent to Email", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(), asda.class));
            }
        });
    }
}
