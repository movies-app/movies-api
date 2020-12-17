package com.moviesapp.movies.repository;

import com.moviesapp.movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findMovieByTitle(String title);

}
