package com.example.AttendenceDownloader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditText,signUpEmailEditText,collegeNameEditText,signUpPasswordEditText,confirmPasswordEditText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEditText = findViewById(R.id.nameEditText);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditText);
        collegeNameEditText = findViewById(R.id.collegeNameEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void createAccountClicked(View view){
        if(nameEditText.getText().toString().isEmpty() || signUpEmailEditText.getText().toString().isEmpty() ||
                collegeNameEditText.getText().toString().isEmpty() || signUpPasswordEditText.getText().toString().isEmpty()
        || confirmPasswordEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Fill all fields.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(signUpPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
                mAuth.createUserWithEmailAndPassword(signUpEmailEditText.getText().toString(), signUpPasswordEditText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(this.toString(), "createUserWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getBaseContext(), "Account created Successfully!!\nPlease Login.", Toast.LENGTH_SHORT).show();
                                    Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i1);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(this.toString(), "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getBaseContext(), "Sign Up failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                 // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Name", nameEditText.getText().toString());
                user.put("Email", signUpEmailEditText.getText().toString());
                user.put("College Name", collegeNameEditText.getText().toString());

                 // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(this.toString(), "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(getBaseContext(), "Account created successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(this.toString(), "Error adding document", e);
                                Toast.makeText(getBaseContext(), "Account creation failed.\nPlease try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else {
                Toast.makeText(getBaseContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}