package com.brookmanholmes.bma.ui.newmatchwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.BaseFragment;
import com.brookmanholmes.bma.ui.newmatchwizard.model.DataCollectionPage;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by helios on 11/4/2016.
 */

public class DataCollectionFragment extends BaseFragment {
    private static final String ARG_KEY = "key";
    private static final int[] checkBoxes = new int[]{R.id.cbShotTypePlayer, R.id.cbShotTypeOpponent, R.id.cbCuePlayer,
            R.id.cbCueOpponent, R.id.cbHowPlayer, R.id.cbHowOpponent, R.id.cbSafetiesPlayer,
            R.id.cbSafetiesOpponent, R.id.cbSpeedPlayer, R.id.cbSpeedOpponent, R.id.cbBallDistPlayer,
            R.id.cbBallDistOpponent};

    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;

    private PageFragmentCallbacks callbacks;
    private DataCollectionPage page;
    private String key;

    public static DataCollectionFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        DataCollectionFragment fragment = new DataCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof PageFragmentCallbacks) {
            callbacks = (PageFragmentCallbacks) getActivity();
        } else {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = (DataCollectionPage) callbacks.onGetPage(key);

        View view = inflater.inflate(R.layout.fragment_page_advanced_stats, container, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        page.registerFragment(this);
    }

    @Override
    public void onPause() {
        page.unregisterFragment();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public void setPlayerNames(String player, String opponent) {
        playerName.setText(player);
        opponentName.setText(opponent);
    }

    @OnCheckedChanged({R.id.cbShotTypePlayer, R.id.cbShotTypeOpponent, R.id.cbCuePlayer,
            R.id.cbCueOpponent, R.id.cbHowPlayer, R.id.cbHowOpponent, R.id.cbSafetiesPlayer,
            R.id.cbSafetiesOpponent, R.id.cbSpeedPlayer, R.id.cbSpeedOpponent, R.id.cbBallDistPlayer,
            R.id.cbBallDistOpponent})
    public void notifyPage(CheckBox box) {
        ArrayList<String> list = new ArrayList<>();

        for (int i : checkBoxes) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), i);
            if (checkBox.isChecked())
                list.add(getEnumFromId(i).name());
        }

        page.getData().putStringArrayList(Page.SIMPLE_DATA_KEY, list);
        page.notifyDataChanged();
    }

    private Match.StatsDetail getEnumFromId(int id) {
        switch (id) {
            case R.id.cbShotTypePlayer:
                return Match.StatsDetail.SHOT_TYPE_PLAYER;
            case R.id.cbShotTypeOpponent:
                return Match.StatsDetail.SHOT_TYPE_OPPONENT;
            case R.id.cbCuePlayer:
                return Match.StatsDetail.CUEING_PLAYER;
            case R.id.cbCueOpponent:
                return Match.StatsDetail.CUEING_OPPONENT;
            case R.id.cbHowPlayer:
                return Match.StatsDetail.HOW_MISS_PLAYER;
            case R.id.cbHowOpponent:
                return Match.StatsDetail.HOW_MISS_OPPONENT;
            case R.id.cbSafetiesPlayer:
                return Match.StatsDetail.SAFETIES_PLAYER;
            case R.id.cbSafetiesOpponent:
                return Match.StatsDetail.SAFETIES_OPPONENT;
            case R.id.cbSpeedPlayer:
                return Match.StatsDetail.SPEED_PLAYER;
            case R.id.cbSpeedOpponent:
                return Match.StatsDetail.SPEED_OPPONENT;
            case R.id.cbBallDistPlayer:
                return Match.StatsDetail.BALL_DISTANCES_PLAYER;
            case R.id.cbBallDistOpponent:
                return Match.StatsDetail.BALL_DISTANCES_OPPONENT;
            default:
                throw new IllegalArgumentException("No corresponding StatsDetail from id " + id);
        }
    }
}
