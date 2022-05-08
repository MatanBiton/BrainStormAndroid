package com.mbco.brainstormandroid.models;

import androidx.dynamicanimation.animation.SpringAnimation;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentAdvancing {

    private String courseUid;
    private String studentUid;
    private int pageNumber;
    private double grade;

    public StudentAdvancing(String courseUid, String studentUid, int pageNumber, double grade) {
        this.courseUid = courseUid;
        this.studentUid = studentUid;
        this.pageNumber = pageNumber;
        this.grade = grade;
    }

    public StudentAdvancing(JSONObject object) throws JSONException {
        this.courseUid = object.getString("course_uid");
        this.studentUid = object.getString("student_uid");
        this.pageNumber = object.getInt("page_number");
        this.grade = object.getDouble("grade");
    }

    public String getCourseUid() {
        return courseUid;
    }

    public void setCourseUid(String courseUid) {
        this.courseUid = courseUid;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public JSONObject GetJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("course_uid", courseUid);
        object.put("student_uid", studentUid);
        object.put("page_number", pageNumber);
        object.put("grade", grade);
        return object;

    }
}
