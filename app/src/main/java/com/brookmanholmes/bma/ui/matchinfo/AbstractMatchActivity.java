package com.brookmanholmes.bma.ui.matchinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.ui.stats.AdvStatsDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by helios on 1/17/2017.
 */

abstract class AbstractMatchActivity extends BaseActivity implements
        AddTurnDialog.AddTurnListener,
        OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot>, NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback, UpdatesPlayerNames {
    static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_PLAYER_NAME;
    static final String KEY_UNDONE_TURNS = "key_undone_turns";
    static final String ARG_MATCH_KEY = "arg_match_key";
    private static final String TAG = "AbstractMatchActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout layout;
    @Bind(R.id.buttonAddTurn)
    FloatingActionButton fabAddTurn;

    DatabaseAdapter db;
    Match match;
    Menu menu;
    Snackbar uploadMatchSnackbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

        setSupportActionBar(toolbar);

        db = new DatabaseAdapter(this);
        match = db.getMatchWithTurns(getMatchId());
        if (savedInstanceState != null) {
            match.setUndoneTurns((ArrayList) savedInstanceState.getSerializable(KEY_UNDONE_TURNS));
        }

        setToolbarTitle();
        setupNfc();
    }

    private void setupNfc() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        byte[] payload = MatchModel.marshall(match);
        String mime = getMimeType();

        return new NdefMessage(NdefRecord.createMime(mime, payload));
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) rawMsgs[0];

        Match match = MatchModel.unmarshall(message.getRecords()[0].getPayload()).getMatch();
        DatabaseAdapter db = new DatabaseAdapter(this);
        final long matchId = db.insertMatch(match);
        getIntent().putExtra(ARG_MATCH_ID, matchId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_UNDONE_TURNS, match.getUndoneTurns());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.menu = menu;
        updateMenuItems();

        // createGuide(); todo this needs more work so I'm going to not show it until I get it where I want it

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

    long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    void updateMenuItems() {
        if (menu != null) {
            menu.findItem(R.id.action_undo).setEnabled(match.isUndoTurn())
                    .getIcon().setAlpha(match.isUndoTurn() ? 255 : 97);
            menu.findItem(R.id.action_redo).setEnabled(match.isRedoTurn())
                    .getIcon().setAlpha(match.isRedoTurn() ? 255 : 97);
        }
    }

    void showAdvancedStatsDialog(String name, PlayerTurn turn) {
        DialogFragment dialogFragment = AdvStatsDialog.create(getMatchId(), name, turn);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    void showEditPlayerNameDialog(final String name) {
        AbstractMatchEditTextDialog dialog = AbstractMatchEditTextDialog.EditPlayerNameDialog.create(getString(R.string.edit_player_name, name), name, getMatchId());
        dialog.show(getSupportFragmentManager(), "EditPlayerName");
    }

    void showEditMatchNotesDialog() {
        AbstractMatchEditTextDialog dialog = AbstractMatchEditTextDialog.EditMatchNotesDialog.create(getString(R.string.match_notes), match.getNotes(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchNotes");
    }

    void showEditMatchLocationDialog() {
        AbstractMatchEditTextDialog dialog = AbstractMatchEditTextDialog.EditMatchLocationDialog.create(getString(R.string.match_location), match.getLocation(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchLocation");
    }

    private void shareMatch() {
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

    protected abstract void setToolbarTitle();

    protected abstract void updateViews();

    protected abstract String getMimeType();

    @LayoutRes
    protected abstract int getLayout();

    @OnClick(R.id.buttonAddTurn)
    void showAddTurnDialog() {
        if (getSupportFragmentManager().findFragmentByTag("AddTurnDialog") == null) {
            AddTurnDialog addTurnDialog = AddTurnDialog.create(match);
            addTurnDialog.show(getSupportFragmentManager(), "AddTurnDialog");
        }
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

    /*
    private void createGuide() {
        if (preferences.getBoolean("first_run_tutorial_info", true)) {
            Overlay overlay = new Overlay()
                    .setStyle(Overlay.Style.Circle)
                    .setBackgroundColor(getColor2(R.color.colorPrimaryTransparent))
                    .disableClick(true)
                    .disableClickThroughHole(true);

            ChainTourGuide t1;

            if (match.isMatchOver()) {
                t1 = ChainTourGuide.init(this)
                        .setToolTip(new ToolTip()
                                .setBackgroundColor(getColor2(R.color.colorAccent))
                                .setTextColor(getColor2(R.color.white))
                                .setDescription("When you finish a match this snackbar will alert you that it is over")
                                .setGravity(Gravity.TOP | Gravity.LEFT))
                        .setOverlay(overlay)
                        .playLater(matchOverSnackbar.getView());
            } else {
                t1 = ChainTourGuide.init(this)
                        .setToolTip(new ToolTip()
                                .setBackgroundColor(getColor2(R.color.colorAccent))
                                .setTextColor(getColor2(R.color.white))
                                .setDescription("You can add a turn to the match by clicking here")
                                .setGravity(Gravity.TOP | Gravity.LEFT))
                        .setOverlay(overlay)
                        .playLater(fabAddTurn);
            }

            ChainTourGuide t2 = ChainTourGuide.init(this)
                    .setToolTip(new ToolTip()
                            .setTextColor(getColor2(R.color.white))
                            .setBackgroundColor(getColor2(R.color.colorAccent))
                            .setDescription("Clicking on items in the toolbar and menu will allow you to do things like " +
                                    "undo turns, change the way the match is viewed, " +
                                    "view the status of the current game and more")
                            .setGravity(Gravity.CENTER)
                            .setShadow(true))
                    .setOverlay(new Overlay()
                            .setStyle(Overlay.Style.Rectangle)
                            .setBackgroundColor(getColor2(R.color.colorPrimaryTransparent))
                            .disableClick(true)
                            .disableClickThroughHole(true))
                    .playLater(toolbar);
            ChainTourGuide t5 = ChainTourGuide.init(this)
                    .setToolTip(new ToolTip()
                            .setTextColor(getColor2(R.color.white))
                            .setBackgroundColor(getColor2(R.color.colorAccent))
                            .setDescription("Clicking on a player's name will give you some options\n" +
                                    "to choose from, like view their advanced stats for this match\n" +
                                    "or going directly to their profile")
                            .setGravity(Gravity.LEFT | Gravity.BOTTOM))
                    .playLater(opponentNameLayout);

            Sequence sequence = new Sequence.SequenceBuilder()
                    .add(t1, t2, t5)
                    .setDefaultPointer(null)
                    .setDefaultOverlay(overlay)
                    .setContinueMethod(Sequence.ContinueMethod.Overlay)
                    .build();

            ChainTourGuide.init(this).playInSequence(sequence);

            preferences.edit().putBoolean("first_run_tutorial_info", false).apply();
        }
    }
    */
}
