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

import com.brookmanholmes.billiardmatchanalyzer.MyApplication;
import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.BreakPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.ui.PageFragmentCallbacks;
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.squareup.leakcanary.RefWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.convertBallToId;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.convertIdToBall;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.getLayoutByGameType;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallDead;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallMade;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.setViewToBallOnTable;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class BreakFragment extends Fragment {
    private static final String ARG_KEY = "key";
    private static final int ballIds[] = {R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball};
    @Bind(R.id.title) TextView title;
    private PageFragmentCallbacks callbacks;
    private String key;
    private BreakPage page;

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
    }

    @Nullable
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        page = (BreakPage) callbacks.onGetPage(key);

        View view = inflater.inflate(getLayoutByGameType(getGameType()), container, false);
        ButterKnife.bind(this, view);
        title.setText(page.getTitle());

        for (int i = 1; i <= ballIds.length; i++) {
            ImageView ballImage = (ImageView) view.findViewById(convertBallToId(i));

            if (ballImage != null) {
                ballImage.setImageLevel(1);
            }
        }

        return view;
    }

    @Override public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Nullable
    @OnClick({R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball,
            R.id.nine_ball, R.id.ten_ball, R.id.eleven_ball, R.id.twelve_ball,
            R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball})
    public void onClick(ImageView view) {
        int ball = convertIdToBall(view.getId());

        BallStatus ballStatus = page.updateBallStatus(ball);

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
        return status == BallStatus.ON_TABLE;
    }

    private boolean ballIsDead(BallStatus status) {
        return status == BallStatus.DEAD_ON_BREAK || status == BallStatus.GAME_BALL_DEAD_ON_BREAK;
    }

    private boolean ballIsMade(BallStatus status) {
        return status == BallStatus.MADE_ON_BREAK || status == BallStatus.GAME_BALL_MADE_ON_BREAK;
    }
}
