package com.brookmanholmes.billiards.acceptance.eberlecoates;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerColor;

import java.util.Arrays;
import java.util.List;

import static com.brookmanholmes.billiards.game.util.PlayerTurn.OPPONENT;
import static com.brookmanholmes.billiards.game.util.PlayerTurn.PLAYER;

/**
 * Created by Brookman Holmes on 11/10/2015.
 */
public class GameStatusList {
    private static final GameStatus startOfMatch = eberleBreaking();
    private static final GameStatus afterTurn1 = eberleIsBreaker().turn(OPPONENT).allowSkip().removeBalls(6, 8).build();
    private static final GameStatus afterTurn2 = eberleIsBreaker().turn(PLAYER).removeBalls(6, 8).build();
    private static final GameStatus afterTurn3 = coatesBreaking();
    private static final GameStatus afterTurn4 = coatesIsBreaker().turn(PLAYER).removeBalls(4).build();
    private static final GameStatus afterTurn5 = coatesIsBreaker().turn(OPPONENT).removeBalls(1, 2, 4).build();
    private static final GameStatus afterTurn6 = eberleBreaking();
    private static final GameStatus afterTurn7 = coatesBreaking();
    private static final GameStatus afterTurn8 = coatesIsBreaker().turn(PLAYER).removeBalls(1, 4).build();
    private static final GameStatus afterTurn9 = eberleBreaking();
    private static final GameStatus afterTurn10 = eberleIsBreaker().turn(OPPONENT).removeBalls(8).build();
    private static final GameStatus afterTurn11 = eberleIsBreaker().turn(PLAYER).removeBalls(8).build();
    private static final GameStatus afterTurn12 = eberleIsBreaker().turn(OPPONENT).removeBalls(8, 1, 2).build();
    private static final GameStatus afterTurn13 = eberleIsBreaker().turn(PLAYER).removeBalls(8, 1, 2).safetyLastTurn().build();
    private static final GameStatus afterTurn14 = eberleIsBreaker().turn(OPPONENT).removeBalls(8, 2, 1).build();
    private static final GameStatus afterTurn15 = coatesBreaking();
    private static final GameStatus afterTurn16 = coatesIsBreaker().turn(PLAYER).removeBalls(5).build();
    private static final GameStatus afterTurn17 = coatesIsBreaker().turn(OPPONENT).removeBalls(5).build();
    private static final GameStatus afterTurn18 = coatesIsBreaker().turn(PLAYER).removeBalls(5).safetyLastTurn().build();
    private static final GameStatus afterTurn19 = eberleBreaking();
    private static final GameStatus afterTurn20 = eberleIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).removeBalls(1, 2, 3, 4, 5, 6, 7, 8).build();
    private static final GameStatus afterTurn21 = coatesBreaking();
    private static final GameStatus afterTurn22 = coatesIsBreaker().turn(PLAYER).removeBalls(3, 5).safetyLastTurn().build();
    private static final GameStatus afterTurn23 = coatesIsBreaker().turn(OPPONENT).removeBalls(3, 5).build();
    private static final GameStatus afterTurn24 = coatesIsBreaker().turn(PLAYER).removeBalls(3, 5).build();
    private static final GameStatus afterTurn25 = coatesIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).removeBalls(3, 5).build();
    private static final GameStatus afterTurn26 = coatesIsBreaker().turn(PLAYER).currentPlayerConsecutiveFouls(1).consecutivePlayerFouls(1).removeBalls(1, 2, 3, 5).safetyLastTurn().build();
    private static final GameStatus afterTurn27 = coatesIsBreaker().turn(OPPONENT).consecutivePlayerFouls(2).removeBalls(1, 2, 3, 5).build();
    private static final GameStatus afterTurn28 = eberleBreaking();
    private static final GameStatus afterTurn29 = eberleIsBreaker().turn(OPPONENT).removeBalls(5, 8).build();
    private static final GameStatus afterTurn30 = eberleIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 5, 6, 8).safetyLastTurn().build();
    private static final GameStatus afterTurn31 = eberleIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3, 5, 6, 8).build();
    private static final GameStatus afterTurn32 = coatesBreaking();
    private static final GameStatus afterTurn33 = coatesIsBreaker().turn(PLAYER).removeBalls(3, 7).build();
    private static final GameStatus afterTurn34 = coatesIsBreaker().turn(OPPONENT).removeBalls(3, 7).build();
    private static final GameStatus afterTurn35 = coatesIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 7).safetyLastTurn().build();
    private static final GameStatus afterTurn36 = coatesIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3, 7).build();
    private static final GameStatus afterTurn37 = eberleBreaking();
    private static final GameStatus afterTurn38 = eberleIsBreaker().turn(OPPONENT).removeBalls(4).safetyLastTurn().build();
    private static final GameStatus afterTurn39 = eberleIsBreaker().turn(PLAYER).removeBalls(4).safetyLastTurn().build();
    private static final GameStatus afterTurn40 = eberleIsBreaker().turn(OPPONENT).removeBalls(4).safetyLastTurn().build();
    private static final GameStatus afterTurn41 = eberleIsBreaker().turn(PLAYER).removeBalls(4).build();
    private static final GameStatus afterTurn42 = coatesBreaking();
    private static final GameStatus afterTurn43 = coatesIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 4, 5, 8).build();
    private static final GameStatus afterTurn44 = eberleBreaking();
    private static final GameStatus afterTurn45 = eberleIsBreaker().turn(OPPONENT).removeBalls(8).build();
    private static final GameStatus afterTurn46 = eberleIsBreaker().turn(PLAYER).removeBalls(1, 2, 8).build();
    private static final GameStatus afterTurn47 = coatesBreaking();
    private static final GameStatus afterTurn48 = coatesIsBreaker().turn(PLAYER).removeBalls(1, 4, 5, 6).build();
    private static final GameStatus afterTurn49 = coatesIsBreaker().turn(OPPONENT).removeBalls(1, 4, 5, 6).safetyLastTurn().build();
    private static final GameStatus afterTurn50 = coatesIsBreaker().turn(PLAYER).removeBalls(1, 4, 5, 6).build();
    private static final GameStatus afterTurn51 = coatesIsBreaker().turn(OPPONENT).removeBalls(1, 4, 5, 6).build();
    private static final GameStatus afterTurn52 = coatesIsBreaker().turn(PLAYER).removeBalls(1, 4, 5, 6, 2).build();
    private static final GameStatus afterTurn53 = eberleBreaking();
    private static final GameStatus afterTurn54 = eberleIsBreaker().turn(OPPONENT).removeBalls(2).build();
    private static final GameStatus afterTurn55 = eberleIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(2).build();
    private static final GameStatus endOfMatch = coatesBreaking();

    private static GameStatus.Builder status() {
        return new GameStatus.Builder(GameType.BCA_NINE_BALL)
                .breakType(BreakType.ALTERNATE)
                .playerColor(PlayerColor.OPEN);
    }

    private static GameStatus eberleBreaking() {
        return status().turn(PLAYER).breaker(PLAYER).newGame().allowPush().build();
    }

    private static GameStatus coatesBreaking() {
        return status().turn(OPPONENT).breaker(OPPONENT).newGame().allowPush().build();
    }

    private static GameStatus.Builder eberleIsBreaker() {
        return status().breaker(PLAYER);
    }

    private static GameStatus.Builder coatesIsBreaker() {
        return status().breaker(OPPONENT);
    }

    public static List<GameStatus> getGameStatuses() {
        return Arrays.asList(
                startOfMatch, afterTurn1, afterTurn2, afterTurn3, afterTurn4, afterTurn5,
                afterTurn6, afterTurn7, afterTurn8, afterTurn9, afterTurn10, afterTurn11,
                afterTurn12, afterTurn13, afterTurn14, afterTurn15, afterTurn16, afterTurn17,
                afterTurn18, afterTurn19, afterTurn20, afterTurn21, afterTurn22, afterTurn23,
                afterTurn24, afterTurn25, afterTurn26, afterTurn27, afterTurn28, afterTurn29,
                afterTurn30, afterTurn31, afterTurn32, afterTurn33, afterTurn34, afterTurn35,
                afterTurn36, afterTurn37, afterTurn38, afterTurn39, afterTurn40, afterTurn41,
                afterTurn42, afterTurn43, afterTurn44, afterTurn45, afterTurn46, afterTurn47,
                afterTurn48, afterTurn49, afterTurn50, afterTurn51, afterTurn52, afterTurn53,
                afterTurn54, afterTurn55, endOfMatch
        );
    }
}
