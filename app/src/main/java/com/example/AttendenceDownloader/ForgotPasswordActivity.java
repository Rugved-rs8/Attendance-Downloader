package com.example.AttendenceDownloader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailForPasswordChange;
    private final FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Forgot Password?");

        emailForPasswordChange = findViewById(R.id.emailForPasswordChange);
    }

    public void submitButtonClicked(View view){
        mauth.sendPasswordResetEmail(emailForPasswordChange.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "Reset email instructions sent to " + emailForPasswordChange.getText().toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), emailForPasswordChange.getText().toString() + " does not exist", Toast.LENGTH_LONG).show();
                    }
                });
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}