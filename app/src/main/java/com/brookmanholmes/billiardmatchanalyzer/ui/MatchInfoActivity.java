package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.TurnListDialog;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.Turn;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener {
    public static final String INFO_FRAGMENT_TAG = "infoFragment";
    private static final String TAG = "MatchInfoActivity";
    private static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_PLAYER_NAME;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
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

        Match<?> match = db.getMatch(getMatchId());
        playerName.setText(match.getPlayer().getName());
        opponentName.setText(match.getOpponent().getName());

        setToolbarTitle(match.getGameStatus().gameType);

        infoFragment = (MatchInfoFragment) getSupportFragmentManager().findFragmentByTag(INFO_FRAGMENT_TAG);

        if (infoFragment == null) {
            infoFragment = MatchInfoFragment.createMatchInfoFragment(getMatchId());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, infoFragment, INFO_FRAGMENT_TAG).commit();
        }
    }

    private void setToolbarTitle(GameType gameType) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_match_info, getGameTypeString(gameType)));
    }

    private String getGameTypeString(GameType gameType) {
        switch (gameType) {
            case APA_EIGHT_BALL:
                return getString(R.string.game_apa_eight);
            case APA_NINE_BALL:
                return getString(R.string.game_apa_nine);
            case BCA_EIGHT_BALL:
                return getString(R.string.game_bca_eight);
            case BCA_NINE_BALL:
                return getString(R.string.game_bca_nine);
            case BCA_TEN_BALL:
                return getString(R.string.game_bca_ten);
            case AMERICAN_ROTATION:
                return getString(R.string.game_american_rotation);
            case STRAIGHT_POOL:
                return getString(R.string.game_straight);
            default:
                throw new InvalidGameTypeException("No such GameType: " + gameType);
        }
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
        displayChoiceDialog(playerName.getText().toString(), PlayerTurn.PLAYER);
    }

    @OnClick(R.id.opponentName) public void showTestLayout2() {
        displayChoiceDialog(opponentName.getText().toString(), PlayerTurn.OPPONENT);
    }

    private void displayChoiceDialog(final String name, final PlayerTurn turn) {
        final String[] items;

        if (playerHasAdvancedStats(turn, db.getMatch(getMatchId()).getAdvStats())) {
            items = new String[]{getString(R.string.view_profile), getString(R.string.view_adv_stats), getString(R.string.edit_name)};
        } else {
            items = new String[]{getString(R.string.view_profile), getString(R.string.edit_name)};
        }

        new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle(name)
                .setItems(items, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                if (items[which].equals(getString(R.string.view_adv_stats)))
                                    displayAdvancedStatsDialog(name, turn);
                                else if (items[which].equals(getString(R.string.view_profile))) {
                                    Intent intent = new Intent(MatchInfoActivity.this, PlayerProfileActivity.class);
                                    intent.putExtra(ARG_PLAYER_NAME, name);
                                    startActivity(intent);
                                } else if (items[which].equals(getString(R.string.edit_name))) {
                                    Snackbar.make(layout, "Change name selected - not yet implemented", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }).create().show();
    }

    private boolean playerHasAdvancedStats(PlayerTurn turn, Match.StatsDetail detail) {
        if (turn == PlayerTurn.PLAYER && detail == Match.StatsDetail.ADVANCED_PLAYER)
            return true;
        else if (turn == PlayerTurn.OPPONENT && detail == Match.StatsDetail.ADVANCED_OPPONENT)
            return true;
        else return detail == Match.StatsDetail.ADVANCED;
    }

    private void displayAdvancedStatsDialog(String name, PlayerTurn turn) {
        DialogFragment dialogFragment =
                AdvStatsDialog.create(getMatchId(), name, turn);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    public void updateViews() {
        if (menu != null)
            updateMenuItems();

        if (infoFragment.getCurrentPlayersName().equals(playerName.getText().toString())) {
            playerName.setTextColor(getResources().getColor(android.R.color.white));
            opponentName.setTextColor(getResources().getColor(R.color.non_current_players_turn));
        } else {
            playerName.setTextColor(getResources().getColor(R.color.non_current_players_turn));
            opponentName.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private void updateMenuItems() {
        menu.findItem(R.id.action_undo).setEnabled(infoFragment.isUndoTurn());
        menu.findItem(R.id.action_redo).setEnabled(infoFragment.isRedoTurn());
        this.menu.findItem(R.id.action_undo).getIcon().setAlpha(this.menu.findItem(R.id.action_undo).isEnabled() ? 255 : 120);
        this.menu.findItem(R.id.action_redo).getIcon().setAlpha(this.menu.findItem(R.id.action_redo).isEnabled() ? 255 : 120);
    }

    @Override public void addTurn(TurnBuilder turnBuilder) {
        if (turnBuilder.advStats.build().use())
            addTurn(infoFragment.createAndAddTurnToMatch(
                    turnBuilder.tableStatus,
                    turnBuilder.turnEnd,
                    turnBuilder.foul,
                    turnBuilder.lostGame),
                    turnBuilder.advStats.build());
        else
            addTurn(infoFragment.createAndAddTurnToMatch(
                    turnBuilder.tableStatus,
                    turnBuilder.turnEnd,
                    turnBuilder.foul,
                    turnBuilder.lostGame));
    }

    private void addTurn(Turn turn, AdvStats advStats) {
        db.insertTurn(turn, advStats, getMatchId(), infoFragment.getTurnCount());
        updateViews();
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
                        @Override public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        }

        if (id == R.id.action_undo) {
            Snackbar.make(layout, R.string.undid_turn, Snackbar.LENGTH_SHORT).show();
            undoTurn();
        }

        if (id == R.id.action_redo) {
            Snackbar.make(layout, R.string.redid_turn, Snackbar.LENGTH_SHORT).show();
            addTurn(infoFragment.redoTurn());
        }

        if (item.getItemId() == R.id.action_adv_stats) {
            TurnListDialog dialog = TurnListDialog.create(getMatchId());
            dialog.show(getSupportFragmentManager(), "TurnListDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayEditMatchNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.edit_text, null);
        final EditText input = (EditText) view.findViewById(R.id.editText);
        input.setText(db.getMatch(getMatchId()).getNotes());
        builder.setTitle(getString(R.string.match_notes))
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        db.updateMatchNotes(input.getText().toString(), getMatchId());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
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
        builder.setTitle(getString(R.string.match_location))
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        db.updateMatchLocation(input.getText().toString(), getMatchId());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }
}
