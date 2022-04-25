package com.example.cse3311project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ReplyingActivity extends AppCompatActivity {

    RecyclerView Comment_Database_result;
    RecyclerView Username_Comment;
    FirebaseAuth fAuth;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replying);
        Intent intent = getIntent();
        /*
        Username_Comment = findViewById(R.id.Username_Comment);
        Comment_Database_result = findViewById(R.id.comment_database_result);
        Button Post = findViewById(R.id.Reply);
        */
        String username_selected_name = intent.getStringExtra("username_from_list");
        String review_selected_name = intent.getStringExtra("review_from_list");
        String rating_selected_name = intent.getStringExtra("rating_from_list");
        String date_selected_name = intent.getStringExtra("date_from_list");
        String class_selected_name = intent.getStringExtra("class_from_list");

        TextView UsernameComment = findViewById(R.id.review_database_result);

        String Comment = "   " + username_selected_name + "        Rating: " + rating_selected_name + "\n   " + date_selected_name + "       " + class_selected_name + "\n    " + review_selected_name;

        UsernameComment.setText(Comment);

    }
}