package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;

    public MovieCatalogResource(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = getUserRatings(userId);

        // for each movie ID, call movie info service and get details
        return ratings.getUserRating().stream()
                .map(this::getCatalogItem)
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = webClientBuilder.build()
                .get()
                .uri("http://movie-info-service/movies/" + rating.getMovieId())
                .retrieve()
                .bodyToMono(Movie.class)
                .block();
//             put them all together
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Moview name not found", "", rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")

    private UserRating getUserRatings(String userId) {
        return restTemplate.getForObject("http://rating-data-service/ratingsdata/users/" + userId, UserRating.class);
    }

    private UserRating getFallbackUserRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserRating(Arrays.asList(
                new Rating("0", 0)
        ));
        return userRating;
    }

}
