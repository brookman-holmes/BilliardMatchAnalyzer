package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.ShotPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.convertBallToId;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.convertIdToBall;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.getLayoutByGameType;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallDead;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallMade;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallOffTable;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallOnTable;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class ShotFragment extends Fragment {
    private static final String ARG_KEY = "key";
    @Bind(R.id.title)
    TextView title;
    @Nullable
    @Bind(R.id.playerColor)
    TextView playerColor;

    private PageFragmentCallbacks callbacks;
    private String key;
    private ShotPage page;

    public ShotFragment() {
    }

    public static ShotFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putAll(matchData);
        args.putString(ARG_KEY, key);

        ShotFragment fragment = new ShotFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
        page = (ShotPage) callbacks.onGetPage(key);
    }

    @Override
    public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByGameType(getGameType()), container, false);
        ButterKnife.bind(this, view);
        title.setText("Input balls made this turn");
        if (playerColor != null)
            playerColor.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onPause() {
        page.unregisterListener();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    private GameType getGameType() {
        return GameType.valueOf(getArguments().getString(GAME_TYPE_KEY));
    }

    @Nullable
    @OnClick({R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball})
    public void onClick(View view) {
        int ball = convertIdToBall(view.getId());

        page.updateBallStatus(ball);
    }

    public void updateView(List<BallStatus> ballStatuses, String playerColor) {
        View view = getView();

        if (view != null) {
            for (int i = 0; i < ballStatuses.size(); i++) {
                ImageView ballImage = (ImageView) view.findViewById(convertBallToId(i + 1));

                setBallView(ballStatuses.get(i), ballImage);
            }

            if (this.playerColor != null)
                this.playerColor.setText(playerColor);
        }
    }

    private void setBallView(BallStatus status, ImageView ballImage) {
        if (ballIsOnTable(status)) {
            setViewToBallOnTable(ballImage);
        } else if (ballIsMade(status)) {
            setViewToBallMade(ballImage);
        } else if (ballIsDead(status)) {
            setViewToBallDead(ballImage);
        } else {
            setViewToBallOffTable(ballImage);
        }
    }

    private boolean ballIsOnTable(BallStatus status) {
        return status == BallStatus.ON_TABLE || status == BallStatus.GAME_BALL_MADE_ON_BREAK;
    }

    private boolean ballIsDead(BallStatus status) {
        return status == BallStatus.DEAD || status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }

    private boolean ballIsMade(BallStatus status) {
        return status == BallStatus.MADE || status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE;
    }
}
