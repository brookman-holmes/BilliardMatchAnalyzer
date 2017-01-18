package com.brookmanholmes.bma.ui.matchinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.ITurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.data.MatchModel;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.bma.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.bma.ui.dialog.AbstractMatchEditTextDialog;
import com.brookmanholmes.bma.ui.dialog.GameStatusViewBuilder;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.AdvStatsDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Brookman Holmes on 12/20/2016.
 */

public class HighRunAttemptActivity extends BaseActivity implements AddTurnDialog.AddTurnListener,
        OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot>, NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback, UpdatesPlayerNames {

    static final String ARG_MATCH_KEY = "arg_match_key";
    static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_PLAYER_NAME;
    static final String KEY_UNDONE_TURNS = "key_undone_turns";
    private static final String TAG = "HighRunAttemptAct";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout layout;
    @Bind(R.id.scrollView)
    RecyclerView recyclerView;

    DatabaseAdapter db;
    Match match;
    Menu menu;
    Snackbar uploadMatchSnackbar;
    HighRunAttemptAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_run);
        ButterKnife.bind(this);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

        setSupportActionBar(toolbar);
        setToolbarTitle();

        db = new DatabaseAdapter(this);
        match = db.getMatchWithTurns(getMatchId());

        adapter = new HighRunAttemptAdapter(this, match);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupNfc();
    }

    public void showAdvShotData() {
        showAdvancedStatsDialog(match.getPlayer().getName(), PlayerTurn.PLAYER);
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_UNDONE_TURNS, match.getUndoneTurns());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.menu = menu;
        menu.add(0, R.id.action_edit_name, 103, R.string.edit_name);
        updateMenuItems();
        return true;
    }

    private void updateMenuItems() {
        if (menu != null) {
            menu.findItem(R.id.action_undo).setEnabled(match.isUndoTurn());
            menu.findItem(R.id.action_redo).setEnabled(match.isRedoTurn());
            menu.findItem(R.id.action_undo).getIcon().setAlpha(match.isUndoTurn() ? 255 : 97);
            menu.findItem(R.id.action_redo).getIcon().setAlpha(match.isRedoTurn() ? 255 : 97);
        }
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

        if (id == R.id.action_share_match) {
            shareMatch();
        }

        if (id == R.id.action_notes) {
            showEditMatchNotesDialog();
            analytics.logEvent("show_edit_match_notes", null);
        }

        if (id == R.id.action_location) {
            showEditMatchLocationDialog();
            analytics.logEvent("show_edit_match_location", null);
        }

        if (id == R.id.action_game_status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            View view = getLayoutInflater().inflate(R.layout.dialog_game_status, null);
            GameStatusViewBuilder.bindView(match, view);
            builder.setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();

            analytics.logEvent("show_game_status", null);
        }

        if (id == R.id.action_undo) {
            makeSnackbar(R.string.undid_turn, Snackbar.LENGTH_SHORT).setAction(R.string.redo_turn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTurn(match.redoTurn());
                }
            }).show();
            undoTurn();
        }

        if (id == R.id.action_redo) {
            makeSnackbar(R.string.redid_turn, Snackbar.LENGTH_SHORT).setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    undoTurn();
                }
            }).show();
            addTurn(match.redoTurn());
            analytics.logEvent("redid_turn", null);
        }

        return super.onOptionsItemSelected(item);
    }

    void setupNfc() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        byte[] payload = MatchModel.marshall(match);
        String mime = getMime();

        return new NdefMessage(NdefRecord.createMime(mime, payload));
    }

    String getMime() {
        return "application/com.brookmanholmes.bma.hramatchmodel";
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) rawMsgs[0];

        Match match = MatchModel.unmarshall(message.getRecords()[0].getPayload()).getMatch();
        DatabaseAdapter db = new DatabaseAdapter(this);
        final long matchId = db.insertMatch(match);
        getIntent().putExtra(ARG_MATCH_ID, matchId);
    }

    void shareMatch() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.setMaxUploadRetryTimeMillis(10000);
        String matchKey = getPreferences().getString(ARG_MATCH_KEY + getMatchId(), "");
        if (matchKey.isEmpty() || !matchKey.equals(matchKey.toUpperCase())) {
            matchKey = RandomStringUtils.randomAlphabetic(6).toUpperCase();
            getPreferences().edit().putString(ARG_MATCH_KEY + getMatchId(), matchKey).apply();
        }

        final UploadTask task = storage
                .getReferenceFromUrl(getString(R.string.firebase_storage_ref))
                .child("matches/" + matchKey + "/match")
                .putBytes(MatchModel.marshall(match));

        task.addOnFailureListener(this).addOnSuccessListener(this);

        uploadMatchSnackbar = Snackbar.make(layout, "Uploading match", Snackbar.LENGTH_INDEFINITE)
                .setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (task.isInProgress())
                            task.cancel();
                    }
                });
        uploadMatchSnackbar.show();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        uploadMatchSnackbar.dismiss();
        if (!e.getMessage().equals("The operation was cancelled.")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            builder.setTitle("Error uploading match")
                    .setMessage(e.getLocalizedMessage())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        uploadMatchSnackbar.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_share_match, null, false);
        TextView text = ButterKnife.findById(view, R.id.matchKey);
        text.setText(getPreferences().getString(ARG_MATCH_KEY + getMatchId(), ""));
        builder.setTitle("Match uploaded")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }

    void setToolbarTitle() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.game_straight_ghost);
    }

    long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    @OnClick(R.id.buttonAddTurn)
    public void addTurn() {
        showAddTurnDialog();
    }

    @Override
    public void addTurn(TurnBuilder turnBuilder) {
        addTurn(match.createAndAddTurn(
                turnBuilder.tableStatus,
                turnBuilder.turnEnd,
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.insertTurn(turn, getMatchId(), match.getTurnCount());
            }
        }).start();
        updateViews();
    }

    void undoTurn() {
        analytics.logEvent("turn_undone", null);
        match.undoTurn();
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.undoTurn(getMatchId(), match.getTurnCount() + 1);
            }
        }).start();
        updateViews();
    }

    Snackbar makeSnackbar(@StringRes int resId, int duration) {
        return Snackbar.make(layout, resId, duration).setActionTextColor(getColor2(R.color.colorAccent));
    }

    void updateViews() {
        adapter.update(match);
        updateMenuItems();
    }

    void showAddTurnDialog() {
        if (getSupportFragmentManager().findFragmentByTag("AddTurnDialog") == null) {
            AddTurnDialog addTurnDialog = AddTurnDialog.create(match);
            addTurnDialog.show(getSupportFragmentManager(), "AddTurnDialog");
        }
    }

    void showAdvancedStatsDialog(String name, PlayerTurn turn) {
        DialogFragment dialogFragment = AdvStatsDialog.create(getMatchId(), name, turn);
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

    private void showEditPlayerNameDialog(final String name) {
        AbstractMatchEditTextDialog dialog = AbstractMatchEditTextDialog.EditPlayerNameDialog.create(getString(R.string.edit_player_name, name), name, getMatchId());
        dialog.show(getSupportFragmentManager(), "EditPlayerName");
    }

    @Override
    public void updatePlayerName(String playerToUpdate, String newName) {
        match.setPlayerName(newName);
        adapter.update(match);
    }
}
