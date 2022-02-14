package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReviewActivity extends AppCompatActivity
{
    private Button returnHomeButton_ReviewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        returnHomeButton_ReviewPage = (Button) findViewById(R.id.returnHomeButton_ReviewPage);


        returnHomeButton_ReviewPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });
    }

    // Use this function to open the home (Main) activity
    public void openHomeActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}