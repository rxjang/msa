package com.example.moviecatalogservice.services;

import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {

    private final RestTemplate restTemplate;

    public UserRatingInfo(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
    commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500")
    })
    public UserRating getUserRatings(String userId) {
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
