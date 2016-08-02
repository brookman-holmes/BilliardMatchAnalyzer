package com.brookmanholmes.billiardmatchanalyzer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Brookman Holmes on 8/2/2016.
 */
public class ConversionUtils {
    private ConversionUtils() {

    }

    public static float convertDpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
