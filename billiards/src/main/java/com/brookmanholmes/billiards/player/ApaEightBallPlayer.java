package com.brookmanholmes.billiards.player;

import com.brookmanholmes.billiards.game.util.ApaRaceToHelper;
import com.brookmanholmes.billiards.game.util.RaceTo;
import com.brookmanholmes.billiards.player.interfaces.Apa;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreak;
import com.brookmanholmes.billiards.player.interfaces.WinsOnBreakImp;

/**
 * Created by Brookman Holmes on 1/12/2016.
 */
public class ApaEightBallPlayer extends AbstractPlayer implements Apa {
    WinsOnBreak winsOnBreak;
    int rank;

    public ApaEightBallPlayer(String name, int rank) {
        super(name);
        winsOnBreak = new WinsOnBreakImp();
        this.rank = rank;
    }

    @Override
    public void addPlayerStats(AbstractPlayer player) {
        super.addPlayerStats(player);

        if (player instanceof Apa) {
            winsOnBreak.addWinsOnBreak(((Apa) player).getWinsOnBreak());
            winsOnBreak.addEarlyWins(((Apa) player).getEarlyWins());
        }
    }

    @Override
    public void addEarlyWin() {
        winsOnBreak.addEarlyWin();
    }

    @Override
    public int getEarlyWins() {
        return winsOnBreak.getEarlyWins();
    }

    @Override
    public void addWinOnBreak() {
        winsOnBreak.addWinOnBreak();
    }

    @Override
    public int getWinsOnBreak() {
        return winsOnBreak.getWinsOnBreak();
    }

    @Override
    public void addWinsOnBreak(int wins) {
        winsOnBreak.addWinsOnBreak(wins);
    }

    @Override
    public void addEarlyWins(int wins) {
        winsOnBreak.addEarlyWins(wins);
    }

    @Override
    public int getMatchPoints(int opponentScore, int opponentRank) {
        RaceTo raceTo = ApaRaceToHelper.apa8BallRaceTo(rank, opponentRank);

        if (getWins() == raceTo.getPlayerRaceTo()) {
            if (opponentScore == 0)
                return 3;
            else return 2;
        } else if (getWins() + 1 == raceTo.getPlayerRaceTo()) {
            return 1;
        } else return 0;
    }

    @Override
    public int getRank() {
        return rank;
    }
}
