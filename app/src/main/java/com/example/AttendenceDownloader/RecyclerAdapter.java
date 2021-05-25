package com.example.AttendenceDownloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    List<String> rollnosList;
    public RecyclerAdapter(List<String> rollnosList) {
        this.rollnosList = rollnosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.roll_call_item, parent, false);
        return new ViewHolder(view);
    }

    // Passing data into the RecyclerView Items.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rollNoTextView.setText(rollnosList.get(position));
    }

    @Override
    public int getItemCount() {
        return rollnosList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rollNoTextView, studentNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rollNoTextView = itemView.findViewById(R.id.rollNoTextView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
        }
    }
}
