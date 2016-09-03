package com.brookmanholmes.bma.ui.stats;

import android.support.annotation.NonNull;

import java.text.NumberFormat;

/**
 * Created by Brookman Holmes on 9/1/2016.
 */
public class StatLineItem implements Comparable<StatLineItem> {
    final NumberFormat pctf = NumberFormat.getPercentInstance();

    private final String description;
    private final int total;
    private int count;

    public StatLineItem(String description, int total) {
        this.description = description;
        this.count = 0;
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPercentage() {
        if (total == 0)
            return pctf.format(0);
        else
            return pctf.format((double) count / (double) total);
    }

    @Override public int compareTo(@NonNull StatLineItem another) {
        return Integer.compare(count, another.count);
    }
}
