package com.example.AttendenceDownloader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

//                                 #######           Splash Screen           #######

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView iconImageView;
    TextView rs8TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        iconImageView = findViewById(R.id.iconImageView);
        rs8TextView = findViewById(R.id.rs8TextView);

        iconImageView.setAnimation(topAnim);
        rs8TextView.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}