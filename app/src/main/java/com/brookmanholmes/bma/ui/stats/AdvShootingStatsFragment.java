package com.brookmanholmes.bma.ui.stats;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.view.HeatGraphV2;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.Bind;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

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
    ColumnChartView speedChart;
    @Bind(R.id.distanceChart)
    ColumnChartView distanceChart;
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
    HeatGraphV2 cueBallHeatGraph;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        speedChart.setColumnChartData(getSpeedData(stats));
        distanceChart.setColumnChartData(getDistanceData(stats));

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

    private ColumnChartData getSpeedData(List<AdvStats> stats) {
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            columns.add(new Column());

        for (Column column : columns) {
            List<SubcolumnValue> values = new ArrayList<>();
            values.add(new MySubColumnValue(0f, getColor(R.color.colorAccent)));
            column.setValues(values)
                    .setHasLabels(true);
        }

        for (AdvStats stat : stats) {
            if (stat.getSpeed() > 0) {
                float val = columns.get(stat.getSpeed() - 1).getValues().get(0).getValue();
                columns.get(stat.getSpeed() - 1).getValues().get(0).setValue(val + 1);
            }
        }

        Axis xAxis = new Axis();

        xAxis.setHasLines(true)
                .setInside(false)
                .setHasSeparationLine(true)
                .setFormatter(new SpeedAxisValueFormatter(0))
                .setTextColor(getColor(R.color.primary_text))
                .setName("Speed");

        ColumnChartData data = new ColumnChartData(columns);
        data.setFillRatio(.8f)
                .setStacked(false)
                .setAxisXBottom(xAxis);
        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelBackgroundColor(getColor(R.color.colorAccent));

        return data;
    }

    private ColumnChartData getDistanceData(List<AdvStats> stats) {
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            columns.add(new Column());

        for (int i = 0; i < columns.size(); i++) {
            List<SubcolumnValue> values = new ArrayList<>();
            values.add(new MySubColumnValue(0f, getColor(R.color.colorAccent)));
            values.add(new MySubColumnValue(0f, getColor(R.color.colorPrimary)));

            columns.get(i).setValues(values)
                    .setHasLabels(true);
        }

        for (AdvStats stat : stats) {
            int cbCol = (int) (stat.getCbToOb() * 2);
            int obCol = (int) (stat.getObToPocket() * 2);

            if (stat.getCbToOb() >= 0) {
                SubcolumnValue value = columns.get(cbCol).getValues().get(0);
                float newVal = value.getValue() + 1;
                value.setValue(newVal);
            }

            if (stat.getObToPocket() >= 0) {
                SubcolumnValue value = columns.get(obCol).getValues().get(1);
                float newVal = value.getValue() + 1;
                value.setValue(newVal);
            }
        }

        Axis xAxis = new Axis();

        xAxis.setHasLines(true)
                .setInside(false)
                .setFormatter(new DistanceAxisValueFormatter(0).setAppendedText("'".toCharArray()))
                .setHasSeparationLine(true)
                .setTextColor(getColor(R.color.primary_text));

        ColumnChartData data = new ColumnChartData(columns);
        data.setFillRatio(.8f)
                .setStacked(true)
                .setAxisXBottom(xAxis);
        data.setValueLabelBackgroundAuto(true);
        data.setValueLabelTextSize(10);

        return data;
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

        speedChart.setColumnChartData(getSpeedData(filteredStats));
        distanceChart.setColumnChartData(getDistanceData(filteredStats));
        cueBallHeatGraph.setData(points);
    }

    private void setShotSubType() {
        if (!shotType.equals(getString(R.string.miss_cut)) && !shotType.equals(getString(R.string.all))) {
            shotSubTypeSpinner.setSelection(0);
            subType = getString(R.string.all);
        }
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_adv_shooting_stats;
    }

    private static class MySubColumnValue extends SubcolumnValue {
        private static NumberFormat formatter = DecimalFormat.getNumberInstance();

        public MySubColumnValue() {
            super();
        }

        public MySubColumnValue(float value) {
            super(value);
        }

        public MySubColumnValue(float value, int color) {
            super(value, color);
        }

        public MySubColumnValue(SubcolumnValue columnValue) {
            super(columnValue);
        }

        @Override
        public int getDarkenColor() {
            return getColor();
        }

        @Override
        public SubcolumnValue setValue(float value) {
            setLabel(formatter.format(value));
            return super.setValue(value);
        }
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
