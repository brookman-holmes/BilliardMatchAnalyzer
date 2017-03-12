package com.brookmanholmes.bma.ui.stats;

import android.databinding.BaseObservable;

import com.brookmanholmes.bma.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Brookman Holmes on 1/10/2017.
 */

public class SafetyStatBinder extends BaseObservable {
    private static final String TAG = "SafetyStatBinder";
    private int fullHookCount;
    private int partialHookCount;
    private int longTCount;
    private int shortTCount;
    private int noShotCount;
    private int openCount;

    SafetyStatBinder(List<StatLineItem> items) {
        update(items);
    }

    void update(List<StatLineItem> items) {
        for (StatLineItem item : items) {
            switch (item.getStringRes()) {
                case R.string.safety_full_hook:
                    fullHookCount = item.getCount();
                    break;
                case R.string.safety_partial_hook:
                    partialHookCount = item.getCount();
                    break;
                case R.string.safety_long_t:
                    longTCount = item.getCount();
                    break;
                case R.string.safety_short_t:
                    shortTCount = item.getCount();
                    break;
                case R.string.safety_no_shot:
                    noShotCount = item.getCount();
                    break;
                case R.string.safety_open:
                    openCount = item.getCount();
                    break;
            }
        }
        notifyChange();
    }

    public String getFullHookCount() {
        return format(fullHookCount);
    }

    public String getPartialHookCount() {
        return format(partialHookCount);
    }

    public String getLongTCount() {
        return format(longTCount);
    }

    public String getShortTCount() {
        return format(shortTCount);
    }

    public String getNoShotCount() {
        return format(noShotCount);
    }

    public String getOpenCount() {
        return format(openCount);
    }

    public String getFullHookPct() {
        return formatPct(fullHookCount);
    }

    public String getPartialHookPct() {
        return formatPct(partialHookCount);
    }

    public String getLongTPct() {
        return formatPct(longTCount);
    }

    public String getShortTPct() {
        return formatPct(shortTCount);
    }

    public String getNoShotPct() {
        return formatPct(noShotCount);
    }

    public String getOpenPct() {
        return formatPct(openCount);
    }

    private String format(int value) {
        return String.format(Locale.getDefault(), "%1$d", value);
    }

    private String formatPct(int value) {
        if (getTotal() == 0)
            return DecimalFormat.getPercentInstance().format(0f);
        else
            return DecimalFormat.getPercentInstance().format((double) value / (double) getTotal());
    }

    private int getTotal() {
        return fullHookCount + partialHookCount + longTCount + shortTCount + noShotCount + openCount;
    }
}
