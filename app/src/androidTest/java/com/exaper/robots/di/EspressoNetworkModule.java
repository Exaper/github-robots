package com.exaper.robots.di;

import android.text.TextUtils;

import com.exaper.robots.data.api.RobotsDataService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Custom espresso network module that allows setting customized backend base url.
 */
@Module
public class EspressoNetworkModule {
    private String mBaseUrl;

    @Provides
    public RobotsDataService provideDataService() {
        return new RestAdapter.Builder()
                .setEndpoint(getBaseUrl())
                .build().create(RobotsDataService.class);
    }

    public String getBaseUrl() {
        return !TextUtils.isEmpty(mBaseUrl) ? mBaseUrl : NetworkModule.BASE_URL;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

}
