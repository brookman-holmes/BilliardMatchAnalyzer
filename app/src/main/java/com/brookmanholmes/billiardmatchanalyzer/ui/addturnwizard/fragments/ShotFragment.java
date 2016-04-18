package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.ShotPage;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public abstract class ShotFragment extends Fragment {
    static final String ARG_KEY = "key";
    @Bind(R.id.title) TextView title;

    @Bind(R.id.buttonRunOut) Button btnRunOut;
    ShotPage page;
    GameType gameType;
    private PageFragmentCallbacks callbacks;
    private String key;

    public ShotFragment() {
    }

    public static ShotFragment create(String key, Bundle matchData) {
        Bundle args = new Bundle();
        args.putAll(matchData);
        args.putString(ARG_KEY, key);

        ShotFragment fragment;

        GameType gameType = GameType.valueOf(matchData.getString(MatchDialogHelperUtils.GAME_TYPE_KEY));
        if (gameType == GameType.APA_EIGHT_BALL || gameType == GameType.BCA_EIGHT_BALL)
            fragment = new EightBallShotFragment();
        else fragment = new RotationShotFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getParentFragment() instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        callbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        key = args.getString(ARG_KEY);
        page = (ShotPage) callbacks.onGetPage(key);
        gameType = GameType.valueOf(getArguments().getString(MatchDialogHelperUtils.GAME_TYPE_KEY));
    }

    @Override public void onResume() {
        super.onResume();
        page.registerListener(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByGameType(gameType), container, false);
        ButterKnife.bind(this, view);
        title.setText(page.getTitle());

        return view;
    }

    @Override public void onPause() {
        page.unregisterListener();
        super.onPause();
    }

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Nullable @OnClick({R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball})
    public void onClick(View view) {
        int ball = convertIdToBall(view.getId());

        page.updateBallStatus(ball);
    }

    public void updateView(List<BallStatus> ballStatuses, PlayerColor playerColor) {
        View view = getView();

        if (view != null) {
            for (int i = 0; i < ballStatuses.size(); i++) {
                ImageView ballImage = (ImageView) view.findViewById(convertBallToId(i + 1));
                setBallView(ballStatuses.get(i), ballImage);
            }
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

    abstract void runOut();
}
