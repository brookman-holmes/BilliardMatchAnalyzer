package com.brookmanholmes.bma.ui.matchinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;


/**
 * Created by Brookman Holmes on 12/20/2016.
 */

class MinimalTurnListAdapter extends RecyclerView.Adapter<MinimalTurnViewHolder> {
    Match match;

    MinimalTurnListAdapter(Match match) {
        this.match = match;
        setHasStableIds(true);
    }

    @Override
    public MinimalTurnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MinimalTurnViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.row_turn_min, parent, false),
                match);
    }

    @Override
    public void onBindViewHolder(MinimalTurnViewHolder holder, int position) {
        PlayerTurn turn = match.getGameStatus(position).turn;
        AbstractPlayer player = turn == PlayerTurn.PLAYER ?
                match.getPlayer(0, position + 1) : match.getOpponent(0, position + 1);

        holder.bind(match.getTurns().get(position), turn, player);
    }

    @Override
    public int getItemCount() {
        return match.getTurns().size();
    }

    public void update(Match match) {
        this.match = match;
        notifyDataSetChanged(); // lazy!
    }
}
