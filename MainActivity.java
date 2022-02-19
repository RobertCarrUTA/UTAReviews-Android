package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private Button SearchButton_Homepage, accountButton_Homepage;

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


        // On click listeners that allow the user to go from page to page
        // when clicking on a button
        accountButton_Homepage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openAccountActivity();
            }
        });

        SearchButton_Homepage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openReviewActivity();
            }
        });
    }

    // Use this function to open any new review activity
    public void openReviewActivity()
    {
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }

    // Use this function to open any new account activity
    public void openAccountActivity()
    {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
}