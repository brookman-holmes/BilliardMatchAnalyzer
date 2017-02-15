package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;

import java.util.List;

/**
 * Created by Brookman Holmes on 1/22/2017.
 */

public class BallsOnTableBinder extends BindingAdapter {
    private static final String TAG = "BallsOnTableBinder";
    public boolean smallScreen = false;
    private ObservableList<Integer> ballsOnTable;
    private int MAX_BALLS = 0;

    // empty constructor that disappears this view when it's not needed
    BallsOnTableBinder() {
        super(true, false);
        ballsOnTable = new ObservableArrayList<>();
    }

    BallsOnTableBinder(GameStatus gameStatus, float screenDips) {
        super(true, showCard(gameStatus.gameType));

        ballsOnTable = new ObservableArrayList<>();
        MAX_BALLS = gameStatus.MAX_BALLS;

        update(gameStatus.ballsOnTable);

        if (screenDips < 380)
            smallScreen = true;
        //else if (screenDips > 700 && screenDips < 1000)
        //smallScreen = true;

        smallScreen = smallScreen && MAX_BALLS == 15 && gameStatus.GAME_BALL == 8;
    }

    private static boolean showCard(GameType gameType) {
        if (gameType == GameType.STRAIGHT_POOL)
            return false;
        else if (gameType.isGhostGame())
            return false;
        return true;
    }

    public void update(List<Integer> ballsOnTable) {
        if (showCard) {
            this.ballsOnTable.clear();
            this.ballsOnTable.addAll(ballsOnTable);
            notifyChange();
        }
    }

    public int getVisibility(int ball) {
        if (ball > MAX_BALLS)
            return View.GONE;
        else return View.VISIBLE;
    }

    public float getAlpha(int ball) {
        if (ballsOnTable.contains(ball))
            return 1;
        else return .1f;
    }
}
