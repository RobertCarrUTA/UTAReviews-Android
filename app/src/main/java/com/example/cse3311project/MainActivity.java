package com.example.cse3311project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView professor_search_result;
    private EditText professorSearchBar;

    FirebaseAuth fAuth;
    private DatabaseReference ProfessorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Robert is putting these notes here for later use, ignore if you want.

        For implementing user picked avatars, maybe look here:
        https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
        */

        // This needs to be here to keep all of our layout in place, without the line of code
        // below, everything will be moved up once the keyboard is opened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ProfessorDatabase = FirebaseDatabase.getInstance().getReference("Professors");

        Button accountButton_Homepage = findViewById(R.id.accountButton_Homepage);
        Button signOutButton = findViewById(R.id.SignOutButton);
        professorSearchBar = findViewById(R.id.professorSearchBar);

        // This is are our RecyclerView
        professor_search_result = findViewById(R.id.professor_search_result);
        professor_search_result.setHasFixedSize(true);
        professor_search_result.setLayoutManager(new LinearLayoutManager(this));
        // This divides our search results so they are more easily separated visually for the user
        // Documentation: https://developer.android.com/reference/androidx/recyclerview/widget/DividerItemDecoration
        professor_search_result.addItemDecoration(new DividerItemDecoration(professor_search_result.getContext(), DividerItemDecoration.VERTICAL));

        fAuth = FirebaseAuth.getInstance();

        accountButton_Homepage.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AccountActivity.class)));

        signOutButton.setOnClickListener(v ->
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            Toast.makeText(MainActivity.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
        });

        professorSearchBar.setOnClickListener(v ->
        {
            // This string needs to be passed to our firebaseProfessorSearch method so that we can use it to search the database
            // and show the results to the user (the string in question is the text inside the search bar)
            String searchEntry = professorSearchBar.getText().toString();

            firebaseProfessorSearch(searchEntry);
        });
    }

    // Robert is leaving this here for later use: https://developer.android.com/guide/topics/ui/layout/recyclerview-custom

    // This is our firebase search function
    // Any documentation about Firebase UI can be found here: https://github.com/firebase/FirebaseUI-Android
    private void firebaseProfessorSearch(String searchEntry)
    {

        // This is a search query that allows for the user to search and only find relevant results in the search options
        Query firebaseQuery = ProfessorDatabase.orderByChild("name").startAt(searchEntry).endAt(searchEntry + "\uf8ff");

        // This must be included for our FirebaseRecyclerAdapter options
        FirebaseRecyclerOptions<Professors> options =
                new FirebaseRecyclerOptions.Builder<Professors>()
                .setQuery(firebaseQuery, Professors.class)
                .build();

        FirebaseRecyclerAdapter<Professors, ProfessorViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Professors, ProfessorViewHolder>(options)
        {
            @NonNull
            @Override
            public ProfessorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professor_search_layout, parent, false);
                return new ProfessorViewHolder((view));
            }

            @Override
            protected void onBindViewHolder(@NonNull ProfessorViewHolder holder, int position, @NonNull Professors model)
            {
                holder.professor_Name.setText(model.getName());
                String professor_department_text = "Department: " + model.getDepartment();
                holder.professor_department.setText(professor_department_text);
                String professor_rating_setText = "Rating: " + model.getRating();
                holder.professor_rating.setText(professor_rating_setText);

                // TODO: (Robert) Come back and make this better, this is not optimal but it works
                holder.itemView.setOnClickListener(view ->
                {
                    // This is how we would get the selected professor name
                    String professorNameIntent = model.getName();

                    // To pass this name that the user has selected, This is the only way I could figure
                    // out how to do it, we start the review activity with this intent or it cannot be passed
                    Intent name_selection_intent = new Intent(getApplicationContext(), ReviewActivity.class);
                    name_selection_intent.putExtra("professor_name_from_list", professorNameIntent);

                    startActivity(name_selection_intent);
                    Toast.makeText(MainActivity.this, "Now viewing reviews for " + model.getName(), Toast.LENGTH_SHORT).show();
                });
            }
        };

        professor_search_result.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    // Making view holder class for our professors in the database
    public static class ProfessorViewHolder extends RecyclerView.ViewHolder
    {
        TextView professor_Name, professor_department, professor_rating;
        View View1;

        public ProfessorViewHolder(@NonNull View itemView)
        {
            super(itemView);
            View1 = itemView;

            // This is where we get the information for onBindViewHolder(@NonNull ProfessorViewHolder holder, ...)
            professor_Name = View1.findViewById(R.id.professor_name);
            professor_department = View1.findViewById(R.id.professor_department);
            professor_rating = View1.findViewById(R.id.professor_rating);
        }
    }
}