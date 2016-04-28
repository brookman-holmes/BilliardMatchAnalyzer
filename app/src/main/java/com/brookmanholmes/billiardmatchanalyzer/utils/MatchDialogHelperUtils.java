package com.brookmanholmes.billiardmatchanalyzer.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.InvalidBallException;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/27/2016.
 */
public class MatchDialogHelperUtils {
    public static final String NEW_GAME_KEY = "new game";
    public static final String OPPOSING_PLAYER_NAME_KEY = "opposing player name";
    public static final String CURRENT_PLAYER_NAME_KEY = "player name";
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
    public static final String STATS_LEVEL_KEY = "stats level";
    public static final String OPPONENT_FOULS_KEY = "opponent fouls";
    public static final String PLAYER_FOULS_KEY = "player fouls";
    public static final String PLAYER_COLOR_KEY = "player color";


    private MatchDialogHelperUtils() {
    }

    public static Bundle createBundleFromMatch(Match<?> match) {
        Bundle args = new Bundle();

        args.putBoolean(ALLOW_TURN_SKIP_KEY, match.getGameStatus().allowTurnSkip);
        args.putBoolean(NEW_GAME_KEY, match.getGameStatus().newGame);
        args.putBoolean(ALLOW_PUSH_KEY, match.getGameStatus().allowPush);
        args.putString(CURRENT_PLAYER_NAME_KEY, getCurrentPlayersName(match));
        args.putString(OPPOSING_PLAYER_NAME_KEY, getOpposingPlayersName(match));
        args.putString(GAME_TYPE_KEY, match.getGameStatus().gameType.name());
        args.putIntegerArrayList(BALLS_ON_TABLE_KEY, new ArrayList<>(match.getGameStatus().ballsOnTable));
        args.putString(TURN_KEY, match.getGameStatus().turn.name());
        args.putString(CURRENT_PLAYER_COLOR_KEY, match.getGameStatus().currentPlayerColor.name());
        args.putString(BREAKER_KEY, match.getGameStatus().breaker.name());
        args.putInt(CONSECUTIVE_FOULS_KEY, match.getGameStatus().currentPlayerConsecutiveFouls);
        args.putString(BREAK_TYPE_KEY, match.getGameStatus().breakType.name());
        args.putBoolean(SUCCESSFUL_SAFE_KEY, match.getGameStatus().opponentPlayedSuccessfulSafe);
        args.putBoolean(ALLOW_BREAK_AGAIN_KEY, match.getGameStatus().playerAllowedToBreakAgain);
        args.putString(STATS_LEVEL_KEY, match.getAdvStats().name());
        args.putInt(PLAYER_FOULS_KEY, match.getGameStatus().consecutivePlayerFouls);
        args.putInt(OPPONENT_FOULS_KEY, match.getGameStatus().consecutiveOpponentFouls);
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

    public static String getOpposingPlayersName(Match<?> match) {
        switch (match.getGameStatus().turn) {
            case PLAYER:
                return match.getOpponent().getName();
            case OPPONENT:
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

    @LayoutRes public static int getLayoutByGameType(GameType gameType) {
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
        gameStatus.consecutiveOpponentFouls(args.getInt(OPPONENT_FOULS_KEY));
        gameStatus.consecutivePlayerFouls(args.getInt(PLAYER_FOULS_KEY));
        gameStatus.currentPlayerConsecutiveFouls(args.getInt(CONSECUTIVE_FOULS_KEY));
        gameStatus.setBalls(args.getIntegerArrayList(BALLS_ON_TABLE_KEY));

        return gameStatus.build();
    }

    public static int convertIdToBall(@IdRes int id) {
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

    @IdRes public static int convertBallToId(int ball) {
        switch (ball) {
            case 1:
                return R.id.one_ball;
            case 2:
                return R.id.two_ball;
            case 3:
                return R.id.three_ball;
            case 4:
                return R.id.four_ball;
            case 5:
                return R.id.five_ball;
            case 6:
                return R.id.six_ball;
            case 7:
                return R.id.seven_ball;
            case 8:
                return R.id.eight_ball;
            case 9:
                return R.id.nine_ball;
            case 10:
                return R.id.ten_ball;
            case 11:
                return R.id.eleven_ball;
            case 12:
                return R.id.twelve_ball;
            case 13:
                return R.id.thirteen_ball;
            case 14:
                return R.id.fourteen_ball;
            case 15:
                return R.id.fifteen_ball;
            default:
                throw new InvalidBallException("Ball must be between 1-15");
        }
    }

    public static void setViewToBallMade(ImageView view) {
        if (view != null)
            view.setImageLevel(2);
    }

    public static void setViewToBallDead(ImageView view) {
        if (view != null)
            view.setImageLevel(3);
    }

    public static void setViewToBallOnTable(ImageView view) {
        if (view != null) {
            view.setImageLevel(1);
        }
    }

    public static void setViewToBallOffTable(ImageView view) {
        if (view != null)
            view.setImageLevel(0);
    }

    @StringRes public static int convertTurnEndToStringRes(TurnEnd end) {
        switch (end) {
            case SAFETY:
                return R.string.turn_safety;
            case SAFETY_ERROR:
                return R.string.turn_safety_error;
            case MISS:
                return R.string.turn_miss;
            case BREAK_MISS:
                return R.string.turn_break_miss;
            case GAME_WON:
                return R.string.turn_won_game;
            case PUSH_SHOT:
                return R.string.turn_push;
            case SKIP_TURN:
                return R.string.turn_skip;
            case CURRENT_PLAYER_BREAKS_AGAIN:
                return R.string.turn_current_player_breaks;
            case OPPONENT_BREAKS_AGAIN:
                return R.string.turn_non_current_player_breaks;
            case CONTINUE_WITH_GAME:
                return R.string.turn_continue_game;
            case ILLEGAL_BREAK:
                return R.string.turn_illegal_break;
            default:
                throw new IllegalArgumentException("No such conversion for: " + end.toString());
        }
    }
}
