package com.moviesapp.movies.assembler;

import com.moviesapp.movies.controller.MovieCatalogController;
import com.moviesapp.movies.entity.Movie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MovieModelAssembler extends RepresentationModelAssemblerSupport<Movie, MovieModel> {

    public MovieModelAssembler() {
        super(MovieCatalogController.class, MovieModel.class);
    }

    @Override
    public MovieModel toModel(Movie movie) {

        MovieModel model = new MovieModel();
        model.setMovieId(movie.getId());
        model.setTitle(movie.getTitle());
        model.setReleaseDate(movie.getReleaseDate());
        model.add(linkTo(methodOn(MovieCatalogController.class).getMovie(movie.getId())).withSelfRel());

        return model;
    }

    @Override
    public CollectionModel<MovieModel> toCollectionModel(Iterable<? extends Movie> entities) {
        var models = super.toCollectionModel(entities);
        models.add(linkTo(methodOn(MovieCatalogController.class).allMovies()).withSelfRel());
        return models;
    }
}
