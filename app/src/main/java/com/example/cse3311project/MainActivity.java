package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private Button SearchButton_Homepage, accountButton_Homepage, SignOutButton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Robert is putting these notes here for later use, ignore if you want.

        For implementing user picked avatars, maybe look here:
        https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview

        We will need a search bar eventually,
        official documentation for that can be found here:
        https://developer.android.com/guide/topics/search/search-dialog
        */

        // Identifying our buttons that are located on the .xml file
        SearchButton_Homepage = (Button) findViewById(R.id.SearchButton_Homepage);
        accountButton_Homepage = (Button) findViewById(R.id.accountButton_Homepage);
        SignOutButton = (Button) findViewById(R.id.SignOutButton);

        fAuth = FirebaseAuth.getInstance();

        accountButton_Homepage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
            }
        });

        SearchButton_Homepage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReviewActivity.class));
            }
        });

        SignOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                Toast.makeText(MainActivity.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}