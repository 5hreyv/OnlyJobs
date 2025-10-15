package com.example.onlyjobs;

public class Job {
    private String jobTitle;
    private String companyName;
    private String location;
    private String description;

    // IMPORTANT: A public no-argument constructor is required for Firestore
    public Job() {}

    // Getters for all fields
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
}