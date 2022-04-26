package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostReplyActivity extends AppCompatActivity
{
    Button postReply, cancelButtonReply;
    EditText replyEditText;

    String professor_selected_name, usernameCommentSection, review_text_from_review, rating_text_from_review, class_taken_from_review;

    FirebaseAuth fAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply);

        postReply = findViewById(R.id.postReplyButton);
        cancelButtonReply = findViewById(R.id.cancelButtonReply);
        replyEditText = findViewById(R.id.replyEditText);

        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Replies");

        Intent intent = getIntent();
        professor_selected_name = intent.getStringExtra("professor_name_from_list");
        usernameCommentSection = intent.getStringExtra("usernameCommentSection");
        review_text_from_review = intent.getStringExtra("review_text_from_review");
        rating_text_from_review = intent.getStringExtra("rating_text_from_review");
        class_taken_from_review = intent.getStringExtra("class_taken_from_review");

        cancelButtonReply.setOnClickListener(v ->
        {
            Intent comment_selection_intent = new Intent(getApplicationContext(), ReplyActivity.class);
            comment_selection_intent.putExtra("professor_name_from_list", professor_selected_name);
            comment_selection_intent.putExtra("usernameCommentSection", usernameCommentSection);
            comment_selection_intent.putExtra("review_text_from_review", review_text_from_review);
            comment_selection_intent.putExtra("rating_text_from_review", rating_text_from_review);
            comment_selection_intent.putExtra("class_taken_from_review", class_taken_from_review);

            startActivity(comment_selection_intent);
        });

        postReply.setOnClickListener(v ->
        {
            String replyText = replyEditText.getText().toString().trim();

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            assert currentUser != null;
            String userEmail = currentUser.getEmail();
            assert userEmail != null;
            String[] splits = userEmail.split("@");
            String username = splits[0];

            String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

            // For making a new review each time
            // To overwrite review, do differently
            Replies reply = new Replies();

            reply.setComment(replyText);
            reply.setDate(currentDate);
            reply.setUsername(username);

            //Intent intent = getIntent();
            //String professor_selected_name = intent.getStringExtra("professor_name_from_list");
            //String usernameCommentSection = intent.getStringExtra("usernameCommentSection");
            //usernameCommentSection = usernameCommentSection.replace(".", "");

            DatabaseReference newRef = ref.child(professor_selected_name).child(usernameCommentSection).push();
            newRef.setValue(reply);

            Intent comment_selection_intent = new Intent(getApplicationContext(), ReplyActivity.class);
            comment_selection_intent.putExtra("professor_name_from_list", professor_selected_name);
            comment_selection_intent.putExtra("usernameCommentSection", usernameCommentSection);
            comment_selection_intent.putExtra("review_text_from_review", review_text_from_review);
            comment_selection_intent.putExtra("rating_text_from_review", rating_text_from_review);
            comment_selection_intent.putExtra("class_taken_from_review", class_taken_from_review);

            startActivity(comment_selection_intent);
        });

    }
}