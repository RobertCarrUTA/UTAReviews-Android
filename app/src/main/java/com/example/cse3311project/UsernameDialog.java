package com.example.cse3311project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UsernameDialog extends AppCompatDialogFragment
{
    private EditText editTextUsername;
    private ExampleDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        // building a dialog from the xml file named username_dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_username_dialog, null);

        // Documentation for setting up a username?:
        // https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile
        //setting up the title and the buttons cancel and ok
        builder.setView(view);
        builder.setTitle("Change Username");
        builder.setNegativeButton("Cancel", (dialogInterface, i) ->
        {

        }).setPositiveButton("Ok", (dialogInterface, i) -> {
            // declare the string to get new username and pass it to applyTexts
            String username = editTextUsername.getText().toString();
            listener.applyTexts(username);
        });

        editTextUsername = view.findViewById(R.id.new_username);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (ExampleDialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    // this function is passing the new username to account activity.
    public interface ExampleDialogListener
    {
        void applyTexts(String username);
    }
}