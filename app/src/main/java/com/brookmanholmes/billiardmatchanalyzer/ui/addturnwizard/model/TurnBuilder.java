package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;

import java.util.List;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnBuilder {
    public TableStatus tableStatus;
    public String turnEnd;
    public String scratch;
    public String lostGame;
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

    @Override public String toString() {
        return "TurnBuilder{" +
                "tableStatus=" + tableStatus.toString() +
                ", turnEnd=" + (turnEnd == null ? "null" : turnEnd) +
                ", lostGame=" + lostGame +
                ", foul=" + scratch +
                '}';
    }
}
