package com.example.biplobi_cr;

public class Student {
    private String name;
    private String roll;
    private String email;
    private String status;

    public Student() {
    }

    public Student(String name, String roll, String email, String status) {
        this.name = name;
        this.roll = roll;
        this.email = email;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
