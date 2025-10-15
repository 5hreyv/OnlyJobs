package com.example.onlyjobs;
// You can add more fields as needed
public class User {
    public String uid;
    public String name;
    public String email;
    public String university;
    public String branch;

    // A public no-argument constructor is required
    public User() {}

    public User(String uid, String name, String email, String university, String branch) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.university = university;
        this.branch = branch;
    }
}