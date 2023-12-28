package com.example.studentssystem;

public class Subject {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subject(int id,String name){
        this.id=id;
        this.nameOfSubject=name;
    }
    private String nameOfSubject;

    @Override
    public String toString() {
        return nameOfSubject;
    }

    public Subject(String nameOfSubject, Double gradeOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }

    public Subject(Subject subject) {
        this.nameOfSubject = subject.getNameOfSubject();
    }

    public Subject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }

    public String getNameOfSubject() {
        return nameOfSubject;
    }

    public void setNameOfSubject(String nameOfSubject) {
        this.nameOfSubject = nameOfSubject;
    }
}
