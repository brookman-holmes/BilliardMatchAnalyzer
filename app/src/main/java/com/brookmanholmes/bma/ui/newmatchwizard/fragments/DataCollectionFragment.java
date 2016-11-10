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

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by helios on 11/4/2016.
 */

public class DataCollectionFragment extends BaseFragment {
    private static final String ARG_KEY = "key";
    private static final String ARG_GHOST = "arg_ghost";
    private static final int[] checkBoxes = new int[]{R.id.cbShotTypePlayer, R.id.cbShotTypeOpponent, R.id.cbCuePlayer,
            R.id.cbCueOpponent, R.id.cbHowPlayer, R.id.cbHowOpponent, R.id.cbSafetiesPlayer,
            R.id.cbSafetiesOpponent, R.id.cbSpeedPlayer, R.id.cbSpeedOpponent, R.id.cbBallDistPlayer,
            R.id.cbBallDistOpponent};
    private static final int[] playerBoxes = new int[]{R.id.cbShotTypePlayer, R.id.cbCuePlayer,
            R.id.cbHowPlayer, R.id.cbSafetiesPlayer, R.id.cbSpeedPlayer, R.id.cbBallDistPlayer};
    private static final int[] opponentBoxes = new int[]{R.id.cbShotTypeOpponent, R.id.cbCueOpponent,
            R.id.cbHowOpponent, R.id.cbSafetiesOpponent, R.id.cbSpeedOpponent, R.id.cbBallDistOpponent};

    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;

    private PageFragmentCallbacks callbacks;
    private DataCollectionPage page;
    private String key;
    private boolean isGhostGame;

    public static DataCollectionFragment create(String key, boolean isGhost) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putBoolean(ARG_GHOST, isGhost);
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
        isGhostGame = args.getBoolean(ARG_GHOST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        page = (DataCollectionPage) callbacks.onGetPage(key);
        View view = inflater.inflate(isGhostGame ? R.layout.fragment_page_advanced_stats_ghost : R.layout.fragment_page_advanced_stats, container, false);
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
        ArrayList<String> enumList = new ArrayList<>();
        ArrayList<String> playerDescriptions = new ArrayList<>();
        ArrayList<String> opponentDescriptions = new ArrayList<>();

        for (int i : checkBoxes) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), i);
            if (checkBox.isChecked()) {
                enumList.add(getEnumFromId(i).name());
                if (ArrayUtils.contains(playerBoxes, i))
                    playerDescriptions.add(getStringFromId(i));
                else if (ArrayUtils.contains(opponentBoxes, i))
                    opponentDescriptions.add(getStringFromId(i));
            }
        }

        page.getData().putStringArrayList(Page.SIMPLE_DATA_KEY, enumList);
        page.getData().putStringArrayList(DataCollectionPage.PLAYER_DESC_KEY, playerDescriptions);
        page.getData().putStringArrayList(DataCollectionPage.OPP_DESC_KEY, opponentDescriptions);
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

    private String getStringFromId(int id) {
        switch (id) {
            case R.id.cbShotTypePlayer:
                return getString(R.string.shot_type);
            case R.id.cbShotTypeOpponent:
                return getString(R.string.shot_type);
            case R.id.cbCuePlayer:
                return getString(R.string.cueing);
            case R.id.cbCueOpponent:
                return getString(R.string.cueing);
            case R.id.cbHowPlayer:
                return getString(R.string.how_miss);
            case R.id.cbHowOpponent:
                return getString(R.string.how_miss);
            case R.id.cbSafetiesPlayer:
                return getString(R.string.title_safeties);
            case R.id.cbSafetiesOpponent:
                return getString(R.string.title_safeties);
            case R.id.cbSpeedPlayer:
                return getString(R.string.cb_speed);
            case R.id.cbSpeedOpponent:
                return getString(R.string.cb_speed);
            case R.id.cbBallDistPlayer:
                return getString(R.string.ball_distances);
            case R.id.cbBallDistOpponent:
                return getString(R.string.ball_distances);
            default:
                throw new IllegalArgumentException("No corresponding StatsDetail from id " + id);
        }
    }
}
