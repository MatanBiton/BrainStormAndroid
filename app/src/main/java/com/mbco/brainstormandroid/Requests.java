package com.mbco.brainstormandroid;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mbco.brainstormandroid.models.Course;
import com.mbco.brainstormandroid.models.CourseAddOn;
import com.mbco.brainstormandroid.models.CourseData;
import com.mbco.brainstormandroid.models.CourseInfo;
import com.mbco.brainstormandroid.models.CourseReview;
import com.mbco.brainstormandroid.models.Page;
import com.mbco.brainstormandroid.models.StudentAdvancing;
import com.mbco.brainstormandroid.models.StudentInCourse;
import com.mbco.brainstormandroid.models.StudentsCourseInfo;
import com.mbco.brainstormandroid.models.Teacher;
import com.mbco.brainstormandroid.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Requests {

    //base url for the api
    private final static String BASE_URL = "http://192.168.1.202:5000/";
    private static Requests instance;
    private static RequestQueue queue;

    public Requests(Context context){
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }


    public static synchronized Requests getInstance(Context context){
        if (null == instance)
            instance = new Requests(context);
        return instance;
    }

    public static synchronized Requests getInstance(){
        if (null == instance)
        {
            throw new IllegalStateException(Requests.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public static void GetTeacherCourses(String uid, RequestsResultListener<ArrayList<Course>> listener) throws JSONException {
        String url = BASE_URL + "get-teacher-courses";
        JSONObject body = new JSONObject();
        body.put("uid", uid);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray infos = response.getJSONArray("courses_info");
                    JSONArray pages = response.getJSONArray("pages");
                    ArrayList<Course> courses = new ArrayList<>();
                    for (int i = 0;i<infos.length();i++){
                        JSONArray currentPagesJson = pages.getJSONArray(i);
                        ArrayList<Page> currantPages = new ArrayList<>();
                        for (int k = 0;k<currentPagesJson.length();k++){
                            currantPages.add(new Page(currentPagesJson.getJSONObject(k)));
                        }
                        courses.add(new Course(new CourseInfo(infos.getJSONObject(i)), currantPages));
                    }
                    listener.getResult(courses);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetAllCourses(RequestsResultListener<ArrayList<Course>> listener) throws JSONException{
        String url = BASE_URL + "get-all-courses";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonInfos = response.getJSONArray("courses_info");
                    JSONArray jsonPages = response.getJSONArray("pages");
                    ArrayList<Course> courses = new ArrayList<>();
                    for (int i = 0;i<jsonInfos.length();i++){
                        JSONArray currentPagesJson = jsonPages.getJSONArray(i);
                        ArrayList<Page> currantPages = new ArrayList<>();
                        for (int k = 0;k<currentPagesJson.length();k++){
                            currantPages.add(new Page(currentPagesJson.getJSONObject(k)));
                        }
                        courses.add(new Course(new CourseInfo(jsonInfos.getJSONObject(i)), currantPages));
                    }
                    listener.getResult(courses);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    //need testing
    public static void GetCoursesByUid(String uid, RequestsResultListener<ArrayList<Course>> listener) throws JSONException{
        String url = BASE_URL + "get-courses-by-uid";
        JSONObject body = new JSONObject();
        body.put("uid", uid);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonInfos = response.getJSONArray("courses_info");
                    JSONArray jsonPages = response.getJSONArray("pages");
                    ArrayList<Course> courses = new ArrayList<>();
                    for (int i = 0;i<jsonInfos.length();i++){
                        JSONArray currentPagesJson = jsonPages.getJSONArray(i);
                        ArrayList<Page> currantPages = new ArrayList<>();
                        for (int k = 0;k<currentPagesJson.length();k++){
                            currantPages.add(new Page(currentPagesJson.getJSONObject(k)));
                        }
                        courses.add(new Course(new CourseInfo(jsonInfos.getJSONObject(i)), currantPages));
                    }
                    listener.getResult(courses);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void JoinCourses(String uid, ArrayList<String> selectedCoursesUid, RequestsResultListener<Boolean> listener)  throws JSONException{
        String url = BASE_URL + "join-courses";
        JSONObject body = new JSONObject();
        body.put("uid", uid);
        body.put("courses_uid", selectedCoursesUid);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    if (msg.equals("success")){
                        listener.getResult(true);
                    } else{
                        listener.getResult(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetCourseData(String courseUid, String studentUid, RequestsResultListener<CourseData> listener) throws JSONException {
        String url = BASE_URL + "get-course-data";
        JSONObject body = new JSONObject();
        body.put("course_uid", courseUid);
        body.put("student_uid", studentUid);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray advancings = response.getJSONArray("advancings");
                    JSONArray addOns = response.getJSONArray("addOns");
                    JSONArray files = response.getJSONArray("files");
                    ArrayList<StudentAdvancing> studentAdvancings = new ArrayList<>();
                    ArrayList<CourseAddOn> courseAddOns = new ArrayList<>();
                    for (int i = 0; i < advancings.length(); i++) {
                        studentAdvancings.add(new StudentAdvancing(advancings.getJSONObject(i)));
                    }
                    for (int i = 0; i < addOns.length() ; i++) {
                        for (int j = 0; j < files.length(); j++) {
                            if (files.getJSONObject(j).getString("file_uid").equals(addOns.getJSONObject(i).getString("file_uid"))){
                                courseAddOns.add(new CourseAddOn(addOns.getJSONObject(i),
                                        HelpFunctions.StringToByteArr(files.getJSONObject(j).getString("file"))));
                                break;
                            }
                        }

                    }
                    listener.getResult(new CourseData(studentAdvancings, courseAddOns));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void LeaveReview(String courseUid, String studentUid, int rating, String comment,
                                   RequestsResultListener<Boolean> listener) throws JSONException {
        String url = BASE_URL + "leave-review";
        JSONObject body = new JSONObject();
        body.put("course_uid", courseUid);
        body.put("student_uid", studentUid);
        body.put("rating", rating);
        body.put("comment", comment);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("msg").equals("success")){
                        listener.getResult(true);
                    } else {
                        listener.getResult(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetAvailableCourses(String uid, RequestsResultListener<ArrayList<Course>> listener){
        String url = BASE_URL + "get-available-courses/" + uid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonInfos = response.getJSONArray("courses_info");
                    JSONArray jsonPages = response.getJSONArray("pages");
                    ArrayList<Course> courses = new ArrayList<>();
                    for (int i = 0;i<jsonInfos.length();i++){
                        JSONArray currentPagesJson = jsonPages.getJSONArray(i);
                        ArrayList<Page> currantPages = new ArrayList<>();
                        for (int k = 0;k<currentPagesJson.length();k++){
                            currantPages.add(new Page(currentPagesJson.getJSONObject(k)));
                        }
                        courses.add(new Course(new CourseInfo(jsonInfos.getJSONObject(i)), currantPages));
                    }
                    listener.getResult(courses);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void SaveCourseData(RequestsResultListener<Boolean> listener) throws JSONException {
        String url = BASE_URL + "save-course-data";
        JSONObject body = new JSONObject();
        ArrayList<CourseAddOn> addOns = HelpFunctions.CurrentCourseData.getAddOns();
        ArrayList<StudentAdvancing> advancings = HelpFunctions.CurrentCourseData.getStudentAdvancings();
        JSONArray addOnsJson = new JSONArray();
        JSONArray advancingsJson = new JSONArray();
        JSONArray files = new JSONArray();
        for (CourseAddOn object: addOns) {
            addOnsJson.put(object.getJson());
            JSONObject temp = new JSONObject();
            temp.put("file_uid", object.getFileUid());
            temp.put("file", HelpFunctions.ByteArrToString(object.getFile()));
            files.put(temp);
        }
        for (StudentAdvancing object: advancings){
            advancingsJson.put(object.GetJson());
        }
        body.put("addOns", addOnsJson);
        body.put("advancings", advancingsJson);
        body.put("files", files);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.getResult(response.getString("msg").equals("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetReviews(String uid, RequestsResultListener<ArrayList<ArrayList<Object>>> listener) {
        String url = BASE_URL + "get-reviews/" + uid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray infoJson = response.getJSONArray("info");
                    JSONArray reviewJson = response.getJSONArray("review");
                    ArrayList<Object> info = new ArrayList<>();
                    ArrayList<Object> review = new ArrayList<>();
                    for (int i = 0; i < infoJson.length(); i++) {
                        info.add(new CourseInfo(infoJson.getJSONObject(i)));
                        review.add(new CourseReview(reviewJson.getJSONObject(i)));
                    }
                    ArrayList<ArrayList<Object>> data = new ArrayList<>();
                    data.add(info);
                    data.add(review);
                    listener.getResult(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void LoadStudentInCourses(String uid, RequestsResultListener<ArrayList<StudentsCourseInfo>> listener){
        String url = BASE_URL + "get-student-in-courses/" + uid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray infoJson = response.getJSONArray("info");
                    JSONArray studentJson = response.getJSONArray("students");
                    ArrayList<CourseInfo> infos = new ArrayList<>();
                    ArrayList<StudentInCourse> students = new ArrayList<>();
                    for (int i = 0; i < infoJson.length(); i++){
                        infos.add(new CourseInfo(infoJson.getJSONObject(i)));
                    }
                    for (int i = 0; i < studentJson.length(); i++){
                        students.add(new StudentInCourse(studentJson.getJSONObject(i)));
                    }
                    ArrayList<StudentsCourseInfo> courseInfo = new ArrayList<>();
                    for (CourseInfo info: infos){
                        ArrayList<StudentInCourse> temp = new ArrayList<>();
                        for (StudentInCourse student: students){
                            if (student.getUid().equals(info.getUID())){
                                temp.add(student);
                            }
                        }
                        courseInfo.add(new StudentsCourseInfo(info, temp));
                    }
                    listener.getResult(courseInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public static void GetStudent(String uid, RequestsResultListener<User> listener) {
        String url = BASE_URL + "get-student/" + uid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.getResult(new User(response.getJSONObject("user")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetStudents(String uid, RequestsResultListener<ArrayList<User>> listener) {
        String url = BASE_URL + "get-students/" + uid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray usersJson = response.getJSONArray("users");

                    ArrayList<User> users = new ArrayList<>();
                    for (int i = 0; i < usersJson.length(); i++) {
                        users.add(new User(usersJson.getJSONObject(i)));
                    }
                    listener.getResult(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void UpdateGrade(StudentInCourse studentInCourse, double grade) throws JSONException{
        String url = BASE_URL + "update-grade";
        JSONObject body = new JSONObject();
        body.put("course_uid", studentInCourse.getUid());
        body.put("student_uid", studentInCourse.getStudentUid());
        body.put("grade", grade);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,null,null);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void UpdateUser(User newUser, RequestsResultListener<Boolean> listener) throws JSONException{
        String url = BASE_URL + "update-user";
        JSONObject body = new JSONObject();
        body.put("user", newUser.getJSONObject());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.getResult(response.getString("msg").equals("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getResult(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetReviewsTeacher(String courseUid, RequestsResultListener<ArrayList<CourseReview>> listener) {
        String url = BASE_URL + "get-reviews-teacher/" + courseUid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray reviewsJson = response.getJSONArray("reviews");
                    JSONArray usersJson = response.getJSONArray("users");
                    ArrayList<CourseReview> reviews = new ArrayList<>();
                    for (int i = 0; i < reviewsJson.length(); i++) {
                        CourseReview review = new CourseReview(reviewsJson.getJSONObject(i));
                        User user = new User(usersJson.getJSONObject(i));
                        review.setStudent(user);
                        reviews.add(review);
                    }
                    listener.getResult(reviews);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    //this function checks if the device can get data to and from the api
    public void TestConnection(RequestsResultListener<Boolean> listener){
        String url = BASE_URL + "test";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {listener.getResult(true);}, error -> {listener.getResult(false); });
        queue.add(request);
    }

    public void Login(String email, String password, RequestsResultListener<User> listener) throws JSONException {
        String url = BASE_URL + "login";
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("message").equals("User not found")){
                        listener.getResult(null);
                    } else{
                        listener.getResult(response.get("user_type").equals("teacher") ?
                                new Teacher(response) : new User(response));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void Register(User user, RequestsResultListener<User> listener) throws JSONException {
        String url = BASE_URL + "register";
        JSONObject body = user.getJSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("message").equals("Registration Successful!")){
                        listener.getResult(response.get("user_type").equals("teacher") ? new Teacher(response) : new User(response));
                    } else{
                        listener.getResult(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetUsersByFilter(String filter, String filterValue, String type, RequestsResultListener<ArrayList<User>> listener) throws JSONException{
        String url = BASE_URL + "get-users";

        JSONObject body = new JSONObject();
        body.put("filter", filter);
        body.put("filterValue", filterValue);
        body.put("type", type);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<User> users = new ArrayList<>();
                try {
                    JSONArray arr = response.getJSONArray("users");
                    for (int i = 0; i < arr.length(); i++) {
                        users.add(arr.getJSONObject(i).getString("user_type").equals("teacher") ?
                                new Teacher(arr.getJSONObject(i)): new User(arr.getJSONObject(i)));
                    }
                    listener.getResult(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void GetRequests(String type, RequestsResultListener<ArrayList<Object>> listener) throws JSONException{
        String url = BASE_URL + "requests";
        JSONObject body = new JSONObject();
        body.put("type", type);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Object> data = new ArrayList<>();
                if (type.equals("teacher")) {
                    try {
                        JSONArray arr = response.getJSONArray("requests");
                        for (int i = 0; i < arr.length(); i++) {
                            data.add(new Teacher(arr.getJSONObject(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.getResult(data);
                } else{
                    try {
                        JSONArray jsonInfos = response.getJSONArray("courses_info");
                        JSONArray jsonPages = response.getJSONArray("pages");
                        ArrayList<Object> courses = new ArrayList<>();
                        for (int i = 0;i<jsonInfos.length();i++){
                            JSONArray currentPagesJson = jsonPages.getJSONArray(i);
                            ArrayList<Page> currantPages = new ArrayList<>();
                            for (int k = 0;k<currentPagesJson.length();k++){
                                currantPages.add(new Page(currentPagesJson.getJSONObject(k)));
                            }
                            courses.add(new Course(new CourseInfo(jsonInfos.getJSONObject(i)), currantPages));
                        }
                        listener.getResult(courses);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.getResult(null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(null);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void DeleteRequest(String uid, boolean b, RequestsResultListener<Boolean> listener) throws JSONException{
        String url = BASE_URL + "delete-request";
        JSONObject body = new JSONObject();
        body.put("uid", uid);
        body.put("result", b);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    listener.getResult(msg.equals("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public static void CreateCourse(CourseInfo courseInfo, ArrayList<Page> pages,
                                    RequestsResultListener<Boolean> listener) throws JSONException{
        String url = BASE_URL + "create-course";
        JSONObject body = new JSONObject();
        body.put("course_info", courseInfo.GetJson());
        JSONArray pagesJson = new JSONArray();
        for (Page page:pages){
            pagesJson.put(page.GetJson());
        }
        body.put("pages", pagesJson);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("msg").equals("success")){
                                listener.getResult(true);
                            } else{
                                listener.getResult(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getResult(false);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

}
