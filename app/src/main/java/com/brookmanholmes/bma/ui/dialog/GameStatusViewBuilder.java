package com.brookmanholmes.bma.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.brookmanholmes.bma.utils.ConversionUtils.getBreakType;
import static com.brookmanholmes.bma.utils.ConversionUtils.getGameTypeString;

/**
 * Created by Brookman Holmes on 3/23/2016.
 */
public class GameStatusViewBuilder {
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.gameStatus) TextView gameStatus;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.breakInfo) TextView breakInfo;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.turnInfo) TextView turnInfo;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.playerFoulInfo) TextView playerFoulInfo;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.opponentFoulInfo) TextView opponentFoulInfo;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.colorInfo) TextView colorInfo;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.ballContainer) GridLayout ballContainer;
    @SuppressWarnings("WeakerAccess")
    @Bind(R.id.pushStatus) TextView pushStatus;

    private GameStatusViewBuilder(Match match, View view) {
        ButterKnife.bind(this, view);
        Context context = view.getContext();
        String playerName = match.getPlayer().getName();
        String opponentName = match.getOpponent().getName();

        gameStatus.setText(
                String.format(Locale.getDefault(), "Game: %1$s",
                        getGameTypeString(context, match.getGameStatus().gameType)));
        breakInfo.setText(String.format(Locale.getDefault(), "%1$s / %2$s",
                getBreakType(context, match.getGameStatus().breakType, playerName, opponentName),
                getLastBreaker(match)));
        turnInfo.setText(getPlayerTurnString(match));
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
                    getPlayerFoulsString(playerName, match.getGameStatus().consecutivePlayerFouls));
            opponentFoulInfo.setText(
                    getPlayerFoulsString(opponentName, match.getGameStatus().consecutiveOpponentFouls));
        }

        setBallsOnTable(match, ballContainer);
    }

    private static String getLastBreaker(Match match) {
        GameStatus gameStatus = match.getGameStatus();
        String playerName = getPlayerName(match, gameStatus.breaker);
        if (gameStatus.newGame) {
            return String.format(Locale.getDefault(), "%1$s is breaking", playerName);
        } else {
            return String.format(Locale.getDefault(), "%1$s broke last", playerName);
        }
    }

    private static String getPlayerName(Match match, PlayerTurn turn) {
        if (turn == PlayerTurn.PLAYER)
            return match.getPlayer().getName();
        else return match.getOpponent().getName();
    }

    private static String getPlayerTurnString(Match match) {
        if (match.getGameStatus().newGame)
            return "Start of new game";
        else return String.format(Locale.getDefault(), "%1$s\'s turn", getPlayerName(match, match.getGameStatus().turn));
    }

    private static String getPlayerFoulsString(String name, int fouls) {
        return String.format(Locale.getDefault(), "%1$s is on %2$d fouls", name, fouls);
    }

    private static String getColorInfo(Context context, Match match) {
        if (match.getGameStatus().playerColor == PlayerColor.OPEN)
            return context.getString(R.string.open_table);
        else return String.format(Locale.getDefault(), "%1$s and %2$s",
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
        //reverse loop because you want to remove the last fucking ball and not change the count of the children by removing from the front
        for (int ball = ballContainer.getChildCount() - 1; ball >= 0; ball--) {
            if (!match.getGameStatus().ballsOnTable.contains(ball + 1))
                ballContainer.removeViewAt(ball);
        }
    }

    public static void bindView(Match match, View view) {
        new GameStatusViewBuilder(match, view);
    }
}
