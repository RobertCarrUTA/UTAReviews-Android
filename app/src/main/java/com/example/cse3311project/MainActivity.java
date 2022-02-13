package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button SearchButton_Homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* We will need a search bar eventually,
        official documentation for that can be found here:
        https://developer.android.com/guide/topics/search/search-dialog */


        SearchButton_Homepage = (Button) findViewById(R.id.SearchButton_Homepage);
        SearchButton_Homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
    }

    // Use this function to open any new activity as seen above
    public void openNewActivity()
    {
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivity(intent);
    }
}