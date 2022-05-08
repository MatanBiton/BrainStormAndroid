package com.mbco.brainstormandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.CourseData;
import com.mbco.brainstormandroid.models.CourseInfo;
import com.mbco.brainstormandroid.models.User;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class HelpFunctions {

    public static User CurrentUser;

    public static CourseInfo CurrentCourseCreationInfo;

    public static Course CurrentCourse;

    public static CourseData CurrentCourseData;

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        //Log.d("test", temp);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String ByteArrToString(byte [] arr){
        return new String(arr, StandardCharsets.UTF_8);
    }

    public static byte[] StringToByteArr(String s){
        return s.getBytes(StandardCharsets.UTF_8);
    }


    public static boolean IsAdmin() {
        return CurrentUser.getEmail().equals("matanb1905@gmail.com");
    }
}
