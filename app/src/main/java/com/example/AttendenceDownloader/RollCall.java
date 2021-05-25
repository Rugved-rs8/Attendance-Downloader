 package com.example.AttendenceDownloader;

 import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

 public class RollCall extends AppCompatActivity implements FilenameDialog.FilenameDialogListener{

    private RecyclerView rollCallRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private final List<String> rollnosList = new ArrayList<>();
    private final Map<String, String> finalList = new HashMap<>();
    private final List<String[]> finalArrayList = new ArrayList<>();
    private File csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);

        Intent intent = getIntent();
        ClassInfo classInfo = intent.getParcelableExtra("Example Item");
        int noOfStudents = Integer.parseInt(classInfo.getNoOfStudents());

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

     final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
         @Override
         public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
             return false;
         }

         @Override
         public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
             final int position = viewHolder.getAdapterPosition();
             switch (direction) {
                 case ItemTouchHelper.LEFT:                                      //For Marking Absent
                     absent = rollnosList.get(position);
                     finalList.put(absent, "A");
                     rollnosList.remove(position);
                     recyclerAdapter.notifyItemRemoved(position);
                     Snackbar.make(rollCallRecyclerView, absent + " Absent.", Snackbar.LENGTH_LONG)
                             .setAction("Undo", v -> {
                                 rollnosList.add(position, absent);
                                 finalList.remove(absent);
                                 recyclerAdapter.notifyItemInserted(position);
                             }).show();
                     if (rollnosList.isEmpty()) {
                         whenListGetsEmpty();
                     }
                     break;

                 case ItemTouchHelper.RIGHT:                                    //For Marking Present
                     present = rollnosList.get(position);
                     finalList.put(present, "P");
                     rollnosList.remove(position);
                     recyclerAdapter.notifyItemRemoved(position);
                     Snackbar.make(rollCallRecyclerView, present + " Present.", Snackbar.LENGTH_LONG)
                             .setAction("Undo", v -> {
                                 rollnosList.add(position, present);
                                 finalList.remove(present);
                                 recyclerAdapter.notifyItemInserted(position);
                             }).show();
                     if (rollnosList.isEmpty()) {
                         whenListGetsEmpty();
                     }
                     break;
             }
         }

         @Override
         public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

             new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                     .addSwipeLeftBackgroundColor(ContextCompat.getColor(RollCall.this, R.color.red))
                     .addSwipeRightBackgroundColor(ContextCompat.getColor(RollCall.this, R.color.green))
                     .create()
                     .decorate();
             super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
         }
     };

     public boolean isExternalStorageWritable() {
         String state = Environment.getExternalStorageState();
         return Environment.MEDIA_MOUNTED.equals(state);
     }

     public void openDialog(){
         FilenameDialog obj = new FilenameDialog();
         obj.show(getSupportFragmentManager(), "Create File");
     }

     @Override
     public void applyFilename(String fn) {
         //  Creating CSV file
         try {
             csv = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/AttendanceFolder");

         /*    boolean var = false;
             if (!csv.exists()) var = csv.mkdir();  */

         } catch (Exception e) {
             e.printStackTrace();
         }

         String fileName = csv.toString()+"/"+fn+".csv";
         Log.d("  File path  ",fileName);

         if(isExternalStorageWritable()){
             Log.d("  writable?  ", " YAS !!");

             CSVWriter writer = null;
             try {
                 writer = new CSVWriter(new FileWriter(fileName));
             } catch (NullPointerException | IOException ignored) { }

             try {
                 assert writer != null;
                 writer.writeAll(finalArrayList);                           // data is adding to csv
                 writer.close();
                 Toast.makeText(this, "File created Successfully.",Toast.LENGTH_SHORT).show();
                 Log.d("  File Creation ", " Successful !!!!");
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

         Log.d(" Filename  ", "!!"+fn);
     }

    public void whenListGetsEmpty(){

         openDialog();      // Dialog box opens after the last roll no in the list is swiped

        //  Converting HashMap 'finalList' to an ArrayList 'finalArrayList' so that it can be written to the CSV file
        List<String> finalAttendance = new ArrayList<>(finalList.values());
        List<String> finalRoll = new ArrayList<>(finalList.keySet());
        finalArrayList.add("Roll No.,Attendance".split(","));
        for(int i=0;i<finalList.size();i++){
            finalArrayList.add((finalRoll.get(i) + "," + finalAttendance.get(i)).split(","));
        }

        for(int i=0;i<finalArrayList.size();i++){
            Log.d(i+".  ", Arrays.toString(finalArrayList.get(i)));
        }
    }
 }