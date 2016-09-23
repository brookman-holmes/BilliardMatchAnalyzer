package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.BaseObservable;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public class BindingAdapter extends BaseObservable {
    private static final String TAG = "BindingAdapter";
    public boolean visible = false;
    String title;
    int helpLayout;

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

    @android.databinding.BindingAdapter("android:typeface")
    public static void setTextSize(TextView view, int size) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public String getTitle() {
        return title;
    }

    public void infoButton(View view) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);

        builder.setView(LayoutInflater.from(view.getContext()).inflate(helpLayout, null))
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    public void onCollapse(View view) {
        View grid = (View) view.getParent().getParent();
        // find the linear layout above this
        LinearLayout container = (LinearLayout) grid.getParent().getParent();
        // animate the collapse/expand button
        view.animate().rotationXBy(180f);

        // toggle the visibility
        TransitionManager.beginDelayedTransition(container);
        visible = !visible;
        notifyChange();
    }
}
