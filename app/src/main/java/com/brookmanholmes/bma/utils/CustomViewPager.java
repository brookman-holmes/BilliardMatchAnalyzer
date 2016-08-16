package com.brookmanholmes.bma.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Brookman Holmes on 6/17/2016.
 */
public class CustomViewPager extends ViewPager {
    private boolean swipeable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (swipeable)
            return super.onInterceptTouchEvent(ev);

        return false;
    }

    @Override public boolean onTouchEvent(MotionEvent ev) {
        if (swipeable)
            return super.onTouchEvent(ev);

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.swipeable = enabled;
    }
}
