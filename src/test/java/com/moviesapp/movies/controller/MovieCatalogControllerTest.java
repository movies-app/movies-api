package com.moviesapp.movies.controller;

import com.moviesapp.movies.assembler.MovieModel;
import com.moviesapp.movies.assembler.MovieModelAssembler;
import com.moviesapp.movies.entity.Movie;
import com.moviesapp.movies.service.MovieCatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = MovieCatalogController.class)
public class MovieCatalogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MovieCatalogService movieCatalogService;

    @MockBean
    MovieModelAssembler assembler;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    void getMovie_whenMovieIdIsProvided_thenReturnMovie() throws Exception{

        Movie movie = mock(Movie.class);
        MovieModel movieModel = new MovieModel();
        movieModel.setId(1L);
        movieModel.setTitle("Avengers: Infinity Wars");
        movieModel.add(Link.of("http://localhost:8080/movies/1").withSelfRel());
        ArgumentCaptor<Movie> movieArg = ArgumentCaptor.forClass(Movie.class);

//        var model = HalModelBuilder.halModel(order)
//                .preview(new CustomerSummary(customer))
//                .forLink(customerLink)
//                .embed(additional)
//                .link(Link.of(…, IanaLinkRelations.SELF));
//        .build();

        given(movie.getId()).willReturn(1L);
        given(movie.getTitle()).willReturn("Avengers: Infinity Wars");
        given(this.movieCatalogService.getMovie(anyLong())).willReturn(movie);
        given(this.assembler.toModel(any(Movie.class))).willReturn(movieModel);

        this.mockMvc.perform(
                get("/movies/1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/movies/1"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath(".title").value("Avengers: Infinity Wars"))
                .andDo(document("{class-name}/get-movie-by-id"));

        verify(this.movieCatalogService, times(1)).getMovie(1L);
        verify(this.assembler, times(1)).toModel(movieArg.capture());

        assertThat(movieArg.getValue()).isSameAs(movie);
    }

    @Test
    void allMoviesInCatalog() throws Exception{

        Movie movie = mock(Movie.class);
        ArgumentCaptor<List<Movie>> moviesArg = ArgumentCaptor.forClass(List.class);

        MovieModel avengers = new MovieModel();
        MovieModel ironman = new MovieModel();
        MovieModel blackPanther = new MovieModel();

        avengers.setId(1L);
        avengers.setTitle("Avengers: Infinity Wars");
        avengers.add(Link.of("http://localhost:8080/movies/1").withSelfRel());

        ironman.setId(2L);
        ironman.setTitle("Ironman");
        ironman.add(Link.of("http://localhost:8080/movies/2").withSelfRel());

        blackPanther.setId(3L);
        blackPanther.setTitle("Black Panther");
        blackPanther.add(Link.of("http://localhost:8080/movies/3").withSelfRel());

        CollectionModel<MovieModel> movieCollection = CollectionModel.of(List.of(avengers, ironman, blackPanther));
        movieCollection.add(Link.of("http://localhost:8080/movies").withSelfRel());

        given(movie.getId()).willReturn(1L);
        given(movie.getTitle()).willReturn("Avengers: Infinity Wars");
        given(this.movieCatalogService.getAllMovieCatalog()).willReturn(List.of(movie, movie, movie));
        given(this.assembler.toCollectionModel(anyList())).willReturn(movieCollection);

        this.mockMvc.perform(
                get("/movies")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost:8080/movies"))
                .andExpect(jsonPath("$._embedded").exists())
                .andDo(document("{class-name}/get-all-movie-in-catalog"));

        verify(this.movieCatalogService, times(1)).getAllMovieCatalog();
        verify(this.assembler, times(1)).toCollectionModel(moviesArg.capture());

        assertThat(moviesArg.getValue()).isEqualTo(List.of(movie, movie, movie));
    }
}
