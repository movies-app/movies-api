package com.moviesapp.movies.assembler;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

@Relation(collectionRelation = "movies", itemRelation = "movie")
public class MovieModel extends RepresentationModel<MovieModel> {

    private Long movieId;
    private String title;
    private Date releaseDate;

    public MovieModel() {

    }
    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
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
