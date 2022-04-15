package com.mbco.brainstormandroid;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

public class Teacher extends User{

    private String certification;
    private int experience;
    private double rating;
    private int approval;


    //constructors
    public Teacher(String UID, int id, String email, String password, String firstName, String lastName, Bitmap personalPhoto, String address, String city, String phone, String birthDay, String userType, String certification, int experience, double rating, int approval) {
        super(UID, id, email, password, firstName, lastName, personalPhoto, address, city, phone, birthDay, userType);
        this.certification = certification;
        this.experience = experience;
        this.rating = rating;
        this.approval = approval;
    }
    public Teacher(User user){
        super(user);
        rating = -1;
        approval = 0;
    }
    public Teacher(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        this.certification = jsonObject.getString("certification");
        this.experience = jsonObject.getInt("experience");
        this.rating = Double.parseDouble(jsonObject.getString("rating"));
        this.approval = Integer.parseInt(jsonObject.getString("approval"));
    }


    //getters
    public String getCertification() {
        return certification;
    }
    public int getExperience() {
        return experience;
    }
    public double getRating() {
        return rating;
    }
    public int getApproval() {
        return approval;
    }


    //setters
    public void setCertification(String certification) {
        this.certification = certification;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setApproval(int approval) {
        this.approval = approval;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject =  super.getJSONObject();
        jsonObject.put("certification", certification);
        jsonObject.put("experience", experience);
        jsonObject.put("rating", rating);
        jsonObject.put("approval", approval);
        return jsonObject;
    }
}
