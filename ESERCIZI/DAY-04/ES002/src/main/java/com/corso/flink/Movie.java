package com.corso.flink;

public class Movie {

    public int movie;
    public String title;
    public String genres;
    public int year;
    public int rating;
    public int rottonTomato;

    public Movie() {
    }

    public Movie(
            int movie,
            String title,
            String genres,
            int year,
            int rating,
            int rottonTomato) {
        this.movie = movie;
        this.title = title;
        this.genres = genres;
        this.year = year;
        this.rating = rating;
        this.rottonTomato = rottonTomato;
    }

    @Override
    public String toString() {
        return movie + " | " + title;
    }

}
