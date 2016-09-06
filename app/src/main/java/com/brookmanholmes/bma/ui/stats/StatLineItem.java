package com.brookmanholmes.bma.ui.stats;

import android.support.annotation.NonNull;

import com.brookmanholmes.billiards.turn.AdvStats;

import java.text.NumberFormat;

/**
 * Created by Brookman Holmes on 9/1/2016.
 */
public class StatLineItem implements Comparable<StatLineItem> {
    final NumberFormat pctf = NumberFormat.getPercentInstance();

    private final String shotType;
    private final int total;
    private int count;

    public StatLineItem(String shotType, int total) {
        this.shotType = shotType;
        this.count = 0;
        this.total = total;
    }

    public String getDescription() {
        return shotType;
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
