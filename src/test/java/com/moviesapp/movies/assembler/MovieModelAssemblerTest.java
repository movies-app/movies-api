package com.moviesapp.movies.assembler;

import com.moviesapp.movies.entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovieModelAssemblerTest {

    @InjectMocks
    MovieModelAssembler assembler;

    @Test
    void toModel() throws Exception {
        Movie movie = mock(Movie.class);

        given(movie.getId()).willReturn(1L);
        given(movie.getTitle()).willReturn("Avengers: Infinity Wars");

        var model = this.assembler.toModel(movie);

        assertThat(model.getMovieId()).isEqualTo(1L);
        assertThat(model.getTitle()).isEqualTo("Avengers: Infinity Wars");
        assertThat(model.getLink("self").orElseThrow().getHref()).isEqualTo("/movies/1");
    }

    @Test
    void toCollectionModel() throws Exception {

        Movie m1 = mock(Movie.class);
        Movie m2 = mock(Movie.class);

        given(m1.getId()).willReturn(1L);
        given(m1.getTitle()).willReturn("Thor");
        given(m2.getId()).willReturn(2L);
        given(m2.getTitle()).willReturn("Spider Man");

        var models = this.assembler.toCollectionModel(List.of(m1, m2));

        assertThat(models.getContent()).hasSize(2);
        assertThat(models.getLink("self").orElseThrow().getHref()).isEqualTo("/movies");
    }

}
