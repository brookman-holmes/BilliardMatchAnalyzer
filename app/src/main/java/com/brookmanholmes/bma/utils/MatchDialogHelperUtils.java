package com.brookmanholmes.bma.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.PlayerColor;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.InvalidBallException;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;

import java.util.ArrayList;

/**
 * Created by Brookman Holmes on 1/27/2016.
 */
@SuppressWarnings("WeakerAccess")
public class MatchDialogHelperUtils {
    public static final String NEW_GAME_KEY = "new_game";
    public static final String OPPOSING_PLAYER_NAME_KEY = "opposing_player_name";
    public static final String CURRENT_PLAYER_NAME_KEY = "player_name";
    public static final String GAME_TYPE_KEY = "game_type";
    public static final String BALLS_ON_TABLE_KEY = "balls_on_table";
    public static final String TURN_KEY = "turn";
    public static final String ALLOW_PUSH_KEY = "allow_push";
    public static final String ALLOW_TURN_SKIP_KEY = "allow_skip";
    public static final String ALLOW_BREAK_AGAIN_KEY = "re_break";
    public static final String CURRENT_PLAYER_COLOR_KEY = "current_player_color";
    public static final String BREAKER_KEY = "breaker";
    public static final String CONSECUTIVE_FOULS_KEY = "current_player_consecutive_fouls";
    public static final String BREAK_TYPE_KEY = "break_type";
    public static final String SUCCESSFUL_SAFE_KEY = "successful_safe";
    public static final String STATS_LEVEL_KEY = "stats_level";
    public static final String OPPONENT_FOULS_KEY = "opponent_fouls";
    public static final String PLAYER_FOULS_KEY = "player_fouls";
    public static final String PLAYER_COLOR_KEY = "player_color";


    private MatchDialogHelperUtils() {
    }

    public static Bundle getBundle(Match match) {
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
        args.putString(STATS_LEVEL_KEY, match.getStatDetailLevel().name());
        args.putInt(PLAYER_FOULS_KEY, match.getGameStatus().consecutivePlayerFouls);
        args.putInt(OPPONENT_FOULS_KEY, match.getGameStatus().consecutiveOpponentFouls);
        return args;
    }

    /**
     * Used to report analytics to Firebase, strips the player name's from the match to protect
     * user privacy
     *
     * @param match match to be reported
     * @return bundle with all the data of the match
     */
    public static Bundle getStrippedBundle(Match match) {
        Bundle args = new Bundle();

        args.putBoolean(ALLOW_TURN_SKIP_KEY, match.getGameStatus().allowTurnSkip);
        args.putBoolean(NEW_GAME_KEY, match.getGameStatus().newGame);
        args.putBoolean(ALLOW_PUSH_KEY, match.getGameStatus().allowPush);
        args.putString(GAME_TYPE_KEY, match.getGameStatus().gameType.name());
        args.putIntegerArrayList(BALLS_ON_TABLE_KEY, new ArrayList<>(match.getGameStatus().ballsOnTable));
        args.putString(TURN_KEY, match.getGameStatus().turn.name());
        args.putString(CURRENT_PLAYER_COLOR_KEY, match.getGameStatus().currentPlayerColor.name());
        args.putString(BREAKER_KEY, match.getGameStatus().breaker.name());
        args.putInt(CONSECUTIVE_FOULS_KEY, match.getGameStatus().currentPlayerConsecutiveFouls);
        args.putString(BREAK_TYPE_KEY, match.getGameStatus().breakType.name());
        args.putBoolean(SUCCESSFUL_SAFE_KEY, match.getGameStatus().opponentPlayedSuccessfulSafe);
        args.putBoolean(ALLOW_BREAK_AGAIN_KEY, match.getGameStatus().playerAllowedToBreakAgain);
        args.putString(STATS_LEVEL_KEY, match.getStatDetailLevel().name());
        args.putInt(PLAYER_FOULS_KEY, match.getGameStatus().consecutivePlayerFouls);
        args.putInt(OPPONENT_FOULS_KEY, match.getGameStatus().consecutiveOpponentFouls);
        return args;
    }

