package com.brookmanholmes.bma.ui.stats;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<StatLineItem> items = getStats(parent.getContext(), stats);
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
        ((TextView) row.getChildAt(1)).setText(String.valueOf(item.getCount()));
        ((TextView) row.getChildAt(2)).setText(item.getPercentage());
    }

    public static Pair<Integer, Integer> getHowCutErrors(List<AdvStats> stats) {
        int over = 0, under = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(AdvStats.HowType.THIN))
                over++;
            else if (stat.getHowTypes().contains(AdvStats.HowType.THICK))
                under++;
        }

        return new Pair<>(over, under);
    }

    public static Pair<Integer, Integer> getHowAimErrors(List<AdvStats> stats) {
        int left = 0, right = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(AdvStats.HowType.AIM_LEFT))
                left++;
            else if (stat.getHowTypes().contains(AdvStats.HowType.AIM_RIGHT))
                right++;
        }

        return new Pair<>(left, right);
    }

    public static Pair<Integer, Integer> getHowBankErrors(List<AdvStats> stats) {
        int left = 0, right = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(AdvStats.HowType.BANK_LONG))
                right++;
            else if (stat.getHowTypes().contains(AdvStats.HowType.BANK_SHORT))
                left++;
        }

        return new Pair<>(left, right);
    }

    public static Pair<Integer, Integer> getHowKickErrors(List<AdvStats> stats) {
        int left = 0, right = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(AdvStats.HowType.KICK_LONG))
                right++;
            else if (stat.getHowTypes().contains(AdvStats.HowType.KICK_SHORT))
                left++;
        }

        return new Pair<>(left, right);
    }

    public static Pair<Integer, Integer> getHowSpeedErrors(List<AdvStats> stats) {
        int fast = 0, slow = 0;

        for (AdvStats stat : stats) {
            if (stat.getHowTypes().contains(AdvStats.HowType.TOO_HARD))
                fast++;
            else if (stat.getHowTypes().contains(AdvStats.HowType.TOO_SOFT))
                slow++;
        }

        return new Pair<>(slow, fast);
    }

    public static List<StatLineItem> getStats(Context context, List<AdvStats> stats) {
        List<StatLineItem> list = new ArrayList<>();
        Set<AdvStats.WhyType> whyTypes = new HashSet<>();
        int total = stats.size();

        for (AdvStats stat : stats) {
            whyTypes.addAll(stat.getWhyTypes());
        }

        for (AdvStats.WhyType whyType : whyTypes) {
            list.add(new StatLineItem(
                    context.getString(MatchDialogHelperUtils.convertWhyTypeToStringRes(whyType)),
                    total));
        }

        for (StatLineItem item : list) {
            item.setCount(getCountOfWhyItemsInList(context, stats, item.getDescription()));
        }

        Collections.sort(list);
        Collections.reverse(list);

        return list;
    }

    private static int getCountOfWhyItemsInList(Context context, List<AdvStats> stats, String item) {
        int count = 0;
        for (AdvStats stat : stats) {
            if (stat.getWhyTypes().contains(MatchDialogHelperUtils.convertStringToWhyType(context, item)))
                count++;
        }

        return count;
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
            item.setCount(getCountOfSubTypesInList(context, stats, item.getDescription()));
        }

        return list;
    }

    private static int getCountOfSubTypesInList(Context context, List<AdvStats> stats, String item) {
        int count = 0;

        if (item.equals(context.getString(R.string.safety_open))) {
            for (AdvStats stat : stats)
                if (stat.getShotType() == AdvStats.ShotType.SAFETY_ERROR)
                    count++;
        } else
            for (AdvStats stat : stats)
                if (stat.getShotSubtype() == MatchDialogHelperUtils.convertStringToSubType(context, item))
                    count++;

        return count;
    }

    public static int getFailedSafeties(Context context, List<AdvStats> stats) {
        List<StatLineItem> safeties = getSafetyStats(context, stats);

        return safeties.get(OPEN).getCount();
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
