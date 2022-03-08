package com.example.cse3311project;

public class Reviews
{
    // For Windows, ALT + INSERT to do constructors, getters, setters, etc.
    public String review, rating, professor;

    public Reviews(String review, String rating, String professor)
    {
        this.review = review;
        this.rating = rating;
        this.professor = professor;
    }

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

    public String getProfessor()
    {
        return professor;
    }

    public void setProfessor(String professor)
    {
        this.professor = professor;
    }
}
