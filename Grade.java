package com.example.studentssystem;

public class Grade {
    private int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double grade;

    @Override
    public String toString() {
        return "Grade{" +
                "grade=" + grade +
                '}';
    }

    public Grade(int id,double grade) {
        this.id=id;
        this.grade = grade;
    }

    public Grade(Grade grade){
        this.id= grade.getId();
        this.grade=grade.getGrade();
    }

    public Grade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
