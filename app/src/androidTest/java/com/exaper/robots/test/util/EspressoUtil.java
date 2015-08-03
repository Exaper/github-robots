package com.exaper.robots.test.util;

import android.content.Context;
import android.support.annotation.RawRes;
import android.support.test.InstrumentationRegistry;

import com.exaper.robots.EspressoRobotsApplication;
import com.exaper.robots.RobotsApplication;
import com.exaper.robots.di.EspressoNetworkModule;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class EspressoUtil {
    public static String stringFromRawResource(@RawRes int resourceId) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        InputStream raw = context.getResources().openRawResource(resourceId);
        Scanner scanner = new Scanner(raw).useDelimiter("\\A");
        String out = scanner.hasNext() ? scanner.next() : "";
        try {
            raw.close();
        } catch (IOException e) {
            // Oh well
            e.printStackTrace();
        }
        return out;
    }
}
