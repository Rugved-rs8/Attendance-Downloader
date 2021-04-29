package com.example.AttendenceDownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailForPasswordChange;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForPasswordChange = (EditText)findViewById(R.id.emailForPasswordChange);
    }

    public void submitButtonClicked(View view){
        mauth.sendPasswordResetEmail(emailForPasswordChange.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Reset email instructions sent to " + emailForPasswordChange.getText().toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), emailForPasswordChange.getText().toString() + " does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}