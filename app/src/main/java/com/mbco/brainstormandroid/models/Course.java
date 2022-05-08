package com.mbco.brainstormandroid.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Course {
    private CourseInfo info;
    private ArrayList<Page> pages;

    public Course(CourseInfo info, ArrayList<Page> pages) {
        this.info = info;
        this.pages = pages;
    }

    public CourseInfo getInfo() {
        return info;
    }

    public void setInfo(CourseInfo info) {
        this.info = info;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public JSONObject GetJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("course_info", info.GetJson());
        JSONArray pagesJson = new JSONArray();
        for (Page page : pages){
            pagesJson.put(page.GetJson());
        }
        object.put("pages", pagesJson);
        return object;
    }
}
