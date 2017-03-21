package com.brookmanholmes.bma.ui.matchinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */

public class RunAttemptActivity extends AbstractMatchActivity implements ChildEventListener {
    private static final String TAG = "HighRunAttemptAct";

    @Bind(R.id.scrollView)
    RecyclerView recyclerView;

    RunAttemptAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Match> accessoryMatches = (ArrayList<Match>) getIntent().getExtras().getSerializable("accessory_matches");
        if (accessoryMatches == null)
            accessoryMatches = new ArrayList<>();
        List<Player> previousPlayers = new ArrayList<>();
        for (Match match : accessoryMatches) {
            if (match.getPlayer().getId().equals(auth.getCurrentUser().getUid()))
                previousPlayers.add(match.getPlayer());
            else if (match.getOpponent().getId().equals(auth.getCurrentUser().getUid()))
                previousPlayers.add(match.getOpponent());
        }

        adapter = new RunAttemptAdapter(this, match, previousPlayers);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void showAdvShotData() {
        showAdvancedStatsDialog(PlayerTurn.PLAYER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.menu = menu;
        menu.removeItem(R.id.action_game_status);
        menu.removeItem(R.id.action_match_view);
        updateMenuItems();
        return true;
    }

    @Override
    protected void setToolbarTitle() {
        if (getSupportActionBar() != null) {
            if (match.getGameStatus().gameType.isStraightPool())
                getSupportActionBar().setTitle(R.string.game_straight_ghost);
            else
                getSupportActionBar().setTitle(ConversionUtils.getGameTypeString(this, match.getGameStatus().gameType));
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_high_run;
    }

    @Override
    protected void updateViews() {
        adapter.update(match);
        updateMenuItems();
    }
}
