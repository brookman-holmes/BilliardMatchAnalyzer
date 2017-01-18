package com.brookmanholmes.bma.ui.addturnwizard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.BallStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.addturnwizard.model.BreakPage;
import com.brookmanholmes.bma.wizard.ui.BasePageFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertBallToId;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertIdToBall;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.getLayout;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.setViewToBallDead;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.setViewToBallMade;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.setViewToBallOnTable;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
@SuppressWarnings("WeakerAccess")
public class BreakFragment extends BasePageFragment<BreakPage> {
    private static final String TAG = "BreakFragment";
    private static final String ARG_KEY = "key";
    private static final int ballIds[] = {R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball};
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.title)
    TextView title;

    public BreakFragment() {
    }

    public static BreakFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putAll(matchData);
        args.putString(ARG_KEY, key);

        BreakFragment fragment = new BreakFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        page.registerFragment(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = (BreakPage) callbacks.onGetPage(key);

        View view = inflater.inflate(getLayout(getGameType()), container, false);
        ButterKnife.bind(this, view);
        title.setText(page.getTitle());

        for (int i = 1; i <= ballIds.length; i++) {
            ImageView ballImage = ButterKnife.findById(view, convertBallToId(i));

            if (ballImage != null) {
                ballImage.setImageLevel(1);
            }
        }

        return view;
    }

    public void updateView(List<BallStatus> ballStatuses) {
        View view = getView();
        if (view != null) {
            for (int i = 0; i < ballStatuses.size(); i++) {
                ImageView ballImage = ButterKnife.findById(view, convertBallToId(i + 1));
                setBallView(ballStatuses.get(i), ballImage);
            }
        }
    }

    @Nullable
    @OnClick({R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball})
    public void onClick(ImageView view) {
        int ball = convertIdToBall(view.getId());

        BallStatus ballStatus = page.getBallStatus(ball);
        if (!modifiedByShotPage(ballStatus)) {// if the ball has not been modified by ShotPage
            ballStatus = page.updateBallStatus(ball);
        } else { // if the ball has been modified by ShotPage then it needs to be treated like it's on table
            if (ball == page.getGameBall())
                ballStatus = BallStatus.GAME_BALL_MADE_ON_BREAK;
            else
                ballStatus = BallStatus.MADE_ON_BREAK;
            page.setBallStatus(BallStatus.MADE_ON_BREAK, ball);
        }
        setBallView(ballStatus, view);
    }

    private GameType getGameType() {
        return GameType.valueOf(getArguments().getString(GAME_TYPE_KEY));
    }

    private void setBallView(BallStatus status, ImageView ballImage) {
        if (ballIsOnTable(status)) {
            setViewToBallOnTable(ballImage);
        } else if (ballIsMade(status)) {
            setViewToBallMade(ballImage);
        } else if (ballIsDead(status)) {
            setViewToBallDead(ballImage);
        }
    }

    private boolean ballIsOnTable(BallStatus status) {
        return !ballIsDead(status) && !ballIsMade(status);
    }

    private boolean ballIsDead(BallStatus status) {
        return status == BallStatus.DEAD_ON_BREAK ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD;
    }

    private boolean ballIsMade(BallStatus status) {
        return status == BallStatus.MADE_ON_BREAK ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }

    private boolean modifiedByShotPage(BallStatus status) {
        return BallStatus.MADE == status ||
                BallStatus.DEAD == status ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_DEAD ||
                status == BallStatus.GAME_BALL_DEAD_ON_BREAK_THEN_MADE ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_MADE ||
                status == BallStatus.GAME_BALL_MADE_ON_BREAK_THEN_DEAD;
    }
}
