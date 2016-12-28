package com.brookmanholmes.bma.ui.stats;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.view.HeatGraph;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.Bind;

import static com.brookmanholmes.billiards.turn.AdvStats.HowType.AIM_LEFT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.AIM_RIGHT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.BANK_LONG;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.BANK_SHORT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_LONG;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.KICK_SHORT;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.THICK;
import static com.brookmanholmes.billiards.turn.AdvStats.HowType.THIN;
import static com.brookmanholmes.bma.ui.stats.AdvStatsDialog.ARG_MATCH_ID;
import static com.brookmanholmes.bma.ui.stats.AdvStatsDialog.ARG_PLAYER_NAME;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvShootingStatsFragment extends BaseAdvStatsFragment {
    private static final String TAG = "AdvShootingStatsFrag";
    private final StatGetter speedGetter = new StatGetter() {
        @Override
        public float getStat(AdvStats stat) {
            return stat.getSpeed();
        }
    };
    private final StatGetter cbGetter = new StatGetter() {
        @Override
        public float getStat(AdvStats stat) {
            return stat.getCbToOb();
        }
    };
    private final StatGetter obGetter = new StatGetter() {
        @Override
        public float getStat(AdvStats stat) {
            return stat.getObToPocket();
        }
    };
    @Bind(R.id.over)
    TextView overCut;
    @Bind(R.id.under)
    TextView underCut;
    @Bind(R.id.left)
    TextView leftOfAim;
    @Bind(R.id.right)
    TextView rightOfAim;
    @Bind(R.id.bankLong)
    TextView bankLong;
    @Bind(R.id.bankShort)
    TextView bankShort;
    @Bind(R.id.kickLong)
    TextView kickLong;
    @Bind(R.id.kickShort)
    TextView kickShort;
    @Bind(R.id.speedChart)
    BarChart speedChart;
    @Bind(R.id.distanceChart)
    BarChart distanceChart;
    @Bind(R.id.shootingErrorTitle)
    TextView title;
    @Bind(R.id.shotTypeSpinner)
    Spinner shotTypeSpinner;
    @Bind(R.id.shotSubTypeSpinner)
    Spinner shotSubTypeSpinner;
    @Bind(R.id.shotSubTypeLayout)
    View shotSubTypeLayout;
    @Bind(R.id.angleSpinner)
    Spinner angleSpinner;
    @Bind(R.id.miscues)
    TextView miscues;
    @Bind(R.id.heatGraph)
    HeatGraph cueBallHeatGraph;
    private BarData speedData = new BarData(), distanceData = new BarData();
    private String shotType, subType, angle;
    private GetFilteredStatsAsync task2;

    public static AdvShootingStatsFragment create(String name, long matchId) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER_NAME, name);
        args.putLong(ARG_MATCH_ID, matchId);
        frag.setArguments(args);
        return frag;
    }

    public static AdvShootingStatsFragment create(Bundle args) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    public static AdvShootingStatsFragment create(String name) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER_NAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    String[] getShotTypes() {
        return AdvStats.ShotType.getShots();
    }

    private List<AdvStats> getFilteredStats() {
        List<AdvStats> list = new ArrayList<>();

        for (AdvStats stat : stats) {
            if (isShotType(stat)) {
                if (isSubType(stat)) {
                    if (isAngle(stat))
                        list.add(stat);
                }
            }
        }

        return list;
    }

    private boolean isShotType(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        return shotType.equals(getString(R.string.all)) || stat.getShotType() == MatchDialogHelperUtils.convertStringToShotType(getContext(), shotType);
    }

    private boolean isSubType(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        return subType.equals(getString(R.string.all)) || stat.getShotSubtype() == MatchDialogHelperUtils.convertStringToSubType(getContext(), subType);
    }

    private boolean isAngle(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        if (angle.equals(getString(R.string.angle_shallow)))
            return stat.getAngles().contains(MatchDialogHelperUtils.convertStringToAngle(getContext(), angle)) ||
                    stat.getAngles().contains(AdvStats.Angle.TEN) ||
                    stat.getAngles().contains(AdvStats.Angle.FIFTEEN) ||
                    stat.getAngles().contains(AdvStats.Angle.TWENTY) ||
                    stat.getAngles().contains(AdvStats.Angle.TWENTY_FIVE);
        else if (angle.equals(getString(R.string.angle_medium)))
            return stat.getAngles().contains(MatchDialogHelperUtils.convertStringToAngle(getContext(), angle)) ||
                    stat.getAngles().contains(AdvStats.Angle.THIRTY) ||
                    stat.getAngles().contains(AdvStats.Angle.THIRTY_FIVE) ||
                    stat.getAngles().contains(AdvStats.Angle.FORTY) ||
                    stat.getAngles().contains(AdvStats.Angle.FORTY_FIVE) ||
                    stat.getAngles().contains(AdvStats.Angle.FIFTY) ||
                    stat.getAngles().contains(AdvStats.Angle.FIFTY_FIVE);
        else if (angle.equals(getString(R.string.angle_steep)))
            return stat.getAngles().contains(MatchDialogHelperUtils.convertStringToAngle(getContext(), angle)) ||
                    stat.getAngles().contains(AdvStats.Angle.SIXTY) ||
                    stat.getAngles().contains(AdvStats.Angle.SIXTY_FIVE) ||
                    stat.getAngles().contains(AdvStats.Angle.SEVENTY) ||
                    stat.getAngles().contains(AdvStats.Angle.SEVENTY_FIVE) ||
                    stat.getAngles().contains(AdvStats.Angle.EIGHTY);
        else
            return angle.equals(getString(R.string.all)) || stat.getAngles().contains(MatchDialogHelperUtils.convertStringToAngle(getContext(), angle));
    }

    private boolean isKickShot() {
        return shotType.equals(getString(R.string.miss_kick)) || shotType.equals("All");
    }

    private boolean isBankShot() {
        return shotType.equals(getString(R.string.miss_bank)) || shotType.equals("All");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (shotType == null)
            shotType = getString(R.string.all);
        if (subType == null)
            subType = getString(R.string.all);
        if (angle == null)
            angle = getString(R.string.all);

        Utils.init(getContext()); // call this so that text size is converted to DP correctly
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setupChart(speedChart, 10f);
        speedChart.setFitBars(true);
        speedChart.getXAxis().setAxisMinimum(1f);
        setupChart(distanceChart, 9.5f);
        speedChart.getXAxis().setValueFormatter(new DefaultAxisValueFormatter(0));
        distanceChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (Float.compare(value, 0) == 0)
                    return "<.5'";
                else if (Float.compare(value, .5f) == 0)
                    return ".5'";
                else if (Float.compare(value, 1f) == 0)
                    return "1'";
                else return new DecimalFormat("#.#").format(value) + "'";
            }
        });

        displayChartData(stats);

        shotTypeSpinner.setAdapter(createAdapter(getPossibleShotTypes()));
        shotSubTypeSpinner.setAdapter(createAdapter(getPossibleShotSubTypes()));
        angleSpinner.setAdapter(createAdapter(getPossibleAngles()));

        shotTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shotType = getAdapter(shotTypeSpinner).getItem(position);
                updateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shotSubTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subType = getAdapter(shotSubTypeSpinner).getItem(position);
                updateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        angleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                angle = getAdapter(angleSpinner).getItem(position);
                updateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private BarDataSet getDataSet(@StringRes int label, @ColorRes int color, List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, getString(label));
        dataSet.setValueFormatter(new DefaultValueFormatter(0));
        dataSet.setValueTextSize(12);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        dataSet.setColor(ContextCompat.getColor(getContext(), color));
        return dataSet;
    }

    private void setupChart(BarChart chart) {
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setTouchEnabled(false);
        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(10);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
        chart.getAxisRight().setEnabled(false); // no right axis
    }

    private void setupChart(BarChart chart, float xMax) {
        setupChart(chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(xMax);
    }

    private List<BarEntry> getEntries(List<AdvStats> stats, StatGetter statGetter) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (AdvStats stat : stats) {
            boolean hasEntry = false;
            for (BarEntry entry : entries) {
                if (Float.compare(entry.getX(), statGetter.getStat(stat)) == 0) {
                    hasEntry = true;
                    entry.setY(entry.getY() + 1);
                }
            }

            if (!hasEntry && statGetter.getStat(stat) >= 0)
                entries.add(new BarEntry(statGetter.getStat(stat), 1));

        }

        Collections.sort(entries, new EntryXComparator());
        Log.i(TAG, "getEntries: " + entries);
        return entries;
    }

    @Override
    public void onDestroy() {
        if (task2 != null)
            task2.cancel(true);
        super.onDestroy();
    }

    private List<String> getPossibleShotTypes() {
        SortedSet<String> shotTypes = new TreeSet<>();
        for (AdvStats stat : stats) {
            shotTypes.add(getString(MatchDialogHelperUtils.convertShotTypeToStringRes(stat.getShotType())));
        }
        shotTypes.add(getString(R.string.all));

        return new ArrayList<>(shotTypes);
    }

    private List<String> getPossibleShotSubTypes() {
        SortedSet<String> shotSubTypes = new TreeSet<>();
        for (AdvStats stat : stats) {
            if (isShotType(stat))
                shotSubTypes.add(getString(MatchDialogHelperUtils.convertSubTypeToStringRes(stat.getShotSubtype())));
        }
        shotSubTypes.add(getString(R.string.all));
        shotSubTypes.remove(getString(R.string.empty_string));

        return new ArrayList<>(shotSubTypes);
    }

    private List<String> getPossibleAngles() {
        SortedSet<String> angles = new TreeSet<>();
        for (AdvStats stat : stats) {
            if ((isShotType(stat)) && (isSubType(stat))) {
                for (AdvStats.Angle angle : stat.getAngles())
                    angles.add(getString(MatchDialogHelperUtils.convertAngleToStringRes(angle)));
            }
        }

        List<String> list = new ArrayList<>(angles);
        list.add(0, getString(R.string.all));

        return list;
    }

    private ArrayAdapter<String> createAdapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void setItems(Spinner spinner, List<String> items) {
        getAdapter(spinner).clear();
        getAdapter(spinner).addAll(items);
    }

    private ArrayAdapter<String> getAdapter(Spinner spinner) {
        return (ArrayAdapter<String>) spinner.getAdapter();
    }

    @Override
    void updateView() {
        setItems(shotTypeSpinner, getPossibleShotTypes());
        setItems(shotSubTypeSpinner, getPossibleShotSubTypes());
        setItems(angleSpinner, getPossibleAngles());
        setShotSubType();

        if (task2 == null) {
            task2 = new GetFilteredStatsAsync();
            task2.execute();
        }

        if (task2.getStatus() != AsyncTask.Status.PENDING) {
            task2.cancel(true);
            task2 = new GetFilteredStatsAsync();
            task2.execute();
        }
    }

    private void updateView(List<AdvStats> filteredStats) {
        speedData.clearValues();
        distanceData.clearValues();

        displayChartData(filteredStats);

        TransitionManager.beginDelayedTransition(baseLayout);
        StatsUtils.setLayoutWeights(filteredStats, AIM_LEFT, AIM_RIGHT, leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(filteredStats, THIN, THICK, overCut, underCut);
        StatsUtils.setLayoutWeights(filteredStats, BANK_SHORT, BANK_LONG, bankShort, bankLong);
        StatsUtils.setLayoutWeights(filteredStats, KICK_SHORT, KICK_LONG, kickShort, kickLong);
        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(filteredStats)));

        title.setText(getString(R.string.title_shooting_errors, filteredStats.size()));

        List<Point> points = new ArrayList<>();

        for (AdvStats stat : filteredStats) {
            if (stat.getCueX() > -200 && stat.getCueY() > -200) // make sure it's a valid point
                points.add(new Point(stat.getCueX(), stat.getCueY()));
        }

        cueBallHeatGraph.setData(points);
    }

    private void displayChartData(List<AdvStats> stats) {
        Log.i(TAG, "displayChartData: " + stats);
        speedData = new BarData(
                getDataSet(R.string.chart_speed_legend, R.color.colorAccentTransparent, getEntries(stats, speedGetter)));
        distanceData = new BarData(
                getDataSet(R.string.chart_cue_legend, R.color.colorPrimaryTransparent, getEntries(stats, cbGetter)),
                getDataSet(R.string.chart_object_legend, R.color.colorAccentTransparent, getEntries(stats, obGetter)));

        distanceData.setBarWidth(.15f);

        speedChart.setData(speedData);
        distanceChart.setData(distanceData);
        distanceChart.groupBars(0f, .1f, .05f);
        distanceChart.setFitBars(true);
        speedChart.setFitBars(true);
        speedChart.invalidate();
        distanceChart.invalidate();
    }

    private void setShotSubType() {
        if (!shotType.equals(getString(R.string.miss_cut))) {
            shotSubTypeSpinner.setSelection(0);
            subType = getString(R.string.all);
        }
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_adv_shooting_stats;
    }

    private interface StatGetter {
        float getStat(AdvStats stat);
    }

    private class GetFilteredStatsAsync extends AsyncTask<Void, Void, List<AdvStats>> {
        @Override
        protected void onPostExecute(List<AdvStats> stats) {
            if (isAdded() && !isCancelled())
                updateView(stats);
        }

        @Override
        protected List<AdvStats> doInBackground(Void... params) {
            return getFilteredStats();
        }
    }
}
