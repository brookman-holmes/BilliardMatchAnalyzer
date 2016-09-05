package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerColor;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.game.PlayerTurn.OPPONENT;
import static com.brookmanholmes.billiards.game.PlayerTurn.PLAYER;

/**
 * Created by Brookman Holmes on 11/9/2015.
 */
public class GameStatusList {
    private static final GameStatus startOfMatch = newGameRobertsBreaking();
    private static final GameStatus afterTurn1 = newGameShawBreaking();
    private static final GameStatus afterTurn2 = shawIsBreaker().turn(OPPONENT).allowPush().build();
    private static final GameStatus afterTurn3 = shawIsBreaker().turn(PLAYER).allowSkip().removeBalls(7, 9).build();
    private static final GameStatus afterTurn4 = shawIsBreaker().turn(OPPONENT).removeBalls(7, 9).build();
    private static final GameStatus afterTurn5 = shawIsBreaker().turn(PLAYER).removeBalls(7, 9, 1, 2, 3, 8).build();
    private static final GameStatus afterTurn6 = shawIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).removeBalls(1, 2, 3, 4, 5, 7, 8, 9).build();
    private static final GameStatus afterTurn7 = newGameRobertsBreaking();
    private static final GameStatus afterTurn8 = robertsIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(6).build();
    private static final GameStatus afterTurn9 = newGameShawBreaking();
    private static final GameStatus afterTurn10 = newGameRobertsBreaking();
    private static final GameStatus afterTurn11 = newGameShawBreaking();
    private static final GameStatus afterTurn12 = shawIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3).safetyLastTurn().build();
    private static final GameStatus afterTurn13 = shawIsBreaker().turn(PLAYER).removeBalls(1, 2, 3).build();
    private static final GameStatus afterTurn14 = newGameRobertsBreaking();
    private static final GameStatus afterTurn15 = robertsIsBreaker().turn(PLAYER).removeBalls(1, 4, 6).allowSkip().build();
    private static final GameStatus afterTurn16 = robertsIsBreaker().turn(OPPONENT).removeBalls(1, 4, 6).build();
    private static final GameStatus afterTurn17 = robertsIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(1, 4, 6).build();
    private static final GameStatus afterTurn18 = robertsIsBreaker().turn(OPPONENT).removeBalls(1, 4, 6)
            .currentPlayerConsecutiveFouls(1).consecutiveOpponentFouls(1).safetyLastTurn().build();
    private static final GameStatus afterTurn19 = robertsIsBreaker().turn(PLAYER).consecutiveOpponentFouls(2).removeBalls(1, 2, 4, 6).build();
    private static final GameStatus afterTurn20 = newGameShawBreaking();
    private static final GameStatus afterTurn21 = shawIsBreaker().turn(OPPONENT).safetyLastTurn().removeBalls(1, 2).build();
    private static final GameStatus afterTurn22 = shawIsBreaker().turn(PLAYER).allowSkip().removeBalls(1, 2).build();
    private static final GameStatus afterTurn23 = shawIsBreaker().turn(OPPONENT).removeBalls(1, 2).build();
    private static final GameStatus afterTurn24 = shawIsBreaker().turn(PLAYER).removeBalls(1, 2).build();
    private static final GameStatus afterTurn25 = newGameRobertsBreaking();
    private static final GameStatus afterTurn26 = robertsIsBreaker().turn(PLAYER).removeBalls(3, 7).build();
    private static final GameStatus afterTurn27 = newGameShawBreaking();
    private static final GameStatus afterTurn28 = shawIsBreaker().turn(OPPONENT).allowPush().build();
    private static final GameStatus afterTurn29 = newGameRobertsBreaking();
    private static final GameStatus afterTurn30 = robertsIsBreaker().turn(PLAYER).allowSkip().removeBalls(7).build();
    private static final GameStatus afterTurn31 = robertsIsBreaker().turn(OPPONENT).removeBalls(7).build();
    private static final GameStatus afterTurn32 = robertsIsBreaker().turn(PLAYER).removeBalls(7).build();
    private static final GameStatus endOfMatch = newGameShawBreaking();

    private static GameStatus.Builder status() {
        return new GameStatus.Builder(GameType.BCA_TEN_BALL)
                .breakType(BreakType.ALTERNATE)
                .playerColor(PlayerColor.OPEN);
    }

    private static GameStatus.Builder robertsIsBreaker() {
        return status().breaker(OPPONENT);
    }

    private static GameStatus.Builder shawIsBreaker() {
        return status().breaker(PLAYER);
    }

    private static GameStatus newGameRobertsBreaking() {
        return status().allowPush().turn(OPPONENT).breaker(OPPONENT).newGame().build();
    }

    private static GameStatus newGameShawBreaking() {
        return status().allowPush().turn(PLAYER).breaker(PLAYER).newGame().build();
    }

    public static List<GameStatus> getGameStatuses() {
        return Arrays.asList(
                startOfMatch, afterTurn1, afterTurn2, afterTurn3, afterTurn4, afterTurn5,
                afterTurn6, afterTurn7, afterTurn8, afterTurn9, afterTurn10, afterTurn11,
                afterTurn12, afterTurn13, afterTurn14, afterTurn15, afterTurn16, afterTurn17,
                afterTurn18, afterTurn19, afterTurn20, afterTurn21, afterTurn22, afterTurn23,
                afterTurn24, afterTurn25, afterTurn26, afterTurn27, afterTurn28, afterTurn29,
                afterTurn30, afterTurn31, afterTurn32, endOfMatch
        );
    }
}
