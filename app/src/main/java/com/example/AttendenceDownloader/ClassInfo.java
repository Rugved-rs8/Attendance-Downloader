package com.example.AttendenceDownloader;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassInfo implements Parcelable {
    private final String className;
    private final String subjectName;
    private final String noOfStudents;

    public ClassInfo(String className, String subjectName, String noOfStudents) {
        this.className = className;
        this.subjectName = subjectName;
        this.noOfStudents = noOfStudents;
    }

    protected ClassInfo(Parcel in) {
        className = in.readString();
        subjectName = in.readString();
        noOfStudents = in.readString();
    }

    public static final Creator<ClassInfo> CREATOR = new Creator<ClassInfo>() {
        @Override
        public ClassInfo createFromParcel(Parcel in) {
            return new ClassInfo(in);
        }

        @Override
        public ClassInfo[] newArray(int size) {
            return new ClassInfo[size];
        }
    };

    public String getClassName() {
        return className;
    }

    public String getNoOfStudents() {
        return noOfStudents;
    }

    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeString(subjectName);
        dest.writeString(noOfStudents);
    }
}
