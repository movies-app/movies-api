package com.moviesapp.movies.assembler;

import com.moviesapp.movies.entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

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

        assertThat(model.getId()).isEqualTo(1L);
        assertThat(model.getTitle()).isEqualTo("Avengers: Infinity Wars");
        assertThat(model.getLink("self").orElseThrow().getHref()).isEqualTo("/movies/1");
    }

}
