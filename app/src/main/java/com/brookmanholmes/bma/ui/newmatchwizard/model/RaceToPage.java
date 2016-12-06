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

import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.OPPONENT_NAME_KEY;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.PLAYER_NAME_KEY;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class RaceToPage extends Page implements RequiresPlayerNames, UpdatesMatchBuilder {
    public static final String PLAYER_RANK_KEY = "player_rank";
    public static final String OPPONENT_RANK_KEY = "opponent_rank";
    private static final String TAG = "RaceToPage";
    private int lower, upper, defaultChoice, increment = 1;
    private RaceToFragment fragment;
    private String reviewString, reviewTitle;
    private GameType gameType = GameType.BCA_NINE_BALL;

    RaceToPage(ModelCallbacks callbacks, String title, String reviewString, GameType gameType, String reviewTitle, String parentKey) {
        super(callbacks, title);
        this.reviewString = reviewString;
        this.reviewTitle = reviewTitle;
        this.gameType = gameType;
        this.parentKey = parentKey;
    }

    RaceToPage setRaceToChoices(int lower, int upper, int defaultChoice) {
        this.lower = lower;
        this.upper = upper;
        this.defaultChoice = defaultChoice;
        return this;
    }

    RaceToPage setRaceToChoices(int lower, int upper, int increment, int defaultChoice) {
        this.lower = lower;
        this.upper = upper;
        this.defaultChoice = defaultChoice;
        this.increment = increment;
        return this;
    }

    @Override
    public Fragment createFragment() {
        return RaceToFragment.create(getKey(), lower, upper, increment, defaultChoice);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(reviewTitle, getReviewString(), getKey()));
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        data.putString(PLAYER_NAME_KEY, playerName);
        data.putString(OPPONENT_NAME_KEY, opponentName);

        if (fragment != null) {
            updateFragment();
        }
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setPlayerRanks(getPlayerRank(), getOpponentRank());
    }

    private String getReviewString() {
        if (gameType == GameType.APA_NINE_BALL) {
            return String.format(Locale.getDefault(), reviewString, getPlayerName(), Players.apa9BallRaceTo(getPlayerRank()), getOpponentName(), Players.apa9BallRaceTo(getOpponentRank()));
        } else if (gameType == GameType.APA_EIGHT_BALL) {
            RaceTo raceTo = Players.apa8BallRaceTo(getPlayerRank(), getOpponentRank());
            return String.format(Locale.getDefault(), reviewString, getPlayerName(), raceTo.getPlayerRaceTo(), getOpponentName(), raceTo.getOpponentRaceTo());
        } else
            return String.format(Locale.getDefault(), reviewString, getPlayerName(), getPlayerRank(), getOpponentName(), getOpponentRank());
    }

    private int getPlayerRank() {
        return data.getInt(PLAYER_RANK_KEY);
    }

    private int getOpponentRank() {
        return data.getInt(OPPONENT_RANK_KEY);
    }

    public void registerListener(RaceToFragment fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    public String getPlayerName() {
        return data.getString(PLAYER_NAME_KEY, "Player 1");
    }

    public String getOpponentName() {
        return data.getString(OPPONENT_NAME_KEY, "Player 2");
    }

    public GameType getGameType() {
        return gameType;
    }

    public void unregisterListener() {
        this.fragment = null;
    }

    private void updateFragment() {
        fragment.setPlayerNames(getPlayerName(), getOpponentName());
    }
}
