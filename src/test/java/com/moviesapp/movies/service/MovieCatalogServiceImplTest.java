package com.moviesapp.movies.service;

import com.moviesapp.movies.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import com.moviesapp.movies.entity.Movie;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieCatalogServiceImplTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieCatalogServiceImpl movieCatalogService;

    @Test
    void getAllMovieCatalog() throws Exception {

        given(this.movieRepository.findAll()).willReturn(List.of(
                mock(Movie.class),
                mock(Movie.class)
        ));

        var actual = this.movieCatalogService.getAllMovieCatalog();

        verify(this.movieRepository, times(1)).findAll();

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(2);

    }

    @Test
    void movieCanBeRenamed() throws Exception {
        Movie originalMovie = mock(Movie.class);
        Movie movieWithNewName = mock(Movie.class);

        given(originalMovie.getId()).willReturn(1L);
        given(originalMovie.getTitle()).willReturn("Star Wars");
        given(movieWithNewName.getId()).willReturn(1L);
        given(movieWithNewName.getTitle()).willReturn("Star Trek");
        given(this.movieRepository.findById(anyLong())).willReturn(Optional.of(originalMovie));
        given(this.movieRepository.save(any(Movie.class))).willReturn(movieWithNewName);

        Movie renamedMovie = this.movieCatalogService.renameMovie(1L, "Star Trek");

        verify(this.movieRepository, times(1)).findById(1L);
        verify(this.movieRepository, times(1)).save(any(Movie.class));

        assertThat(renamedMovie.getId()).isEqualTo(originalMovie.getId());
        assertThat(renamedMovie.getTitle()).isNotEqualTo(originalMovie.getTitle());
    }

    @Test
    void whenTitleIsTheSameThanOriginalMovie_thenReturnOriginalMovie() throws Exception {
        Movie originalMovie = mock(Movie.class);

        given(originalMovie.getTitle()).willReturn("Star Wars");
        given(this.movieRepository.findById(anyLong())).willReturn(Optional.of(originalMovie));

        Movie renamedMovie = this.movieCatalogService.renameMovie(1L, "Star Wars");

        verify(this.movieRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(this.movieRepository);

        assertThat(renamedMovie).isSameAs(originalMovie);
    }

    @Test
    void originalMovieNotFound_thenNoRenameAction() throws Exception {

        given(this.movieRepository.findById(anyLong())).willReturn(Optional.empty());

        var ex = Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.movieCatalogService.renameMovie(1L, "Star Wars");
        });

        verify(this.movieRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(this.movieRepository);
    }

    @Test
    void storeNewMovie() throws Exception {

        Movie movie = mock(Movie.class);
        Movie newMovie = mock(Movie.class);
        ArgumentCaptor<Movie> movieArg = ArgumentCaptor.forClass(Movie.class);

        given(movie.getTitle()).willReturn("Guardians of the Galaxy");
        given(this.movieRepository.findMovieByTitle(anyString())).willReturn(Optional.empty());
        given(this.movieRepository.save(any(Movie.class))).willReturn(newMovie);

        Movie actual = this.movieCatalogService.newMovie(movie);

        verify(movie, times(1)).getTitle();
        verify(this.movieRepository, times(1)).findMovieByTitle(movie.getTitle());
        verify(this.movieRepository, times(1)).save(movieArg.capture());

        assertThat(movieArg.getValue()).isSameAs(movie);
        assertThat(actual).isEqualTo(newMovie);
    }

    @Test
    void storeNewMovie_whenTitleAlreadyExists_thenDontStore() throws Exception {
        String title = "Guardians of the Galaxy";
        Movie movie = mock(Movie.class);
        ArgumentCaptor<String> titleArg = ArgumentCaptor.forClass(String.class);

        given(movie.getTitle()).willReturn(title);
        given(this.movieRepository.findMovieByTitle(anyString())).willReturn(Optional.of(movie));

        var ex = Assertions.assertThrows(Exception.class, () -> {
            this.movieCatalogService.newMovie(movie);
        });

        verify(movie, times(2)).getTitle();
        verify(this.movieRepository, times(1)).findMovieByTitle(titleArg.capture());
        verifyNoMoreInteractions(this.movieRepository);

        assertThat(titleArg.getValue()).isSameAs(title);
        assertThat(ex.getMessage()).isEqualTo("Movie with title: "+ title +" already exists");
    }

}
