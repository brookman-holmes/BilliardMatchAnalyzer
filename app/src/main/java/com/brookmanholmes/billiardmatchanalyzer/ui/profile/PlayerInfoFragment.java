package com.brookmanholmes.billiardmatchanalyzer.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.PlayerInfoGraphicAdapter;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.PlayerInfoAdapter;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoFragment extends Fragment {
    @Bind(R.id.scrollView) RecyclerView recyclerView;
    private PlayerInfoGraphicAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseAdapter database;

    private static final String ARG_PLAYER = "arg player";
    public PlayerInfoFragment() {
    }

    public static PlayerInfoFragment create(String player) {
        PlayerInfoFragment fragment = new PlayerInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER, player);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseAdapter(getContext());
        String player = getArguments().getString(ARG_PLAYER);

        adapter = new PlayerInfoGraphicAdapter(database.getPlayer(player), player, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    static class LayoutManager extends StaggeredGridLayoutManager {
        public LayoutManager(int spanCount, int orientation) {
            super(spanCount, orientation);
        }

        @Override
        public int getGapStrategy() {
            return StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS;
        }
    }
}
