package com.exaper.robots;

import android.app.Application;
import android.content.Context;

import com.exaper.robots.di.ApplicationComponent;
import com.exaper.robots.di.DaggerApplicationComponent;
import com.exaper.robots.di.NetworkModule;

public class RobotsApplication extends Application {
    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = onCreateComponent();
    }

    protected ApplicationComponent onCreateComponent() {
        return DaggerApplicationComponent
                .builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public static RobotsApplication get(Context context) {
        return (RobotsApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
