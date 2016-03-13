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
public class AdvSafetyStats extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_adv_safety_stats, container, false);
        MultiSelectionSpinner spinner1 = (MultiSelectionSpinner) view.findViewById(R.id.spinner);
        spinner1.setListener(this);
        spinner1.setItems(new String[]{"Full hook", "Partial hook", "Long T", "Short T", "Open"});
        return view;
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
