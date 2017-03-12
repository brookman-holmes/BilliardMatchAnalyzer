package com.brookmanholmes.bma.data;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.billiards.turn.ITurn;

import java.util.List;

/**
 * Created by Brookman Holmes on 2/19/2017.
 */
public interface DataSource {
    List<Player> getPlayers();

    List<Player> getPlayer(String playerName, GameType gameType, String id);

    void undoTurn(Match match);

    String insertMatch(Match match);

    void updateMatchNotes(String matchNotes, String matchId);

    void updateMatchLocation(String location, String matchId);

    void insertTurn(ITurn turn, String matchId, String playerId);

    void deleteMatch(Match match, String deletingUser);
}
