package com.mbco.brainstormandroid.models;

import java.util.ArrayList;

public class CourseData {
    private ArrayList<StudentAdvancing> studentAdvancings;

    private ArrayList<CourseAddOn> addOns;

    public CourseData(ArrayList<StudentAdvancing> studentAdvancings, ArrayList<CourseAddOn> addOns) {
        this.studentAdvancings = studentAdvancings;
        this.addOns = addOns;
    }

    public CourseData() {
        this.studentAdvancings = new ArrayList<>();
        this.addOns = new ArrayList<>();
    }

    public ArrayList<StudentAdvancing> getStudentAdvancings() {
        return studentAdvancings;
    }

    public void setStudentAdvancings(ArrayList<StudentAdvancing> studentAdvancings) {
        this.studentAdvancings = studentAdvancings;
    }

    public ArrayList<CourseAddOn> getAddOns() {
        return addOns;
    }

    public void setAddOns(ArrayList<CourseAddOn> addOns) {
        this.addOns = addOns;
    }

    public void AddAddOn(CourseAddOn addOn){addOns.add(addOn);}

    public void AddAdvancing(StudentAdvancing advancing){
        studentAdvancings.add(advancing);}
}
