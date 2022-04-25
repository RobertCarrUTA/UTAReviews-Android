package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostReviewActivity extends AppCompatActivity
{
    private EditText review_text, classTakenTextEntryBox;
    RatingBar ratingStar;
    ToggleButton toggle;

    FirebaseAuth fAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        Intent intent = getIntent();

        // Here we get the passed information from the previous review page of the staff member
        // who was being viewed
        String professor_selected_name = intent.getExtras().getString("teacher_name");

        Button cancelReviewButton = findViewById(R.id.cancelReviewButton);
        Button postReview_Button = findViewById(R.id.postReview_Button);
        TextView professorName = findViewById(R.id.professorName_PostReview);
        review_text = findViewById(R.id.review_text);
        classTakenTextEntryBox = findViewById(R.id.classTakenTextEntryBox);
        ratingStar = findViewById(R.id.ratingBar_review);
        toggle = (ToggleButton) findViewById(R.id.togglebutton);

        professorName.setText("You are currently writing a review for " + professor_selected_name);

        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Reviews");

        cancelReviewButton.setOnClickListener(v ->
        {
            Intent name_selection_intent = new Intent(getApplicationContext(), ReviewActivity.class);
            name_selection_intent.putExtra("professor_name_from_list", professor_selected_name);

            startActivity(name_selection_intent);
        });

        final boolean[] finalAnonymous_review = {false};

        // Documentation for toggle button: https://developer.android.com/guide/topics/ui/controls/togglebutton
        toggle.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            // This helps us know if the toggle is enabled or disabled
            finalAnonymous_review[0] = isChecked;
        });

        postReview_Button.setOnClickListener(v ->
        {
            // When the user clicks the post review button all the information that was entered is
            // now pushed to the Review section of the database
            String review_text_submission = review_text.getText().toString().trim();
            float ratingFloat = ratingStar.getRating();
            String ratingString = String.valueOf(ratingFloat);
            String classTaken = classTakenTextEntryBox.getText().toString().trim();


            if (review_text_submission.length() > 2000)
            {
                review_text.setError("Please enter a review that is no longer than 2,000 characters long");
            }

            if (review_text_submission.length() == 0)
            {
                review_text.setError("Please enter a review that is not empty");
            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            assert currentUser != null;
            String userEmail = currentUser.getEmail();
            assert userEmail != null;
            String[] splits = userEmail.split("@");
            String username = splits[0];

            String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

            // For making a new review each time
            // To overwrite review, do differently
            Reviews review = new Reviews();

            review.setReview(review_text_submission);
            review.setRating(ratingString);
            review.setDate(currentDate);
            review.setClassTaken(classTaken);

            if (finalAnonymous_review[0] == true)
            {
                String anonymous_username = "Anonymous";
                review.setUsername(anonymous_username);
            }
            else
            {
                review.setUsername(username);
            }

            DatabaseReference newRef = ref.child(professor_selected_name).push();
            newRef.setValue(review);

            Intent name_selection_intent = new Intent(getApplicationContext(), ReviewActivity.class);
            name_selection_intent.putExtra("professor_name_from_list", professor_selected_name);

            startActivity(name_selection_intent);
        });
    }
}