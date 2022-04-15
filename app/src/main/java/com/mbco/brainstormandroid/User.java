package com.mbco.brainstormandroid;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String UID;
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Bitmap photo;
    private String address;
    private String city;
    private String phone;
    private String birthDay;
    private String userType;


    //constructors
    public User(String UID, int id, String email, String password, String firstName, String lastName, Bitmap photo, String address, String city, String phone, String birthDay, String userType) {
        this.UID = UID;
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.birthDay = birthDay;
        this.userType = userType;
    }
    public User(){
        this.photo = null;
        this.firstName = "";
        this.lastName = "";
        this.id = 0;
        this.city = "";
        this.address = "";
        this.phone = "";
        this.birthDay = "";
        this.userType = "";
        this.email = "";
        this.password = "";
        this.UID = "";
    }
    public User(User user){
        this.photo = user.photo;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.id = user.id;
        this.city = user.city;
        this.address = user.address;
        this.phone = user.phone;
        this.birthDay = user.birthDay;
        this.userType = user.userType;
        this.email = user.email;
        this.password = user.password;
        this.UID = user.UID;
    }
    public User(JSONObject jsonObject) throws JSONException {
        this.UID = jsonObject.getString("uid");
        this.id = Integer.parseInt(jsonObject.getString("id"));
        this.email = jsonObject.getString("email");
        this.password = jsonObject.getString("password");
        this.firstName = jsonObject.getString("first_name");
        this.lastName = jsonObject.getString("last_name");
        this.photo = HelpFunctions.StringToBitMap(jsonObject.getString("photo"));
        this.address = jsonObject.getString("address");
        this.city = jsonObject.getString("city");
        this.phone = jsonObject.getString("phone");
        this.birthDay = jsonObject.getString("birthday");
        this.userType = jsonObject.getString("user_type");
    }


    //getters
    public String getUID() {
        return UID;
    }
    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Bitmap getPhoto() {
        return photo;
    }
    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }
    public String getPhone() {
        return phone;
    }
    public String getBirthDay() {
        return birthDay;
    }
    public String getUserType() {
        return userType;
    }


    //setters
    public void setUID(String UID) {
        this.UID = UID;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public JSONObject getJSONObject() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", UID);
        jsonObject.put("id", id);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("first_name", firstName);
        jsonObject.put("last_name", lastName);
        jsonObject.put("photo", HelpFunctions.BitMapToString(photo));
        jsonObject.put("address", address);
        jsonObject.put("city", city);
        jsonObject.put("phone", phone);
        jsonObject.put("birthday", birthDay);
        jsonObject.put("user_type", userType);
        return jsonObject;
    }

}
