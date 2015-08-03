package com.exaper.robots.app;

import com.exaper.robots.data.model.Robot;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RobotsSearchFilterTest {
    private RobotsSearchFilter filter;
    private List<Robot> testRobots;

    @Before
    public void setUp() throws Exception {
        filter = new RobotsSearchFilter();
        testRobots = Arrays.asList(
                createRobot("Android"),
                createRobot("Roomba"),
                createRobot("Verter"),
                createRobot("Bender"));
    }

    @Test
    public void testEmptyFilter() {
        filter.setConstraint(null);
        List<Robot> filteredRobots = filter.filter(testRobots);
        // All robots made it through filter.
        assertThat(filteredRobots.size(), is(testRobots.size()));
    }

    @Test
    public void testNoMatchesFilter() {
        filter.setConstraint("Human");
        List<Robot> filteredRobots = filter.filter(testRobots);
        // No robots should make it through filter.
        assertThat(filteredRobots.size(), is(0));
    }

    @Test
    public void testSimpleConstraint() {
        filter.setConstraint("Android");
        List<Robot> filteredRobots = filter.filter(testRobots);
        // No robots should make it through filter.
        assertThat(filteredRobots.size(), is(1));
        Robot android = filteredRobots.get(0);
        assertThat(android.getLogin(), is("Android"));
    }

    private static Robot createRobot(String login) {
        Robot mockRobot = mock(Robot.class);
        when(mockRobot.getLogin()).thenReturn(login);
        return mockRobot;
    }
}