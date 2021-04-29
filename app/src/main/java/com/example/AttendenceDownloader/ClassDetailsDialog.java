package com.example.AttendenceDownloader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ClassDetailsDialog extends DialogFragment {
    private EditText className;
    private EditText subjectName;
    private EditText noOfStudents;
    private ClassDetailsDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.class_details_layout, null);
        builder.setView(view).setTitle("Create Class")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       String cn = className.getText().toString();
                       String sn = subjectName.getText().toString();
                       String nos = noOfStudents.getText().toString();
                       listener.applyTexts(cn, sn, nos);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        className = view.findViewById(R.id.className);
        subjectName = view.findViewById(R.id.subjectName);
        noOfStudents = view.findViewById(R.id.noOfStudents);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface ClassDetailsDialogListener{
        void applyTexts(String cn, String sn, String nos);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ClassDetailsDialogListener) context;
    }
}