package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.BaseObservable;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class BindingAdapter extends BaseObservable {
    String title;

    @android.databinding.BindingAdapter("android:typeface")
    public static void setTextStyle(TextView view, String style) {
        switch (style) {
            case "bold":
                view.setTypeface(null, Typeface.BOLD);
                break;
            default:
                view.setTypeface(null, Typeface.NORMAL);
        }
    }

    public String getTitle() {
        return title;
    }
}
