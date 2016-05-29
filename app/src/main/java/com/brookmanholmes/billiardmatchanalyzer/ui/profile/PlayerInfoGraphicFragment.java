package com.brookmanholmes.billiardmatchanalyzer.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by helios on 5/15/2016.
 */
public class PlayerInfoGraphicFragment extends Fragment {
    private static final String ARG_PLAYER = "arg player";
    @Bind(R.id.scrollView) RecyclerView recyclerView;
    private PlayerInfoGraphicAdapter adapter;
    private GridLayoutManager layoutManager;
    private DatabaseAdapter database;

    public PlayerInfoGraphicFragment() {
    }

    public static PlayerInfoGraphicFragment create(String player) {
        PlayerInfoGraphicFragment fragment = new PlayerInfoGraphicFragment();
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

        layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case PlayerInfoGraphicAdapter.ITEM_GRAPH:
                        return 3;
                    case PlayerInfoGraphicAdapter.ITEM_SAFETY_GRAPH:
                        return 2;
                    case PlayerInfoGraphicAdapter.ITEM_BREAK_GRAPH:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
