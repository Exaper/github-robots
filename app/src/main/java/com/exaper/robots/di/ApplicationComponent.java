package com.exaper.robots.di;

import com.exaper.robots.RobotDetailsActivity;
import com.exaper.robots.RobotsCatalogActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {NetworkModule.class})
public interface ApplicationComponent {
    void inject(RobotsCatalogActivity activity);

    void inject(RobotDetailsActivity activity);
}
