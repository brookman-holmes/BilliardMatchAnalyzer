package com.brookmanholmes.billiardmatchanalyzer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;

/**
 * Created by Brookman Holmes on 8/2/2016.
 */
public class ConversionUtils {
    private ConversionUtils() {

    }

    public static float convertDpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static String getGameTypeString(Context context, GameType gameType) {
        switch (gameType) {
            case APA_EIGHT_BALL:
                return context.getString(R.string.game_apa_eight);
            case APA_NINE_BALL:
                return context.getString(R.string.game_apa_nine);
            case BCA_EIGHT_BALL:
                return context.getString(R.string.game_bca_eight);
            case BCA_NINE_BALL:
                return context.getString(R.string.game_bca_nine);
            case BCA_TEN_BALL:
                return context.getString(R.string.game_bca_ten);
            case AMERICAN_ROTATION:
                return context.getString(R.string.game_american_rotation);
            case STRAIGHT_POOL:
                return context.getString(R.string.game_straight);
            default:
                throw new InvalidGameTypeException("No such GameType: " + gameType);
        }
    }

    public static String getBreakType(Context context, BreakType breakType, String playerName, String opponentName) {
        switch (breakType) {
            case WINNER:
                return context.getString(R.string.break_winner);
            case LOSER:
                return context.getString(R.string.break_loser);
            case ALTERNATE:
                return context.getString(R.string.break_alternate);
            case PLAYER:
                return context.getString(R.string.break_player, playerName);
            case OPPONENT:
                return context.getString(R.string.break_player, opponentName);
            case GHOST:
                return context.getString(R.string.break_player, playerName);
            default:
                throw new IllegalArgumentException();
        }
    }
}
