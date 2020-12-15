package com.moviesapp.movies.assembler;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class MovieModel extends RepresentationModel<MovieModel> {

    private Long id;
    private String title;
    private Date releaseDate;

    public MovieModel() {

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
