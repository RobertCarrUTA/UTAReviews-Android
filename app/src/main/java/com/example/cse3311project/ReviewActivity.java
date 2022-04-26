package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ReviewActivity extends AppCompatActivity
{
    private RecyclerView review_database_result;
    private TextView ratingSumText;

    String ratingSumAverage;

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
        TextView professorName = findViewById(R.id.professorName_Review);
        TextView postReview_redirect = findViewById(R.id.postReview_redirect);
        ratingSumText = findViewById(R.id.rating);

        String professor_Name_set_text = "You are currently looking at reviews for " + professor_selected_name;
        professorName.setText(professor_Name_set_text);

        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Reviews");
        DatabaseReference profRef = FirebaseDatabase.getInstance().getReference("Professors");

        // Documentation for retrieving data can be found here:
        // https://firebase.google.com/docs/database/admin/retrieve-data
        ref.child(professor_selected_name).addValueEventListener(new ValueEventListener()
        {
            float ratingSum = 0;
            String ratingSumString;
            float count; // counter to keep track of the denominator for the average
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ratingSumString = Objects.requireNonNull(snapshot.child("rating").getValue()).toString();
                    ratingSum += Float.parseFloat(ratingSumString);
                    count++;
                }

                // To get the average we need to divide the sum by the number of reviews
                float ratingSumAverageFloat = ratingSum/count;
                ratingSumAverage = String.format("%.2f", ratingSumAverageFloat);
                String ratingSetText = "Rating: " + ratingSumAverage;
                ratingSumText.setText(ratingSetText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        // TODO: There may need to be some improvements and stability issues with the profRef.addValueEventListener(new ValueEventListener()
        // below. I, Robert, have added some already, I do not get the issues any longer but we need to keep this in mind
        // The issues can be repeating the loop forever, or putting a bunch of new ratings under the professors name
        // POSSIBLE FIX (ROBERT): Changed the addValueEventListener to addListenerForSingleValueEvent, issues seem to be gone for now
        // https://stackoverflow.com/questions/62844510/changing-value-in-firebase-results-in-infinite-loop
        final boolean[] matchFound = {false};
        // Push the new average to the professors rating on the database
        profRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                // Keep the below commented line for quick debugging
                //System.out.println("HERE");
                if(matchFound[0] == false)
                {
                    // This for loop will move through all of our professors looking for a match
                    // of the Professor child named professor_selected_name
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        // We get the name of the Professor object and store it into professorName
                        String professorName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();

                        // If we find a match between the current professor and the Professor object name
                        // we set match found to true, no need to keep searching, and we push the average
                        // rating to the database for that Professor object rating value
                        if (professorName.equals(professor_selected_name))
                        {
                            matchFound[0] = true;
                            // Keep the below commented line for quick debugging
                            System.out.println("Prof " + professorName);
                            System.out.println("Rating: " + ratingSumAverage);

                            // Push the new average to the professors rating on the database
                            snapshot.getRef().child("rating").setValue(ratingSumAverage);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // This needs to be here to keep all of our layout in place, without the line of code
        // below, everything will be moved up once the keyboard is opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        postReview_redirect.setOnClickListener(v ->
        {
            // This will allow us to take the teachers name and pass it to the post review page
            Intent i = new Intent (this, PostReviewActivity.class);
            i.putExtra("teacher_name", professor_selected_name);
            startActivity(i);
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
                // This will set the information for our RecyclerView from our Reviews model
                holder.review_username.setText(model.getUsername());
                holder.review_text_from_database.setText(model.getReview());
                String rating_text = "Rating: " + model.getRating();
                holder.review_rating_from_database.setText(rating_text);
                holder.date_the_review_was_posted.setText(model.getDate());
                holder.classTaken_from_database.setText(model.getClassTaken());

                // TODO: (Robert) Come back and make this better, this is not optimal but it works
                // This allows us to click on the RecyclerView item and go to the right comment page
                holder.itemView.setOnClickListener(view ->
                {
                    // This is how we would get the selected reviews information for the comment page
                    //String professorNameIntent = professor_selected_name;
                    String usernameCommentSection = model.getUsername();
                    //usernameCommentSection = usernameCommentSection.replace(".", "");
                    String review_text_from_review = model.getReview();
                    String rating_text_from_review = "Rating: " + model.getRating();
                    String class_taken_from_review = model.getClassTaken();

                    // To pass this name that the user has selected, This is the only way I could figure
                    // out how to do it, we start the review activity with this intent or it cannot be passed
                    Intent comment_selection_intent = new Intent(getApplicationContext(), ReplyActivity.class);
                    comment_selection_intent.putExtra("professor_name_from_list", professor_selected_name);
                    comment_selection_intent.putExtra("usernameCommentSection", usernameCommentSection);
                    comment_selection_intent.putExtra("review_text_from_review", review_text_from_review);
                    comment_selection_intent.putExtra("rating_text_from_review", rating_text_from_review);
                    comment_selection_intent.putExtra("class_taken_from_review", class_taken_from_review);
                    System.out.println("I am here on one review!!!");

                    startActivity(comment_selection_intent);
                    Toast.makeText(ReviewActivity.this, "Now viewing reviews for " + usernameCommentSection + "'s review of " + professor_selected_name, Toast.LENGTH_SHORT).show();
                });
            }
        };

        review_database_result.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    // Making view holder class for our reviews in the database
    public static class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView review_username, review_text_from_database, review_rating_from_database, date_the_review_was_posted, classTaken_from_database;
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
            classTaken_from_database = View1.findViewById(R.id.class_from_database);
        }
    }
}