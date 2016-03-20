package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnBuilder {
    public TableStatus tableStatus;
    public TurnEnd turnEnd;
    public boolean scratch = false;
    public boolean lostGame = false;
    public AdvStats.Builder advStats = new AdvStats.Builder();

    public TurnBuilder(GameType gameType) {
        this.tableStatus = TableStatus.newTable(gameType);
    }

    public TurnBuilder(GameType gameType, List<Integer> ballsOnTable) {
        this.tableStatus = TableStatus.newTable(gameType, ballsOnTable);
    }

    public String advData() {
        return advStats.toString();
    }

    @Override
    public String toString() {
        return "TurnBuilder{" +
                "tableStatus=" + tableStatus.toString() +
                ", turnEnd=" + (turnEnd == null ? "null" : turnEnd.toString()) +
                ", lostGame=" + lostGame +
                ", foul=" + scratch +
                '}';
    }
}
