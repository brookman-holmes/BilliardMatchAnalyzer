package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.MatchInfoRecyclerAdapter;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.match.MatchInterface;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MatchInfoFragment extends Fragment implements MatchInterface {
    @Bind(R.id.scrollView)
    RecyclerView recyclerView;
    MatchInfoRecyclerAdapter<?> adapter;
    DatabaseAdapter db;
    private long matchId;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchInfoFragment() {
    }

    public static MatchInfoFragment createMatchInfoFragment(long matchId) {
        MatchInfoFragment fragment = new MatchInfoFragment();

        Bundle args = new Bundle();
        args.putLong(BaseActivity.ARG_MATCH_ID, matchId);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (getArguments().getBoolean("Card View", false)) {
            view = inflater.inflate(R.layout.fragment_list_view, container, false);
        } else
            view = inflater.inflate(R.layout.fragment_list_view2, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost) {
        return adapter.createAndAddTurnToMatch(tableStatus, turnEnd, scratch, isGameLost);
    }

    @Override
    public String getCurrentPlayersName() {
        return adapter.getCurrentPlayersName();
    }

    @Override
    public void undoTurn() {
        adapter.undoTurn();
    }

    @Override
    public boolean isRedoTurn() {
        return adapter.isRedoTurn();
    }

    @Override
    public boolean isUndoTurn() {
        return adapter.isUndoTurn();
    }

    @Override
    public Turn redoTurn() {
        return adapter.redoTurn();
    }

    @Override
    public AbstractPlayer getPlayer() {
        return adapter.getPlayer();
    }

    @Override
    public AbstractPlayer getOpponent() {
        return adapter.getOpponent();
    }

    @Override
    public String getLocation() {
        return adapter.getLocation();
    }

    @Override
    public int getTurnCount() {
        return adapter.getTurnCount();
    }

    @Override
    public long getMatchId() {
        return adapter.getMatchId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        db = new DatabaseAdapter(getContext());
        db.open();

        if (getArguments().getLong(BaseActivity.ARG_MATCH_ID, -1) != -1) {
            matchId = getArguments().getLong(BaseActivity.ARG_MATCH_ID);
        } else {
            throw new IllegalArgumentException("This fragment must be created with a match ID passed into it");
        }

        adapter = MatchInfoRecyclerAdapter.createMatchAdapter(db.getMatch(matchId));

        db.logDatabase(DatabaseAdapter.TABLE_TURNS);
    }
}
