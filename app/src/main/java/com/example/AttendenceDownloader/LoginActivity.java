package com.example.AttendenceDownloader;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements ClassDetailsDialog.ClassDetailsDialogListener {
    private ListView classLview;
    List<ClassInfo> classList; //= new ArrayList<>();
    public static final String EXTRA_TEXT = "com.example.AttendenceDownloader.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        classLview = findViewById(R.id.classLview);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.AttendenceDownloader", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ClassInfo>>(){}.getType();
        classList = gson.fromJson(json, type);
        if(classList == null){
            classList = new ArrayList<>();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassDetailsDialog obj = new ClassDetailsDialog();
                obj.show(getSupportFragmentManager(), "Create Class");
            }
        });

        classLview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getApplicationContext())
                        .setIcon(android.R.drawable.ic_input_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Delete class "+classList.get(position)+"?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                classList.remove(position);
                                classLview.notify();
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
                return false;
            }
        });
    }

    @Override
    public void applyTexts(String cn, String sn, String nos) {
        ClassInfo obj = new ClassInfo(cn, sn, nos);
        classList.add(obj);
        ClassListAdapter adapter = new ClassListAdapter(this, R.layout.list_item, classList);
        classLview.setAdapter(adapter);
        Toast.makeText(this, "Class: "+cn+"\nSubject: "+sn+"\nnos: "+nos, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.AttendenceDownloader", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classList);
        editor.putString("task list", json);
        editor.apply();

        classLview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RollCall.class);
                intent.putExtra(EXTRA_TEXT, nos);
                startActivity(intent);
            }
        });
    }
}