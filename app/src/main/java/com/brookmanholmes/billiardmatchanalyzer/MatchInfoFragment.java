package com.brookmanholmes.billiardmatchanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.adapters.SimpleDividerItemDecoration;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.MatchInfoRecyclerAdapter;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.game.Turn;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MatchInfoFragment extends Fragment {
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

    public static MatchInfoFragment createMatchInfoFragmentWithCardViews(long matchId) {
        MatchInfoFragment fragment = createMatchInfoFragment(matchId);

        fragment.getArguments().putBoolean("Card View", true);

        return fragment;
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

        db = new DatabaseAdapter(getContext());
        db.open();

        if (getArguments() != null) {
            matchId = getArguments().getLong(BaseActivity.ARG_MATCH_ID);
        } else {
            throw new IllegalArgumentException("This fragment must be created with a match ID passed into it");
        }



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments().getBoolean("Card View", false)) {
            adapter = MatchInfoRecyclerAdapter.createMatchAdapterWithCardViews(db.getMatch(matchId));
        } else {
            adapter = MatchInfoRecyclerAdapter.createMatchAdapter(db.getMatch(matchId));
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        }
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void addTurn(Turn turn) {
        adapter.addTurn(turn.getTableStatus(), turn.getTurnEnd(), turn.isScratch());
    }
}
