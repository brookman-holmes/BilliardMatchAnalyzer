package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;

import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnBuilder {
    TableStatus tableStatus;
    TurnEnd turnEnd;

    public TurnBuilder(GameType gameType) {
        this.tableStatus = TableStatus.newTable(gameType);
    }

    public TurnBuilder(GameType gameType, List<Integer> ballsOnTable) {
        this.tableStatus = TableStatus.newTable(gameType, ballsOnTable);
    }


}
