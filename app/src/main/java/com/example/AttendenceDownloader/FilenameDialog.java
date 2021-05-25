package com.example.AttendenceDownloader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FilenameDialog extends AppCompatDialogFragment {

    private EditText fileNameEditText;
    private FilenameDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.file_name_dialog, null);
        builder.setView(view).setTitle("Create File")
                .setPositiveButton("Create", (dialog, id) -> {

                    String filename = fileNameEditText.getText().toString();
                    Log.d("  Filename  ", filename);
                    listener.applyFilename(filename);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {

                });

        fileNameEditText = view.findViewById(R.id.fileNameEditText);
        return builder.create();

       }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FilenameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement FilenameDialogListener");
        }
    }

    public interface FilenameDialogListener {
        void applyFilename(String fn);
    }
}