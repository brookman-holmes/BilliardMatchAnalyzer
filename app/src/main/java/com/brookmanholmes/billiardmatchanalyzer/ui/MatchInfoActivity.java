package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialog.GameStatusStringBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvStatsDialog;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TurnEnd;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener {
    public static final String INFO_FRAGMENT_TAG = "infoFragment";
    private static final String TAG = "MatchInfoActivity";
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
    @Bind(R.id.buttonAddTurn) FloatingActionButton buttonAddTurn;
    @Bind(R.id.coordinatorLayout) CoordinatorLayout layout;

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

        setToolbarTitle(match.getGameStatus().gameType.toString());

        infoFragment = (MatchInfoFragment) getSupportFragmentManager().findFragmentByTag(INFO_FRAGMENT_TAG);

        if (infoFragment == null) {
            infoFragment = MatchInfoFragment.createMatchInfoFragment(getMatchId());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, infoFragment, INFO_FRAGMENT_TAG).commit();
        }
    }

    private void setToolbarTitle(String gameType) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getResources().getString(R.string.title_match_info, gameType));
    }

    @Override protected void onResume() {
        super.onResume();
        updateViews();
    }

    @OnClick(R.id.buttonAddTurn) public void addInning(View view) {
        showAddTurnDialog();
    }

    private void showAddTurnDialog() {
        AddTurnDialog addTurnDialog = AddTurnDialog.create(db.getMatch(getMatchId()));
        addTurnDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        addTurnDialog.show(getSupportFragmentManager(), "AddTurnDialog");
    }

    @OnClick(R.id.playerName) public void showTestLayout() {
        DialogFragment dialogFragment =
                AdvStatsDialog.create(getMatchId(), playerName.getText().toString(), PlayerTurn.PLAYER);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    @OnClick(R.id.opponentName) public void showTestLayout2() {
        DialogFragment dialogFragment =
                AdvStatsDialog.create(getMatchId(), opponentName.getText().toString(), PlayerTurn.OPPONENT);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    public void updateViews() {
        if (menu != null)
            updateMenuItems();
    }

    private void updateMenuItems() {
        menu.findItem(R.id.action_undo).setEnabled(infoFragment.isUndoTurn());
        menu.findItem(R.id.action_redo).setEnabled(infoFragment.isRedoTurn());
        this.menu.findItem(R.id.action_undo).getIcon().setAlpha(this.menu.findItem(R.id.action_undo).isEnabled() ? 255 : 64);
        this.menu.findItem(R.id.action_redo).getIcon().setAlpha(this.menu.findItem(R.id.action_redo).isEnabled() ? 255 : 64);
    }

    @Override public void addTurn(TurnBuilder turnBuilder) {
        if (turnBuilder.advStats.build().use())
            addTurn(infoFragment.createAndAddTurnToMatch(
                    turnBuilder.tableStatus,
                    convertStringToTurnEnd(turnBuilder.turnEnd),
                    convertStringToScratch(turnBuilder.scratch),
                    convertStringToLostGame(turnBuilder.lostGame)), turnBuilder.advStats.build());
        else
            addTurn(infoFragment.createAndAddTurnToMatch(
                    turnBuilder.tableStatus,
                    convertStringToTurnEnd(turnBuilder.turnEnd),
                    convertStringToScratch(turnBuilder.scratch),
                    convertStringToLostGame(turnBuilder.lostGame)));
    }

    private TurnEnd convertStringToTurnEnd(String turnEnd) {
        if (turnEnd.equals(getString(R.string.turn_safety)))
            return TurnEnd.SAFETY;
        else if (turnEnd.equals(getString(R.string.turn_safety_error)))
            return TurnEnd.SAFETY_ERROR;
        else if (turnEnd.equals(getString(R.string.turn_break_miss)))
            return TurnEnd.BREAK_MISS;
        else if (turnEnd.equals(getString(R.string.turn_miss)))
            return TurnEnd.MISS;
        else if (turnEnd.equals(getString(R.string.turn_push)))
            return TurnEnd.PUSH_SHOT;
        else if (turnEnd.equals(getString(R.string.turn_skip)))
            return TurnEnd.SKIP_TURN;
        else if (turnEnd.equals(getString(R.string.turn_illegal_break)))
            return TurnEnd.ILLEGAL_BREAK;
        else if (turnEnd.equals(getString(R.string.turn_won_game)))
            return TurnEnd.GAME_WON;
        else if (turnEnd.equals(getString(R.string.turn_current_player_breaks, infoFragment.getCurrentPlayersName())))
            return TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN;
        else if (turnEnd.equals(getString(R.string.turn_non_current_player_breaks, infoFragment.getNonCurrentPlayersName())))
            return TurnEnd.OPPONENT_BREAKS_AGAIN;
        else if (turnEnd.equals(getString(R.string.turn_continue_game)))
            return TurnEnd.CONTINUE_WITH_GAME;
        else
            throw new IllegalArgumentException("No such conversion between string and StringRes: " + turnEnd);
    }

    private boolean convertStringToScratch(String scratch) {
        if (scratch.equals(getString(R.string.foul_lost_game)))
            return true;
        else if (scratch.equals(getString(R.string.yes)))
            return true;
        else if (scratch.equals(getString(R.string.no)))
            return false;
        else throw new IllegalArgumentException("No such conversion between string " + scratch
                    + " and StringRes: " + getString(R.string.yes) + " or "
                    + getString(R.string.no));
    }

    private boolean convertStringToLostGame(String scratch) {
        if (scratch.equals(getString(R.string.foul_lost_game)))
            return true;
        else if (scratch.equals(getString(R.string.yes)))
            return false;
        else if (scratch.equals(getString(R.string.no)))
            return false;
        else throw new IllegalArgumentException("No such conversion between string " + scratch
                    + " and StringRes: " + getString(R.string.yes) + " or "
                    + getString(R.string.no));
    }

    private void addTurn(Turn turn, AdvStats advStats) {
        db.insertTurn(turn, advStats, getMatchId(), infoFragment.getTurnCount());
    }

    private void addTurn(Turn turn) {
        db.insertTurn(turn, getMatchId(), infoFragment.getTurnCount());
        updateViews();
    }

    private void undoTurn() {
        infoFragment.undoTurn();
        db.undoTurn(getMatchId(), infoFragment.getTurnCount() + 1);
        updateViews();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.menu = menu;
        updateMenuItems();
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_notes) {
            displayEditMatchNotesDialog();
        }

        if (id == R.id.action_location) {
            displayEditMatchLocationDialog();
        }

        if (id == R.id.action_game_status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            builder.setMessage(GameStatusStringBuilder.getMatchStatusString(db.getMatch(getMatchId())))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        }

        if (id == R.id.action_undo) {
            Snackbar.make(layout, R.string.undo_turn, Snackbar.LENGTH_SHORT).show();
            undoTurn();
        }

        if (id == R.id.action_redo) {
            Snackbar.make(layout, R.string.redo_turn, Snackbar.LENGTH_SHORT).show();
            addTurn(infoFragment.redoTurn());
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayEditMatchNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.edit_text, null);
        final EditText input = (EditText) view.findViewById(R.id.editText);
        input.setText(db.getMatch(getMatchId()).getNotes());
        builder.setTitle("Match Notes")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.updateMatchNotes(input.getText().toString(), getMatchId());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    private void displayEditMatchLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.edit_text, null);
        final EditText input = (EditText) view.findViewById(R.id.editText);
        input.setText(db.getMatch(getMatchId()).getLocation());
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setTitle("Match Location")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.updateMatchLocation(input.getText().toString(), getMatchId());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }
}
