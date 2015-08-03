package com.exaper.robots.test;

import android.test.ActivityInstrumentationTestCase2;

import com.exaper.robots.R;
import com.exaper.robots.RobotsCatalogActivity;
import com.exaper.robots.test.util.EspressoUtil;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class InvalidResponseTest extends ActivityInstrumentationTestCase2<RobotsCatalogActivity> {
    private MockWebServer mWebServer;

    public InvalidResponseTest() {
        super(RobotsCatalogActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mWebServer = new MockWebServer();
        mWebServer.enqueue(new MockResponse().setResponseCode(500));
        mWebServer.start();
        EspressoUtil.registerWebServer(getInstrumentation().getTargetContext(), mWebServer);
        getActivity();
    }

    public void testEmptyTestDisplayed() {
        String pullToRefreshText = getActivity().getString(R.string.pull_to_refresh_hint);
        onView(withText(pullToRefreshText)).check(matches(isDisplayed()));
    }

    @Override
    protected void tearDown() throws Exception {
        mWebServer.shutdown();
        super.tearDown();
    }
}
