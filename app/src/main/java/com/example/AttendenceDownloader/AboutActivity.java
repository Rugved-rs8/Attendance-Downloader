package com.example.AttendenceDownloader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");

        TextView aboutTextView = findViewById(R.id.aboutTextView);
        aboutTextView.setText("Attendance Downloader is an Android application intended for Teachers and Professors to mark the attendance of their class. \nThis application is designed to avoid proxy(false attendance being awarded to a student by means of his/her friends or batchmates) and also saves a lot of paper.");
    }
}