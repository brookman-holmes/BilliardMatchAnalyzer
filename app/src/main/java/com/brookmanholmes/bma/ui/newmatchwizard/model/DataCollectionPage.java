package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.ui.newmatchwizard.fragments.DataCollectionFragment;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.FragmentDependentPage;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.OPPONENT_NAME_KEY;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.PLAYER_NAME_KEY;

/**
 * Created by helios on 11/4/2016.
 */

public class DataCollectionPage extends FragmentDependentPage<DataCollectionFragment> implements RequiresPlayerNames, UpdatesMatchBuilder {
    public static final String PLAYER_DESC_KEY = "player_key";
    public static final String OPP_DESC_KEY = "opp_key";
    String reviewTitle;
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

    public void updateFragment() {
        if (fragment != null)
            fragment.setPlayerNames(data.getString(PLAYER_NAME_KEY), data.getString(OPPONENT_NAME_KEY));
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

        dest.add(new ReviewItem(String.format(Locale.getDefault(), reviewTitle, data.getString(PLAYER_NAME_KEY)), MatchDialogHelperUtils.formatAdvShotData(playerItems, "\n"), getKey()));
        if (!isGhost)
            dest.add(new ReviewItem(String.format(Locale.getDefault(), reviewTitle, data.getString(OPPONENT_NAME_KEY)), MatchDialogHelperUtils.formatAdvShotData(opponentItems, "\n"), getKey()));
    }

}
