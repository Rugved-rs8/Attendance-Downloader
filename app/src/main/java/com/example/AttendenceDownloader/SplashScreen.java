package com.example.AttendenceDownloader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

//                                        ########      Login Page        ########

public class SplashScreen extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            login();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setTitle("Login");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        // Set the dimensions of the Google sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);

        createRequest();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // When Google SignIn button is clicked this function is called
        signInButton.setOnClickListener(v -> signIn());
    }

    public void forgotPasswordClicked(View view){
        Intent i1 = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i1);
    }

    public void loginClicked(View view){
        if(emailEditText.getText().toString().isEmpty() && passwordEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Email and Password are Required.", Toast.LENGTH_SHORT).show();
        }
        else if(emailEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Email is Required.", Toast.LENGTH_SHORT).show();
        }
        else if(passwordEditText.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Password is Required.", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(this.toString(), "signInWithEmail:success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getBaseContext(), "Login Successful!!", Toast.LENGTH_SHORT).show();
                                login();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(this.toString(), "signInWithEmail:failure", task.getException());
                                Toast.makeText(getBaseContext(), "Login failed. Please Try again.", Toast.LENGTH_SHORT).show();
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
        finish();
    }

    // For Google Sign In

    private void createRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @SuppressWarnings("deprecation")
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("firebaseAuthWithGoogle:" , "  Success!!");
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getBaseContext(), "Login Successful!!", Toast.LENGTH_SHORT).show();
                        login();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getBaseContext(), "Login failed. Please Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}