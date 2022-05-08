package com.mbco.brainstormandroid.models;

import android.graphics.Bitmap;

import com.mbco.brainstormandroid.HelpFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CourseInfo {

    private String UID;
    private String creatorUid;
    private String name;
    private String field;
    private String description;
    private Bitmap logo;
    private int approval;

    public CourseInfo(String UID, String creatorUid, String name, String field, String description, Bitmap logo, int approval) {
        this.UID = UID;
        this.creatorUid = creatorUid;
        this.name = name;
        this.field = field;
        this.description = description;
        this.logo = logo;
        this.approval = approval;
    }

    public CourseInfo() {
        this.UID = "";
        this.creatorUid = "";
        this.name = "";
        this.field = "";
        this.description = "";
        this.logo = null;
        this.approval = -1;
    }

    public CourseInfo(JSONObject jsonObject) throws JSONException{
        this.UID = jsonObject.getString("uid");
        this.creatorUid = jsonObject.getString("creator_uid");
        this.name = jsonObject.getString("name");
        this.field = jsonObject.getString("field");
        this.description = jsonObject.getString("description");
        this.logo = HelpFunctions.StringToBitMap(jsonObject.getString("logo"));
        this.approval = jsonObject.getInt("approval");
    }

    public String getUID() {
        return UID;
    }
    public String getCreatorUid() {
        return creatorUid;
    }
    public String getName() {
        return name;
    }
    public String getField() {
        return field;
    }
    public String getDescription() {
        return description;
    }
    public Bitmap getLogo() {
        return logo;
    }
    public int getApproval() {
        return approval;
    }


    public void setUID(String UID) {
        this.UID = UID;
    }
    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setField(String field) {
        this.field = field;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }
    public void setApproval(int approval) {
        this.approval = approval;
    }
    public void GenerateUid() throws NoSuchAlgorithmException {
        String originalString = this.creatorUid + this.name;
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
        this.UID = hexString.toString();
    }

    public JSONObject GetJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("uid", this.UID);
        object.put("creator_uid", this.creatorUid);
        object.put("name", this.name);
        object.put("field", this.field);
        object.put("description", this.description);
        object.put("logo", HelpFunctions.BitMapToString(this.logo));
        object.put("approval", this.approval);
        return object;
    }
}
