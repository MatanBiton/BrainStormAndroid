package com.mbco.brainstormandroid.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Page {
    private String courseUid;
    private String title;
    private String body;
    private String uploadFeature;

    public Page(String courseUid, String title, String body, String uploadFeature) {
        this.courseUid = courseUid;
        this.title = title;
        this.body = body;
        this.uploadFeature = uploadFeature;
    }

    public Page(JSONObject jsonObject) throws JSONException{
        this.courseUid = jsonObject.getString("course_uid");
        this.title = jsonObject.getString("title");
        this.body = jsonObject.getString("body");
        this.uploadFeature = jsonObject.getString("upload_feature");
    }

    public String getCourseUid() {
        return courseUid;
    }
    public void setCourseUid(String courseUid) {
        this.courseUid = courseUid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getUploadFeature() {
        return uploadFeature;
    }
    public void setUploadFeature(String uploadFeature) {
        this.uploadFeature = uploadFeature;
    }

    public JSONObject GetJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("course_uid", this.courseUid);
        object.put("title", this.title);
        object.put("body", body);
        object.put("upload_feature", this.uploadFeature);
        return object;
    }
}
