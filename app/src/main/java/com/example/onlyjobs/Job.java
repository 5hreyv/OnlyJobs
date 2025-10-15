package com.example.onlyjobs;

// This class represents a single job posting.
public class Job {
    private String jobTitle;
    private String companyName;
    private String location;
    private String description;

    // IMPORTANT: A public no-argument constructor is required for Firebase Realtime Database
    public Job() {}

    // Getters are needed for Firebase to read the data
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
}