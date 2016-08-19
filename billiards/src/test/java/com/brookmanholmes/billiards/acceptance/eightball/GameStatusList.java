package com.brookmanholmes.billiards.acceptance.eightball;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.game.util.PlayerColor.OPEN;
import static com.brookmanholmes.billiards.game.util.PlayerColor.SOLIDS;
import static com.brookmanholmes.billiards.game.util.PlayerColor.STRIPES;
import static com.brookmanholmes.billiards.game.util.PlayerTurn.OPPONENT;
import static com.brookmanholmes.billiards.game.util.PlayerTurn.PLAYER;

/**
 * Created by Brookman Holmes on 11/16/2015.
 */
public class GameStatusList {
    private static final GameStatus startOfMatch = playerBreaking();
    private static final GameStatus afterTurn1 = opponentBreaking();
    private static final GameStatus afterTurn2 = opponentIsBreaker().turn(PLAYER).removeBalls(4, 9).playerColor(SOLIDS).currentPlayerColor(SOLIDS).build();
    private static final GameStatus afterTurn3 = opponentIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3, 4, 5, 5, 6, 7, 9).playerColor(SOLIDS).currentPlayerColor(STRIPES).build();
    private static final GameStatus afterTurn4 = playerBreaking();
    private static final GameStatus afterTurn5 = playerIsBreaker().turn(OPPONENT).removeBalls(1, 2).playerColor(SOLIDS).currentPlayerColor(STRIPES).build();
    private static final GameStatus afterTurn6 = opponentBreaking();
    private static final GameStatus afterTurn7 = opponentIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).reBreak().build();
    private static final GameStatus afterTurn8 = opponentIsBreaker().turn(PLAYER).newGame().build();
    private static final GameStatus afterTurn9 = opponentIsBreaker().turn(OPPONENT).removeBalls(1, 7, 9, 10, 11).playerColor(STRIPES).currentPlayerColor(SOLIDS).build();
    private static final GameStatus afterTurn10 = playerBreaking();
    private static final GameStatus afterTurn11 = opponentBreaking();
    private static final GameStatus afterTurn12 = opponentIsBreaker().turn(PLAYER).playerColor(STRIPES).currentPlayerColor(STRIPES).removeBalls(10, 1).safetyLastTurn().build();
    private static final GameStatus afterTurn13 = opponentIsBreaker().turn(OPPONENT).playerColor(STRIPES).currentPlayerColor(SOLIDS).removeBalls(10, 1).safetyLastTurn().build();
    private static final GameStatus afterTurn14 = playerBreaking();
    private static final GameStatus afterTurn15 = playerIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).reBreak().build();
    private static final GameStatus afterTurn16 = playerBreaking();
    private static final GameStatus afterTurn17 = playerBreaking();
    private static final GameStatus afterTurn18 = opponentBreaking();

    private static GameStatus.Builder status() {
        return new GameStatus.Builder(GameType.BCA_EIGHT_BALL)
                .breakType(BreakType.ALTERNATE)
                .playerColor(OPEN);
    }

    private static GameStatus playerBreaking() {
        return status().turn(PLAYER).breaker(PLAYER).newGame().build();
    }

    private static GameStatus opponentBreaking() {
        return status().turn(OPPONENT).breaker(OPPONENT).newGame().build();
    }

    private static GameStatus.Builder playerIsBreaker() {
        return status().breaker(PLAYER);
    }

    private static GameStatus.Builder opponentIsBreaker() {
        return status().breaker(OPPONENT);
    }

    public static List<GameStatus> getGameStatuses() {
        return Arrays.asList(
                startOfMatch, afterTurn1, afterTurn2, afterTurn3, afterTurn4, afterTurn5,
                afterTurn6, afterTurn7, afterTurn8, afterTurn9, afterTurn10, afterTurn11,
                afterTurn12, afterTurn13, afterTurn14, afterTurn15, afterTurn16, afterTurn17,
                afterTurn18
        );
    }

}
