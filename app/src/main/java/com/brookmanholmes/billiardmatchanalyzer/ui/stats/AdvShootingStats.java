package com.brookmanholmes.billiardmatchanalyzer.ui.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.utils.MultiSelectionSpinner;

import java.util.List;

/**
 * Created by Brookman Holmes on 3/12/2016.
 */
public class AdvShootingStats extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    String[] shotTypes = new String[]{"Cut", "Long straight shot", "Bank", "Kick", "Combo", "Carom", "Jump"};
    String[] angleList = new String[]{"0 degrees", "15 degrees", "30 degrees", "45 degrees", "60 degrees", "75 degrees", "90 degrees"};
    String[] howList = new String[]{"Too thin", "Too thick", "Left of aim point", "Right of aim point"};
    String[] whyList = new String[]{"Bad position", "Jacked up", "Lack of focus", "Over spin", "Unintentional english", "Too slow", "Too fast", "CB curved", "On the rail", "Forcing position"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_adv_shooting_stats, container, false);
        MultiSelectionSpinner spinner1 = (MultiSelectionSpinner) view.findViewById(R.id.spinner);
        MultiSelectionSpinner spinner2 = (MultiSelectionSpinner) view.findViewById(R.id.spinner2);
        MultiSelectionSpinner spinner3 = (MultiSelectionSpinner) view.findViewById(R.id.spinner3);
        MultiSelectionSpinner spinner4 = (MultiSelectionSpinner) view.findViewById(R.id.spinner4);

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

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
