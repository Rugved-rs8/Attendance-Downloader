package com.example.AttendenceDownloader;

public class ClassInfo {
    private String className, subjectName, noOfStudents;

    public ClassInfo(String className, String subjectName, String noOfStudents) {
        this.className = className;
        this.subjectName = subjectName;
        this.noOfStudents = noOfStudents;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNoOfStudents() {
        return noOfStudents;
    }

    public void setNoOfStudents(String noOfStudents) {
        this.noOfStudents = noOfStudents;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
