package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvShootingStatsFragment extends BaseAdvStatsFragment {
    @Bind(R.id.over) TextView overCut;
    @Bind(R.id.under) TextView underCut;
    @Bind(R.id.left) TextView leftOfAim;
    @Bind(R.id.right) TextView rightOfAim;
    @Bind(R.id.bankLong) TextView bankLong;
    @Bind(R.id.bankShort) TextView bankShort;
    @Bind(R.id.kickLong) TextView kickLong;
    @Bind(R.id.kickShort) TextView kickShort;
    @Bind(R.id.kickGraph) View kickGraph;
    @Bind(R.id.bankGraph) View bankGraph;
    @Bind(R.id.shootingErrorTitle) TextView title;
    @Bind(R.id.shotTypeSpinner) Spinner shotTypeSpinner;
    @Bind(R.id.shotSubTypeSpinner) Spinner shotSubTypeSpinner;
    @Bind(R.id.shotSubTypeLayout) View shotSubTypeLayout;
    @Bind(R.id.angleSpinner) Spinner angleSpinner;
    String shotType = "All", subType = "All", angle = "All";

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

    @Override List<String> getShotTypes() {
        return Arrays.asList(getContext().getResources().getStringArray(R.array.shot_types));
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
        return stat.getShotType().equals(shotType) || shotType.equals("All");
    }

    private boolean isSubType(AdvStats stat) {
        return stat.getShotSubtype().equals(subType) || subType.equals("All");
    }

    private boolean isAngle(AdvStats stat) {
        return stat.getAngles().contains(angle) || angle.equals("All");
    }

    private boolean isKickShot() {
        return shotType.equals(getString(R.string.miss_kick)) || shotType.equals("All");
    }

    private boolean isBankShot() {
        return shotType.equals(getString(R.string.miss_bank)) || shotType.equals("All");
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        shotTypeSpinner.setAdapter(createAdapter(getPossibleShotTypes()));
        shotSubTypeSpinner.setAdapter(createAdapter(getPossibleShotSubTypes()));
        angleSpinner.setAdapter(createAdapter(getPossibleAngles()));


        shotTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shotType = getAdapter(shotTypeSpinner).getItem(position);
                updateView();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shotSubTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subType = getAdapter(shotSubTypeSpinner).getItem(position);
                updateView();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        angleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                angle = getAdapter(angleSpinner).getItem(position);
                updateView();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private List<String> getPossibleShotTypes() {
        SortedSet<String> shotTypes = new TreeSet<>();
        for (AdvStats stat : stats) {
            shotTypes.add(stat.getShotType());
        }
        shotTypes.add("All");

        return new ArrayList<>(shotTypes);
    }


    private List<String> getPossibleShotSubTypes() {
        SortedSet<String> shotSubTypes = new TreeSet<>();
        for (AdvStats stat : stats) {
            if (isShotType(stat))
                shotSubTypes.add(stat.getShotSubtype());
        }
        shotSubTypes.add("All");
        shotSubTypes.remove("");

        return new ArrayList<>(shotSubTypes);
    }

    private List<String> getPossibleAngles() {
        SortedSet<String> angles = new TreeSet<>();
        for (AdvStats stat : stats) {
            if ((isShotType(stat)) && (isSubType(stat)))
                angles.addAll(stat.getAngles());
        }

        List<String> list = new ArrayList<>(angles);
        list.add(0, "All");

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

    @Override void updateView() {
        setItems(shotTypeSpinner, getPossibleShotTypes());
        setItems(shotSubTypeSpinner, getPossibleShotSubTypes());
        setItems(angleSpinner, getPossibleAngles());

        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getContext(), getFilteredStats()), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), getFilteredStats()), overCut, underCut);
        StatsUtils.setLayoutWeights(StatsUtils.getHowBankErrors(getContext(), getFilteredStats()), bankShort, bankLong);
        StatsUtils.setLayoutWeights(StatsUtils.getHowKickErrors(getContext(), getFilteredStats()), kickShort, kickLong);

        setVisibilities();

        title.setText(getString(R.string.title_shooting_errors, getFilteredStats().size()));

        if (statsLayout != null)
            StatsUtils.setListOfMissReasons(statsLayout, getFilteredStats());
        else Log.i("ASSF", "View is null");
    }

    private void setVisibilities() {
        showKickGraph();
        showBankGraph();
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
            shotSubTypeLayout.setVisibility(View.VISIBLE);
        } else {
            shotSubTypeSpinner.setSelection(0);
            shotSubTypeLayout.setVisibility(View.GONE);
            subType = "All";
        }
    }

    @Override int getLayoutId() {
        return R.layout.fragment_adv_shooting_stats;
    }
}
