package com.exaper.robots.di;

import com.exaper.robots.data.api.RobotsDataService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

import javax.inject.Singleton;

@Module
public class NetworkModule {
    public static final String BASE_URL = "https://api.github.com";

    @Provides
    @Singleton
    RobotsDataService provideDataService() {
        return new RestAdapter.Builder().setEndpoint(BASE_URL).build().create(RobotsDataService.class);
    }
}
