package com.brookmanholmes.billiards.acceptance.hohmannsvb;

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
    private static final GameStatus startOfMatch = hohmannBreaking();
    private static final GameStatus afterTurn1 = hohmannIsBreaker().turn(OPPONENT).allowPush().build();
    private static final GameStatus afterTurn2 = hohmannIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 4, 5, 6, 7, 8).build();
    private static final GameStatus afterTurn3 = svbBreaking();
    private static final GameStatus afterTurn4 = svbIsBreaker().turn(PLAYER).allowPush().build();
    private static final GameStatus afterTurn5 = svbIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3, 4).safetyLastTurn().build();
    private static final GameStatus afterTurn6 = svbIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 4).build();
    private static final GameStatus afterTurn7 = svbIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3, 4, 8).safetyLastTurn().build();
    private static final GameStatus afterTurn8 = svbIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(1, 2, 3, 4, 8).build();
    private static final GameStatus afterTurn9 = hohmannBreaking();
    private static final GameStatus afterTurn10 = hohmannIsBreaker().turn(OPPONENT).allowSkip().removeBalls(3, 6, 9).build();
    private static final GameStatus afterTurn11 = hohmannIsBreaker().turn(PLAYER).removeBalls(3, 6, 9).build();
    private static final GameStatus afterTurn12 = svbBreaking();
    private static final GameStatus afterTurn13 = svbIsBreaker().turn(PLAYER).allowPush().build();
    private static final GameStatus afterTurn14 = svbIsBreaker().turn(OPPONENT).allowSkip().build();
    private static final GameStatus afterTurn15 = svbIsBreaker().turn(PLAYER).build();
    private static final GameStatus afterTurn16 = svbIsBreaker().turn(OPPONENT).safetyLastTurn().build();
    private static final GameStatus afterTurn17 = svbIsBreaker().turn(PLAYER).safetyLastTurn().build();
    private static final GameStatus afterTurn18 = svbIsBreaker().turn(OPPONENT).build();
    private static final GameStatus afterTurn19 = hohmannBreaking();
    private static final GameStatus afterTurn20 = hohmannIsBreaker().turn(OPPONENT).removeBalls(1, 3, 8, 9).build();
    private static final GameStatus afterTurn21 = hohmannIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 4, 8, 9).safetyLastTurn().build();
    private static final GameStatus afterTurn22 = hohmannIsBreaker().turn(OPPONENT).removeBalls(1, 2, 3, 4, 8, 9).build();
    private static final GameStatus afterTurn23 = hohmannIsBreaker().turn(PLAYER).removeBalls(1, 2, 3, 4, 8, 9).build();
    private static final GameStatus afterTurn24 = svbBreaking();
    private static final GameStatus afterTurn25 = svbIsBreaker().turn(PLAYER).removeBalls(1, 2).build();
    private static final GameStatus afterTurn26 = svbIsBreaker().turn(OPPONENT).removeBalls(1, 2).build();
    private static final GameStatus afterTurn27 = svbIsBreaker().turn(PLAYER).consecutiveOpponentFouls(1).removeBalls(1, 2, 3, 4).build();
    private static final GameStatus afterTurn28 = hohmannBreaking();
    private static final GameStatus afterTurn29 = hohmannIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).removeBalls(5).build();
    private static final GameStatus afterTurn30 = hohmannIsBreaker().turn(PLAYER).currentPlayerConsecutiveFouls(1).consecutivePlayerFouls(1).removeBalls(1, 2, 3, 5).safetyLastTurn().build();
    private static final GameStatus afterTurn31 = hohmannIsBreaker().turn(OPPONENT).consecutivePlayerFouls(2).removeBalls(1, 2, 3, 5).build();
    private static final GameStatus afterTurn32 = svbBreaking();
    private static final GameStatus afterTurn33 = hohmannBreaking();
    private static final GameStatus afterTurn34 = hohmannIsBreaker().turn(OPPONENT).allowPush().build();
    private static final GameStatus afterTurn35 = svbBreaking();
    private static final GameStatus afterTurn36 = hohmannBreaking();
    private static final GameStatus afterTurn37 = hohmannIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).build();
    private static final GameStatus afterTurn38 = hohmannIsBreaker().turn(PLAYER).currentPlayerConsecutiveFouls(1).consecutivePlayerFouls(1).removeBalls(1, 2, 3, 4, 5, 6, 7).build();
    private static final GameStatus afterTurn39 = svbBreaking();
    private static final GameStatus afterTurn40 = hohmannBreaking();
    private static final GameStatus afterTurn41 = hohmannIsBreaker().turn(OPPONENT).allowPush().build();
    private static final GameStatus afterTurn42 = hohmannIsBreaker().turn(PLAYER).safetyLastTurn().build();
    private static final GameStatus afterTurn43 = hohmannIsBreaker().turn(OPPONENT).consecutivePlayerFouls(1).build();
    private static final GameStatus afterTurn44 = svbBreaking();
    private static final GameStatus afterTurn45 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).allowSkip().build();
    private static final GameStatus afterTurn46 = svbIsBreaker().removeBalls(3, 8).turn(OPPONENT).build();
    private static final GameStatus afterTurn47 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).build();
    private static final GameStatus afterTurn48 = svbIsBreaker().removeBalls(3, 8).turn(OPPONENT).build();
    private static final GameStatus afterTurn49 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).safetyLastTurn().build();
    private static final GameStatus afterTurn50 = svbIsBreaker().removeBalls(3, 8).turn(OPPONENT).safetyLastTurn().build();
    private static final GameStatus afterTurn51 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).safetyLastTurn().build();
    private static final GameStatus afterTurn52 = svbIsBreaker().removeBalls(3, 8).turn(OPPONENT).safetyLastTurn().build();
    private static final GameStatus afterTurn53 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).safetyLastTurn().build();
    private static final GameStatus afterTurn54 = svbIsBreaker().removeBalls(3, 8).turn(OPPONENT).safetyLastTurn().build();
    private static final GameStatus afterTurn55 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).consecutiveOpponentFouls(1).build();
    private static final GameStatus afterTurn56 = svbIsBreaker().removeBalls(3, 8).turn(OPPONENT).currentPlayerConsecutiveFouls(1).consecutiveOpponentFouls(1).safetyLastTurn().build();
    private static final GameStatus afterTurn57 = svbIsBreaker().removeBalls(3, 8).turn(PLAYER).build();
    private static final GameStatus afterTurn58 = svbIsBreaker().removeBalls(3, 8, 1).turn(OPPONENT).consecutivePlayerFouls(1).build();
    private static final GameStatus afterTurn59 = hohmannBreaking();
    private static final GameStatus afterTurn60 = svbBreaking();
    private static final GameStatus endOfMatch = hohmannBreaking();

    private static GameStatus.Builder status() {
        return new GameStatus.Builder(GameType.BCA_TEN_BALL)
                .breakType(BreakType.ALTERNATE)
                .playerColor(PlayerColor.OPEN);
    }

    private static GameStatus hohmannBreaking() {
        return status().turn(PLAYER).breaker(PLAYER).newGame().allowPush().build();
    }

    private static GameStatus svbBreaking() {
        return status().turn(OPPONENT).breaker(OPPONENT).newGame().allowPush().build();
    }

    private static GameStatus.Builder hohmannIsBreaker() {
        return status().breaker(PLAYER);
    }

    private static GameStatus.Builder svbIsBreaker() {
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
                afterTurn54, afterTurn55, afterTurn56, afterTurn57, afterTurn58, afterTurn59,
                afterTurn60, endOfMatch
        );
    }
}
