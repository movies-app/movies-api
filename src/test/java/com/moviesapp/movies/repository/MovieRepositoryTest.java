package com.moviesapp.movies.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Test
    void findAllMoviesInCatalog() {
        var movies = this.movieRepository.findAll();
        assertThat(movies).hasSize(3);
    }

    @Test
    void findMovieByTitle() {
        var movie = this.movieRepository.findMovieByTitle("Ant-Man");
        assertThat(movie.orElseThrow().getTitle()).isEqualTo("Ant-Man");
    }
}
