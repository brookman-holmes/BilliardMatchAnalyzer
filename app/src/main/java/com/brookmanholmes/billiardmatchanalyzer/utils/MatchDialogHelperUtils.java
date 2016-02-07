package com.brookmanholmes.billiardmatchanalyzer.utils;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.inning.InvalidBallException;
import com.brookmanholmes.billiards.match.Match;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/27/2016.
 */
public class MatchDialogHelperUtils {
    public static final String NEW_GAME_KEY = "new game";
    public static final String PLAYER_NAME_KEY = "player name";
    public static final String GAME_TYPE_KEY = "game type";
    public static final String BALLS_ON_TABLE_KEY = "balls on table";
    public static final String TURN_KEY = "turn";
    public static final String ALLOW_PUSH_KEY = "allow push";
    public static final String ALLOW_TURN_SKIP_KEY = "allow skip";
    public static final String ALLOW_BREAK_AGAIN_KEY = "re break";
    public static final String CURRENT_PLAYER_COLOR_KEY = "current player color";
    public static final String BREAKER_KEY = "breaker";
    public static final String CONSECUTIVE_FOULS_KEY = "current player consecutive fouls";
    public static final String BREAK_TYPE_KEY = "break type";
    public static final String SUCCESSFUL_SAFE_KEY = "successful safe";

    private MatchDialogHelperUtils() {
    }

    public static Bundle createBundleFromMatch(Match<?> match) {
        Bundle args = new Bundle();
        args.putBoolean(NEW_GAME_KEY, match.getGameStatus().newGame);
        args.putString(PLAYER_NAME_KEY, getCurrentPlayersName(match));
        args.putString(GAME_TYPE_KEY, match.getGameStatus().gameType.toString());
        args.putIntegerArrayList(BALLS_ON_TABLE_KEY, new ArrayList<>(match.getGameStatus().ballsOnTable));
        args.putString(TURN_KEY, match.getGameStatus().turn.toString());
        args.putString(CURRENT_PLAYER_COLOR_KEY, match.getGameStatus().currentPlayerColor.toString());
        args.putString(BREAKER_KEY, match.getGameStatus().breaker.toString());
        args.putInt(CONSECUTIVE_FOULS_KEY, match.getGameStatus().currentPlayerConsecutiveFouls);
        args.putString(BREAK_TYPE_KEY, match.getGameStatus().breakType.toString());
        args.putBoolean(SUCCESSFUL_SAFE_KEY, match.getGameStatus().opponentPlayedSuccessfulSafe);
        args.putBoolean(ALLOW_BREAK_AGAIN_KEY, match.getGameStatus().playerAllowedToBreakAgain);

        return args;
    }

    public static String getCurrentPlayersName(Match<?> match) {
        switch (match.getGameStatus().turn) {
            case OPPONENT:
                return match.getOpponent().getName();
            case PLAYER:
                return match.getPlayer().getName();
            default:
                throw new IllegalStateException("There is no such player turn: "
                        + match.getGameStatus().turn.toString());
        }
    }

    public static int getGameBall(Match<?> match) {
        switch (match.getGameStatus().gameType) {
            case APA_EIGHT_BALL:
                return 8;
            case APA_NINE_BALL:
                return 9;
            case BCA_EIGHT_BALL:
                return 8;
            case BCA_TEN_BALL:
                return 10;
            case BCA_NINE_BALL:
                return 9;
            default:
                throw new InvalidGameTypeException("That game is probably not implemented yet: " + match.getGameStatus().gameType.toString());
        }
    }

    @LayoutRes
    public static int getLayoutByGameType(GameType gameType) {
        switch (gameType) {
            case APA_EIGHT_BALL:
                return R.layout.select_eight_ball_dialog;
            case APA_NINE_BALL:
                return R.layout.select_nine_ball_dialog;
            case BCA_EIGHT_BALL:
                return R.layout.select_eight_ball_dialog;
            case BCA_TEN_BALL:
                return R.layout.select_ten_ball_dialog;
            case BCA_NINE_BALL:
                return R.layout.select_nine_ball_dialog;
            default:
                throw new InvalidGameTypeException("Game type not implemented yet: " + gameType.toString());
        }
    }

    public static GameStatus createGameStatusFromBundle(Bundle args) {
        GameStatus.Builder gameStatus = new GameStatus.Builder(GameType.valueOf(args.getString(GAME_TYPE_KEY)));

        if (args.getBoolean(NEW_GAME_KEY)) gameStatus.newGame();
        if (args.getBoolean(ALLOW_PUSH_KEY)) gameStatus.allowPush();
        if (args.getBoolean(ALLOW_TURN_SKIP_KEY)) gameStatus.allowSkip();
        if (args.getBoolean(ALLOW_BREAK_AGAIN_KEY)) gameStatus.reBreak();
        gameStatus.breakType(BreakType.valueOf(args.getString(BREAK_TYPE_KEY)));
        if (args.getBoolean(SUCCESSFUL_SAFE_KEY)) gameStatus.safetyLastTurn();
        gameStatus.turn(PlayerTurn.valueOf(args.getString(TURN_KEY)));
        gameStatus.breaker(PlayerTurn.valueOf(args.getString(BREAKER_KEY)));
        gameStatus.currentPlayerColor(PlayerColor.valueOf(args.getString(CURRENT_PLAYER_COLOR_KEY)));
        // // TODO: 1/27/2016 ballsOnTable is not set

        return gameStatus.build();
    }

    public static int convertIdToBall(int id) {
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

    public static void setViewToBallMade(View view) {

    }

    public static void setViewToBallDead(View view) {

    }

    public static void setViewToBallOnTable(View view) {

    }
}