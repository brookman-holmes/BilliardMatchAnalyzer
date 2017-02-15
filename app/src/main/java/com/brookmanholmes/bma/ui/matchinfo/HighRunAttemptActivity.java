package com.brookmanholmes.bma.ui.matchinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.player.Player;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */

public class HighRunAttemptActivity extends AbstractMatchActivity {
    private static final String TAG = "HighRunAttemptAct";

    @Bind(R.id.scrollView)
    RecyclerView recyclerView;

    HighRunAttemptAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Player> players = new DatabaseAdapter(this)
                .getPlayer(match.getPlayer().getName(),
                        match.getGameStatus().gameType,
                        match.getMatchId());

        adapter = new HighRunAttemptAdapter(this, match, players);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void showAdvShotData() {
        showAdvancedStatsDialog(match.getPlayer().getName(), PlayerTurn.PLAYER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.menu = menu;
        menu.add(0, R.id.action_edit_name, 103, R.string.edit_name);
        menu.removeItem(R.id.action_game_status);
        menu.removeItem(R.id.action_match_view);
        updateMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit_name) {
            showEditPlayerNameDialog(match.getPlayer().getName());
            analytics.logEvent("edit_player_name", null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getMimeType() {
        return "application/com.brookmanholmes.bma.hramatchmodel";
    }

    @Override
    protected void setToolbarTitle() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.game_straight_ghost);
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

    @Override
    public void updatePlayerName(String playerToUpdate, String newName) {
        match.setPlayerName(newName);
        adapter.update(match);
    }
}
