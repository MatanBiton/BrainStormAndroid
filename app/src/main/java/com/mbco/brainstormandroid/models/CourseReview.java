package com.mbco.brainstormandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseReview {
    private String courseUid;
    private String studentUid;
    private int rating;
    private String comment;

    public CourseReview(String courseUid, String studentUid, int rating, String comment) {
        this.courseUid = courseUid;
        this.studentUid = studentUid;
        this.rating = rating;
        this.comment = comment;
    }

    public CourseReview(JSONObject object) throws JSONException{
        this.courseUid = object.getString("course_uid");
        this.studentUid = object.getString("student_uid");
        this.rating = object.getInt("rating");
        this.comment = object.getString("comment");
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
