package com.example.AttendenceDownloader;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentClasses extends Fragment implements ClassDetailsDialog.ClassDetailsDialogListener{

    private ListView classLview;
    private List<ClassInfo> classList;
    private ClassListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewc = inflater.inflate(R.layout.activity_login2, container, false);
        classLview = viewc.findViewById(R.id.classLview);
        FloatingActionButton floatingActionButton = viewc.findViewById(R.id.floatingActionButton);

        //Load data
        loadData();

        classLview.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), RollCall.class);
            intent.putExtra("Example Item", classList.get(position));
            startActivity(intent);
        });

        classLview.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            final int pos_of_item_to_delete = position;
            new AlertDialog.Builder(getContext())
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
            obj.show(getChildFragmentManager(), "Create Class");
        });

        return viewc;
    }

    @Override
    public void applyTexts(String cn, String sn, String nos) {
        ClassInfo obj = new ClassInfo(cn, sn, nos);
        classList.add(obj);
        Toast.makeText(getContext(), "Class: "+cn+"\nSubject: "+sn+"\nNo. of Students: "+nos, Toast.LENGTH_LONG).show();

        //Save Data
        saveData();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classList);
        editor.putString("task list", json);
        editor.apply();
        adapter.notifyDataSetChanged();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ClassInfo>>(){}.getType();
        classList = gson.fromJson(json, type);
        if(classList == null){
            classList = new ArrayList<>();
        }
        if(adapter == null){
            adapter = new ClassListAdapter(requireContext(), R.layout.list_item, classList);
        }
        adapter = new ClassListAdapter(requireContext(), R.layout.list_item, classList);
        classLview.setAdapter(adapter);
    }
}
