package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.turn.AdvStats;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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

        if (integerPair.getLeft() == 0)
            leftWeight = .1f;
        else
            leftWeight = (float) integerPair.getLeft() / ((float) integerPair.getLeft() + (float) integerPair.getRight());

        if (integerPair.getRight() == 0)
            rightWeight = .1f;
        else
            rightWeight = (float) integerPair.getRight() / ((float) integerPair.getLeft() + (float) integerPair.getRight());

        leftView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, leftWeight));
        rightView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, rightWeight));

        leftView.setText(Integer.toString(integerPair.getLeft()));
        rightView.setText(Integer.toString(integerPair.getRight()));
    }

    public static void updateGridOfMissReasons(View view, List<StatLineItem> items) {
        if (view != null) {
            if (items.size() > 0) {
                view.findViewById(R.id.line0Desc).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line0Count).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line0Pct).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.line0Desc)).setText(items.get(0).getDescription());
                ((TextView) view.findViewById(R.id.line0Count)).setText(items.get(0).getCount());
                ((TextView) view.findViewById(R.id.line0Pct)).setText(items.get(0).getPercentage());
            }

            if (items.size() > 1) {
                view.findViewById(R.id.line1Desc).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line1Count).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line1Pct).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.line1Desc)).setText(items.get(1).getDescription());
                ((TextView) view.findViewById(R.id.line1Count)).setText(items.get(1).getCount());
                ((TextView) view.findViewById(R.id.line1Pct)).setText(items.get(1).getPercentage());
            }

            if (items.size() > 2) {
                view.findViewById(R.id.line2Desc).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line2Count).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line2Pct).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.line2Desc)).setText(items.get(2).getDescription());
                ((TextView) view.findViewById(R.id.line2Count)).setText(items.get(2).getCount());
                ((TextView) view.findViewById(R.id.line2Pct)).setText(items.get(2).getPercentage());
            }

            if (items.size() > 3) {
                view.findViewById(R.id.line3Desc).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line3Count).setVisibility(View.VISIBLE);
                view.findViewById(R.id.line3Pct).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.line3Desc)).setText(items.get(3).getDescription());
                ((TextView) view.findViewById(R.id.line3Count)).setText(items.get(3).getCount());
                ((TextView) view.findViewById(R.id.line3Pct)).setText(items.get(3).getPercentage());
            }
        }
    }

    public static Pair<Integer, Integer> getHowCutErrors(List<AdvStats> stats) {
        int over = 0, under = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains("Too thin"))
                over++;
            else if (stat.getHowTypes().contains("Too thick"))
                under++;
        }

        return new ImmutablePair<>(over, under);
    }

    public static Pair<Integer, Integer> getHowAimErrors(List<AdvStats> stats) {
        int left = 0, right = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains("Left of aim point"))
                left++;
            else if (stat.getHowTypes().contains("Right of aim point"))
                right++;
        }

        return new ImmutablePair<>(left, right);
    }

    public static Pair<Integer, Integer> getHowSpeedErrors(List<AdvStats> stats) {
        int fast = 0, slow = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains("Too hard"))
                fast++;
            else if (stat.getHowTypes().contains("Too soft"))
                slow++;
        }

        return new ImmutablePair<>(slow, fast);
    }

    public static List<StatLineItem> getFourMostCommonItems(List<AdvStats> stats) {
        List<StatLineItem> list = getStats(stats);

        return list.subList(0, 4);
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

    public static List<StatLineItem> getSuccessfulSafetyStats(List<AdvStats> stats) {
        int total = stats.size();

        List<StatLineItem> list = new ArrayList<>();
        list.add(new StatLineItem("Full hook", total));
        list.add(new StatLineItem("Partial hook", total));
        list.add(new StatLineItem("Long T", total));
        list.add(new StatLineItem("Short T", total));
        list.add(new StatLineItem("Open", total));

        for (StatLineItem item : list) {
            item.count = getCountOfSubTypesInList(stats, item.description);
        }

        return list;
    }

    private static int getCountOfSubTypesInList(List<AdvStats> stats, String item) {
        int count = 0;

        if (item.equals("Open")) {
            for (AdvStats stat : stats) {
                if (stat.getShotType().equals("Safety error"))
                    count++;
            }
        } else
            for (AdvStats stat : stats) {
                if (stat.getShotSubtype().equals(item))
                    count++;
            }

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
            return "(" + pctf.format((double) count / (double) total) + ")";
        }

        @Override
        public int compareTo(@NonNull StatLineItem another) {
            return Integer.compare(count, another.count);
        }
    }
}
