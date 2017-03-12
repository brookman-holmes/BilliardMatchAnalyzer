package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.newmatchwizard.model.MaxAttemptsPage;
import com.brookmanholmes.bma.wizard.ui.BaseFragmentDependentPageFragment;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage.PLAYER_RANK_KEY;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class MaxAttemptsFragment extends BaseFragmentDependentPageFragment<MaxAttemptsPage> {
    private static final String TAG = "RaceToFragment";
    private static final String ARG_KEY = "key";
    private static final String ARG_LOWER_BOUND_KEY = "lower_bound";
    private static final String ARG_UPPER_BOUND_KEY = "upper_bound";
    private static final String ARG_DEFAULT_CHOICE_KEY = "default_choice";
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.playerPicker)
    NumberPicker playerPicker;

    public MaxAttemptsFragment() {
    }

    public static MaxAttemptsFragment create(String key, int lowerBound, int upperBound, int defaultChoice) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(ARG_DEFAULT_CHOICE_KEY, defaultChoice);
        args.putInt(ARG_LOWER_BOUND_KEY, lowerBound);
        args.putInt(ARG_UPPER_BOUND_KEY, upperBound);

        MaxAttemptsFragment fragment = new MaxAttemptsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = (MaxAttemptsPage) callbacks.onGetPage(key);

        int playerChoice = getArguments().getInt(ARG_DEFAULT_CHOICE_KEY, 1);

        View view = inflater.inflate(R.layout.fragment_max_attempts_page, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);

        setupPicker(playerPicker);
        setPlayerName(page.getPlayerName());
        playerPicker.setValue(playerChoice);

        updatePage();
        return view;
    }

    public void setPlayerName(String player) {
        playerName.setText(String.format(Locale.getDefault(), "How many attempts per game is %1$s allowed?", player));
    }

    private void setupPicker(NumberPicker picker) {
        picker.setMinValue(getArguments().getInt(ARG_LOWER_BOUND_KEY));
        picker.setMaxValue(getArguments().getInt(ARG_UPPER_BOUND_KEY));

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updatePage();
            }
        });
    }

    private void updatePage() {
        page.getData().putInt(PLAYER_RANK_KEY, playerPicker.getValue());
        page.notifyDataChanged();
    }
}
