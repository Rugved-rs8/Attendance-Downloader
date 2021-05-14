 package com.example.AttendenceDownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RollCall extends AppCompatActivity {
    private RecyclerView rollCallRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    List<String> rollnosList;
    Map<String, String> finalList = new HashMap<>();
    List<String[]> finalArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);

        Intent intent = getIntent();
        ClassInfo classInfo = intent.getParcelableExtra("Example Item");
        int noOfStudents = Integer.parseInt(classInfo.getNoOfStudents());

        rollnosList = new ArrayList<>();
        for(int i=1;i<=noOfStudents;i++){
            rollnosList.add(String.valueOf(i));
        }
        Toast.makeText(this, "No. of Students: "+noOfStudents, Toast.LENGTH_SHORT).show();
        rollCallRecyclerView = findViewById(R.id.rollCallRecyclerView);
        recyclerAdapter = new RecyclerAdapter(rollnosList);
        rollCallRecyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rollCallRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rollCallRecyclerView);
    }

    String present = null, absent = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            switch(direction){
                case ItemTouchHelper.LEFT:                                      //For Marking Absent
                    absent = rollnosList.get(position);
                    finalList.put(absent, "A");
                    rollnosList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);
                    Snackbar.make(rollCallRecyclerView, absent + " Absent.", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rollnosList.add(position, absent);
                                    finalList.remove(absent);
                                    recyclerAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    if(rollnosList.isEmpty()) whenListGetsEmpty();
                    break;

                case ItemTouchHelper.RIGHT:                                    //For Marking Present
                    present = rollnosList.get(position);
                    finalList.put(present, "P");
                    rollnosList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);
                    Snackbar.make(rollCallRecyclerView, present + " Present.", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rollnosList.add(position, present);
                                    finalList.remove(present);
                                    recyclerAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    if(rollnosList.isEmpty()) whenListGetsEmpty();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(RollCall.this, R.color.design_default_color_error))
                    .addSwipeLeftActionIcon(R.drawable.absentsymbol)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(RollCall.this, R.color.design_default_color_secondary_variant))
                    .addSwipeRightActionIcon(R.drawable.presentsymbol)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void whenListGetsEmpty(){
        AlertDialog.Builder enterFilenameAlert = new AlertDialog.Builder(RollCall.this);
        View fnView = getLayoutInflater().inflate(R.layout.file_name_dialog, null);
        EditText fileNameEditText = fnView.findViewById(R.id.fileNameEditText);
        String fileName = fileNameEditText.getText().toString();
        Button createFileButton = fnView.findViewById(R.id.createFileButton);
        Button cancelButton = fnView.findViewById(R.id.cancelButton);
        enterFilenameAlert.setView(fnView);

        AlertDialog alertDialog = enterFilenameAlert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        createFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Converting HashMap 'finalList' to an ArrayList 'finalArrayList' so that it can be written in the CSV file
                List<String> finalAttendance = new ArrayList<>(finalList.values());
                List<String> finalRoll = new ArrayList<>(finalList.keySet());
                for(int i=0;i<finalList.size();i++){
                    finalArrayList.add(new String[]{finalRoll.get(i), finalAttendance.get(i)});
                }

                //  Creating CSV file and inserting the 'finalArrayList' of marked students into it
                String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+fileName+".csv");
                try {
                     CSVWriter writer = null;
                     writer = new CSVWriter(new FileWriter(csv));
                     writer.writeAll(finalArrayList); // data is adding to csv
                     writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}