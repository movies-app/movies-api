package com.moviesapp.movies.service;

import com.moviesapp.movies.entity.Movie;

import java.util.List;

public interface MovieCatalogService {

    List<Movie> getAllMovieCatalog();

    Movie getMovie(Long id);

    Movie getMovieByTitle(String title);

    Movie renameMovie(Long movieId, String newTitle);

    Movie newMovie(Movie movie);

}
