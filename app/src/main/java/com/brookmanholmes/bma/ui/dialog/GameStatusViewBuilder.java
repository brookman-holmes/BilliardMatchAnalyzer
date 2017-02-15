package com.brookmanholmes.bma.ui.dialog;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerColor;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.bma.utils.ConversionUtils.getBreakType;
import static com.brookmanholmes.bma.utils.ConversionUtils.getGameTypeString;

/**
 * Created by Brookman Holmes on 3/23/2016.
 */
public class GameStatusViewBuilder {
    @Bind(R.id.gameStatus)
    TextView gameStatus;
    @Bind(R.id.breakInfo)
    TextView breakInfo;
    @Bind(R.id.turnInfo)
    TextView turnInfo;
    @Bind(R.id.playerFoulInfo)
    TextView playerFoulInfo;
    @Bind(R.id.opponentFoulInfo)
    TextView opponentFoulInfo;
    @Bind(R.id.colorInfo)
    TextView colorInfo;
    @Bind(R.id.pushStatus)
    TextView pushStatus;
    @Bind(R.id.playerStatsTracked)
    TextView playerStatsTracked;
    @Bind(R.id.opponentStatsTracked)
    TextView opponentStatsTracked;

    private GameStatusViewBuilder(Match match, View view) {
        ButterKnife.bind(this, view);
        Context context = view.getContext();
        String playerName = match.getPlayer().getName();
        String opponentName = match.getOpponent().getName();

        gameStatus.setText(context.getString(R.string.label_game,
                getGameTypeString(context, match.getGameStatus().gameType)));

        if (match.getGameStatus().gameType != GameType.STRAIGHT_POOL)
            breakInfo.setText(context.getString(R.string.out_of_strings,
                getBreakType(context, match.getGameStatus().breakType, playerName, opponentName),
                getLastBreaker(context, match)));
        else breakInfo.setVisibility(View.GONE);

        turnInfo.setText(getPlayerTurnString(context, match));

        if (match.getGameStatus().gameType == GameType.APA_EIGHT_BALL ||
                match.getGameStatus().gameType == GameType.BCA_EIGHT_BALL) {
            playerFoulInfo.setVisibility(View.GONE);
            opponentFoulInfo.setVisibility(View.GONE);
            colorInfo.setText(getColorInfo(context, match));
            pushStatus.setVisibility(View.GONE);
        } else {
            pushStatus.setText(getPushStatusString(context, match));
            colorInfo.setVisibility(View.GONE);
            playerFoulInfo.setText(
                    getPlayerFoulsString(context, playerName, match.getGameStatus().consecutivePlayerFouls));
            opponentFoulInfo.setText(
                    getPlayerFoulsString(context, opponentName, match.getGameStatus().consecutiveOpponentFouls));
        }

        playerStatsTracked.setText("Stats tracked for " + playerName + ": " +
                MatchDialogHelperUtils.getDataCollectionStringsFromSet(
                        view.getContext(),
                        match.getDetails(),
                        Match.StatsDetail.getPlayerStatsTracked()));
        opponentStatsTracked.setText("Stats tracked for " + opponentName + ": " +
                MatchDialogHelperUtils.getDataCollectionStringsFromSet(
                        view.getContext(),
                        match.getDetails(),
                        Match.StatsDetail.getOpponentStatsTracked()));
    }

    private static String getLastBreaker(Context context, Match match) {
        GameStatus gameStatus = match.getGameStatus();
        String playerName = getPlayerName(match, gameStatus.breaker);
        if (gameStatus.newGame) {
            return context.getString(R.string.label_breaker, playerName);
        } else {
            return context.getString(R.string.label_broke, playerName);
        }
    }

    private static String getPlayerName(Match match, PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER)
            return match.getPlayer().getName();
        else return match.getOpponent().getName();
    }

    private static String getPlayerTurnString(Context context, Match match) {
        if (match.getGameStatus().newGame)
            return context.getString(R.string.label_start_new_game);
        else
            return context.getString(R.string.label_turn, getPlayerName(match, match.getGameStatus().turn));
    }

    private static String getPlayerFoulsString(Context context, String name, int fouls) {
        return context.getString(R.string.label_fouls, name, fouls);
    }

    private static String getColorInfo(Context context, Match match) {
        if (match.getGameStatus().playerColor == PlayerColor.OPEN)
            return context.getString(R.string.open_table);
        else return context.getString(R.string.out_of_strings,
                getPlayerColorString(context, match), getOpponentColorString(context, match));
    }

    private static String getPlayerColorString(Context context, Match match) {
        if (match.getGameStatus().playerColor == PlayerColor.OPEN)
            return "";
        else if (match.getGameStatus().playerColor == PlayerColor.STRIPES)
            return context.getString(R.string.stripes_table, match.getPlayer().getName());
        else return context.getString(R.string.solids_table, match.getPlayer().getName());
    }

    private static String getOpponentColorString(Context context, Match match) {
        if (match.getGameStatus().playerColor == PlayerColor.OPEN)
            return "";
        else if (match.getGameStatus().playerColor == PlayerColor.SOLIDS)
            return context.getString(R.string.stripes_table, match.getOpponent().getName());
        else return context.getString(R.string.solids_table, match.getOpponent().getName());
    }

    private static String getPushStatusString(Context context, Match match) {
        if (match.getGameStatus().allowPush && !match.getGameStatus().newGame)
            return context.getString(R.string.allow_push, getPlayerName(match, match.getGameStatus().turn));
        else if (match.getGameStatus().allowTurnSkip)
            return context.getString(R.string.allow_turn_skip, getPlayerName(match, match.getGameStatus().turn));
        else
            return context.getString(R.string.no_push_no_turn_skip);
    }

    private static void setBallsOnTable(Match match, GridLayout ballContainer) {
        while (ballContainer.getChildCount() > match.getGameStatus().MAX_BALLS) // remove balls that are not on the table
            ballContainer.removeViewAt(ballContainer.getChildCount() - 1);

        for (int ball = 0; ball < ballContainer.getChildCount(); ball++) {
            if (!match.getGameStatus().ballsOnTable.contains(ball + 1))
                ballContainer.getChildAt(ball).setAlpha(.4f);
        }
    }

    public static void bindView(Match match, View view) {
        new GameStatusViewBuilder(match, view);
    }
}
