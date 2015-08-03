package com.exaper.robots.data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginToRobotImageConverter {
    private static final String ROBOHASH_BASE_URL = "http://robohash.org";

    @Inject
    public LoginToRobotImageConverter() {

    }

    public String convert(String login) {
        return ROBOHASH_BASE_URL + "/" + login + ".png?size=500x500";
    }
}
