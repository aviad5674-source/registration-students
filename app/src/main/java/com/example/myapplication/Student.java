package com.example.myapplication;
public class Student {
    String firstName;
    String lastName;
    String email;
    String studentClass;
    String major;
    boolean notifications;

    public Student(String firstName, String lastName, String email,
                   String studentClass, String major, boolean notifications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentClass = studentClass;
        this.major = major;
        this.notifications = notifications;
    }
}
