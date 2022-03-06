package com.example.cse3311project;

public class Professors
{
    // For Windows, ALT + INSERT to do constructors, getters, setters, etc.
    public String name, department, rating;

    public Professors() {

    }

    public Professors(String name, String department, String rating) {
        this.name = name;
        this.department = department;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
