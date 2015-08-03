package com.exaper.robots.test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.exaper.robots.EspressoRobotsApplication;
import com.exaper.robots.R;
import com.exaper.robots.RobotsCatalogActivity;
import com.exaper.robots.test.util.EspressoUtil;
import com.squareup.okhttp.mockwebserver.MockResponse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RobotsCatalogActivityTest {
    @Rule
    public final ActivityTestRule<RobotsCatalogActivity> activityRule = new ActivityTestRule<>(
            RobotsCatalogActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Test
    public void testEmptyTestDisplayed() {
        EspressoRobotsApplication.get().getMockWebServer().enqueue(new MockResponse().setResponseCode(500));
        Activity activity = activityRule.launchActivity(null);
        String pullToRefreshText = activity.getString(R.string.pull_to_refresh_hint);
        onView(withText(pullToRefreshText)).check(matches(isDisplayed()));
    }

    @Test
    public void testRobotNamesDisplayed() {
        String mockResponse = EspressoUtil.stringFromRawResource(com.exaper.robots.test.R.raw.robots);
        EspressoRobotsApplication.get().getMockWebServer().enqueue(new MockResponse().setBody(mockResponse));
        activityRule.launchActivity(null);
        for (String name : new String[]{"torvalds", "mojombo", "paulirish"}) {
            onView(withText(name)).check(matches(isDisplayed()));
        }
    }
}
