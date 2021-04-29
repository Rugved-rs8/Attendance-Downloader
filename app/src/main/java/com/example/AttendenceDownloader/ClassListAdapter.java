package com.example.AttendenceDownloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class ClassListAdapter extends ArrayAdapter<ClassInfo> {
    private Context mContext;
    int mResource;

    public ClassListAdapter(@NonNull Context context, int resource, @NonNull List<ClassInfo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String classname = getItem(position).getClassName();
        String subname = getItem(position).getSubjectName();
        String nos = getItem(position).getNoOfStudents();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView tvcn = convertView.findViewById(R.id.heading);
        TextView tvsn = convertView.findViewById(R.id.subHeading);
        tvcn.setText(classname);
        tvsn.setText(subname);
        return convertView;
    }
}
