package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button SearchButton_Homepage, accountButton_Homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* We will need a search bar eventually,
        official documentation for that can be found here:
        https://developer.android.com/guide/topics/search/search-dialog */


        SearchButton_Homepage = (Button) findViewById(R.id.SearchButton_Homepage);
        accountButton_Homepage = (Button) findViewById(R.id.accountButton_Homepage);


        accountButton_Homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccountActivity();
            }
        });

        SearchButton_Homepage.setOnClickListener(new View.OnClickListener() {
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