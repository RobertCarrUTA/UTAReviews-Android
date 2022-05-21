package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginPage extends AppCompatActivity
{
    EditText Email, Password;
    Button Login;
    TextView signupButton, forgotPasswordButton;
    FirebaseAuth auth;


    protected void onCreate(Bundle savedInstanceSate)
    {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_login_page);
        Email = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.buttonLogin);
        signupButton = findViewById(R.id.signupButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        auth = FirebaseAuth.getInstance();

        Login.setOnClickListener(v ->
        {
            // This is what happens when the user selects the login button.
            String email = Email.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String[] emailSplit = email.split("@");
            if(TextUtils.isEmpty(email))
            {
                Email.setError("Email is Required.");//No email entered
                return;
            }

            if(!email.contains("@"))
            {
                Email.setError("Please enter a UTA email");//Email is not the correct format
                return;
            }

            else if (!(emailSplit[1].equals("mavs.uta.edu") || emailSplit[1].equals("uta.edu")))
            {
                Email.setError("Must be UTA email");
            }

            if (TextUtils.isEmpty(password))
            {
                Password.setError("Password is Required.");
                return;
            }

            if (password.length() < 8)
            {
                Password.setError("Password must have 8 characters or more.");
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
            {
                //Sign the user in using the given email and password combo
                if(task.isSuccessful())
                {
                    //If you can successfully login using the entered details, set the current user.
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert currentUser != null;
                    if(currentUser.isEmailVerified())
                    {
                        //If the user has verified their email, redirect to the home page,otherwise send a verification email and wait.
                        Toast.makeText(LoginPage.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else
                    {
                        currentUser.sendEmailVerification();
                        Toast.makeText(LoginPage.this, "Please verify your email first!", Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(LoginPage.this, "Wrong Email or Password"
                            + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        forgotPasswordButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPasswordPage.class)));

        signupButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Registration.class)));
    }
}