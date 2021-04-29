package com.example.AttendenceDownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            login();
        }
    }

    public void forgotPasswordClicked(View view){
        Intent i1 = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i1);
    }

    public void loginClicked(View view){
        if(emailEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Email is Required.", Toast.LENGTH_SHORT).show();
        }
        if(passwordEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Password is Required.", Toast.LENGTH_SHORT).show();
        }
        if(emailEditText.getText().toString().isEmpty() && passwordEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Email and Password are Required.", Toast.LENGTH_SHORT).show();
        }
        if(passwordEditText.getText().toString() != null && emailEditText.getText().toString() != null){
            mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(this.toString(), "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getBaseContext(), "Login Successful!!", Toast.LENGTH_SHORT).show();
                                login();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(this.toString(), "signInWithEmail:failure", task.getException());
                                Toast.makeText(getBaseContext(), "Login failed.Please Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void signUpClicked(View view){
        Intent i3 = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i3);
    }

    public void login(){
        Intent i2 = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i2);
    }
}