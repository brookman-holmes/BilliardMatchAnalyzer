package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adapters.matchinfo.PlayerInfoAdapter;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 4/13/2016.
 */
public class PlayerProfileActivity extends BaseActivity {
    public static final String ARG_PLAYER_NAME = "arg player name";

    @Bind(R.id.scrollView) RecyclerView recyclerView;
    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
    @Bind(R.id.toolbar) Toolbar toolbar;
    private PlayerInfoAdapter adapter;
    private LinearLayoutManager layoutManager;
    private DatabaseAdapter database;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        String player = getIntent().getExtras().getString(ARG_PLAYER_NAME);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_player_profile, player));

        database = new DatabaseAdapter(getApplicationContext());
        layoutManager = new LinearLayoutManager(getBaseContext());
        adapter = new PlayerInfoAdapter(database.getPlayer(player), player, getString(R.string.opponents));
        playerName.setText(player);
        opponentName.setText(R.string.opponents);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
