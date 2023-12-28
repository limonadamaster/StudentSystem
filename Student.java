package com.example.studentssystem.student;


import com.example.studentssystem.Grade;
import com.example.studentssystem.Subject;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class Student {
    private int id ;
    private String firstName;
    private String secondName;
    private String thirdName;
    //FK number
    private String collegeNumber;
    //True for Male, False for Female
    private double averageGrade;

    private byte[] imageData;

    private Map<Subject, Grade> subjectGradeMap;

    public Map<Subject, Grade> getSubjectGradeMap() {
        return subjectGradeMap;
    }

    public void setSubjectGradeMap(Map<Subject, Grade> subjectGradeMap) {
        this.subjectGradeMap = subjectGradeMap;
    }

    public Image getImageData() {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        Image image = new Image(bis);
        return image;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    private List<Subject> studentGradeList;

    public Student(int id,String firstName, String secondName, String thirdName, String collegeNumber,byte[] imageData) {
        this.id=id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.collegeNumber = collegeNumber;
        this.imageData = imageData;
    }

    public Student(Student student){
        this.id=student.id;
        this.firstName = student.getFirstName();
        this.secondName = student.getSecondName();
        this.thirdName = student.getThirdName();
        this.collegeNumber = student.getCollegeNumber();
        this.imageData = student.imageData;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", thirdName='" + thirdName + '\'' +
                ", collegeNumber='" + collegeNumber + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getCollegeNumber() {
        return collegeNumber;
    }

    public void setCollegeNumber(String collegeNumber) {
        this.collegeNumber = collegeNumber;
    }
    }

