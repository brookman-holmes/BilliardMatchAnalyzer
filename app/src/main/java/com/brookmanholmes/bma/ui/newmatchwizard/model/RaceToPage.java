package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.bma.ui.newmatchwizard.fragments.RaceToFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
// TODO: 8/23/2016 fix APA version of this and also just complete this in general
public class RaceToPage extends Page implements RequiresPlayerNames, UpdatesMatchBuilder {
    public static final String PLAYER_RANK_KEY = "player_rank";
    public static final String OPPONENT_RANK_KEY = "opponent_rank";
    private RaceToFragment fragment;
    private String playerName = "Player 1", opponentName = "Player 2";
    private String reviewString = "%1$s goes to %2$d games\n\n%3$s goes to %4$d games";
    int lower, upper, defaultChoice;

    protected RaceToPage(ModelCallbacks callbacks, String title, String reviewString) {
        super(callbacks, title);
        this.reviewString = reviewString;
    }

    public RaceToPage setRaceToChoices(int lower, int upper, int defaultChoice) {
        this.lower = lower;
        this.upper = upper;
        this.defaultChoice = defaultChoice;
        return this;
    }

    @Override public Fragment createFragment() {
        return RaceToFragment.create(getKey(), lower, upper, defaultChoice);
    }

    @Override public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), getReviewString(), getKey()));
    }

    @Override public void setPlayerNames(String playerName, String opponentName) {
        this.playerName = playerName;
        this.opponentName = opponentName;

        if (fragment != null) {
            updateFragment();
        }
    }

    @Override public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setPlayerRanks(getPlayerRank(), getOpponentRank());
    }

    private String getReviewString() {
        return String.format(Locale.getDefault(), reviewString, playerName, getPlayerRank(), opponentName, getOpponentRank());
    }

    private int getPlayerRank() {
        return data.getInt(PLAYER_RANK_KEY, 101);
    }

    private int getOpponentRank() {
        return data.getInt(OPPONENT_RANK_KEY, 101);
    }

    public void registerListener(RaceToFragment fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    public void unregisterListener() {
        this.fragment = null;
    }

    private void updateFragment() {
        fragment.setPlayerNames(playerName, opponentName);
    }
}
