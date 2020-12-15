package com.moviesapp.movies.controller;

import com.moviesapp.movies.assembler.MovieModel;
import com.moviesapp.movies.assembler.MovieModelAssembler;
import com.moviesapp.movies.service.MovieCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieCatalogController {

    private final static Logger LOG = LoggerFactory.getLogger(MovieCatalogController.class);

    private final MovieCatalogService movieCatalogService;
    private final MovieModelAssembler assembler;

    @Autowired
    public MovieCatalogController(MovieCatalogService movieCatalogService, MovieModelAssembler assembler) {
        this.movieCatalogService = movieCatalogService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MovieModel>> allMovies() {
        LOG.debug("Retrieving all movies from Catalog");
        var movies = this.movieCatalogService.getAllMovieCatalog();
        var resource = this.assembler.toCollectionModel(movies);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<MovieModel> getMovie(@PathVariable("id") Long movieId) {
        LOG.debug("Retrieving movie id {}", movieId);
        var movie = this.movieCatalogService.getMovie(movieId);
        var resource = this.assembler.toModel(movie);
        return ResponseEntity.ok(resource);
    }
}
