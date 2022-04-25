package com.example.cse3311project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class ReplyActivity extends AppCompatActivity
{
    RecyclerView comment_database_result;
    TextView reviewUserName, reviewRating, reviewClass;
    EditText reviewText;
    Button returnToReviewButton, replyButton;

    FirebaseAuth fAuth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        reviewUserName = findViewById(R.id.reviewUserName);
        reviewRating = findViewById(R.id.reviewRating);
        reviewClass = findViewById(R.id.reviewClass);
        reviewText = findViewById(R.id.reviewText);
        returnToReviewButton = findViewById(R.id.returnToReviewButton);
        replyButton = findViewById(R.id.replyButton);


        Intent intent = getIntent();
        String professor_selected_name = intent.getStringExtra("professor_selected_name");
        String usernameCommentSection = intent.getStringExtra("usernameCommentSection");
        String review_text_from_review = intent.getStringExtra("review_text_from_review");
        String rating_text_from_review = intent.getStringExtra("rating_text_from_review");
        String class_taken_from_review = intent.getStringExtra("class_taken_from_review");

        reviewUserName.setText(usernameCommentSection);
        reviewText.setText(review_text_from_review);
        reviewRating.setText(rating_text_from_review);
        reviewClass.setText(class_taken_from_review);

        // This is are our RecyclerView
        comment_database_result = findViewById(R.id.comment_database_result);
        comment_database_result.setHasFixedSize(true);
        // This reverses the order of the RecyclerView (descending order)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        comment_database_result.setLayoutManager(layoutManager);
        // This divides our search results so they are more easily separated visually for the user
        // Documentation: https://developer.android.com/reference/androidx/recyclerview/widget/DividerItemDecoration
        comment_database_result.addItemDecoration(new DividerItemDecoration(comment_database_result.getContext(), DividerItemDecoration.VERTICAL));

        // This is the call to our RecyclerView for our comments, it updates in real time, if a comment is added it will
        // show up in the app as soon as those changes are reflected in the database
        firebaseReviewResults(professor_selected_name, usernameCommentSection);

        returnToReviewButton.setOnClickListener(v ->
        {
            // This will allow us to take the teachers name and pass it to the post review page
            Intent i = new Intent (this, PostReviewActivity.class);
            i.putExtra("teacher_name", professor_selected_name);
            startActivity(i);
        });

        replyButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }

        // This is our firebase function to view the reviews for the selected teacher
        // Any documentation about Firebase UI can be found here: https://github.com/firebase/FirebaseUI-Android
        private void firebaseReviewResults(String professor_selected_name, String usernameCommentSection)
        {
            // This is a search query that allows for the user to see comments for a review under a professor
            // and orders them by the date the comments were posted
            Query firebaseQuery = ref.child(professor_selected_name).child(usernameCommentSection).orderByChild("date");

            // This must be included for our FirebaseRecyclerAdapter options
            FirebaseRecyclerOptions<Reviews> options =
                    new FirebaseRecyclerOptions.Builder<Reviews>()
                            .setQuery(firebaseQuery, Reviews.class)
                            .build();

            FirebaseRecyclerAdapter<Reviews, ReviewActivity.ReviewViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Replys, ReplyActivity.ReplyViewHolder>(options)
            {
                @NonNull
                @Override
                public ReplyActivity.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_from_database_layout, parent, false);
                    return new ReplyActivity.ReplyViewHolder((view));
                }

                @Override
                protected void onBindViewHolder(@NonNull ReplyActivity.ReplyViewHolder holder, int position, @NonNull Replys model)
                {
                    // This will set the information for our RecyclerView from our Reviews model
                    holder.comment_username.setText(model.getUsername());
                    holder.comment_text_from_database.setText(model.getReview());
                    //String rating_text = "Rating: " + model.getRating();
                    //holder.review_rating_from_database.setText(rating_text);
                    holder.date_the_comment_was_posted.setText(model.getDate());
                    //holder.classTaken_from_database.setText(model.getClassTaken());
                }
            };

            comment_database_result.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();
        }

        // Making view holder class for our reviews in the database
        public static class ReplyViewHolder extends RecyclerView.ViewHolder
        {
            TextView review_username, review_text_from_database, review_rating_from_database, date_the_review_was_posted, classTaken_from_database;
            View View1;

            public ReplyViewHolder(@NonNull View itemView)
            {
                super(itemView);
                View1 = itemView;

                // This is where we get the information for onBindViewHolder(@NonNull ReviewViewHolder holder, ...)
                review_username = View1.findViewById(R.id.username_from_database_reviews);
                review_text_from_database = View1.findViewById(R.id.review_text_from_database);
                review_rating_from_database = View1.findViewById(R.id.rating_from_database);
                date_the_review_was_posted = View1.findViewById(R.id.date_the_review_was_posted);
                classTaken_from_database = View1.findViewById(R.id.class_from_database);
            }
        }
    }
}