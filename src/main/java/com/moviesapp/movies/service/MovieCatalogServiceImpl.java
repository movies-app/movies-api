package com.moviesapp.movies.service;

import com.moviesapp.movies.entity.Movie;
import com.moviesapp.movies.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MovieCatalogServiceImpl implements MovieCatalogService {

    private static Logger LOG = LoggerFactory.getLogger(MovieCatalogServiceImpl.class);

    private final MovieRepository movieRepository;

    @Autowired
    public MovieCatalogServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getAllMovieCatalog() {
        return this.movieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getMovie(Long id) {
        return null;
    }

    @Override
    @Transactional
    public Movie renameMovie(Long movieId, String newTitle) {
        return this.movieRepository.findById(movieId)
                .filter(m -> Objects.nonNull(m.getTitle()))
                .filter(m -> !m.getTitle().isEmpty())
                .map(originalMovie -> {
                    if(!originalMovie.getTitle().equals(newTitle)) {
                        originalMovie.setTitle(newTitle);
                        return this.movieRepository.save(originalMovie);
                    } else {
                        LOG.debug("MovieId {} has same title than the new title", movieId);
                        return originalMovie;
                    }
                })
                .orElseThrow();
    }


}
