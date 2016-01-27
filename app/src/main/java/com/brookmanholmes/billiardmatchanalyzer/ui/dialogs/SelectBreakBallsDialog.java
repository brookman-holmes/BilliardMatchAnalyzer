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
import com.brookmanholmes.billiards.game.util.BallStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.InvalidBallException;
import com.brookmanholmes.billiards.inning.TableStatus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils.PLAYER_NAME_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils.getLayoutByGameType;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectBreakBallsDialog extends Fragment {
    private static final String TAG = "SelectBreakBallsDialog";
    TableStatus tableStatus;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.ballGrid)
    GridLayout ballGrid;
    GameType gameType;

    public static SelectBreakBallsDialog create(Bundle args) {
        SelectBreakBallsDialog fragment = new SelectBreakBallsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameType = GameType.valueOf(getArguments().getString(GAME_TYPE_KEY));

        tableStatus = TableStatus.newTable(gameType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutByGameType(gameType), container, false);
        ButterKnife.bind(this, view);
        title.setText("Select balls made on break by " + getArguments().getString(PLAYER_NAME_KEY, "--"));

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
        EventBus.getDefault().post(new BreakStatus(tableStatus));
    }

    private void setBallStatus(int ball) {
        switch (tableStatus.getBallStatus(ball)) {
            case ON_TABLE:
                tableStatus.setBallTo(BallStatus.MADE_ON_BREAK, ball);
                break;
            case MADE_ON_BREAK:
                tableStatus.setBallTo(BallStatus.DEAD_ON_BREAK, ball);
                break;
            case DEAD_ON_BREAK:
                tableStatus.setBallTo(BallStatus.ON_TABLE, ball);
                break;
            default:
                Log.i(TAG, "This probably shouldn't be called and I'm not sure how to handle it if so...");
        }
    }

    private int convertIdToBall(int id) {
        switch (id) {
            case R.id.one_ball:
                return 1;
            case R.id.two_ball:
                return 2;
            case R.id.three_ball:
                return 3;
            case R.id.four_ball:
                return 4;
            case R.id.five_ball:
                return 5;
            case R.id.six_ball:
                return 6;
            case R.id.seven_ball:
                return 7;
            case R.id.eight_ball:
                return 8;
            case R.id.nine_ball:
                return 9;
            case R.id.ten_ball:
                return 10;
            case R.id.eleven_ball:
                return 11;
            case R.id.twelve_ball:
                return 12;
            case R.id.thirteen_ball:
                return 13;
            case R.id.fourteen_ball:
                return 14;
            case R.id.fifteen_ball:
                return 15;
            default:
                throw new InvalidBallException("This ball does not exist");
        }
    }

    public static class BreakStatus {
        public final TableStatus tableStatus;

        public BreakStatus(TableStatus tableStatus) {
            this.tableStatus = tableStatus;
        }
    }
}
