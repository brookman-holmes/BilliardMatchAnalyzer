package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;

import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnBuilder {
    public TableStatus tableStatus;
    public TurnEnd turnEnd;
    public boolean scratch = false;
    public boolean lostGame = false;

    public TurnBuilder(GameType gameType) {
        this.tableStatus = TableStatus.newTable(gameType);
    }

    public TurnBuilder(GameType gameType, List<Integer> ballsOnTable) {
        this.tableStatus = TableStatus.newTable(gameType, ballsOnTable);
    }


    @Override
    public String toString() {
        return "TurnBuilder{" +
                "tableStatus=" + tableStatus.toString() +
                ", turnEnd=" + (turnEnd == null ? "null" : turnEnd.toString()) +
                ", foul=" + scratch +
                '}';
    }
}
