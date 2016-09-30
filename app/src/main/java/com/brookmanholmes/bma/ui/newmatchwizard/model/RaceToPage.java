package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Players;
import com.brookmanholmes.billiards.player.RaceTo;
import com.brookmanholmes.bma.ui.newmatchwizard.fragments.RaceToFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class RaceToPage extends Page implements RequiresPlayerNames, UpdatesMatchBuilder {
    public static final String PLAYER_RANK_KEY = "player_rank";
    public static final String OPPONENT_RANK_KEY = "opponent_rank";
    int lower, upper, defaultChoice, columns;
    private RaceToFragment fragment;
    private String playerName = "Player 1", opponentName = "Player 2";
    private String reviewString, reviewTitle;
    private GameType gameType = GameType.BCA_NINE_BALL;

    RaceToPage(ModelCallbacks callbacks, String title, String reviewString, GameType gameType, String reviewTitle) {
        super(callbacks, title);
        this.reviewString = reviewString;
        this.reviewTitle = reviewTitle;
        this.gameType = gameType;
    }

    RaceToPage(ModelCallbacks callbacks, String title, String reviewString) {
        super(callbacks, title);
        this.reviewString = reviewString;
        this.reviewTitle = title;
    }

    RaceToPage setRaceToChoices(int lower, int upper, int defaultChoice, int columns) {
        this.lower = lower;
        this.upper = upper;
        this.defaultChoice = defaultChoice;
        this.columns = columns;
        return this;
    }

    @Override public Fragment createFragment() {
        return RaceToFragment.create(getKey(), lower, upper, defaultChoice, columns);
    }

    @Override public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(reviewTitle, getReviewString(), getKey()));
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
        if (gameType == GameType.APA_NINE_BALL) {
            return String.format(Locale.getDefault(), reviewString, playerName, Players.apa9BallRaceTo(getPlayerRank()), opponentName, Players.apa9BallRaceTo(getOpponentRank()));
        } else if (gameType == GameType.APA_EIGHT_BALL) {
            RaceTo raceTo = Players.apa8BallRaceTo(getPlayerRank(), getOpponentRank());
            return String.format(Locale.getDefault(), reviewString, playerName, raceTo.getPlayerRaceTo(), opponentName, raceTo.getOpponentRaceTo());
        } else
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
