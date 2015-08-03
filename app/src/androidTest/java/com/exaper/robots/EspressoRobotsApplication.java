package com.exaper.robots;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.exaper.robots.di.ApplicationComponent;
import com.exaper.robots.di.DaggerEspressoApplicationComponent;
import com.exaper.robots.di.EspressoNetworkModule;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.IOException;

public class EspressoRobotsApplication extends RobotsApplication {
    private final EspressoNetworkModule mNetworkModule = new EspressoNetworkModule();
    private MockWebServer mMockWebServer;

    public static EspressoRobotsApplication get() {
        Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
        return (EspressoRobotsApplication) RobotsApplication.get(context);
    }

    @Override
    protected ApplicationComponent onCreateComponent() {
        return DaggerEspressoApplicationComponent.builder()
                .espressoNetworkModule(mNetworkModule)
                .build();
    }

    public MockWebServer getMockWebServer() {
        if (mMockWebServer == null) {
            mMockWebServer = new MockWebServer();
            try {
                mMockWebServer.start();
            } catch (IOException e) {
                throw new IllegalStateException("Could not start mock web server", e);
            }
            mNetworkModule.setBaseUrl(mMockWebServer.getUrl("").toString());
        }
        return mMockWebServer;
    }

    public void onFinishInstrumentation(){
        try {
            mMockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
