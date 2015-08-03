#Github Robots application demo
![logo](http://robohash.org/robots.png?size=200x200)

[![Build Status](https://travis-ci.org/Exaper/github-robots.svg?branch=master)](https://travis-ci.org/Exaper/github-robots)

## Key features
1. Material Design [guidelines](https://www.google.com/design/spec/material-design/introduction.html) compliant
2. Dependency Injection with [Dagger 2](http://google.github.io/dagger/)
3. Network calls + JSON parsing via [Retrofit](http://square.github.io/retrofit/)
3. Instrumentation Testing with [Espresso](https://code.google.com/p/android-test-kit/wiki/Espresso)
4. Unit Testing with [Android JUnit](http://tools.android.com/tech-docs/unit-testing-support)
5. [Gradle-based](http://tools.android.com/tech-docs/new-build-system) build system
5. Continious integration with [Travis CI](https://travis-ci.org/Exaper/github-robots)

## Setup instructions
1. Import project into Android Studio (make sure to have latest build and updated Android SDK installed)
2. "app" module should be buildable. All dependencies will be pulled in by gradle.
3. Project includes two types of testing explained below.

## Instrumentation testing
1. In Android Studio, switch run configuration to Android Test. Make sure to have "Android Instrumentation Tests"
selected for your "Test Artifact" in "Build Flavors" section.
2. Espresso tests spawn mock web server and able to be executed without making actual network calls.
3. From command line: do ```gradlew connectedAndroidTest```

## Unit testing
1. In Android Studio, switch to JUnit run configuration. Make sure to have "Unit tests" selected for your
"Test Artifact" in "Build Flavors" section. Make sure to use ```com.exaper.robots.RobotsTestRunner``` test runner for
espresso tests.
2. Run tests as regular JUnit tests.
3. From command line: do ```gradlew test```