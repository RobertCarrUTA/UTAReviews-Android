package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewActivity extends AppCompatActivity
{
    private EditText review_text;
    private RecyclerView review_database_result;
    RatingBar ratingStar;
    ToggleButton toggle;

    FirebaseAuth fAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();

        String professor_selected_name = intent.getStringExtra("professor_name_from_list");

        Button returnHomeButton_ReviewPage = findViewById(R.id.returnHomeButton_ReviewPage);
        Button postReview_Button = findViewById(R.id.postReview_Button);
        TextView professorName = findViewById(R.id.professorName_Review);
        review_text = findViewById(R.id.review_text);
        ratingStar = findViewById(R.id.ratingBar);
        toggle = (ToggleButton) findViewById(R.id.togglebutton);

        String professor_Name_set_text = "You are currently looking at reviews for " + professor_selected_name;


        professorName.setText(professor_Name_set_text);

        // This needs to be here to keep all of our layout in place, without the line of code
        // below, everything will be moved up once the keyboard is opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Reviews");

        // This is are our RecyclerView
        review_database_result = findViewById(R.id.review_database_result);
        review_database_result.setHasFixedSize(true);
        // This reverses the order of the RecyclerView (descending order)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        review_database_result.setLayoutManager(layoutManager);
        // This divides our search results so they are more easily separated visually for the user
        // Documentation: https://developer.android.com/reference/androidx/recyclerview/widget/DividerItemDecoration
        review_database_result.addItemDecoration(new DividerItemDecoration(review_database_result.getContext(), DividerItemDecoration.VERTICAL));

        // This is the call to our RecyclerView for our reviews, it updates in real time, if a review is changed, added, or deleted, it will
        // show up in the app as soon as those changes are reflected in the database
        firebaseReviewResults(professor_selected_name);

        //ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebutton);
        final boolean[] finalAnonymous_review = {false};
        // Documentation for toggle button: https://developer.android.com/guide/topics/ui/controls/togglebutton
        toggle.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            // The toggle is enabled
            // The toggle is disabled
            finalAnonymous_review[0] = isChecked;
        });

        postReview_Button.setOnClickListener(v ->
        {
            String review_text_submission = review_text.getText().toString().trim();
            float ratingFloat = ratingStar.getRating();
            String ratingString = String.valueOf(ratingFloat);


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
            //review.setUsername(username);



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

            review_text.setText("");
            ratingStar.setRating(0);
        });

        returnHomeButton_ReviewPage.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

    }

    // This is our firebase function to view the reviews for the selected teacher
    // Any documentation about Firebase UI can be found here: https://github.com/firebase/FirebaseUI-Android
    private void firebaseReviewResults(String professor_selected_name)
    {

        // This is a search query that allows for the user to see reviews for the selected professor
        // and orders them by the date they were posted
        Query firebaseQuery = ref.child(professor_selected_name).orderByChild("date");

        // This must be included for our FirebaseRecyclerAdapter options
        FirebaseRecyclerOptions<Reviews> options =
                new FirebaseRecyclerOptions.Builder<Reviews>()
                        .setQuery(firebaseQuery, Reviews.class)
                        .build();

        FirebaseRecyclerAdapter<Reviews, ReviewActivity.ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reviews, ReviewActivity.ReviewViewHolder>(options)
        {
            @NonNull
            @Override
            public ReviewActivity.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_from_database_layout, parent, false);
                return new ReviewViewHolder((view));
            }

            @Override
            protected void onBindViewHolder(@NonNull ReviewActivity.ReviewViewHolder holder, int position, @NonNull Reviews model)
            {
                holder.review_username.setText(model.getUsername());
                holder.review_text_from_database.setText(model.getReview());
                String rating_text = "Rating: " + model.getRating();
                holder.review_rating_from_database.setText(rating_text);
                holder.date_the_review_was_posted.setText(model.getDate());
            }
        };

        review_database_result.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    // Making view holder class for our reviews in the database
    public static class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView review_username, review_text_from_database, review_rating_from_database, date_the_review_was_posted;
        View View1;

        public ReviewViewHolder(@NonNull View itemView)
        {
            super(itemView);
            View1 = itemView;

            // This is where we get the information for onBindViewHolder(@NonNull ReviewViewHolder holder, ...)
            review_username = View1.findViewById(R.id.username_from_database_reviews);
            review_text_from_database = View1.findViewById(R.id.review_text_from_database);
            review_rating_from_database = View1.findViewById(R.id.rating_from_database);
            date_the_review_was_posted = View1.findViewById(R.id.date_the_review_was_posted);
        }
    }
}