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
            R.id.cbBallDistOpponent, R.id.cbAnglePlayer, R.id.cbAngleOpponent, R.id.cbAngleSimplePlayer,
            R.id.cbAngleSimpleOpponent};
    private static final int[] playerBoxes = new int[]{R.id.cbShotTypePlayer, R.id.cbCuePlayer,
            R.id.cbHowPlayer, R.id.cbSafetiesPlayer, R.id.cbSpeedPlayer, R.id.cbBallDistPlayer,
            R.id.cbAnglePlayer, R.id.cbAngleSimplePlayer};
    private static final int[] opponentBoxes = new int[]{R.id.cbShotTypeOpponent, R.id.cbCueOpponent,
            R.id.cbHowOpponent, R.id.cbSafetiesOpponent, R.id.cbSpeedOpponent, R.id.cbBallDistOpponent,
            R.id.cbAngleOpponent, R.id.cbAngleSimpleOpponent};

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
            R.id.cbBallDistOpponent, R.id.cbAnglePlayer, R.id.cbAngleOpponent, R.id.cbAngleSimplePlayer,
            R.id.cbAngleSimpleOpponent})
    public void notifyPage(CheckBox box) {
        ArrayList<String> enumList = new ArrayList<>();
        ArrayList<String> playerDescriptions = new ArrayList<>();
        ArrayList<String> opponentDescriptions = new ArrayList<>();

        if (box.getId() == R.id.cbShotTypePlayer) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAnglePlayer);
            if (!box.isChecked()) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
            } else checkBox.setEnabled(true);

            checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAngleSimplePlayer);
            if (!box.isChecked()) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
            } else checkBox.setEnabled(true);
        } else if (box.getId() == R.id.cbShotTypeOpponent) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAngleOpponent);
            if (!box.isChecked()) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
            } else checkBox.setEnabled(true);
            checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAngleSimpleOpponent);
            if (!box.isChecked()) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
            } else checkBox.setEnabled(true);
        }

        // uncheck duplicate boxes
        if (box.getId() == R.id.cbAnglePlayer) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAngleSimplePlayer);
            if (box.isChecked())
                checkBox.setChecked(false);
        } else if (box.getId() == R.id.cbAngleOpponent) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAngleSimpleOpponent);
            if (box.isChecked())
                checkBox.setChecked(false);
        } else if (box.getId() == R.id.cbAngleSimplePlayer) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAnglePlayer);
            if (box.isChecked())
                checkBox.setChecked(false);
        } else if (box.getId() == R.id.cbAngleSimpleOpponent) {
            CheckBox checkBox = ButterKnife.findById(box.getRootView(), R.id.cbAngleOpponent);
            if (box.isChecked())
                checkBox.setChecked(false);
        }

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
            case R.id.cbAnglePlayer:
                return Match.StatsDetail.ANGLE_PLAYER;
            case R.id.cbAngleOpponent:
                return Match.StatsDetail.ANGLE_OPPONENT;
            case R.id.cbAngleSimplePlayer:
                return Match.StatsDetail.ANGLE_SIMPLE_PLAYER;
            case R.id.cbAngleSimpleOpponent:
                return Match.StatsDetail.ANGLE_SIMPLE_OPPONENT;
            default:
                throw new IllegalArgumentException("No corresponding StatsDetail from id " + id);
        }
    }

    private String getStringFromId(int id) {
        switch (id) {
            case R.id.cbShotTypePlayer:
            case R.id.cbShotTypeOpponent:
                return getString(R.string.shot_type);
            case R.id.cbCuePlayer:
            case R.id.cbCueOpponent:
                return getString(R.string.cueing);
            case R.id.cbHowPlayer:
            case R.id.cbHowOpponent:
                return getString(R.string.how_miss);
            case R.id.cbSafetiesPlayer:
            case R.id.cbSafetiesOpponent:
                return getString(R.string.title_safeties);
            case R.id.cbSpeedPlayer:
            case R.id.cbSpeedOpponent:
                return getString(R.string.cb_speed);
            case R.id.cbBallDistPlayer:
            case R.id.cbBallDistOpponent:
                return getString(R.string.ball_distances);
            case R.id.cbAnglePlayer:
            case R.id.cbAngleOpponent:
                return getString(R.string.angle_detailed);
            case R.id.cbAngleSimplePlayer:
            case R.id.cbAngleSimpleOpponent:
                return getString(R.string.angle_simple);
            default:
                throw new IllegalArgumentException("No corresponding StatsDetail from id " + id);
        }
    }
}
