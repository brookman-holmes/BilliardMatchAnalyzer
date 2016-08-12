package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.turn.AdvStats;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Brookman Holmes on 3/20/2016.
 */
public class StatsUtils {
    private static final int FULL_HOOK = 0,
            PARTIAL_HOOK = 1,
            LONG_T = 2,
            SHORT_T = 3,
            NO_DIRECT_SHOT = 4,
            OPEN = 5;
    private StatsUtils() {
    }

    static void setLayoutWeights(Pair<Integer, Integer> integerPair, TextView leftView, TextView rightView) {
        float leftWeight;
        float rightWeight;

        if (integerPair.first == 0)
            leftWeight = .1f;
        else
            leftWeight = (float) integerPair.first / ((float) integerPair.first + (float) integerPair.second);

        if (integerPair.second == 0)
            rightWeight = .1f;
        else
            rightWeight = (float) integerPair.second / ((float) integerPair.first + (float) integerPair.second);

        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, leftWeight));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, rightWeight));

        leftView.setText(Integer.toString(integerPair.first));
        rightView.setText(Integer.toString(integerPair.second));
    }

    public static void setListOfMissReasons(LinearLayout parent, List<AdvStats> stats) {
        List<StatLineItem> items = getStats(stats);
        addRows(parent, items);
    }

    private static void addRows(LinearLayout parent, List<StatLineItem> items) {
        parent.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            LinearLayout row = inflateRow(parent.getContext());
            setTextOfRow(row, items.get(i));
            row.setLayoutParams(parent.getLayoutParams());
            if (i % 2 == 0)
                setTextColorOfRow(row); // highlight every other row

            parent.addView(row);
        }
    }

    private static void setTextColorOfRow(LinearLayout row) {
        for (int i = 0; i < row.getChildCount(); i++) {
            if (row.getChildAt(i) instanceof TextView)
                ((TextView) row.getChildAt(i))
                        .setTextColor(ContextCompat.getColor(row.getContext(), R.color.primary_text));
        }
    }

    private static LinearLayout inflateRow(Context context) {
        return (LinearLayout) LinearLayout.inflate(context, R.layout.container_adv_stat_row, null);
    }

    private static void setTextOfRow(LinearLayout row, StatLineItem item) {
        ((TextView) row.getChildAt(0)).setText(item.getDescription());
        ((TextView) row.getChildAt(1)).setText(item.getCount());
        ((TextView) row.getChildAt(2)).setText(item.getPercentage());
    }

    public static Pair<Integer, Integer> getHowCutErrors(Context context, List<AdvStats> stats) {
        int over = 0, under = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(context.getString(R.string.thin_hit)))
                over++;
            else if (stat.getHowTypes().contains(context.getString(R.string.thick_hit)))
                under++;
        }

        return new Pair<>(over, under);
    }

    public static Pair<Integer, Integer> getHowAimErrors(Context context, List<AdvStats> stats) {
        int left = 0, right = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(context.getString(R.string.aim_to_left)))
                left++;
            else if (stat.getHowTypes().contains(context.getString(R.string.aim_to_right)))
                right++;
        }

        return new Pair<>(left, right);
    }

    public static Pair<Integer, Integer> getHowSpeedErrors(Context context, List<AdvStats> stats) {
        int fast = 0, slow = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(context.getString(R.string.too_hard)))
                fast++;
            else if (stat.getHowTypes().contains(context.getString(R.string.too_soft)))
                slow++;
        }

        return new Pair<>(slow, fast);
    }

    public static List<StatLineItem> getStats(List<AdvStats> stats) {
        List<StatLineItem> list = new ArrayList<>();
        Set<String> whyTypes = new HashSet<>();
        int total = stats.size();

        for (AdvStats stat : stats) {
            whyTypes.addAll(stat.getWhyTypes());
        }

        for (String string : whyTypes) {
            list.add(new StatLineItem(string, total));
        }

        for (StatLineItem item : list) {
            item.count = getCountOfWhyItemsInList(stats, item.description);
        }

        Collections.sort(list);
        Collections.reverse(list);

        return list;
    }

    public static void setListOfSafetyStats(LinearLayout parent, List<AdvStats> stats) {
        List<StatLineItem> items = getSafetyStats(parent.getContext(), stats);

        addRows(parent, items);
    }

    private static List<StatLineItem> getSafetyStats(Context context, List<AdvStats> stats) {
        int total = stats.size();

        List<StatLineItem> list = new ArrayList<>();
        list.add(new StatLineItem(context.getString(R.string.safety_full_hook), total));
        list.add(new StatLineItem(context.getString(R.string.safety_partial_hook), total));
        list.add(new StatLineItem(context.getString(R.string.safety_long_t), total));
        list.add(new StatLineItem(context.getString(R.string.safety_short_t), total));
        list.add(new StatLineItem(context.getString(R.string.safety_no_shot), total));
        list.add(new StatLineItem(context.getString(R.string.safety_open), total));

        for (StatLineItem item : list) {
            item.count = getCountOfSubTypesInList(context, stats, item.description);
        }

        return list;
    }

    public static int getFailedSafeties(Context context, List<AdvStats> stats) {
        List<StatLineItem> safeties = getSafetyStats(context, stats);

        return safeties.get(OPEN).count;
    }

    private static int getCountOfSubTypesInList(Context context, List<AdvStats> stats, String item) {
        int count = 0;

        if (item.equals(context.getString(R.string.safety_open)))
            for (AdvStats stat : stats)
                if (stat.getShotType().equals("Safety error"))
                    count++;
                else
                    ;
        else
            for (AdvStats stat : stats)
                if (stat.getShotSubtype().equals(item))
                    count++;

        return count;
    }

    private static int getCountOfWhyItemsInList(List<AdvStats> stats, String item) {
        int count = 0;
        for (AdvStats stat : stats) {
            if (stat.getWhyTypes().contains(item))
                count++;
        }

        return count;
    }

    private static class StatLineItem implements Comparable<StatLineItem> {
        final NumberFormat pctf = NumberFormat.getPercentInstance();

        private String description;
        private int count;
        private int total;

        public StatLineItem(String description) {
            this.description = description;
            count = 0;
            total = 0;
            pctf.setMaximumFractionDigits(0);
        }

        public StatLineItem(String description, int total) {
            this.description = description;
            this.count = 0;
            this.total = total;
        }

        public String getDescription() {
            return description;
        }

        public String getCount() {
            return Integer.toString(count);
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
}
