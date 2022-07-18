package com.example.moviecatalogservice.services;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MovieInfo {

    private final WebClient.Builder webClientBuilder;

    public MovieInfo(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
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
}
