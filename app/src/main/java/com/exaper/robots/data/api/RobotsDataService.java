package com.exaper.robots.data.api;

import com.exaper.robots.data.model.RobotsResponse;
import retrofit.Callback;
import retrofit.http.GET;

public interface RobotsDataService {
    //https://api.github.com/search/users?q=repos:%3E1&page=1&per_page=100
    @GET("/search/users?q=repos:%3E1&page=1&per_page=100")
    void getRobots(Callback<RobotsResponse> callback);
}