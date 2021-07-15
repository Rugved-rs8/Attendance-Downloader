package com.example.AttendenceDownloader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentRecords extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewfrag = inflater.inflate(R.layout.fragment_records, container, false);
        ListView recordsListView = viewfrag.findViewById(R.id.recordsListView);

        return viewfrag;
    }
}
