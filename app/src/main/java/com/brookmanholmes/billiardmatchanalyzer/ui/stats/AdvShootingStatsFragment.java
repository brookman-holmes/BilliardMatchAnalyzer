package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.utils.MultiSelectionSpinner;
import com.brookmanholmes.billiards.turn.AdvStats;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvShootingStatsFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    String[] shotTypes = new String[]{"Cut", "Long straight shot", "Bank", "Kick", "Combo", "Carom", "Jump"};
    String[] angleList = new String[]{"0°", "15°", "30°", "45°", "60°", "75°", "90°"};
    String[] howList = new String[]{"Too thin", "Too thick", "Left of aim point", "Right of aim point"};
    String[] whyList = new String[]{"Bad position", "Jacked up", "Lack of focus", "Over spin", "Unintentional english", "Too slow", "Too fast", "CB curved", "On the rail", "Forcing position"};
    List<AdvStats> stats = new ArrayList<>();
    @Bind(R.id.spinner) MultiSelectionSpinner spinner1;
    @Bind(R.id.spinner2) MultiSelectionSpinner spinner2;
    @Bind(R.id.spinner3) MultiSelectionSpinner spinner3;
    @Bind(R.id.spinner4) MultiSelectionSpinner spinner4;
    @Bind(R.id.over) TextView overCut;
    @Bind(R.id.under) TextView underCut;
    @Bind(R.id.left) TextView leftOfAim;
    @Bind(R.id.right) TextView rightOfAim;
    @Bind(R.id.shootingErrorTitle) TextView title;

    public static AdvShootingStatsFragment create(Bundle args) {
        AdvShootingStatsFragment frag = new AdvShootingStatsFragment();
        frag.setArguments(args);

        return frag;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseAdapter db = new DatabaseAdapter(getContext());
        db.open();

        long matchId = getArguments().getLong(AdvStatsDialog.ARG_MATCH_ID);
        String playerName = getArguments().getString(AdvStatsDialog.ARG_PLAYER_NAME);
        String[] shotTypes = getContext().getResources().getStringArray(R.array.shot_types);
        stats = db.getAdvStats(matchId, playerName, shotTypes);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_adv_shooting_stats, container, false);
        ButterKnife.bind(this, view);

        updateView(view);

        spinner1.setItems(shotTypes);
        spinner2.setItems(angleList);
        spinner3.setItems(howList);
        spinner4.setItems(whyList);
        spinner1.setListener(this);
        spinner2.setListener(this);
        spinner3.setListener(this);
        spinner4.setListener(this);

        return view;
    }

    public void updateView(View view) {
        StatsUtils.setLayoutWeights(StatsUtils.getHowAimErrors(getContext(), stats), leftOfAim, rightOfAim);
        StatsUtils.setLayoutWeights(StatsUtils.getHowCutErrors(getContext(), stats), overCut, underCut);

        title.setText("Shooting errors (" + stats.size() + ")");

        StatsUtils.updateGridOfMissReasons(view, StatsUtils.getFourMostCommonItems(stats));
    }


    @Override public void selectedIndices(List<Integer> indices) {

    }

    @Override public void selectedStrings(List<String> strings) {

    }
}
