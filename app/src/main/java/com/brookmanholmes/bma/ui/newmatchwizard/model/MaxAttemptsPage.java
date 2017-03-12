package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.bma.ui.newmatchwizard.fragments.MaxAttemptsFragment;
import com.brookmanholmes.bma.wizard.model.FragmentDependentPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;

import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.PLAYER_NAME_KEY;

/**
 * Created by Brookman Holmes on 8/23/2016.
 */
public class MaxAttemptsPage extends FragmentDependentPage<MaxAttemptsFragment> implements RequiresPlayerNames, UpdatesMatchBuilder {
    public static final String PLAYER_RANK_KEY = "player_rank";
    private static final String TAG = "MaxAttemptsPage";
    private int lower, upper, defaultChoice;
    private GameType gameType = GameType.BCA_NINE_BALL;

    MaxAttemptsPage(ModelCallbacks callbacks, String title, String parentKey) {
        super(callbacks, title);
        this.parentKey = parentKey;
    }

    MaxAttemptsPage setRaceToChoices(int lower, int upper, int defaultChoice) {
        this.lower = lower;
        this.upper = upper;
        this.defaultChoice = defaultChoice;
        return this;
    }

    @Override
    public Fragment createFragment() {
        return MaxAttemptsFragment.create(getKey(), lower, upper, defaultChoice);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Max attempts per game", getPlayerRank() + "", getKey()));
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        data.putString(PLAYER_NAME_KEY, playerName);

        if (fragment != null) {
            updateFragment();
        }
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        model.setMaxAttemptsPerGame(getPlayerRank());
    }

    private int getPlayerRank() {
        return data.getInt(PLAYER_RANK_KEY);
    }


    public String getPlayerName() {
        return data.getString(PLAYER_NAME_KEY, "Player 1");
    }


    public GameType getGameType() {
        return gameType;
    }

    @Override
    public void updateFragment() {
        fragment.setPlayerName(getPlayerName());
    }
}
