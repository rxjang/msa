package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.UserRating;
import com.example.moviecatalogservice.services.MovieInfo;
import com.example.moviecatalogservice.services.UserRatingInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final MovieInfo movieInfo;

    private final UserRatingInfo userRatingInfo;

    public MovieCatalogResource(MovieInfo movieInfo, UserRatingInfo userRatingInfo) {
        this.movieInfo = movieInfo;
        this.userRatingInfo = userRatingInfo;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = userRatingInfo.getUserRatings(userId);
        // for each movie ID, call movie info service and get details
        return ratings.getUserRating().stream()
                .map(movieInfo::getCatalogItem)
                .collect(Collectors.toList());
    }

}
