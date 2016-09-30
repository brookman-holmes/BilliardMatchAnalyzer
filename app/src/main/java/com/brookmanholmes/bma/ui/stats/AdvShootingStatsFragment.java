package com.brookmanholmes.bma.ui.stats;

import android.graphics.Point;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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
    @Bind(R.id.barChart)
    BarChart chart;
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
    List<BarEntry> obEntries = new ArrayList<>();
    List<BarEntry> cbEntries = new ArrayList<>();
    BarData barData = new BarData(new String[]{"6\"", "1'", "2'", "3'", "4'", "5'", "6'", "7'", "8'", "9'"});
    private String shotType = "All", subType = "All", angle = "All";
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

        cbEntries.add(new BarEntry(0, 0));
        cbEntries.add(new BarEntry(0, 1));
        cbEntries.add(new BarEntry(0, 2));
        cbEntries.add(new BarEntry(0, 3));
        cbEntries.add(new BarEntry(0, 4));
        cbEntries.add(new BarEntry(0, 5));
        cbEntries.add(new BarEntry(0, 6));
        cbEntries.add(new BarEntry(0, 7));
        cbEntries.add(new BarEntry(0, 8));
        cbEntries.add(new BarEntry(0, 9));

        obEntries.add(new BarEntry(0, 0));
        obEntries.add(new BarEntry(0, 1));
        obEntries.add(new BarEntry(0, 2));
        obEntries.add(new BarEntry(0, 3));
        obEntries.add(new BarEntry(0, 4));
        obEntries.add(new BarEntry(0, 5));
        obEntries.add(new BarEntry(0, 6));
        obEntries.add(new BarEntry(0, 7));
        obEntries.add(new BarEntry(0, 8));
        obEntries.add(new BarEntry(0, 9));

        BarDataSet obDataSet = new BarDataSet(obEntries, "Cue ball to object distance");
        BarDataSet cbDataSet = new BarDataSet(cbEntries, "Object ball to pocket distance");

        obDataSet.setDrawValues(false);
        obDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        cbDataSet.setDrawValues(false);
        cbDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        barData.addDataSet(obDataSet);
        barData.addDataSet(cbDataSet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        chart.setData(barData);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        chart.setDescription("");
        chart.invalidate();

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
        for (BarEntry entry : obEntries)
            entry.setVal(0f);
        for (BarEntry entry : cbEntries)
            entry.setVal(0f);

        for (AdvStats stat : filteredStats) {
            BarEntry obEntry = obEntries.get(getIndex(stat.getObToPocket()));
            float obCount = obEntry.getVal();
            obEntry.setVal(obCount + 1);

            BarEntry cbEntry = cbEntries.get(getIndex(stat.getCbToOb()));
            float cbCount = cbEntry.getVal();
            cbEntry.setVal(cbCount + 1);

            cueBallHeatGraph.addDataPoint(new Point(stat.getCueX(), stat.getCueY()));
        }
        cueBallHeatGraph.reDraw();


        barData.calcMinMax(0, barData.getYValCount());
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private int getIndex(float val) {
        return (int) Math.floor(val);
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
