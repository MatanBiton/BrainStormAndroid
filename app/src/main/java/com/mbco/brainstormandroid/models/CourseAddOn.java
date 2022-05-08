package com.mbco.brainstormandroid.models;

import android.graphics.Bitmap;

import com.mbco.brainstormandroid.student.StudentCourses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class CourseAddOn {

    private String courseUid;
    private String studentUid;
    private int pageNumber;
    private String fileUid;
    private byte[] file;

    public CourseAddOn(String courseUid, String studentUid, int pageNumber, String fileUid, byte[] file) {
        this.courseUid = courseUid;
        this.studentUid = studentUid;
        this.pageNumber = pageNumber;
        this.fileUid = fileUid;
        this.file = file;
        if (this.fileUid.equals("")){
            try {
                GenerateUid();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public CourseAddOn(JSONObject object, byte[] file) throws JSONException {
        this.courseUid = object.getString("course_uid");
        this.studentUid = object.getString("student_uid");
        this.pageNumber = object.getInt("page_number");
        this.fileUid = object.getString("file_uid");
        this.file = file;
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

    public String getFileUid() {
        return fileUid;
    }

    public void setFileUid(String fileUid) {
        this.fileUid = fileUid;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public JSONObject getJson() throws JSONException{
        JSONObject object = new JSONObject();
        object.put("course_uid", this.courseUid);
        object.put("student_uid", this.studentUid);
        object.put("page_number", this.pageNumber);
        object.put("file_uid", this.fileUid);
        return object;
    }

    public void GenerateUid() throws NoSuchAlgorithmException {
        String originalString = this.courseUid + this.studentUid + this.pageNumber;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        this.fileUid = hexString.toString();
    }
}
