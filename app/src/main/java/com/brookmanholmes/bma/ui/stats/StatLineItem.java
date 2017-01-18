package com.brookmanholmes.bma.ui.stats;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Brookman Holmes on 9/1/2016.
 */
public class StatLineItem implements Comparable<StatLineItem> {
    private final int total;
    private int count;
    @StringRes
    private int stringRes;

    public StatLineItem(@StringRes int stringRes, int total) {
        this.count = 0;
        this.total = total;
        this.stringRes = stringRes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @StringRes
    public int getStringRes() {
        return stringRes;
    }

    @Override
    public int compareTo(@NonNull StatLineItem another) {
        return Integer.compare(count, another.count);
    }
}
