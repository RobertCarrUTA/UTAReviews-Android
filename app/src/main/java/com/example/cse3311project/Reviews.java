package com.example.cse3311project;

public class Reviews
{
    // For Windows, ALT + INSERT to do constructors, getters, setters, etc.
    public String review, rating, date, username, classTaken;

    // This is the non-empty constructor
    public Reviews(String review, String rating, String date, String username, String classTaken)
    {
        this.review = review;
        this.rating = rating;
        this.date = date;
        this.username = username;
        this.classTaken = classTaken;
    }

    // We must have an empty constructor
    public Reviews()
    {

    }

    public String getReview()
    {
        return review;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public String getRating()
    {
        return rating;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getClassTaken()
    {
        return classTaken;
    }

    public void setClassTaken(String classTaken)
    {
        this.classTaken = classTaken;
    }
}
