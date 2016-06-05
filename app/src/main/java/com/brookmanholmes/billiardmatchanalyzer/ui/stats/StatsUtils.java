package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.TypedValue;
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
import java.util.Set;

/**
 * Created by Brookman Holmes on 3/20/2016.
 */
public class StatsUtils {
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

    // todo: dynamically create each line in the list and put it in a gridview that's inside a scrolling container?
    public static void updateGridOfMissReasons(GridLayout grid, List<StatLineItem> items) {
        if (grid != null) {
            Context context = grid.getContext();

            TextView reason = getTextView(context);
            reason.setLayoutParams(getParams(0, 0));
            reason.setText("Reason for missing");

            TextView number = getTextView(context);
            number.setLayoutParams(getParams(1, 0));
            number.setText("#");

            TextView pct = getTextView(context);
            pct.setLayoutParams(getParams(2, 0));
            pct.setText("%");

            for (int i = 0, r = 0; i < items.size(); i++, r++) {
                TextView description = getTextView(context);
                TextView count = getTextView(context);
                TextView percent = getTextView(context);

                description.setLayoutParams(getParams(0, i + 1));
                count.setLayoutParams(getParams(1, i + 1));
                percent.setLayoutParams(getParams(2, i + 1));
                description.setText(items.get(i).description);
                count.setText(String.valueOf(items.get(i).count));
                percent.setText(items.get(i).getPercentage());

                grid.addView(description);
                grid.addView(count);
                grid.addView(percent);
            }
        }
    }

    private static TextView getTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.medium_text));

        return textView;
    }

    private static GridLayout.LayoutParams getParams(int column, int row) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(column);
        params.rowSpec = GridLayout.spec(row);
        return params;
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

    public static List<StatLineItem> getFourMostCommonItems(List<AdvStats> stats) {
        List<StatLineItem> list = getStats(stats);

        if (list.size() > 4)
            return list.subList(0, 4);
        else return list;
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

    public static List<StatLineItem> getSafetyStats(Context context, List<AdvStats> stats) {
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

    public static class StatLineItem implements Comparable<StatLineItem> {
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
