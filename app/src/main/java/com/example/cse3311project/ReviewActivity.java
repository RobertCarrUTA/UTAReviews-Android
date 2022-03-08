package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewActivity extends AppCompatActivity
{
    private Button returnHomeButton_ReviewPage, postReview_Button;
    private TextView professorName, posted_Review;
    private EditText review_text;


    // Might be helpful in the future:
    // https://firebase.google.com/docs/database/admin/retrieve-data
    FirebaseAuth fAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();

        String professor_selected_name = intent.getStringExtra("professor_name_from_list");

        returnHomeButton_ReviewPage = (Button) findViewById(R.id.returnHomeButton_ReviewPage);
        postReview_Button = (Button) findViewById(R.id.postReview_Button);
        professorName = (TextView) findViewById(R.id.professorName_Review);
        posted_Review = (TextView) findViewById(R.id.posted_review_text);
        review_text = (EditText) findViewById(R.id.review_text);

        professorName.setText("You are currently looking at reviews for " + professor_selected_name);

        // This needs to be here to keep all of our layout in place, without the line of code
        // below, everything will be moved up once the keyboard is opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Reviews");


        ref.child("0").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                String postedReview = snapshot.child("review").getValue().toString();
                posted_Review.setText(postedReview);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        postReview_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String review_text_submission = review_text.getText().toString().trim();

                if(review_text_submission.length() > 1000)
                {
                    review_text.setError("Please enter a review that is no longer than 1000 characters long");
                }

                // For making a new review each time
                // To overwrite review, do differently
                Reviews review = new Reviews();

                review.setReview(review_text_submission);
                review.setProfessor(professor_selected_name);
                review.setRating("4");
                DatabaseReference newRef = ref.push();
                newRef.setValue(review);

                review_text.setText("");
            }
        });

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