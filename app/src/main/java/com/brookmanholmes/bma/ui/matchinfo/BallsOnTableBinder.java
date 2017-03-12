package com.brookmanholmes.bma.ui.matchinfo;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.view.View;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.player.Player;

/**
 * Created by Brookman Holmes on 1/22/2017.
 */

public class BallsOnTableBinder extends BindingAdapter {
    private static final String TAG = "BallsOnTableBinder";

    public boolean smallScreen = false;
    private ObservableList<Integer> ballsOnTable = new ObservableArrayList<>();
    private int MAX_BALLS = 0;

    // empty constructor that disappears this view when it's not needed
    BallsOnTableBinder(GameType gameType, float screenDips) {
        super("", true, showCard(gameType));

        if (screenDips < 380 || (screenDips >= 700 && screenDips < 1200))
            smallScreen = true;

        smallScreen = smallScreen && gameType.is8Ball();
    }

    private static boolean showCard(GameType gameType) {
        if (gameType == GameType.STRAIGHT_POOL)
            return false;
        else if (gameType == GameType.ALL)
            return false;
        return true;
    }

    @Override
    public void update(Player player, Player opponent, @Nullable GameStatus gameStatus) {
        if (showCard && gameStatus != null) {
            MAX_BALLS = gameStatus.gameType.getMaxBalls();

            smallScreen = smallScreen && gameStatus.gameType.is8Ball();
            this.ballsOnTable.clear();
            this.ballsOnTable.addAll(gameStatus.ballsOnTable);
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
