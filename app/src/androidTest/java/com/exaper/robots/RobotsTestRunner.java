package com.exaper.robots;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

public class RobotsTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, EspressoRobotsApplication.class.getName(), context);
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        EspressoRobotsApplication.get().onFinishInstrumentation();
        super.finish(resultCode, results);
    }
}
