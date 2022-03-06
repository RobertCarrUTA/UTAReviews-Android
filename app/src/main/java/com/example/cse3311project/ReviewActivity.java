package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewActivity extends AppCompatActivity
{
    private Button returnHomeButton_ReviewPage;
    private TextView professorName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();

        String professor_selected_name = intent.getStringExtra("professor_name_from_list");

        returnHomeButton_ReviewPage = (Button) findViewById(R.id.returnHomeButton_ReviewPage);
        professorName = (TextView) findViewById(R.id.professorName_Review);

        professorName.setText("You are currently looking at reviews for " +professor_selected_name);

        returnHomeButton_ReviewPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}