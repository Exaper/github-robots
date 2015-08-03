package com.exaper.robots.di;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = EspressoNetworkModule.class)
public interface EspressoApplicationComponent extends ApplicationComponent {
}
