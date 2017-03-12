package com.brookmanholmes.bma.ui.stats;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.brookmanholmes.bma.ui.view.HowMissLayout;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.Bind;
import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

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

    @Bind(R.id.hmlAim)
    HowMissLayout aim;
    @Bind(R.id.hmlCut)
    HowMissLayout cut;
    @Bind(R.id.hmlKick)
    HowMissLayout kick;
    @Bind(R.id.hmlBank)
    HowMissLayout bank;

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
    HeatGraph cueBallHeatGraph;

    private String shotType, cutTypes, angle;
    private ColumnChartData speedData, distanceData;
    private final AdapterView.OnItemSelectedListener shotTypeSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            shotType = ((ArrayAdapter<String>) shotTypeSpinner.getAdapter()).getItem(position);
            updateView();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private final AdapterView.OnItemSelectedListener subTypeSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cutTypes = ((ArrayAdapter<String>) shotSubTypeSpinner.getAdapter()).getItem(position);
            updateView();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private final AdapterView.OnItemSelectedListener angleSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            angle = ((ArrayAdapter<String>) angleSpinner.getAdapter()).getItem(position);
            updateView();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
        if (shotType.equals(getString(R.string.all)))
            return ArrayUtils.contains(AdvStats.ShotType.getShots(), stat.getShotType());
        else
            return stat.getShotType() == MatchDialogHelperUtils.convertStringToShotType(getContext(), shotType);
    }

    private boolean isSubType(AdvStats stat) {
        // all has to go first otherwise it throws an illegal argument exception
        return cutTypes.equals(getString(R.string.all)) || stat.getShotSubtype() == MatchDialogHelperUtils.convertStringToSubType(getContext(), cutTypes);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (shotType == null)
            shotType = getString(R.string.all);
        if (cutTypes == null)
            cutTypes = getString(R.string.all);
        if (angle == null)
            angle = getString(R.string.all);

        List<AxisValue> speedAxisValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AxisValue value = new AxisValue(i);
            value.setLabel(String.valueOf(i + 1));
            speedAxisValues.add(value);
        }

        List<AxisValue> distanceAxisValues = new ArrayList<>();
        for (float value = 0f; value < 20; value += .5f) {
            distanceAxisValues.add(new AxisValue(value));
        }

        speedData = createColumnData(createAxis(speedAxisValues, "", new SpeedAxisValueFormatter(0)), false, 10);
        distanceData = createColumnData(createAxis(distanceAxisValues, "", new DistanceAxisValueFormatter(1).setAppendedText("'".toCharArray())), true, 20);

        setData(stats);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        speedChart.setColumnChartData(speedData);
        distanceChart.setColumnChartData(distanceData);

        distanceChart.setZoomEnabled(false);
        speedChart.setZoomEnabled(false);
        distanceChart.setViewportCalculationEnabled(false);

        shotTypeSpinner.setAdapter(createAdapter(new ArrayList<String>()));
        shotSubTypeSpinner.setAdapter(createAdapter(new ArrayList<String>()));
        angleSpinner.setAdapter(createAdapter(new ArrayList<String>()));
        setSpinnerItems();

        shotTypeSpinner.setOnItemSelectedListener(shotTypeSpinnerListener);
        shotSubTypeSpinner.setOnItemSelectedListener(subTypeSpinnerListener);
        angleSpinner.setOnItemSelectedListener(angleSpinnerListener);
        return view;
    }

    private ColumnChartData createColumnData(Axis axis, boolean stacked, int columnCount) {
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            columns.add(new Column());
        }

        for (Column column : columns) {
            List<SubcolumnValue> values = new ArrayList<>();
            values.add(new MySubColumnValue(0f, getColor(R.color.colorPrimary)));
            if (stacked)
                values.add(new MySubColumnValue(0f, getColor(R.color.colorTertiary)));
            column.setValues(values).setHasLabels(true);
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setFillRatio(.8f)
                .setStacked(stacked)
                .setAxisXBottom(axis);
        data.setValueLabelBackgroundAuto(true);
        data.setValueLabelTextSize(10);
        return data;
    }

    private Axis createAxis(List<AxisValue> values, String title, AxisValueFormatter formatter) {
        Axis axis = new Axis(values);
        axis.setHasLines(true)
                .setHasSeparationLine(true)
                .setFormatter(formatter)
                .setTextColor(getColor(R.color.primary_text))
                .setName(title);
        return axis;
    }

    private void setData(Collection<AdvStats> stats) {
        // clear values
        for (Column column : speedData.getColumns()) {
            for (SubcolumnValue value : column.getValues())
                value.setValue(0f);
        }

        float max = 0;
        for (Column column : distanceData.getColumns()) {
            for (SubcolumnValue value : column.getValues())
                value.setValue(0f);
        }

        for (AdvStats stat : stats) {
            if (stat.getSpeed() > 0) {
                float val = speedData.getColumns().get(stat.getSpeed() - 1).getValues().get(0).getValue();
                speedData.getColumns().get(stat.getSpeed() - 1).getValues().get(0).setValue(val + 1);
            }

            int cbCol = (int) (stat.getCbToOb() * 2);
            int obCol = (int) (stat.getObToPocket() * 2);

            if (stat.getCbToOb() >= 0) {
                SubcolumnValue value = distanceData.getColumns().get(cbCol).getValues().get(0);
                float newVal = value.getValue() + 1;
                max = max < newVal ? newVal : max;
                value.setValue(newVal);
            }

            if (stat.getObToPocket() >= 0) {
                SubcolumnValue value = distanceData.getColumns().get(obCol).getValues().get(1);
                float newVal = value.getValue() - 1;
                max = max < newVal ? newVal : max;
                value.setValue(newVal);
            }
        }

        if (distanceChart != null) {
            final Viewport viewPort = new Viewport();
            viewPort.set(-.5f, max, 19.5f, -max);
            distanceChart.setCurrentViewport(viewPort);
            distanceChart.setMaximumViewport(viewPort);
        }

    }

    private void setSpinnerItems() {
        SortedSet<String> shotTypes = new TreeSet<>();
        SortedSet<String> cutTypes = new TreeSet<>();
        SortedSet<String> angles = new TreeSet<>();

        for (AdvStats stat : stats) {
            shotTypes.add(getString(MatchDialogHelperUtils.convertShotTypeToStringRes(stat.getShotType())));
            cutTypes.add(getString(MatchDialogHelperUtils.convertSubTypeToStringRes(stat.getShotSubtype())));
            for (AdvStats.Angle angle : stat.getAngles())
                angles.add(getString(MatchDialogHelperUtils.convertAngleToStringRes(angle)));
        }

        String emptyString = getString(R.string.empty_string);
        String all = getString(R.string.all);
        String none = getString(R.string.none);

        List<String> possibleShotTypes, possibleAngles, possibleCutTypes;

        possibleShotTypes = new ArrayList<>(shotTypes);
        possibleShotTypes.add(0, all);
        possibleShotTypes.remove(emptyString);
        possibleShotTypes.remove(none);

        possibleCutTypes = new ArrayList<>(cutTypes);
        possibleCutTypes.add(0, all);
        possibleCutTypes.remove(emptyString);
        possibleCutTypes.remove(none);

        possibleAngles = new ArrayList<>(angles);
        possibleAngles.add(0, all);
        possibleAngles.remove(emptyString);
        possibleAngles.remove(none);

        setItems(shotTypeSpinner, possibleShotTypes);
        setItems(shotSubTypeSpinner, possibleCutTypes);
        setItems(angleSpinner, possibleAngles);
    }

    private void setItems(Spinner spinner, Collection<String> items) {
        ((ArrayAdapter<String>) spinner.getAdapter()).clear();
        ((ArrayAdapter<String>) spinner.getAdapter()).addAll(items);
    }

    private ArrayAdapter<String> createAdapter(Collection<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(data));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    void updateView() {
        setSpinnerItems();
        setShotSubType();

        updateView(getFilteredStats());
    }

    private void updateView(List<AdvStats> filteredStats) {
        aim.setWeights(StatsUtils.getHowError(filteredStats, AIM_LEFT, AIM_RIGHT));
        cut.setWeights(StatsUtils.getHowError(filteredStats, THIN, THICK));
        kick.setWeights(StatsUtils.getHowError(filteredStats, KICK_SHORT, KICK_LONG));
        bank.setWeights(StatsUtils.getHowError(filteredStats, BANK_SHORT, BANK_LONG));

        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(filteredStats)));
        title.setText(getString(R.string.title_shooting_errors, filteredStats.size()));

        List<Point> points = new ArrayList<>();

        for (AdvStats stat : filteredStats) {
            if (stat.getCueX() > -200 && stat.getCueY() > -200) // make sure it's a valid point
                points.add(new Point(stat.getCueX(), stat.getCueY()));
        }

        setData(filteredStats);
        speedChart.setColumnChartData(speedData);
        distanceChart.setColumnChartData(distanceData);
        cueBallHeatGraph.setData(points);
    }

    private void setShotSubType() {
        if (!shotType.equals(getString(R.string.miss_cut)) && !shotType.equals(getString(R.string.all))) {
            shotSubTypeSpinner.setSelection(0);
            cutTypes = getString(R.string.all);
        }
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_adv_shooting_stats;
    }

    @Override
    AdvStats.ShotType[] getShotTypes() {
        return AdvStats.ShotType.getShots();
    }

    private static class MySubColumnValue extends SubcolumnValue {
        private static NumberFormat formatter = DecimalFormat.getNumberInstance();

        public MySubColumnValue(float value, int color) {
            super(value, color);
        }

        @Override
        public int getDarkenColor() {
            return getColor();
        }

        @Override
        public SubcolumnValue setValue(float value) {
            setLabel(formatter.format(Math.abs(value)));
            return super.setValue(value);
        }
    }
}
