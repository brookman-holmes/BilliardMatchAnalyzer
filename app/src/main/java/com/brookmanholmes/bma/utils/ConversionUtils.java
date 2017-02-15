package com.brookmanholmes.bma.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.bma.R;

import java.text.DecimalFormat;

/**
 * Created by Brookman Holmes on 8/2/2016.
 */
public class ConversionUtils {
    // formatter for percentages (e.g. .875)
    public final static DecimalFormat pctf = new DecimalFormat("#.000");
    // formatter for average number of balls made per turn (e.g. 5.33)
    public final static DecimalFormat avgf = new DecimalFormat("##.##");

    private ConversionUtils() {
    }

    public static float convertDpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static float convertPxToDp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return px / metrics.density;
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
            case APA_GHOST_EIGHT_BALL:
                return context.getString(R.string.game_apa_eight_ghost);
            case APA_GHOST_NINE_BALL:
                return context.getString(R.string.game_apa_nine_ghost);
            case BCA_GHOST_EIGHT_BALL:
                return context.getString(R.string.game_bca_eight_ghost);
            case BCA_GHOST_NINE_BALL:
                return context.getString(R.string.game_bca_nine_ghost);
            case BCA_GHOST_TEN_BALL:
                return context.getString(R.string.game_bca_ten_ghost);
            case AMERICAN_ROTATION:
                return context.getString(R.string.game_american_rotation);
            case STRAIGHT_POOL:
                return context.getString(R.string.game_straight);
            case STRAIGHT_GHOST:
                return context.getString(R.string.game_straight_ghost);
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
            default:
                throw new IllegalArgumentException();
        }
    }

    public static
    @ColorInt
    int getPctColor(Context context, double pct) {
        @ColorRes int color;
        if (pct > .9)
            color = R.color.good;
        else if (pct > .75)
            color = R.color.almost_good;
        else if (pct > .66)
            color = R.color.okay;
        else if (pct > .5)
            color = R.color.just_above_bad;
        else
            color = R.color.bad;

        return ContextCompat.getColor(context, color);
    }

    public static float convertSpToPx(Context context, int sp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static String getColorString(@ColorInt int color) {
        return String.format("#%02X%02X%02X", Color.red(color), Color.green(color), Color.blue(color));
    }
}
