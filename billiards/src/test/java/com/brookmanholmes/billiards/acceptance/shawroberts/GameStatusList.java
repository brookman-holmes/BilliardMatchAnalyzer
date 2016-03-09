package com.brookmanholmes.billiards.acceptance.shawroberts;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.game.util.PlayerTurn.OPPONENT;
import static com.brookmanholmes.billiards.game.util.PlayerTurn.PLAYER;

/**
 * Created by Brookman Holmes on 11/9/2015.
 */
public class GameStatusList {
    static GameStatus startOfMatch = newGameRobertsBreaking();
    static GameStatus afterTurn1 = newGameShawBreaking();
    static GameStatus afterTurn2 = shawIsBreaker().turn(OPPONENT).allowPush().build();
    static GameStatus afterTurn3 = shawIsBreaker().turn(PLAYER).allowSkip().removeBalls(7, 9).build();
    static GameStatus afterTurn4 = shawIsBreaker().turn(OPPONENT).removeBalls(7, 9).build();
    static GameStatus afterTurn5 = shawIsBreaker().turn(PLAYER).removeBalls(7, 9, 1, 2, 3, 8).build();
    static GameStatus afterTurn6 = shawIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).removeBalls(1, 2, 3, 4, 5, 7, 8, 9).build();
    static GameStatus afterTurn7 = newGameRobertsBreaking();
    static GameStatus afterTurn8 = robertsIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(6).build();
    static GameStatus afterTurn9 = newGameShawBreaking();
    static GameStatus afterTurn10 = newGameRobertsBreaking();
    static GameStatus afterTurn11 = newGameShawBreaking();
    static GameStatus afterTurn12 = shawIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3).safetyLastTurn().build();
    static GameStatus afterTurn13 = shawIsBreaker().turn(PLAYER).removeBalls(1, 2, 3).build();
    static GameStatus afterTurn14 = newGameRobertsBreaking();
    static GameStatus afterTurn15 = robertsIsBreaker().turn(PLAYER).removeBalls(1, 4, 6).allowSkip().build();
    static GameStatus afterTurn16 = robertsIsBreaker().turn(OPPONENT).removeBalls(1, 4, 6).build();
    static GameStatus afterTurn17 = robertsIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(1, 4, 6).build();
    static GameStatus afterTurn18 = robertsIsBreaker().turn(OPPONENT).removeBalls(1, 4, 6)
            .currentPlayerConsecutiveFouls(1).consecutiveOpponentFouls(1).safetyLastTurn().build();
    static GameStatus afterTurn19 = robertsIsBreaker().turn(PLAYER).consecutiveOpponentFouls(2).removeBalls(1, 2, 4, 6).build();
    static GameStatus afterTurn20 = newGameShawBreaking();
    static GameStatus afterTurn21 = shawIsBreaker().turn(OPPONENT).safetyLastTurn().removeBalls(1, 2).build();
    static GameStatus afterTurn22 = shawIsBreaker().turn(PLAYER).allowSkip().removeBalls(1, 2).build();
    static GameStatus afterTurn23 = shawIsBreaker().turn(OPPONENT).removeBalls(1, 2).build();
    static GameStatus afterTurn24 = shawIsBreaker().turn(PLAYER).removeBalls(1, 2).build();
    static GameStatus afterTurn25 = newGameRobertsBreaking();
    static GameStatus afterTurn26 = robertsIsBreaker().turn(PLAYER).removeBalls(3, 7).build();
    static GameStatus afterTurn27 = newGameShawBreaking();
    static GameStatus afterTurn28 = shawIsBreaker().turn(OPPONENT).allowPush().build();
    static GameStatus afterTurn29 = newGameRobertsBreaking();
    static GameStatus afterTurn30 = robertsIsBreaker().turn(PLAYER).allowSkip().removeBalls(7).build();
    static GameStatus afterTurn31 = robertsIsBreaker().turn(OPPONENT).removeBalls(7).build();
    static GameStatus afterTurn32 = robertsIsBreaker().turn(PLAYER).removeBalls(7).build();
    static GameStatus endOfMatch = newGameShawBreaking();

    static GameStatus.Builder status() {
        return new GameStatus.Builder(GameType.BCA_TEN_BALL)
                .breakType(BreakType.ALTERNATE)
                .playerColor(PlayerColor.OPEN);
    }

    static GameStatus.Builder robertsIsBreaker() {
        return status().breaker(OPPONENT);
    }

    static GameStatus.Builder shawIsBreaker() {
        return status().breaker(PLAYER);
    }

    static GameStatus newGameRobertsBreaking() {
        return status().allowPush().turn(OPPONENT).breaker(OPPONENT).newGame().build();
    }

    static GameStatus newGameShawBreaking() {
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
