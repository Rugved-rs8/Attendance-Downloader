 package com.example.AttendenceDownloader;

 import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVWriter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

 public class RollCall extends AppCompatActivity implements FilenameDialog.FilenameDialogListener{

    private RecyclerView rollCallRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private final List<String> rollnosList = new ArrayList<>();
    private final Map<String, String> finalList = new HashMap<>();
    private final List<String[]> finalArrayList = new ArrayList<>();
    private String date, classname, subjectname;
    private String[] stdnamesArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        setTitle("Take Attendance");

        Intent intent = getIntent();
        ClassInfo classInfo = intent.getParcelableExtra("Example Item");
        int noOfStudents = Integer.parseInt(classInfo.getNoOfStudents());
        classname = classInfo.getClassName();
        subjectname = classInfo.getSubjectName();
        stdnamesArr = new String[noOfStudents];

      //  FloatingActionButton importStdntNamesFloatingActionButton = findViewById(R.id.importStdntNamesFloatingActionButton);

        for(int i=1;i<=noOfStudents;i++){
            rollnosList.add(String.valueOf(i));
        }
        Toast.makeText(this, "No. of Students: "+noOfStudents, Toast.LENGTH_SHORT).show();
        rollCallRecyclerView = findViewById(R.id.rollCallRecyclerView);
        recyclerAdapter = new RecyclerAdapter(rollnosList, stdnamesArr);
        rollCallRecyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rollCallRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rollCallRecyclerView);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        }

     /*   importStdntNamesFloatingActionButton.setOnClickListener(v -> {
            openDialog();      // Dialog box opens after the last roll no in the list is swiped
        });*/
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

  /*   public void openDialog(){
         FilenameDialog obj = new FilenameDialog();
         obj.show(getSupportFragmentManager(), "Create File");
     }*/

     @Override
     public void applyFilename(String fn, String cn) {

         // Importing the given column from the file provided by the user
         readExcelFromStorage(this, fn);

     }

    public void whenListGetsEmpty(){

        //  Converting HashMap 'finalList' to an ArrayList 'finalArrayList' so that it can be written to the CSV file
        List<String> finalAttendance = new ArrayList<>(finalList.values());
        List<String> finalRoll = new ArrayList<>(finalList.keySet());
        String rd = "Roll No.,"+"Student Name,"+date;
        finalArrayList.add(rd.split(","));
        for(int i=0;i<finalList.size();i++){
            finalArrayList.add((finalRoll.get(i) + ","+ stdnamesArr[i] +","+ finalAttendance.get(i)).split(","));
        }

        for(int i=0;i<finalArrayList.size();i++){
            Log.d(i+".  ", Arrays.toString(finalArrayList.get(i)));
        }

        //  Creating CSV file

        File csv = null;
        try {
            csv = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/AttendanceFolder/");

            boolean mkd = false;
            if (!csv.exists()) mkd = csv.mkdir();
            Log.d("  Directory made:  ", Boolean.toString(mkd));

        } catch (Exception e) {
            e.printStackTrace();
        }

        assert csv != null;
        String fileName = csv.toString()+"/"+classname+"_"+subjectname+".csv";
        Log.d("  File path  ",fileName);

        if(isExternalStorageWritable()){
            Log.d("  writable?  ", " YAS !!");

            CSVWriter writer;
            try {
                File csvFile = new File(fileName);
                boolean cf = csvFile.createNewFile();
                Log.d(" File exists: ", String.valueOf(cf));
                writer = new CSVWriter(new FileWriter(fileName));
                writer.writeAll(finalArrayList);                           // data is adding to csv
                writer.close();
                Toast.makeText(this, "File created Successfully.",Toast.LENGTH_SHORT).show();
                Log.d("  File Creation ", " Successful !!!!");
            } catch (NullPointerException | IOException ignored) { }

            // For creating a notification
            String message = "To view excel file go to "+ fileName;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(RollCall.this, "MyNotification")
                    .setContentTitle("Attendance Marked!!!")
                    .setSmallIcon(R.drawable.ic_baseline_done_24)
                    .setContentText(message)
                    .setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(RollCall.this);
            managerCompat.notify(1, builder.build());

        }
    }

     public static void readExcelFromStorage(Context context, String fileName) {
         File file = new File(context.getExternalFilesDir(null), fileName);
         FileInputStream fileInputStream = null;

         try {
             fileInputStream = new FileInputStream(file);
             Log.d("Reading from Excel " , String.valueOf(file));

             // Create instance having reference to .xls file
             HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

             // Fetch sheet at position 'i' from the workbook
             HSSFSheet sheet = workbook.getSheetAt(0);

             // Iterate through each row
             for (Row row : sheet) {
                 if (row.getRowNum() > 0) {
                     // Iterate through all the cells in a row (Excluding header row)
                     Iterator<Cell> cellIterator = row.cellIterator();

                     while (cellIterator.hasNext()) {
                         Cell cell = cellIterator.next();

                         // Check cell type and format accordingly
                         switch (cell.getCellType()) {
                             case Cell.CELL_TYPE_NUMERIC:
                                 // Print cell value
                                 System.out.println(cell.getNumericCellValue());
                                 break;

                             case Cell.CELL_TYPE_STRING:
                                 System.out.println(cell.getStringCellValue());
                                 break;
                         }
                     }
                 }
             }
         }catch (IOException e) {
             Log.d("ErrorReadingException: ", String.valueOf(e));
         } catch (Exception e) {
             Log.d("FailedToReadFileDueTo: ", String.valueOf(e));
         } finally {
             try {
                 if (null != fileInputStream) {
                     fileInputStream.close();
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
         }
     }

 }