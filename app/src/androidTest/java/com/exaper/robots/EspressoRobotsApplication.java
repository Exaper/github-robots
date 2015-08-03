package com.exaper.robots;

import com.exaper.robots.di.ApplicationComponent;
import com.exaper.robots.di.DaggerEspressoApplicationComponent;
import com.exaper.robots.di.EspressoNetworkModule;

public class EspressoRobotsApplication extends RobotsApplication {
    private final EspressoNetworkModule mNetworkModule = new EspressoNetworkModule();

    public EspressoNetworkModule getNetworkModule() {
        return mNetworkModule;
    }

    @Override
    protected ApplicationComponent onCreateComponent() {
        return DaggerEspressoApplicationComponent.builder()
                .espressoNetworkModule(mNetworkModule)
                .build();
    }
}
