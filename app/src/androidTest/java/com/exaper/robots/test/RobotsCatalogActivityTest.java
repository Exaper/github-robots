package com.exaper.robots.test;

import android.test.ActivityInstrumentationTestCase2;

import com.exaper.robots.RobotsCatalogActivity;
import com.exaper.robots.test.util.EspressoUtil;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class RobotsCatalogActivityTest extends ActivityInstrumentationTestCase2<RobotsCatalogActivity> {
    private MockWebServer mWebServer;

    public RobotsCatalogActivityTest() {
        super(RobotsCatalogActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        String mockResponse = EspressoUtil.stringFromRawResource(getInstrumentation().getContext(), R.raw.robots);
        mWebServer = new MockWebServer();
        mWebServer.enqueue(new MockResponse().setBody(mockResponse));
        mWebServer.start();
        EspressoUtil.registerWebServer(getInstrumentation().getTargetContext(), mWebServer);
        getActivity();
    }

    public void testRobotNamesDisplayed() {
        for (String name : new String[]{"torvalds", "mojombo", "paulirish"}) {
            onView(withText(name)).check(matches(isDisplayed()));
        }
    }

    @Override
    protected void tearDown() throws Exception {
        mWebServer.shutdown();
        super.tearDown();
    }
}
