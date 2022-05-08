package com.mbco.brainstormandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentInCourse {

    private String uid, studentUid, date;
    private int completed;
    private double grade;

    public StudentInCourse(String uid, String studentUid, String date, int completed, double grade) {
        this.uid = uid;
        this.studentUid = studentUid;
        this.date = date;
        this.completed = completed;
        this.grade = grade;
    }

    public StudentInCourse(JSONObject object) throws JSONException {
        this.uid = object.getString("uid");
        this.studentUid = object.getString("student_uid");
        this.date = object.getString("date");
        this.completed = object.getInt("completed");
        this.grade = object.getDouble("grade");
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public JSONObject getJson() {
        try {
            JSONObject object = new JSONObject();
            object.put("uid", uid);
            object.put("student_uid", studentUid);
            object.put("date", date);
            object.put("completed", completed);
            object.put("grade", grade);
            return object;
        } catch (JSONException e){
            return null;
        }
    }
}
