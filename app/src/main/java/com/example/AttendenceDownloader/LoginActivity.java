package com.example.AttendenceDownloader;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
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
    ListView classLview;
    List<ClassInfo> classList;
    ClassListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        classLview = findViewById(R.id.classLview);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        //Load data
        loadData();

        classLview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoginActivity.this, RollCall.class);
                intent.putExtra("Example Item", classList.get(position));
                startActivity(intent);
            }
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
            return true;
        });

        floatingActionButton.setOnClickListener(view -> {
            ClassDetailsDialog obj = new ClassDetailsDialog();
            obj.show(getSupportFragmentManager(), "Create Class");
        });
    }

    @Override
    public void applyTexts(String cn, String sn, String nos) {
        ClassInfo obj = new ClassInfo(cn, sn, nos);
        classList.add(obj);
        Toast.makeText(this, "Class: "+cn+"\nSubject: "+sn+"\nnos: "+nos, Toast.LENGTH_LONG).show();

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
}