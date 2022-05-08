package com.mbco.brainstormandroid.models;

import java.util.ArrayList;

public class StudentsCourseInfo {

    private CourseInfo courseInfo;

    private ArrayList<StudentInCourse> students;

    public StudentsCourseInfo(CourseInfo courseInfo, ArrayList<StudentInCourse> students) {
        this.courseInfo = courseInfo;
        this.students = students;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public ArrayList<StudentInCourse> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentInCourse> students) {
        this.students = students;
    }
}
