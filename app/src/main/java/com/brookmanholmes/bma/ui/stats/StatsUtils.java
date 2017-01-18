package com.brookmanholmes.bma.ui.stats;

import android.graphics.PorterDuff;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Brookman Holmes on 3/20/2016.
 */
class StatsUtils {
    private static final int FULL_HOOK = 0,
            PARTIAL_HOOK = 1,
            LONG_T = 2,
            SHORT_T = 3,
            NO_DIRECT_SHOT = 4,
            OPEN = 5;

    private StatsUtils() {
    }

    static void setLayoutWeights(List<AdvStats> stats, AdvStats.HowType left, AdvStats.HowType right,
                                 TextView leftView, TextView rightView) {
        Pair<Float, Float> pair = getHowError(stats, left, right);
        float leftWeight;
        float rightWeight;

        if (pair.first == 0)
            leftWeight = .1f;
        else
            leftWeight = pair.first / (pair.first + pair.second);

        if (pair.second == 0)
            rightWeight = .1f;
        else
            rightWeight = pair.second / (pair.first + pair.second);

        if (pair.first + pair.second > 0) {
            leftView.getBackground().setColorFilter(ContextCompat.getColor(leftView.getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            leftView.setTextColor(ContextCompat.getColor(leftView.getContext(), R.color.white));

            setParams(leftView, leftWeight, 0, 1);
            setParams(rightView, rightWeight, 1, 0);

            leftView.setText(String.format(Locale.getDefault(), "%.0f", pair.first));
            rightView.setText(String.format(Locale.getDefault(), "%.0f", pair.second));
        } else {
            setParams(leftView, 1, 0, 0);
            setParams(rightView, 0, 0, 0);

            leftView.setText("No data");
            leftView.getBackground().setColorFilter(ContextCompat.getColor(leftView.getContext(), R.color.dead_ball), PorterDuff.Mode.SRC_IN);
            leftView.setTextColor(ContextCompat.getColor(leftView.getContext(), R.color.primary_text));
        }
    }

    private static void setParams(View view, float weight, int leftMargin, int rightMargin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);

        params.leftMargin = (int) ConversionUtils.convertDpToPx(view.getContext(), leftMargin);
        params.rightMargin = (int) ConversionUtils.convertDpToPx(view.getContext(), rightMargin);

        view.setLayoutParams(params);
    }

    private static Pair<Float, Float> getHowError(List<AdvStats> stats, AdvStats.HowType left, AdvStats.HowType right) {
        float fast = 0, slow = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(right))
                fast++;
            else if (stat.getHowTypes().contains(left))
                slow++;
        }

        return new Pair<>(slow, fast);
    }

    public static List<StatLineItem> getSafetyStats(List<AdvStats> stats) {
        int total = stats.size();

        List<StatLineItem> list = new ArrayList<>();
        list.add(new StatLineItem(R.string.safety_full_hook, total));
        list.add(new StatLineItem(R.string.safety_partial_hook, total));
        list.add(new StatLineItem(R.string.safety_long_t, total));
        list.add(new StatLineItem(R.string.safety_short_t, total));
        list.add(new StatLineItem(R.string.safety_no_shot, total));
        list.add(new StatLineItem(R.string.safety_open, total));

        for (StatLineItem item : list) {
            item.setCount(getCountOfSubTypesInList(stats, item.getStringRes()));
        }

        return list;
    }

    private static int getCountOfSubTypesInList(List<AdvStats> stats, @StringRes int res) {
        int count = 0;

        if (res == R.string.safety_open) {
            for (AdvStats stat : stats)
                if (stat.getShotType() == AdvStats.ShotType.SAFETY_ERROR)
                    count++;
        } else
            for (AdvStats stat : stats)
                if (res == MatchDialogHelperUtils.convertSubTypeToStringRes(stat.getShotSubtype()))
                    count++;

        return count;
    }

    public static int getFailedSafeties(List<AdvStats> stats) {
        int count = 0;
        for (AdvStats stat : stats)
            if (stat.getShotSubtype() == AdvStats.SubType.OPEN)
                count++;

        return count;
    }

    public static int getMiscues(List<AdvStats> stats) {
        int count = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(AdvStats.HowType.MISCUE))
                count++;
        }

        return count;
    }
}
