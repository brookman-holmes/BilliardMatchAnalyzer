package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.support.v4.app.Fragment;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.ui.newmatchwizard.fragments.DataCollectionFragment;
import com.brookmanholmes.bma.wizard.model.ModelCallbacks;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.ReviewItem;

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.OPPONENT_NAME_KEY;
import static com.brookmanholmes.bma.ui.newmatchwizard.model.PlayerNamePage.PLAYER_NAME_KEY;

/**
 * Created by helios on 11/4/2016.
 */

public class DataCollectionPage extends Page implements RequiresPlayerNames, UpdatesMatchBuilder {
    private DataCollectionFragment fragment;

    protected DataCollectionPage(ModelCallbacks callbacks, String title, String parentKey) {
        super(callbacks, title);
        this.parentKey = parentKey;
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

    @Override
    public Fragment createFragment() {
        return DataCollectionFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

    }
}
