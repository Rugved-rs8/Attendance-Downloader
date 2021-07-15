package com.example.AttendenceDownloader;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private final List<String> rollnosList;
    private final String[] namesList;

    public RecyclerAdapter(List<String> rollnosList, String[] namesList) {
        this.rollnosList = rollnosList;
        this.namesList = namesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.roll_call_item, parent, false);
        // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
        // so that we don't have to do this expensive allocation in onBindViewHolder
        return new ViewHolder(view, new MyCustomEditTextListener());
    }

    // Passing data into the RecyclerView Items.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rollNoTextView.setText(rollnosList.get(position));
        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.studentNameEditText.setText(namesList[holder.getAdapterPosition()]);
    }

    @Override
    public int getItemCount() {
        return rollnosList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rollNoTextView;
        EditText studentNameEditText;
        public MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            rollNoTextView = itemView.findViewById(R.id.rollNoTextView);
            studentNameEditText = itemView.findViewById(R.id.studentNameEditText);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.studentNameEditText.addTextChangedListener(myCustomEditTextListener);
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            namesList[position] =  charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

}