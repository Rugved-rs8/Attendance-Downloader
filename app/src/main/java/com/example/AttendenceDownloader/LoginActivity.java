package com.example.AttendenceDownloader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements ClassDetailsDialog.ClassDetailsDialogListener {
    private ListView classLview;
    private List<ClassInfo> classList;
    private ClassListAdapter adapter;
    private final String[] permissionsArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login2);
         setTitle("Home");

         TextView classroomsTextView = findViewById(R.id.classroomsTextView);
         classLview = findViewById(R.id.classLview);
         FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        //Load data
        loadData();

        if(classList.isEmpty())classroomsTextView.setVisibility(View.VISIBLE);

        classLview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(LoginActivity.this, RollCall.class);
            intent.putExtra("Example Item", classList.get(position));
            startActivity(intent);
        });

        classLview.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            final int pos_of_item_to_delete = position;
            new AlertDialog.Builder(LoginActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to delete this class ?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        classList.remove(pos_of_item_to_delete);
                        saveData();
                    })
                    .setNegativeButton("Cancel", null).show();
            if(classList.isEmpty())classroomsTextView.setVisibility(View.VISIBLE);
            return true;
        });

        floatingActionButton.setOnClickListener(view -> {
            ClassDetailsDialog obj = new ClassDetailsDialog();
            obj.show(getSupportFragmentManager(), "Create Class");
            classroomsTextView.setVisibility(View.GONE);
        });

        //  Runtime Premissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionsArr, 80);
        }
    }

    //    Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                startActivity(new Intent(LoginActivity.this, AboutActivity.class));
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void applyTexts(String cn, String sn, String nos) {
        ClassInfo obj = new ClassInfo(cn, sn, nos);
        classList.add(obj);
        Toast.makeText(this, "Class: "+cn+"\nSubject: "+sn+"\nNo. of Students: "+nos, Toast.LENGTH_LONG).show();

        //Save Data
        saveData();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classList);
        editor.putString("task list", json);
        editor.apply();
        adapter.notifyDataSetChanged();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ClassInfo>>(){}.getType();
        classList = gson.fromJson(json, type);
        if(classList == null){
            classList = new ArrayList<>();
        }
        if(adapter == null){
            adapter = new ClassListAdapter(this, R.layout.list_item, classList);
        }
        adapter = new ClassListAdapter(this, R.layout.list_item, classList);
        classLview.setAdapter(adapter);
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