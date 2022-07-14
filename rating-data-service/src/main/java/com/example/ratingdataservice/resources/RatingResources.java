package com.example.ratingdataservice.resources;

import com.example.ratingdataservice.models.Rating;
import com.example.ratingdataservice.models.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResources {

    @GetMapping("/{movieId}")
    public Rating getRatings(@PathVariable("movieId") String movieId) {

        return new Rating(movieId, 4);
    }

    @GetMapping("/users/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId) {

        List<Rating> ratings = Arrays.asList(
                new Rating("100", 4),
                new Rating("200", 3)
        );

        UserRating userRating =  new UserRating();
        userRating.setUserRating(ratings);
        return userRating;
    }
}
