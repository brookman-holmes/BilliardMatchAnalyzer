package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.BaseObservable;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Brookman Holmes on 9/21/2016.
 */

public abstract class BindingAdapter extends BaseObservable {
    // formatter for percentages (e.g. .875)
    final static DecimalFormat pctf = ConversionUtils.pctf;
    // formatter for average number of balls made per turn (e.g. 5.33)
    final static DecimalFormat avgf = ConversionUtils.avgf;
    final static String defaultPct = pctf.format(0);
    final static String defaultAvg = avgf.format(0);

    static final String TAG = "BindingAdapter";
    public boolean expanded = false;
    @DrawableRes
    public int imageResource;
    public boolean showCard = true;
    String title;
    int helpLayout;

    public BindingAdapter(String title, boolean expanded, boolean showCard) {
        imageResource = (expanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand);
        this.title = title;
        this.expanded = expanded;
        this.showCard = showCard;
    }

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

    @android.databinding.BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @android.databinding.BindingAdapter("android:layout_weight")
    public static void setLayoutWeight(View view, float weight) {
        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
        params.weight = weight;
        view.setLayoutParams(params);
    }

    static int compare(String x, String y) {
        return Double.compare(Double.parseDouble(x), Double.parseDouble(y));
    }

    public abstract void update(Player player, Player opponent, @Nullable GameStatus gameStatus);

    float round(float value) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
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
        expanded = !expanded;
        notifyChange();
    }
}
