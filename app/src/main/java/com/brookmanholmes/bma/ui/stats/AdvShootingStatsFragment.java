package com.brookmanholmes.bma.ui.stats;

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

import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
@SuppressWarnings("WeakerAccess")
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
    @Bind(R.id.miscues) TextView miscues;
    private String shotType = "All", subType = "All", angle = "All";

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

    @Override String[] getShotTypes() {
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

    @Override void updateView() {
        setItems(shotTypeSpinner, getPossibleShotTypes());
        setItems(shotSubTypeSpinner, getPossibleShotSubTypes());
        setItems(angleSpinner, getPossibleAngles());

        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getFilteredStats()), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getFilteredStats()), overCut, underCut);
        StatsUtils.setLayoutWeights(StatsUtils.getHowBankErrors(getFilteredStats()), bankShort, bankLong);
        StatsUtils.setLayoutWeights(StatsUtils.getHowKickErrors(getFilteredStats()), kickShort, kickLong);
        miscues.setText(getString(R.string.title_miscues, StatsUtils.getMiscues(getFilteredStats())));
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
