package com.example.appchamcong;

public class ClassItem {
    private long cid;
    private String className;
    private String SubjectName;

    public ClassItem(String className, String subjectName) {
        this.className = className;
        this.SubjectName = subjectName;
    }

    public ClassItem(long cid, String className, String subjectName) {
        this.cid = cid;
        this.className = className;
        SubjectName = subjectName;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }
}

