package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.ui.newmatchwizard.fragments.DataCollectionFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.OPPONENT_NAME_KEY;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.PLAYER_NAME_KEY;

/**
 * Created by helios on 11/4/2016.
 */

public class DataCollectionPage extends Page implements RequiresPlayerNames, UpdatesMatchBuilder {
    public static final String PLAYER_DESC_KEY = "player_key";
    public static final String OPP_DESC_KEY = "opp_key";
    String reviewTitle;
    private DataCollectionFragment fragment;
    private boolean isGhost = false;


    DataCollectionPage(ModelCallbacks callbacks, String title, String reviewTitle, String parentKey) {
        super(callbacks, title);
        this.parentKey = parentKey;
        this.reviewTitle = reviewTitle;
    }

    @Override
    public void setPlayerNames(String playerName, String opponentName) {
        data.putString(PLAYER_NAME_KEY, playerName);
        data.putString(OPPONENT_NAME_KEY, opponentName);

        if (fragment != null) {
            updateFragment();
        }
    }

    private void updateFragment() {
        fragment.setPlayerNames(data.getString(PLAYER_NAME_KEY), data.getString(OPPONENT_NAME_KEY));
    }

    public void registerFragment(DataCollectionFragment fragment) {
        this.fragment = fragment;
        updateFragment();
    }

    public void unregisterFragment() {
        this.fragment = null;
    }

    @Override
    public void updateMatchBuilder(CreateNewMatchWizardModel model) {
        List<Match.StatsDetail> details = new ArrayList<>();
        List<String> dataList = data.getStringArrayList(SIMPLE_DATA_KEY);

        if (dataList != null) {
            for (String string : dataList)
                details.add(Match.StatsDetail.valueOf(string));

            model.setStatDetail(details);
        }
    }

    DataCollectionPage setGhost(boolean isGhost) {
        this.isGhost = isGhost;
        return this;
    }

    @Override
    public Fragment createFragment() {
        return DataCollectionFragment.create(getKey(), isGhost);
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        ArrayList<String> playerItems = data.getStringArrayList(PLAYER_DESC_KEY);
        ArrayList<String> opponentItems = data.getStringArrayList(OPP_DESC_KEY);

        dest.add(new ReviewItem(String.format(Locale.getDefault(), reviewTitle, data.getString(PLAYER_NAME_KEY)), formatAdvShotData(playerItems), getKey()));
        if (!isGhost)
            dest.add(new ReviewItem(String.format(Locale.getDefault(), reviewTitle, data.getString(OPPONENT_NAME_KEY)), formatAdvShotData(opponentItems), getKey()));
    }

    private String formatAdvShotData(List<String> advShotData) {
        String result = "";

        if (advShotData != null) {

            for (int i = 0; i < advShotData.size(); i++) {
                result += advShotData.get(i);

                if (i + 1 < advShotData.size())
                    result += "\n";
            }
        }
        return result;
    }
}