    public static String getCurrentPlayersName(Match match) {
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

    public static String getOpposingPlayersName(Match match) {
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

    public static int getGameBall(Match match) {
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
            case BCA_GHOST_EIGHT_BALL:
                return 8;
            case BCA_GHOST_NINE_BALL:
                return 9;
            case BCA_GHOST_TEN_BALL:
                return 10;
            case APA_GHOST_EIGHT_BALL:
                return 8;
            case APA_GHOST_NINE_BALL:
                return 9;
            default:
                throw new InvalidGameTypeException("That game is probably not implemented yet: " + match.getGameStatus().gameType.toString());
        }
    }

    @LayoutRes
    public static int getLayout(GameType gameType) {
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
            case APA_GHOST_EIGHT_BALL:
                return R.layout.select_eight_ball_dialog;
            case APA_GHOST_NINE_BALL:
                return R.layout.select_nine_ball_dialog;
            case BCA_GHOST_EIGHT_BALL:
                return R.layout.select_eight_ball_dialog;
            case BCA_GHOST_TEN_BALL:
                return R.layout.select_ten_ball_dialog;
            case BCA_GHOST_NINE_BALL:
                return R.layout.select_nine_ball_dialog;
            default:
                throw new InvalidGameTypeException("Game type not implemented yet: " + gameType.toString());
        }
    }

