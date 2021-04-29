package com.example.AttendenceDownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RollCall extends AppCompatActivity {
    private RecyclerView rollCallRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    List<String> rollnosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);

        Intent intent = getIntent();
        int noOfStudents = Integer.parseInt(intent.getStringExtra(LoginActivity.EXTRA_TEXT));
        rollnosList = new ArrayList<>();
        for(int i=1;i<=noOfStudents;i++){
            rollnosList.add(String.valueOf(i));
        }
        Toast.makeText(this, "No. of Students: "+Integer.toString(noOfStudents), Toast.LENGTH_SHORT).show();
        rollCallRecyclerView = findViewById(R.id.rollCallRecyclerView);

        recyclerAdapter = new RecyclerAdapter(rollnosList);
        rollCallRecyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rollCallRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rollCallRecyclerView);
    }

    String present = null, absent = null;
    List<String> presentStudentsList = new ArrayList<>();
    List<String> absentStudentsList = new ArrayList<>();

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            switch(direction){
                case ItemTouchHelper.LEFT:                                                     //For Marking Absent
                    absent = rollnosList.get(position);
                    absentStudentsList.add(absent);
                    rollnosList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);
                    Snackbar.make(rollCallRecyclerView, absent + " Absent.", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rollnosList.add(position, absent);
                                    absentStudentsList.remove(absentStudentsList.lastIndexOf(absent));
                                    recyclerAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;

                case ItemTouchHelper.RIGHT:                                                    //For Marking Present
                    present = rollnosList.get(position);
                    presentStudentsList.add(present);
                    rollnosList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);
                    Snackbar.make(rollCallRecyclerView, present + " Present.", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rollnosList.add(position, present);
                                    presentStudentsList.remove(presentStudentsList.lastIndexOf(present));
                                    recyclerAdapter.notifyItemInserted(position);
                                }
                            }).show();
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
}