package com.example.cse3311project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UsernameDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private ExampleDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // building a dialog from the xml file named username_dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.username_dialog, null);

        //setting up the title and the buttons cancel and ok
        builder.setView(view);
        builder.setTitle("Change Credentials");
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // declear the string to get new username and pass it to applyTexts
                String username = editTextUsername.getText().toString();
                listener.applyTexts(username);
            }
        });
        editTextUsername = view.findViewById(R.id.new_username);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    // this function is passing the new username to account activity.
    public interface ExampleDialogListener{
        void applyTexts(String username);

    }
}