    public static GameStatus getGameStatus(Bundle args) {
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

    @IdRes
    public static int convertBallToId(int ball) {
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

    public static String convertTurnEndToString(Context context, TurnEnd end, String currentPlayer, String opposingPlayer) {
        switch (end) {
            case SAFETY:
                return context.getString(R.string.turn_safety);
            case SAFETY_ERROR:
                return context.getString(R.string.turn_safety_error);
            case MISS:
                return context.getString(R.string.turn_miss);
            case BREAK_MISS:
                return context.getString(R.string.turn_break_miss);
            case GAME_WON:
                return context.getString(R.string.turn_won_game);
            case PUSH_SHOT:
                return context.getString(R.string.turn_push);
            case SKIP_TURN:
                return context.getString(R.string.turn_skip);
            case CURRENT_PLAYER_BREAKS_AGAIN:
                return context.getString(R.string.turn_current_player_breaks, currentPlayer);
            case OPPONENT_BREAKS_AGAIN:
                return context.getString(R.string.turn_non_current_player_breaks, opposingPlayer);
            case CONTINUE_WITH_GAME:
                return context.getString(R.string.turn_continue_game);
            case ILLEGAL_BREAK:
                return context.getString(R.string.turn_illegal_break);
            default:
                throw new IllegalArgumentException("No such conversion for: " + end.toString());
        }
    }

    public static AdvStats.Angle convertStringToAngle(Context context, String angle) {
        if (angle.equals(context.getString(R.string.one_rail)))
            return AdvStats.Angle.ONE_RAIL;
        else if (angle.equals(context.getString(R.string.two_rail)))
            return AdvStats.Angle.TWO_RAIL;
        else if (angle.equals(context.getString(R.string.three_rail)))
            return AdvStats.Angle.THREE_RAIL;
        else if (angle.equals(context.getString(R.string.four_rail)))
            return AdvStats.Angle.FOUR_RAIL;
        else if (angle.equals(context.getString(R.string.five_rail)))
            return AdvStats.Angle.FIVE_RAIL;
        else if (angle.equals(context.getString(R.string.angle_five)))
            return AdvStats.Angle.FIVE;
        else if (angle.equals(context.getString(R.string.angle_ten)))
            return AdvStats.Angle.TEN;
        else if (angle.equals(context.getString(R.string.angle_fifteen)))
            return AdvStats.Angle.FIFTEEN;
        else if (angle.equals(context.getString(R.string.angle_twenty)))
            return AdvStats.Angle.TWENTY;
        else if (angle.equals(context.getString(R.string.angle_twenty_five)))
            return AdvStats.Angle.TWENTY_FIVE;
        else if (angle.equals(context.getString(R.string.angle_thirty)))
            return AdvStats.Angle.THIRTY;
        else if (angle.equals(context.getString(R.string.angle_thirty_five)))
            return AdvStats.Angle.THIRTY_FIVE;
        else if (angle.equals(context.getString(R.string.angle_forty)))
            return AdvStats.Angle.FORTY;
        else if (angle.equals(context.getString(R.string.angle_forty_five)))
            return AdvStats.Angle.FORTY_FIVE;
        else if (angle.equals(context.getString(R.string.angle_fifty)))
            return AdvStats.Angle.FIFTY;
        else if (angle.equals(context.getString(R.string.angle_fifty_five)))
            return AdvStats.Angle.FIFTY_FIVE;
        else if (angle.equals(context.getString(R.string.angle_sixty)))
            return AdvStats.Angle.SIXTY;
        else if (angle.equals(context.getString(R.string.angle_sixty_five)))
            return AdvStats.Angle.SIXTY_FIVE;
        else if (angle.equals(context.getString(R.string.angle_seventy)))
            return AdvStats.Angle.SEVENTY;
        else if (angle.equals(context.getString(R.string.angle_seventy_five)))
            return AdvStats.Angle.SEVENTY_FIVE;
        else if (angle.equals(context.getString(R.string.angle_eighty)))
            return AdvStats.Angle.EIGHTY;
        else if (angle.equals(context.getString(R.string.bank_natural)))
            return AdvStats.Angle.NATURAL;
        else if (angle.equals(context.getString(R.string.bank_crossover)))
            return AdvStats.Angle.CROSSOVER;
        else if (angle.equals(context.getString(R.string.bank_long_rail)))
            return AdvStats.Angle.LONG_RAIL;
        else if (angle.equals(context.getString(R.string.bank_short_rail)))
            return AdvStats.Angle.SHORT_RAIL;
        else
            throw new IllegalArgumentException("No such conversion between string and AdvStats.Angle: " + angle);
    }

    public static AdvStats.ShotType convertStringToShotType(Context context, String shot) {
        if (shot.equals(context.getString(R.string.miss_cut)))
            return AdvStats.ShotType.CUT;
        else if (shot.equals(context.getString(R.string.miss_long)))
            return AdvStats.ShotType.STRAIGHT_SHOT;
        else if (shot.equals(context.getString(R.string.miss_bank)))
            return AdvStats.ShotType.BANK;
        else if (shot.equals(context.getString(R.string.miss_kick)))
            return AdvStats.ShotType.KICK;
        else if (shot.equals(context.getString(R.string.miss_combo)))
            return AdvStats.ShotType.COMBO;
        else if (shot.equals(context.getString(R.string.miss_carom)))
            return AdvStats.ShotType.CAROM;
        else if (shot.equals(context.getString(R.string.miss_jump)))
            return AdvStats.ShotType.JUMP;
        else if (shot.equals(context.getString(R.string.miss_masse)))
            return AdvStats.ShotType.MASSE;
        else if (shot.equals(context.getString(R.string.turn_safety)))
            return AdvStats.ShotType.SAFETY;
        else if (shot.equals(context.getString(R.string.turn_safety_error)))
            return AdvStats.ShotType.SAFETY_ERROR;
        else if (shot.equals(context.getString(R.string.turn_break_miss)))
            return AdvStats.ShotType.BREAK_SHOT;
        else
            throw new IllegalArgumentException("No such conversion between string and AdvStats.ShotType: " + shot);
    }

    public static AdvStats.WhyType convertStringToWhyType(Context context, String whyType) {
        if (whyType.equals(context.getString(R.string.why_position)))
            return AdvStats.WhyType.POSITION;
        else if (whyType.equals(context.getString(R.string.why_focus)))
            return AdvStats.WhyType.LACK_FOCUS;
        else if (whyType.equals(context.getString(R.string.why_jacked_up)))
            return AdvStats.WhyType.JACK_UP;
        else if (whyType.equals(context.getString(R.string.why_english)))
            return AdvStats.WhyType.ENGLISH;
        else if (whyType.equals(context.getString(R.string.why_slow)))
            return AdvStats.WhyType.TOO_SOFT;
        else if (whyType.equals(context.getString(R.string.why_fast)))
            return AdvStats.WhyType.TOO_HARD;
        else if (whyType.equals(context.getString(R.string.why_curved)))
            return AdvStats.WhyType.CURVED;
        else if (whyType.equals(context.getString(R.string.why_rail)))
            return AdvStats.WhyType.RAIL;
        else if (whyType.equals(context.getString(R.string.why_forcing)))
            return AdvStats.WhyType.FORCING;
        else if (whyType.equals(context.getString(R.string.why_tried_too_hard)))
            return AdvStats.WhyType.FORCING_II;
        else if (whyType.equals(context.getString(R.string.why_too_little_draw)))
            return AdvStats.WhyType.TOO_LITTLE_DRAW;
        else if (whyType.equals(context.getString(R.string.why_too_little_follow)))
            return AdvStats.WhyType.TOO_LITTLE_FOLLOW;
        else if (whyType.equals(context.getString(R.string.why_too_little_inside)))
            return AdvStats.WhyType.TOO_LITTLE_INSIDE;
        else if (whyType.equals(context.getString(R.string.why_too_little_outside)))
            return AdvStats.WhyType.TOO_LITTLE_OUTSIDE;
        else if (whyType.equals(context.getString(R.string.why_too_much_draw)))
            return AdvStats.WhyType.TOO_MUCH_DRAW;
        else if (whyType.equals(context.getString(R.string.why_too_much_follow)))
            return AdvStats.WhyType.TOO_MUCH_FOLLOW;
        else if (whyType.equals(context.getString(R.string.why_too_much_inside)))
            return AdvStats.WhyType.TOO_MUCH_INSIDE;
        else if (whyType.equals(context.getString(R.string.why_too_much_outside)))
            return AdvStats.WhyType.TOO_MUCH_OUTSIDE;
        else if (whyType.equals(context.getString(R.string.why_table_issues)))
            return AdvStats.WhyType.TABLE;
        else if (whyType.equals(context.getString(R.string.why_body_mechanics)))
            return AdvStats.WhyType.MECHANICS;
        else if (whyType.equals(context.getString(R.string.why_impeding_ball)))
            return AdvStats.WhyType.IMPEDING_BALL;
        else if (whyType.equals(context.getString(R.string.why_aim)))
            return AdvStats.WhyType.MISJUDGED;
        else if (whyType.equals(context.getString(R.string.why_bad_pattern)))
            return AdvStats.WhyType.PATTERN;
        else throw new IllegalArgumentException("no such conversion for " + whyType);
    }

    public static AdvStats.HowType convertStringToHowType(Context context, String howType) {
        if (howType.equals(context.getString(R.string.miscue)))
            return AdvStats.HowType.MISCUE;
        else if (howType.equals(context.getString(R.string.too_soft)))
            return AdvStats.HowType.TOO_SOFT;
        else if (howType.equals(context.getString(R.string.too_hard)))
            return AdvStats.HowType.TOO_HARD;
        else if (howType.equals(context.getString(R.string.aim_to_left)))
            return AdvStats.HowType.AIM_LEFT;
        else if (howType.equals(context.getString(R.string.aim_to_right)))
            return AdvStats.HowType.AIM_RIGHT;
        else if (howType.equals(context.getString(R.string.thick_hit)))
            return AdvStats.HowType.THICK;
        else if (howType.equals(context.getString(R.string.thin_hit)))
            return AdvStats.HowType.THIN;
        else if (howType.equals(context.getString(R.string.bank_short)))
            return AdvStats.HowType.BANK_SHORT;
        else if (howType.equals(context.getString(R.string.bank_long)))
            return AdvStats.HowType.BANK_LONG;
        else if (howType.equals(context.getString(R.string.kick_long)))
            return AdvStats.HowType.KICK_LONG;
        else if (howType.equals(context.getString(R.string.kick_short)))
            return AdvStats.HowType.KICK_SHORT;
        else if (howType.equals(context.getString(R.string.kicked_in)))
            return AdvStats.HowType.KICKED_IN;
        else if (howType.equals(context.getString(R.string.curve_early)))
            return AdvStats.HowType.CURVE_EARLY;
        else if (howType.equals(context.getString(R.string.curve_late)))
            return AdvStats.HowType.CURVE_LATE;
        else throw new IllegalArgumentException("No such conversion for " + howType);
    }

    public static AdvStats.SubType convertStringToSubType(Context context, String subType) {
        if (subType.equals(context.getString(R.string.safety_full_hook)))
            return AdvStats.SubType.FULL_HOOK;
        else if (subType.equals(context.getString(R.string.safety_partial_hook)))
            return AdvStats.SubType.PARTIAL_HOOK;
        else if (subType.equals(context.getString(R.string.safety_long_t)))
            return AdvStats.SubType.LONG_T;
        else if (subType.equals(context.getString(R.string.safety_short_t)))
            return AdvStats.SubType.SHORT_T;
        else if (subType.equals(context.getString(R.string.safety_no_shot)))
            return AdvStats.SubType.NO_DIRECT_SHOT;
        else if (subType.equals(context.getString(R.string.safety_open)))
            return AdvStats.SubType.OPEN;
        else if (subType.equals(context.getString(R.string.cut_wing)))
            return AdvStats.SubType.WING_CUT;
        else if (subType.equals(context.getString(R.string.cut_back)))
            return AdvStats.SubType.BACK_CUT;
        else if (subType.equals(context.getString(R.string.cut_rail)))
            return AdvStats.SubType.RAIL_CUT;
        else throw new IllegalArgumentException("no such conversion for " + subType);
    }

    public static
    @StringRes
    int convertWhyTypeToStringRes(AdvStats.WhyType whyType) {
        switch (whyType) {
            case POSITION:
                return R.string.why_position;
            case LACK_FOCUS:
                return R.string.why_focus;
            case JACK_UP:
                return R.string.why_jacked_up;
            case ENGLISH:
                return R.string.why_english;
            case TOO_SOFT:
                return R.string.why_slow;
            case TOO_HARD:
                return R.string.why_fast;
            case CURVED:
                return R.string.why_curved;
            case RAIL:
                return R.string.why_rail;
            case FORCING:
                return R.string.why_forcing;
            case FORCING_II:
                return R.string.why_tried_too_hard;
            case TOO_LITTLE_DRAW:
                return R.string.why_too_little_draw;
            case TOO_LITTLE_FOLLOW:
                return R.string.why_too_little_follow;
            case TOO_LITTLE_INSIDE:
                return R.string.why_too_little_inside;
            case TOO_LITTLE_OUTSIDE:
                return R.string.why_too_little_outside;
            case TOO_MUCH_DRAW:
                return R.string.why_too_much_draw;
            case TOO_MUCH_FOLLOW:
                return R.string.why_too_much_follow;
            case TOO_MUCH_INSIDE:
                return R.string.why_too_much_inside;
            case TOO_MUCH_OUTSIDE:
                return R.string.why_too_much_outside;
            case TABLE:
                return R.string.why_table_issues;
            case MECHANICS:
                return R.string.why_body_mechanics;
            case IMPEDING_BALL:
                return R.string.why_impeding_ball;
            case MISJUDGED:
                return R.string.why_aim;
            case PATTERN:
                return R.string.why_bad_pattern;
            default:
                throw new IllegalArgumentException("no conversion for " + whyType);
        }
    }

    public static TurnEnd convertStringToTurnEnd(Context context, String turnEnd,
                                                 String currentPlayer, String opposingPlayer) {
        if (turnEnd.equals(context.getString(R.string.turn_safety)))
            return TurnEnd.SAFETY;
        else if (turnEnd.equals(context.getString(R.string.turn_safety_error)))
            return TurnEnd.SAFETY_ERROR;
        else if (turnEnd.equals(context.getString(R.string.turn_break_miss)))
            return TurnEnd.BREAK_MISS;
        else if (turnEnd.equals(context.getString(R.string.turn_miss)))
            return TurnEnd.MISS;
        else if (turnEnd.equals(context.getString(R.string.turn_push)))
            return TurnEnd.PUSH_SHOT;
        else if (turnEnd.equals(context.getString(R.string.turn_skip)))
            return TurnEnd.SKIP_TURN;
        else if (turnEnd.equals(context.getString(R.string.turn_illegal_break)))
            return TurnEnd.ILLEGAL_BREAK;
        else if (turnEnd.equals(context.getString(R.string.turn_won_game)))
            return TurnEnd.GAME_WON;
        else if (turnEnd.equals(context.getString(R.string.turn_current_player_breaks, currentPlayer)))
            return TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN;
        else if (turnEnd.equals(context.getString(R.string.turn_non_current_player_breaks, opposingPlayer)))
            return TurnEnd.OPPONENT_BREAKS_AGAIN;
        else if (turnEnd.equals(context.getString(R.string.turn_continue_game)))
            return TurnEnd.CONTINUE_WITH_GAME;
        else
            throw new IllegalArgumentException("No such conversion between string and StringRes: " + turnEnd);
    }

    public static
    @StringRes
    int convertShotTypeToStringRes(AdvStats.ShotType shotType) {
        switch (shotType) {
            case CUT:
                return R.string.miss_cut;
            case STRAIGHT_SHOT:
                return R.string.miss_long;
            case BANK:
                return R.string.miss_bank;
            case KICK:
                return R.string.miss_kick;
            case COMBO:
                return R.string.miss_combo;
            case CAROM:
                return R.string.miss_carom;
            case JUMP:
                return R.string.miss_jump;
            case MASSE:
                return R.string.miss_masse;
            case SAFETY:
                return R.string.turn_safety;
            case SAFETY_ERROR:
                return R.string.turn_safety_error;
            case BREAK_SHOT:
                return R.string.turn_break_miss;
            case NONE:
                return R.string.empty_string;
            default:
                throw new IllegalArgumentException("no such conversion " + shotType);
        }
    }

    public static
    @StringRes
    int convertAngleToStringRes(AdvStats.Angle angle) {
        switch (angle) {
            case ONE_RAIL:
                return R.string.one_rail;
            case TWO_RAIL:
                return R.string.two_rail;
            case THREE_RAIL:
                return R.string.three_rail;
            case FOUR_RAIL:
                return R.string.four_rail;
            case FIVE_RAIL:
                return R.string.five_rail;
            case NATURAL:
                return R.string.bank_natural;
            case CROSSOVER:
                return R.string.bank_crossover;
            case LONG_RAIL:
                return R.string.bank_long_rail;
            case SHORT_RAIL:
                return R.string.bank_short_rail;
            case FIVE:
                return R.string.angle_five;
            case TEN:
                return R.string.angle_ten;
            case FIFTEEN:
                return R.string.angle_fifteen;
            case TWENTY:
                return R.string.angle_twenty;
            case TWENTY_FIVE:
                return R.string.angle_twenty_five;
            case THIRTY:
                return R.string.angle_thirty;
            case THIRTY_FIVE:
                return R.string.angle_thirty_five;
            case FORTY:
                return R.string.angle_forty;
            case FORTY_FIVE:
                return R.string.angle_forty_five;
            case FIFTY:
                return R.string.angle_fifty;
            case FIFTY_FIVE:
                return R.string.angle_fifty_five;
            case SIXTY:
                return R.string.angle_sixty;
            case SIXTY_FIVE:
                return R.string.angle_sixty_five;
            case SEVENTY:
                return R.string.angle_seventy;
            case SEVENTY_FIVE:
                return R.string.angle_seventy_five;
            case EIGHTY:
                return R.string.angle_eighty;
            default:
                throw new IllegalArgumentException("No such conversion for " + angle);
        }
    }

    public static
    @StringRes
    int convertSubTypeToStringRes(AdvStats.SubType shotSubtype) {
        switch (shotSubtype) {
            case FULL_HOOK:
                return R.string.safety_full_hook;
            case PARTIAL_HOOK:
                return R.string.safety_partial_hook;
            case LONG_T:
                return R.string.safety_long_t;
            case SHORT_T:
                return R.string.safety_short_t;
            case NO_DIRECT_SHOT:
                return R.string.safety_no_shot;
            case OPEN:
                return R.string.safety_open;
            case WING_CUT:
                return R.string.cut_wing;
            case BACK_CUT:
                return R.string.cut_back;
            case RAIL_CUT:
                return R.string.cut_rail;
            case NONE:
                return R.string.empty_string;
            default:
                throw new IllegalArgumentException("No such conversion for " + shotSubtype);
        }
    }

    public static
    @StringRes
    int convertHowToStringRes(AdvStats.HowType howType) {
        switch (howType) {
            case MISCUE:
                return R.string.miscue;
            case TOO_HARD:
                return R.string.too_hard;
            case TOO_SOFT:
                return R.string.too_soft;
            case AIM_LEFT:
                return R.string.aim_to_left;
            case AIM_RIGHT:
                return R.string.aim_to_right;
            case KICKED_IN:
                return R.string.kicked_in;
            case THIN:
                return R.string.thin_hit;
            case THICK:
                return R.string.thick_hit;
            case KICK_LONG:
                return R.string.kick_long;
            case KICK_SHORT:
                return R.string.kick_short;
            case BANK_LONG:
                return R.string.bank_long;
            case BANK_SHORT:
                return R.string.bank_short;
            case CURVE_EARLY:
                return R.string.curve_early;
            case CURVE_LATE:
                return R.string.curve_late;
            default:
                throw new IllegalArgumentException("No conversion for " + howType);
        }
    }
}
