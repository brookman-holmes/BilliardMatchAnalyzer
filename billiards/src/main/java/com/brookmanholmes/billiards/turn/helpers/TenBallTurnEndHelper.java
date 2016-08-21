package com.brookmanholmes.billiards.turn.helpers;

import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.turn.ITableStatus;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
class TenBallTurnEndHelper extends RotationTurnEndHelper {
    TenBallTurnEndHelper(GameStatus game, ITableStatus tableStatus) {
        super(game, tableStatus);
    }

    @Override boolean showWin() {
        return tableStatus.isGameBallMade();
    }

    @Override boolean checkFoul() {
        return tableStatus.getDeadBallsOnBreak() > 0;
    }
}
