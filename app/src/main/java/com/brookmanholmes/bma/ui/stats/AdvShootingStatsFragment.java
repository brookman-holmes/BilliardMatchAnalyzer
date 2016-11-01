package com.brookmanholmes.bma.ui.stats;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
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

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class AdvShootingStatsFragment extends BaseAdvStatsFragment {
    private static final String TAG = "AdvShootingStatsFrag";

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
    @Bind(R.id.kickGraph)
    View kickGraph;
    @Bind(R.id.bankGraph)
    View bankGraph;
    @Bind(R.id.speedChart)
    BubbleChart speedChart;
    @Bind(R.id.distanceChart)
    BubbleChart distanceChart;
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
    @Bind(R.id.no_data)
    TextView noData;

    List<BubbleEntry> obEntries = new ArrayList<>();
    List<BubbleEntry> cbEntries = new ArrayList<>();
    List<BubbleEntry> speedEntries = new ArrayList<>();
    BubbleData speedData = new BubbleData(
            new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
    BubbleData distanceData = new BubbleData(
            new String[]{"<.5'", ".5'", "1'", "1.5'", "2'", "2.5'", "3'", "3.5'", "4'", "4.5'", "5'",
                    "5.5'", "6'", "6.5'", "7'", "7.5'", "8'", "8.5'", "9'", "9.5'"});
    private String shotType, subType, angle;
    private GetFilteredStatsAsync task2;

    public static AdvShootingStatsFragment create(Bundle args) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    public static AdvShootingStatsFragment create(String name) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        Bundle args = new Bundle();
        args.putString(AdvStatsDialog.ARG_PLAYER_NAME, name);
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

        setupDistanceDataSeries();
        setupSpeedDataSeries();
    }

    private void setupSpeedDataSeries() {
        for (int i = 0; i < 10; i++)
            speedEntries.add(new BubbleEntry(i, 0f, 0f));

        BubbleDataSet speedDataSet = new BubbleDataSet(speedEntries, getString(R.string.chart_speed_legend));
        setupDataSeries(speedDataSet);
        speedDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccentTransparent));
        speedData.addDataSet(speedDataSet);
    }

    private void setupDistanceDataSeries() {
        for (int i = 0; i < 20; i++) {
            cbEntries.add(new BubbleEntry(i, 0, 0));
            obEntries.add(new BubbleEntry(i, 1, 0));
        }

        BubbleDataSet obDataSet = new BubbleDataSet(obEntries, getString(R.string.chart_cue_legend));
        BubbleDataSet cbDataSet = new BubbleDataSet(cbEntries, getString(R.string.chart_object_legend));
        setupDataSeries(obDataSet);
        setupDataSeries(cbDataSet);
        obDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryTransparent));
        cbDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccentTransparent));

        distanceData.addDataSet(obDataSet);
        distanceData.addDataSet(cbDataSet);
    }

    private void setupDataSeries(BubbleDataSet dataSet) {
        dataSet.setValueTextSize(12);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value != 0f)
                    return ((int) value) + "";
                else return "";
            }
        });
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        LimitLine line0 = new LimitLine(0);
        LimitLine line1 = new LimitLine(1);
        line0.setLineColor(ContextCompat.getColor(getContext(), R.color.divider_light));
        line1.setLineColor(ContextCompat.getColor(getContext(), R.color.divider_light));

        setupChart(distanceChart);
        distanceChart.setData(distanceData);
        YAxis left = distanceChart.getAxisLeft();
        left.setAxisMinValue(-1f);
        left.setAxisMaxValue(2f);
        left.addLimitLine(line0);
        left.addLimitLine(line1);

        setupChart(speedChart);
        speedChart.getXAxis().setLabelsToSkip(0);
        speedChart.setData(speedData);
        speedChart.getAxisLeft().addLimitLine(line0);

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

    private void setupChart(BubbleChart chart) {
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.setDescription("");
        chart.invalidate();
        YAxis left = chart.getAxisLeft();
        left.setAxisMinValue(-1f);
        left.setAxisMaxValue(1f);
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(1);
        chart.getAxisRight().setEnabled(false); // no right axis
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
        setVisibilities();

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
        StatsUtils.setLayoutWeights(filteredStats, AIM_LEFT, AIM_RIGHT, leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(filteredStats, THIN, THICK, overCut, underCut);
        StatsUtils.setLayoutWeights(filteredStats, BANK_SHORT, BANK_LONG, bankShort, bankLong);
        StatsUtils.setLayoutWeights(filteredStats, KICK_SHORT, KICK_LONG, kickShort, kickLong);
        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(filteredStats)));

        title.setText(getString(R.string.title_shooting_errors, filteredStats.size()));

        // clear values
        for (BubbleEntry entry : obEntries)
            entry.setSize(0f);
        for (BubbleEntry entry : cbEntries)
            entry.setSize(0f);
        for (BubbleEntry entry : speedEntries)
            entry.setSize(0f);

        List<Point> points = new ArrayList<>();

        for (AdvStats stat : filteredStats) {
            updateEntrySize(obEntries.get(getIndex(stat.getObToPocket())));
            updateEntrySize(cbEntries.get(getIndex(stat.getCbToOb())));
            updateEntrySize(speedEntries.get(stat.getSpeed() - 1));

            points.add(new Point(stat.getCueX(), stat.getCueY()));
        }
        cueBallHeatGraph.setData(points);

        if (filteredStats.size() > 0) {
            speedChart.setVisibility(View.VISIBLE);
            distanceChart.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

            speedData.calcMinMax(0, speedData.getYValCount());
            speedChart.notifyDataSetChanged();
            speedChart.invalidate();

            distanceData.calcMinMax(0, distanceData.getYValCount());
            distanceChart.notifyDataSetChanged();
            distanceChart.invalidate();
        } else {
            // prevents the charts from showing with no data, which has a small bug with the entries being dumb
            speedChart.setVisibility(View.GONE);
            distanceChart.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void updateEntrySize(BubbleEntry entry) {
        float count = entry.getSize();
        entry.setSize(count + 1);
    }

    private int getIndex(float val) {
        return (int) ((val - .5f) * 2);
    }

    private void setVisibilities() {
        //showKickGraph();
        //showBankGraph();
        showShotSubTypeSpinner();
    }

    private void showKickGraph() {
        if (isKickShot())
            kickGraph.setVisibility(View.VISIBLE);
        else kickGraph.setVisibility(View.GONE);
    }

    private void showBankGraph() {
        if (isBankShot())
            bankGraph.setVisibility(View.VISIBLE);
        else bankGraph.setVisibility(View.GONE);
    }

    private void showShotSubTypeSpinner() {
        if (shotType.equals(getString(R.string.miss_cut))) {
            //shotSubTypeLayout.setVisibility(View.VISIBLE);
        } else {
            shotSubTypeSpinner.setSelection(0);
            //shotSubTypeLayout.setVisibility(View.GONE);
            subType = "All";
        }
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_adv_shooting_stats;
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
