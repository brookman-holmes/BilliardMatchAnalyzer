package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.ShotPage;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.PlayerColor;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 4/18/2016.
 */
public class EightBallShotFragment extends ShotFragment {
    @Bind(R.id.playerColor) TextView tvPlayerColor;
    PlayerColor playerColor;

    public static EightBallShotFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putAll(matchData);
        args.putString(ARG_KEY, key);

        EightBallShotFragment fragment = new EightBallShotFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = (ShotPage) callbacks.onGetPage(key);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        tvPlayerColor.setVisibility(View.VISIBLE);
        return view;
    }

    @Override public void updateView(List<BallStatus> ballStatuses, PlayerColor playerColor) {
        super.updateView(ballStatuses, playerColor);
        this.playerColor = playerColor;

        if (playerColor == PlayerColor.OPEN)
            this.tvPlayerColor.setText(R.string.open_table);
        else if (playerColor == PlayerColor.SOLIDS)
            this.tvPlayerColor.setText(getString(R.string.solids_table, getArguments().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY, "NO PLAYER NAME IN ARGUMENTS")));
        else if (playerColor == PlayerColor.STRIPES)
            this.tvPlayerColor.setText(getString(R.string.stripes_table, getArguments().getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY, "NO PLAYER NAME IN ARGUMENTS")));

        setButtonRunOut();
    }

    @OnClick(R.id.buttonRunOut) @Override void runOut() {
        if (playerColor == PlayerColor.SOLIDS) {
            selectAllSolids();
        } else if (playerColor == PlayerColor.STRIPES) {
            selectAllStripes();
        }
    }

    void selectAllStripes() {
        List<BallStatus> ballStatusList = page.getBallStatuses();
        for (int i = 7; i < 15; i++) {
            if (ballStatusList.get(i) == BallStatus.ON_TABLE || ballStatusList.get(i) == BallStatus.GAME_BALL_DEAD_ON_BREAK) {
                page.updateBallStatus(i + 1);
            }
        }
    }

    void selectAllSolids() {
        List<BallStatus> ballStatusList = page.getBallStatuses();
        for (int i = 0; i < 8; i++) {
            if (ballStatusList.get(i) == BallStatus.ON_TABLE || ballStatusList.get(i) == BallStatus.GAME_BALL_DEAD_ON_BREAK) {
                page.updateBallStatus(i + 1);
            }
        }
    }

    void setButtonRunOut() {
        if (playerColor == PlayerColor.OPEN)
            btnRunOut.setVisibility(View.GONE);
        else btnRunOut.setVisibility(View.VISIBLE);

        if (playerColor == PlayerColor.SOLIDS) {
            btnRunOut.setText(R.string.select_solids);
        } else if (playerColor == PlayerColor.STRIPES) {
            btnRunOut.setText(R.string.select_stripes);
        }
    }
}
