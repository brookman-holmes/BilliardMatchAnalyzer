package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvStatsDialog;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener {
    public static final String INFO_FRAGMENT_TAG = "infoFragment";
    private static final String TAG = "MatchInfoActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.addInning)
    Button addInning;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout layout;

    DatabaseAdapter db;
    MatchInfoFragment infoFragment;
    Menu menu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        db = new DatabaseAdapter(this);
        db.open();

        Match<?> match = db.getMatch(getMatchId());
        playerName.setText(match.getPlayer().getName());
        opponentName.setText(match.getOpponent().getName());

        infoFragment = (MatchInfoFragment) getSupportFragmentManager().findFragmentByTag(INFO_FRAGMENT_TAG);

        if (infoFragment == null) {
            infoFragment = MatchInfoFragment.createMatchInfoFragment(getMatchId());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, infoFragment, INFO_FRAGMENT_TAG).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @OnClick(R.id.addInning)
    public void addInning(View view) {
        DialogFragment dialogFragment = AddTurnDialog.create(db.getMatch(getMatchId()));
        dialogFragment.show(getSupportFragmentManager(), "AddTurnDialog");
    }

    @OnClick(R.id.playerName)
    public void showTestLayout() {
        DialogFragment dialogFragment =
                AdvStatsDialog.create(getMatchId(), playerName.getText().toString(), PlayerTurn.PLAYER);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    @OnClick(R.id.opponentName)
    public void showTestLayout2() {
        DialogFragment dialogFragment =
                AdvStatsDialog.create(getMatchId(), opponentName.getText().toString(), PlayerTurn.OPPONENT);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    public void updateViews() {
        if (menu != null) {
            updateMenuItems();
        }

        addInning.setText("Add turn for " + infoFragment.getCurrentPlayersName());
    }

    private void updateMenuItems() {
        menu.findItem(R.id.action_undo).setEnabled(infoFragment.isUndoTurn());
        menu.findItem(R.id.action_redo).setEnabled(infoFragment.isRedoTurn());
        this.menu.findItem(R.id.action_undo).getIcon().setAlpha(this.menu.findItem(R.id.action_undo).isEnabled() ? 255 : 64);
        this.menu.findItem(R.id.action_redo).getIcon().setAlpha(this.menu.findItem(R.id.action_redo).isEnabled() ? 255 : 64);
    }

    @Override
    public void addTurn(TurnBuilder turnBuilder) {
        Turn turn = infoFragment.createAndAddTurnToMatch(
                turnBuilder.tableStatus,
                turnBuilder.turnEnd,
                turnBuilder.scratch,
                turnBuilder.lostGame);

        db.insertTurn(turn, getMatchId(), infoFragment.getTurnCount());

        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.menu = menu;
        updateMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_notes) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            builder.setTitle("Match Notes")
                    .setMessage(db.getMatch(getMatchId()).getNotes())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }

        if (id == R.id.action_game_status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            // TODO: 3/9/2016 beautify this
            builder.setTitle("Current Game Status")
                    .setMessage(db.getMatch(getMatchId()).getGameStatus().toString())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }

        if (id == R.id.action_undo) {
            if (infoFragment.isUndoTurn()) {
                Snackbar.make(layout, "Undid last turn", Snackbar.LENGTH_SHORT).show();
                infoFragment.undoTurn();
                db.undoTurn(getMatchId(), infoFragment.getTurnCount() + 1);
                updateViews();
            }
            else {
                Snackbar.make(layout, "No turn to undo", Snackbar.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.action_redo) {
            if (infoFragment.isRedoTurn()) {
                Snackbar.make(layout, "Redid last turn", Snackbar.LENGTH_SHORT).show();
                Turn turn = infoFragment.redoTurn();
                db.insertTurn(turn, getMatchId(), infoFragment.getTurnCount());
                updateViews();
            } else {
                Snackbar.make(layout, "No turn to redo", Snackbar.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
