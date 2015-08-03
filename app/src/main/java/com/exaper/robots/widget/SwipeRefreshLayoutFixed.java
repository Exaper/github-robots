package com.exaper.robots.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Special version of SwipeRefreshLayout that adds workaround
 * for https://code.google.com/p/android/issues/detail?id=77712
 * <p/>
 * TODO: re-evaluate whether this class is still needed after upgrading to android-support v.23+
 */
public class SwipeRefreshLayoutFixed extends SwipeRefreshLayout {
    private boolean refreshingBeforeLayout;
    private boolean laidOut;

    public SwipeRefreshLayoutFixed(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayoutFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Fix for https://code.google.com/p/android/issues/detail?id=77712
        if (!laidOut) {
            laidOut = true;
            if (refreshingBeforeLayout) {
                super.setRefreshing(true);
            }
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        // Deferring refreshing flag until first layout pass.
        if (laidOut) {
            super.setRefreshing(refreshing);
        } else {
            refreshingBeforeLayout = refreshing;
        }
    }
}
