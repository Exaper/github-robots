package com.exaper.robots.espresso;

import android.os.SystemClock;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkState;

/**
 * Port of com.google.android.apps.common.testing.ui.espresso.contrib.CountingIdlingResource
 */
public class CountingIdlingResource implements IdlingResource {
    private static final String TAG = "CountingIdlingResource";
    private final String resourceName;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final boolean debugCounting;

    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    // read/written from any thread - used for debugging messages.
    private volatile long becameBusyAt = 0;
    private volatile long becameIdleAt = 0;

    /**
     * Creates a CountingIdlingResource without debug tracing.
     *
     * @param resourceName the resource name this resource should report to Espresso.
     */
    public CountingIdlingResource(String resourceName) {
        this(resourceName, false);
    }

    /**
     * Creates a CountingIdlingResource.
     *
     * @param resourceName  the resource name this resource should report to Espresso.
     * @param debugCounting if true increment & decrement calls will print trace information to logs.
     */
    public CountingIdlingResource(String resourceName, boolean debugCounting) {
        this.resourceName = checkNotNull(resourceName);
        this.debugCounting = debugCounting;
    }

    @Override
    public String getName() {
        return resourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    /**
     * Increments the count of in-flight transactions to the resource being monitored.
     * <p/>
     * This method can be called from any thread.
     */
    public void increment() {
        int counterVal = counter.getAndIncrement();
        if (0 == counterVal) {
            becameBusyAt = SystemClock.uptimeMillis();
        }

        if (debugCounting) {
            Log.i(TAG, "Resource: " + resourceName + " in-use-count incremented to: " + (counterVal + 1));
        }
    }

    /**
     * Decrements the count of in-flight transactions to the resource being monitored.
     * <p/>
     * If this operation results in the counter falling below 0 - an exception is raised.
     *
     * @throws IllegalStateException if the counter is below 0.
     */
    public void decrement() {
        int counterVal = counter.decrementAndGet();

        if (counterVal == 0) {
            // we've gone from non-zero to zero. That means we're idle now! Tell espresso.
            if (null != resourceCallback) {
                resourceCallback.onTransitionToIdle();
            }
            becameIdleAt = SystemClock.uptimeMillis();
        }

        if (debugCounting) {
            if (counterVal == 0) {
                Log.i(TAG, "Resource: " + resourceName + " went idle! (Time spent not idle: " +
                        (becameIdleAt - becameBusyAt) + ")");
            } else {
                Log.i(TAG, "Resource: " + resourceName + " in-use-count decremented to: " + counterVal);
            }
        }
        checkState(counterVal > -1, "Counter has been corrupted!");
    }

    /**
     * Prints the current state of this resource to the logcat at info level.
     */
    public void dumpStateToLogs() {
        StringBuilder message = new StringBuilder("Resource: ")
                .append(resourceName)
                .append(" inflight transaction count: ")
                .append(counter.get());
        if (0 == becameBusyAt) {
            Log.i(TAG, message.append(" and has never been busy!").toString());
        } else {
            message.append(" and was last busy at: ")
                    .append(becameBusyAt);
            if (0 == becameIdleAt) {
                Log.w(TAG, message.append(" AND NEVER WENT IDLE!").toString());
            } else {
                message.append(" and last went idle at: ")
                        .append(becameIdleAt);
                Log.i(TAG, message.toString());
            }
        }
    }
}
