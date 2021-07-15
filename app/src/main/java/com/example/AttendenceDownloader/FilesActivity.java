package com.example.AttendenceDownloader;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.AttendenceDownloader.databinding.ActivityFilesBinding;
import com.example.AttendenceDownloader.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class FilesActivity extends AppCompatActivity {

    String[] permissionsArr = {"android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabItem tabItemClasses = findViewById(R.id.tabItemClasses);
        TabItem tabItemRecords = findViewById(R.id.tabItemRecords);

        com.example.AttendenceDownloader.databinding.ActivityFilesBinding binding = ActivityFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        //  Runtime Premissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionsArr, 80);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 80){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d( "Permissions Result: ", "   GRANTED!!!  ");
            }
            else {
                Log.d( "Permissions Result: ", "   DENIED!!!  ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsArr, 80);
                    Toast.makeText(this, "Permission is needed to download your classes attendance as a .csv file.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}