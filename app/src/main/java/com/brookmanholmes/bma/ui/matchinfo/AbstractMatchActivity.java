package com.brookmanholmes.bma.ui.matchinfo;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DataSource;
import com.brookmanholmes.bma.data.FireDataSource;
import com.brookmanholmes.bma.data.TurnModel;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.bma.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.AdvStatsDialog;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by helios on 1/17/2017.
 */

abstract class AbstractMatchActivity extends BaseActivity implements
        AddTurnDialog.AddTurnListener, ChildEventListener {
    static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_ACCOUNT_ID;
    static final String KEY_UNDONE_TURNS = "key_undone_turns";
    static final String ARG_MATCH_KEY = "arg_match_key";
    private static final String TAG = "AbstractMatchActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout layout;
    @Bind(R.id.buttonAddTurn)
    FloatingActionButton fabAddTurn;

    DataSource db;
    Match match;
    Menu menu;
    Set<String> turnIds = new HashSet<>();

    DatabaseReference turnsReference;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        db = new FireDataSource();
        match = (Match) getIntent().getExtras().getSerializable("match");
        ArrayList<String> turns = getIntent().getExtras().getStringArrayList("turnIds");
        if (turns != null)
            turnIds.addAll(turns);
        if (match == null)
            throw new IllegalArgumentException("Must pass in a match");

        if (savedInstanceState != null) {
            match.setUndoneTurns((ArrayList) savedInstanceState.getSerializable(KEY_UNDONE_TURNS));
        }

        turnsReference = FirebaseDatabase.getInstance().getReference()
                .child("turns")
                .child(getMatchId());
        setToolbarTitle();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_UNDONE_TURNS, match.getUndoneTurns());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        turnsReference.addChildEventListener(this);
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        if (id == R.id.action_notes) {
            showEditMatchNotesDialog();
            analytics.logEvent("show_edit_match_notes", null);
        }

        if (id == R.id.action_location) {
            showEditMatchLocationDialog();
            analytics.logEvent("show_edit_match_location", null);
        }

        if (id == R.id.action_undo) {
            makeSnackbar(R.string.undid_turn, Snackbar.LENGTH_SHORT).setAction(R.string.redo_turn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTurn(match.getRedoTurn());
                }
            }).show();
            db.undoTurn(match);
            analytics.logEvent("undid_turn", null);
        }

        if (id == R.id.action_redo) {
            makeSnackbar(R.string.redid_turn, Snackbar.LENGTH_SHORT).setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.undoTurn(match);
                }
            }).show();
            Log.i(TAG, "onOptionsItemSelected: " + match.getUndoneTurns());
            ITurn turn = match.getRedoTurn();
            if (turn != null)
                addTurn(turn);
            analytics.logEvent("redid_turn", null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        turnsReference.removeEventListener(this);
        super.onPause();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (!turnIds.contains(dataSnapshot.getKey())) {
            ITurn turn = TurnModel.getTurn(dataSnapshot.getValue(TurnModel.class));
            turnIds.add(dataSnapshot.getKey());
            match.addTurn(turn);
            updateViews();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        turnIds.remove(dataSnapshot.getKey());
        match.undoTurn();
        updateViews();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.log(databaseError.getMessage());
    }

    @Override
    public void addTurn(TurnBuilder turnBuilder) {
        addTurn(new Turn(
                turnBuilder.turnEnd,
                turnBuilder.tableStatus,
                turnBuilder.foul,
                turnBuilder.seriousFoul,
                turnBuilder.advStats.build()));

        Bundle bundle = new Bundle();
        bundle.putString("turn_end", turnBuilder.turnEnd.name());
        bundle.putBoolean("foul", turnBuilder.foul);
        bundle.putBoolean("lost_game", turnBuilder.seriousFoul);
        bundle.putString("ball_statuses", turnBuilder.tableStatus.getBallStatuses().toString());
        analytics.logEvent("add_turn_finished", bundle);
    }

    void addTurn(final ITurn turn) {
        db.insertTurn(turn, getMatchId(), match.getCurrentPlayersId());
    }

    Snackbar makeSnackbar(@StringRes int resId, int duration) {
        return Snackbar.make(layout, resId, duration).setActionTextColor(getColor2(R.color.colorAccent));
    }

    String getMatchId() {
        return match.getMatchId();
    }

    void updateMenuItems() {
        if (menu != null) {
            menu.findItem(R.id.action_undo).setEnabled(match.isUndoTurn())
                    .getIcon().setAlpha(match.isUndoTurn() ? 255 : 128);
            menu.findItem(R.id.action_redo).setEnabled(match.isRedoTurn())
                    .getIcon().setAlpha(match.isRedoTurn() ? 255 : 128);
        }
    }

    void showAdvancedStatsDialog(PlayerTurn turn) {
        DialogFragment dialogFragment = AdvStatsDialog.create(match, turn);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    void showEditMatchNotesDialog() {
        AbstractMatchEditTextDialog dialog = AbstractMatchEditTextDialog.EditMatchNotesDialog.create(getString(R.string.match_notes), match.getNotes(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchNotes");
    }

    void showEditMatchLocationDialog() {
        AbstractMatchEditTextDialog dialog = AbstractMatchEditTextDialog.EditMatchLocationDialog.create(getString(R.string.match_location), match.getLocation(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchLocation");
    }

    protected abstract void setToolbarTitle();

    protected abstract void updateViews();

    @LayoutRes
    protected abstract int getLayout();

    @OnClick(R.id.buttonAddTurn)
    void showAddTurnDialog() {
        if (getSupportFragmentManager().findFragmentByTag("AddTurnDialog") == null) {
            AddTurnDialog addTurnDialog = AddTurnDialog.create(match, new ArrayList<>(turnIds));
            addTurnDialog.show(getSupportFragmentManager(), "AddTurnDialog");
        }
    }
}
