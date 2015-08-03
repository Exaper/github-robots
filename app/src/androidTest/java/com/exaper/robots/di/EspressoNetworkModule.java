package com.exaper.robots.di;

import android.support.test.espresso.Espresso;
import android.text.TextUtils;

import com.exaper.robots.data.api.RobotsDataService;
import com.exaper.robots.data.model.RobotsResponse;
import com.exaper.robots.espresso.CountingIdlingResource;
import dagger.Module;
import dagger.Provides;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Custom espresso network module that allows setting customized backend base url.
 */
@Module
public class EspressoNetworkModule {
    private String mBaseUrl;

    @Provides
    public RobotsDataService provideDataService() {
        RobotsDataService dataService = new RestAdapter.Builder()
                .setEndpoint(getBaseUrl())
                .build()
                .create(RobotsDataService.class);

        return new EspressoRobotsDataService(dataService);
    }

    public String getBaseUrl() {
        return !TextUtils.isEmpty(mBaseUrl) ? mBaseUrl : NetworkModule.BASE_URL;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    private static final class EspressoRobotsDataService implements RobotsDataService {
        private final RobotsDataService mWrappedDataService;
        private static final CountingIdlingResource IDLING_RESOURCE;

        static {
            IDLING_RESOURCE = new CountingIdlingResource(RobotsDataService.class.getSimpleName());
            Espresso.registerIdlingResources(IDLING_RESOURCE);
        }

        EspressoRobotsDataService(RobotsDataService dataService) {
            mWrappedDataService = dataService;
        }

        @Override
        public void getRobots(Callback<RobotsResponse> callback) {
            IDLING_RESOURCE.increment();
            mWrappedDataService.getRobots(new CallbackWrapper<>(callback));
        }

        private final class CallbackWrapper<T> implements Callback<T> {
            private final Callback<T> mWrappedCallback;

            public CallbackWrapper(Callback<T> callback) {
                mWrappedCallback = callback;
            }

            @Override
            public void success(T result, Response response) {
                IDLING_RESOURCE.decrement();
                mWrappedCallback.success(result, response);
            }

            @Override
            public void failure(RetrofitError error) {
                IDLING_RESOURCE.decrement();
                mWrappedCallback.failure(error);
            }
        }
    }
}
