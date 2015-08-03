package com.exaper.robots.app;

import com.exaper.robots.data.model.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RobotsSearchFilter {
    private String mConstraint;

    public void setConstraint(CharSequence constraint) {
        mConstraint = constraint != null ? toSearchableString(constraint.toString()) : null;
    }

    public List<Robot> filter(List<Robot> robots) {
        if (mConstraint == null) {
            return robots;
        }
        if (robots.isEmpty()) {
            return robots;
        }
        List<Robot> filtered = new ArrayList<>();
        for (Robot robot : robots) {
            if (shouldKeep(robot)) {
                filtered.add(robot);
            }
        }
        return filtered;
    }

    private boolean shouldKeep(Robot robot) {
        String coercedTitle = toSearchableString(robot.getLogin());
        return coercedTitle.contains(mConstraint);
    }

    private static String toSearchableString(String s) {
        return s.trim().replaceAll("\\s+", "").toLowerCase(Locale.getDefault());
    }
}
