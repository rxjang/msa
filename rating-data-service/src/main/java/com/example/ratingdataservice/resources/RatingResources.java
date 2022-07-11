package com.example.ratingdataservice.resources;

import com.example.ratingdataservice.models.Rating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResources {

    @GetMapping("/{movieId}")
    public Rating getRatings(@PathVariable("movieId") String movieId) {

        return new Rating(movieId, 4);
    }
}
