package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.StraightPoolPage;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.ui.BasePageFragment;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Brookman Holmes on 12/1/2016.
 */

public class StraightPoolShotFragment extends BasePageFragment<StraightPoolPage> implements NumberPicker.OnValueChangeListener {
    private static final String TAG = "StraightShotFragment";
    private static final String ARG_KEY = "key";
    private final RadioGroup.OnCheckedChangeListener foulListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.yes)
                page.getData().putString(StraightPoolPage.FOUL_KEY, getString(R.string.yes));
            else if (checkedId == R.id.no)
                page.getData().putString(StraightPoolPage.FOUL_KEY, getString(R.string.no));
            else if (checkedId == R.id.seriousFoul) {
                page.getData().putString(StraightPoolPage.FOUL_KEY, getString(R.string.foul_serious));
            }
        }
    };
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.miss)
    RadioButton missButton;
    @Bind(R.id.safety)
    RadioButton safetyButton;
    @Bind(R.id.safety_error)
    RadioButton safetyErrorButton;
    @Bind(R.id.rebreak)
    RadioButton rebreakButton;
    @Bind(R.id.options)
    RadioGroup turnEndGroup;
    @Bind(R.id.foulGroup)
    RadioGroup foulGroup;
    @Bind(R.id.foulLayout)
    ViewGroup foulLayout;
    @Bind(R.id.subTitle)
    TextView subTitle;
    @Bind(R.id.npHundreds)
    NumberPicker npHundreds;
    @Bind(R.id.npTens)
    NumberPicker npTens;
    @Bind(R.id.npOnes)
    NumberPicker npOnes;
    GameStatus gameStatus;
    private final RadioGroup.OnCheckedChangeListener turnEndListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            updateFoulLayout(checkedId);
            updatePage();
        }
    };

    public static StraightPoolShotFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putAll(matchData);

        StraightPoolShotFragment fragment = new StraightPoolShotFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = (StraightPoolPage) callbacks.onGetPage(key);

        View view = inflater.inflate(R.layout.select_straight_pool_balls_dialog, container, false);
        ButterKnife.bind(this, view);
        title.setText(getString(R.string.title_straight_pool, page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)));

        setNumberPicker(npHundreds);
        setNumberPicker(npTens);
        setNumberPicker(npOnes);

        foulGroup.check(R.id.no);
        turnEndGroup.check(R.id.miss);
        foulGroup.setOnCheckedChangeListener(foulListener);
        turnEndGroup.setOnCheckedChangeListener(turnEndListener);
        String opponentName = page.getData().getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY);
        rebreakButton.setText(getString(R.string.turn_current_player_breaks, opponentName));
        subTitle.setText(getString(R.string.title_foul, page.getData().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)));
        gameStatus = MatchDialogHelperUtils.getGameStatus(page.getData());
        updateTurnEndOptions(gameStatus.playerAllowedToBreakAgain);
        updateFoulOptions(gameStatus.currentPlayerConsecutiveFouls, getBallsMade());

        if (gameStatus.gameType.isSinglePlayer()) {
            foulLayout.setVisibility(View.GONE);
            turnEndGroup.setVisibility(View.GONE);
        }

        return view;
    }

    private void updateFoulLayout(int selection) {
        TransitionManager.beginDelayedTransition(foulLayout);
        if (selection != rebreakButton.getId() && selection != safetyButton.getId()) {
            foulLayout.setVisibility(View.VISIBLE);
        } else {
            foulLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateFoulOptions(int consecutiveFouls, int ballsMade) {
        boolean possibleThreeFoul = consecutiveFouls >= 2 && ballsMade == 0;
        foulGroup.findViewById(R.id.seriousFoul).setVisibility(possibleThreeFoul ? View.VISIBLE : View.INVISIBLE);

        if (!possibleThreeFoul && foulGroup.getCheckedRadioButtonId() == R.id.seriousFoul) {
            foulGroup.check(R.id.yes);
        }
    }

    private void updateTurnEndOptions(boolean canOpponentBreakAgain) {
        if (canOpponentBreakAgain)
            rebreakButton.setVisibility(View.VISIBLE);
        else rebreakButton.setVisibility(View.GONE);
    }

    private void updatePage() {
        page.getData().putInt(StraightPoolPage.BALLS_MADE_KEY, getBallsMade());
        String selection = ((RadioButton) turnEndGroup.findViewById(turnEndGroup.getCheckedRadioButtonId())).getText().toString();
        page.setTurnEnd(selection);
        updateTurnEndOptions(getBallsMade() == 0 && gameStatus.playerAllowedToBreakAgain);
        updateFoulOptions(gameStatus.currentPlayerConsecutiveFouls, getBallsMade());
        page.notifyDataChanged();
    }

    private int getBallsMade() {
        return 100 * flipValue(npHundreds.getValue()) + 10 * flipValue(npTens.getValue()) + flipValue(npOnes.getValue());
    }

    private void setNumberPicker(NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(9);
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {

                return String.format(Locale.getDefault(), "%1$d", flipValue(value));
            }
        });
        numberPicker.setOnValueChangedListener(this);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        updatePage();
    }

    private int flipValue(int valueToFlip) {
        return (10 - valueToFlip) % 10;
    }
}
