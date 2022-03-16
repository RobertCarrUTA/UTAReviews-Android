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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ReviewActivity extends AppCompatActivity
{
    private Button returnHomeButton_ReviewPage, postReview_Button;
    private TextView professorName, posted_Review;
    private EditText review_text;

    private RecyclerView reviews_from_database;


    // Might be helpful in the future:
    // https://firebase.google.com/docs/database/admin/retrieve-data
    FirebaseAuth fAuth;
    private DatabaseReference ref;

    RatingBar ratingStar;

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
        //posted_Review = (TextView) findViewById(R.id.posted_review_text);
        review_text = (EditText) findViewById(R.id.review_text);
        ratingStar = findViewById(R.id.ratingBar);

        professorName.setText("You are currently looking at reviews for " + professor_selected_name);

        // This is are our RecyclerView
        reviews_from_database = (RecyclerView) findViewById(R.id.reviews_from_database);
        reviews_from_database.setHasFixedSize(true);
        reviews_from_database.setLayoutManager(new LinearLayoutManager(this));
        // This divides our search results so they are more easily separated visually for the user
        // Documentation: https://developer.android.com/reference/androidx/recyclerview/widget/DividerItemDecoration
        reviews_from_database.addItemDecoration(new DividerItemDecoration(reviews_from_database.getContext(), DividerItemDecoration.VERTICAL));


        // This needs to be here to keep all of our layout in place, without the line of code
        // below, everything will be moved up once the keyboard is opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Reviews");


        firebaseReviewSearch();

        ref.child(professor_selected_name).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                // TODO: We need to make this not erase the last child. Maybe make a list of strings
                for(DataSnapshot snapShot_looper : snapshot.getChildren())
                {
                    String postedReview = snapShot_looper.child("review").getValue().toString();
                    posted_Review.setText(postedReview);
                }
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
                float ratingfloat = ratingStar.getRating();
                String ratingString = String.valueOf(ratingfloat);


                if(review_text_submission.length() > 2000)
                {
                    review_text.setError("Please enter a review that is no longer than 1000 characters long");
                }

                if(review_text_submission.length() == 0)
                {
                    review_text.setError("Please enter a review that is no longer than 1000 characters long");
                }

                // For making a new review each time
                // To overwrite review, do differently
                Reviews review = new Reviews();

                review.setReview(review_text_submission);
                review.setProfessor(professor_selected_name);
                review.setRating(ratingString);
                DatabaseReference newRef = ref.child(professor_selected_name).push();
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

    // Robert is leaving this here for later use: https://developer.android.com/guide/topics/ui/layout/recyclerview-custom

    // This is our firebase search function
    // Any documentation about Firebase UI can be found here: https://github.com/firebase/FirebaseUI-Android
    private void firebaseReviewSearch(String searchEntry)
    {

        // This is a search query that allows for the user to search and only find relevant results in the search options
        Query firebaseQuery = ref.orderByChild("rating").startAt(searchEntry).endAt(searchEntry + "\uf8ff");

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
                ReviewActivity.ReviewViewHolder viewHolder = new ReviewActivity().ReviewViewHolder((view));
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ReviewActivity.ReviewViewHolder holder, int position, @NonNull Reviews model)
            {
                holder.Username_from_database.setText(model.getProfessor());
                holder.review_from_database_text.setText("Review: " + model.getReview());
                holder.rating_from_database.setText("Rating: " + model.getRating());

                /*
                // TODO: (Robert) Come back and make this better, this is not optimal but it works
                holder.itemView.setOnClickListener(new View.OnClickListener()
                                                   {
                                                       @Override
                                                       public void onClick(View view)
                                                       {
                                                           // This is how we would get the selected professor name
                                                           String professorNameIntent = model.getName();

                                                           // To pass this name that the user has selected, This is the only way I could figure
                                                           // out how to do it, we start the review activity with this intent or it cannot be passed
                                                           Intent name_selection_intent = new Intent(getApplicationContext(), ReviewActivity.class);
                                                           name_selection_intent.putExtra("professor_name_from_list", professorNameIntent);

                                                           startActivity(name_selection_intent);
                                                           //Toast.makeText(ReviewActivity.this, "Now viewing reviews for " + model.getName(), Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                );

                 */
            }
        };

        reviews_from_database.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    // Making view holder class for our professors in the database
    public class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username_from_database, review_from_database_text, rating_from_database;
        View View1;

        public ReviewViewHolder(@NonNull View itemView)
        {
            super(itemView);
            View1 = itemView;

            // This is where we get the information for onBindViewHolder(@NonNull ProfessorViewHolder holder, ...)
            Username_from_database = (TextView) View1.findViewById(R.id.Username_from_database);
            review_from_database_text = (TextView) View1.findViewById(R.id.review_from_database_text);
            rating_from_database = (TextView) View1.findViewById(R.id.rating_from_database);
        }
    }
}