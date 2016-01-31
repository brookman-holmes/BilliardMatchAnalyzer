package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.AddInningFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.BALLS_ON_TABLE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.convertIdToBall;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.getLayoutByGameType;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectBallsDialog extends Fragment {
    private static final String TAG = "SelectBallsDialog";
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.ballGrid)
    GridLayout ballGrid;

    TableStatus tableStatus;
    GameType gameType;

    public static SelectBallsDialog create(Bundle args) {
        SelectBallsDialog fragment = new SelectBallsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameType = GameType.valueOf(getArguments().getString(GAME_TYPE_KEY));
        tableStatus = TableStatus.newTable(gameType, getArguments().getIntegerArrayList(BALLS_ON_TABLE_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByGameType(gameType), container, false);
        ButterKnife.bind(this, view);
        title.setText("Select balls made by " + getArguments().getString(MatchDialogHelperUtils.PLAYER_NAME_KEY, "--"));

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = (ballGrid.getMeasuredWidth());
                int cy = (ballGrid.getMeasuredHeight());
                int radius = Math.max(ballGrid.getWidth(), ballGrid.getHeight());
                Animator anim = ViewAnimationUtils
                        .createCircularReveal(ballGrid, cx, cy, 0, radius);
                anim.setStartDelay(50);
                ballGrid.setVisibility(View.VISIBLE);
                anim.start();
            }
        });

        return view;
    }

    @Nullable
    @OnClick({R.id.one_ball, R.id.two_ball, R.id.three_ball, R.id.four_ball,
            R.id.five_ball, R.id.six_ball, R.id.seven_ball, R.id.eight_ball, R.id.nine_ball,
            R.id.ten_ball, R.id.eleven_ball, R.id.thirteen_ball, R.id.fourteen_ball, R.id.fifteen_ball})
    public void onBallClick(View view) {
        setBallStatus(convertIdToBall(view.getId()));

        if (tableStatus.getBallStatus(convertIdToBall(view.getId())) == com.brookmanholmes.billiards.game.util.BallStatus.MADE) {
            MatchDialogHelperUtils.setViewToBallMade(view);
            Log.i(TAG, "Ball made: " + convertIdToBall(view.getId()));
        } else if (tableStatus.getBallStatus(convertIdToBall(view.getId())) == com.brookmanholmes.billiards.game.util.BallStatus.DEAD) {
            MatchDialogHelperUtils.setViewToBallDead(view);
            Log.i(TAG, "Ball dead: " + convertIdToBall(view.getId()));
        } else {
            MatchDialogHelperUtils.setViewToBallOnTable(view);
            Log.i(TAG, "Ball on table: " + convertIdToBall(view.getId()));
        }

        EventBus.getDefault().post(new BallStatus(tableStatus));
    }

    public void onEvent(AddInningFragment.Update update) {
        this.tableStatus = update.tableStatus;
    }

    private void setBallStatus(int ball) {
        switch (tableStatus.getBallStatus(ball)) {
            case ON_TABLE:
                tableStatus.setBallTo(com.brookmanholmes.billiards.game.util.BallStatus.MADE, ball);
                break;
            case MADE:
                tableStatus.setBallTo(com.brookmanholmes.billiards.game.util.BallStatus.DEAD, ball);
                break;
            case DEAD:
                tableStatus.setBallTo(com.brookmanholmes.billiards.game.util.BallStatus.ON_TABLE, ball);
                break;
            default:
                Log.i(TAG, "This probably shouldn't be called and I'm not sure how to handle it if so...");
        }
    }

    public static class BallStatus {
        public final TableStatus tableStatus;

        public BallStatus(TableStatus tableStatus) {
            this.tableStatus = tableStatus;
        }
    }

}
