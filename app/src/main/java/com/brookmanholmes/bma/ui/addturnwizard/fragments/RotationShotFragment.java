package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.ShotPage;
import com.brookmanholmes.billiards.game.BallStatus;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 4/18/2016.
 */
public class RotationShotFragment extends ShotFragment {
    public static RotationShotFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putAll(matchData);
        args.putString(ARG_KEY, key);

        RotationShotFragment fragment = new RotationShotFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = (ShotPage) callbacks.onGetPage(key);

        View view = super.onCreateView(inflater, container, savedInstanceState);

        btnRunOut.setVisibility(View.VISIBLE);
        btnRunOut.setText(R.string.select_balls);
        return view;
    }

    @OnClick(R.id.buttonRunOut) @Override void runOut() {
        List<BallStatus> ballStatusList = page.getBallStatuses();
        Log.i("BallStatuses", ballStatusList.toString());
        for (int i = 0; i < ballStatusList.size(); i++) {
            if (ballStatusList.get(i) == BallStatus.ON_TABLE) {
                page.updateBallStatus(i + 1);
            }

            if (ballStatusList.get(i) == BallStatus.GAME_BALL_DEAD_ON_BREAK)
                page.updateBallStatus(i + 1);

            if (ballStatusList.get(i) == BallStatus.GAME_BALL_MADE_ON_BREAK)
                page.updateBallStatus(i + 1);
        }
    }
}
