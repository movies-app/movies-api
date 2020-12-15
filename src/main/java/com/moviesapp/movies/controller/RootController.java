package com.moviesapp.movies.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;

@RestController
public class RootController {

    @RequestMapping(method = OPTIONS, path = "/")
    public String root() {
        return "root";
    }

}
