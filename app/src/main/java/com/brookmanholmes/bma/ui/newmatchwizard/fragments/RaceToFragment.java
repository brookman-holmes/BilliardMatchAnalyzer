package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage;
import com.brookmanholmes.bma.wizard.ui.BaseFragmentDependentPageFragment;
import com.shawnlin.numberpicker.NumberPicker;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage.OPPONENT_RANK_KEY;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.RaceToPage.PLAYER_RANK_KEY;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class RaceToFragment extends BaseFragmentDependentPageFragment<RaceToPage> {
    private static final String TAG = "RaceToFragment";
    private static final String ARG_KEY = "key";
    private static final String ARG_LOWER_BOUND_KEY = "lower_bound";
    private static final String ARG_UPPER_BOUND_KEY = "upper_bound";
    private static final String ARG_DEFAULT_CHOICE_KEY = "default_choice";
    private static final String ARG_INCREMENT_KEY = "increment";
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.playerPicker)
    NumberPicker playerPicker;
    @Bind(R.id.opponentPicker)
    NumberPicker opponentPicker;

    public RaceToFragment() {
    }

    public static RaceToFragment create(String key, int lowerBound, int upperBound, int increment, int defaultChoice) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putInt(ARG_DEFAULT_CHOICE_KEY, defaultChoice);
        args.putInt(ARG_LOWER_BOUND_KEY, lowerBound);
        args.putInt(ARG_UPPER_BOUND_KEY, upperBound);
        args.putInt(ARG_INCREMENT_KEY, increment);

        RaceToFragment fragment = new RaceToFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        page = (RaceToPage) callbacks.onGetPage(key);

        int playerChoice = getArguments().getInt(ARG_DEFAULT_CHOICE_KEY);
        int opponentChoice = getArguments().getInt(ARG_DEFAULT_CHOICE_KEY);

        View view = inflater.inflate(R.layout.fragment_race_page, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);

        setupPicker(playerPicker);
        setupPicker(opponentPicker);

        setPlayerNames(page.getPlayerName(), page.getOpponentName());

        // find stored rank if it's available
        SharedPreferences preferences = getActivity().getSharedPreferences("com.brookmanholmes.bma", MODE_PRIVATE);
        if (page.getGameType().isApa()) {
            if (page.getGameType() == GameType.APA_EIGHT_BALL || page.getGameType() == GameType.APA_GHOST_EIGHT_BALL) {
                playerChoice = preferences.getInt("apa8" + page.getPlayerName(), getArguments().getInt(ARG_DEFAULT_CHOICE_KEY));
                opponentChoice = preferences.getInt("apa8" + page.getOpponentName(), getArguments().getInt(ARG_DEFAULT_CHOICE_KEY));
            } else if (page.getGameType() == GameType.APA_NINE_BALL || page.getGameType() == GameType.APA_GHOST_NINE_BALL) {
                playerChoice = preferences.getInt("apa9" + page.getPlayerName(), getArguments().getInt(ARG_DEFAULT_CHOICE_KEY));
                opponentChoice = preferences.getInt("apa9" + page.getOpponentName(), getArguments().getInt(ARG_DEFAULT_CHOICE_KEY));
            }
        } else if (page.getGameType() == GameType.STRAIGHT_POOL) {
            playerChoice = preferences.getInt("straight" + page.getPlayerName() + page.getOpponentName(), getArguments().getInt(ARG_DEFAULT_CHOICE_KEY));
            opponentChoice = preferences.getInt("straight" + page.getOpponentName() + page.getPlayerName(), getArguments().getInt(ARG_DEFAULT_CHOICE_KEY));
        }

        playerPicker.setValue(playerChoice);
        opponentPicker.setValue(opponentChoice);

        updatePage();
        return view;
    }

    public void setPlayerNames(String player, String opponent) {
        if (page.getTitle().equals(getString(R.string.ranks))) {
            playerName.setText(getString(R.string.apa_ranks, player));
            opponentName.setText(getString(R.string.apa_ranks, opponent));
        } else {
            playerName.setText(getString(R.string.race_to, player));
            opponentName.setText(getString(R.string.race_to, opponent));
        }
    }

    private void setupPicker(NumberPicker picker) {
        picker.setMinValue(getArguments().getInt(ARG_LOWER_BOUND_KEY));
        picker.setMaxValue(getArguments().getInt(ARG_UPPER_BOUND_KEY));

        int increment = getArguments().getInt(ARG_INCREMENT_KEY, 1);
        if (increment > 1) {
            String[] displayedValues = new String[picker.getMaxValue() - picker.getMinValue() + 1];
            for (int i = 0; i < displayedValues.length; i++) {
                displayedValues[i] = String.valueOf((i + 1) * increment);
            }
            picker.setDisplayedValues(displayedValues);
        }

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updatePage();
            }
        });
    }

    private void updatePage() {
        page.getData().putInt(PLAYER_RANK_KEY, playerPicker.getValue() * getArguments().getInt(ARG_INCREMENT_KEY, 1));
        page.getData().putInt(OPPONENT_RANK_KEY, opponentPicker.getValue() * getArguments().getInt(ARG_INCREMENT_KEY, 1));
        page.notifyDataChanged();
    }
}
