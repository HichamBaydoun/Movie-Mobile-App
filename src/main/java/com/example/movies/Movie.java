package com.example.movies;

import java.util.ArrayList;

public class Movie {

    private int id;
    private String name, year , genre, rating;
    static ArrayList<Movie> movies = new ArrayList<>();

    public Movie(int id, String name, String year, String genre, String rating) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Year: " + year + "\n" +
                "Genre: " + genre + "\n" +
                "Rating: " + rating + "/10";
    }

}
