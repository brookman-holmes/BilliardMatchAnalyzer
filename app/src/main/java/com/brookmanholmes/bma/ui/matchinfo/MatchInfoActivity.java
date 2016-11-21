package com.brookmanholmes.bma.ui.matchinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameType;
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
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.utils.CustomViewPager;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener,
        OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot> {
    private static final String TAG = "MatchInfoActivity";
    private static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_PLAYER_NAME;
    private static final String KEY_UNDONE_TURNS = "key_undone_turns";
    private static final String ARG_MATCH_KEY = "arg_match_key";
    private final List<UpdateMatchInfo> listeners = new ArrayList<>();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.playerNameLayout)
    View playerNameLayout;
    @Bind(R.id.opponentNameLayout)
    View opponentNameLayout;
    @Bind(R.id.pager)
    CustomViewPager pager;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout layout;
    @Bind(R.id.buttonAddTurn)
    FloatingActionButton fabAddTurn;

    private DatabaseAdapter db;
    private Match match;
    private Menu mMenu;
    private Snackbar matchOverSnackbar, uploadMatchSnackbar;
    private Drawable activeArrow, inactiveArrow;
    private boolean addTurnButtonEnabled = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        ButterKnife.bind(this);
        pager.setPagingEnabled(false);

        setSupportActionBar(toolbar);

        activeArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_drop_down);
        inactiveArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_drop_down_inactive);

        db = new DatabaseAdapter(this);
        match = db.getMatchWithTurns(getMatchId());

        if (savedInstanceState != null) {
            match.setUndoneTurns((ArrayList) savedInstanceState.getSerializable(KEY_UNDONE_TURNS));
        }

        playerName.setText(match.getPlayer().getName());
        playerName.setCompoundDrawablesWithIntrinsicBounds(null, null, activeArrow, null);
        opponentName.setText(match.getOpponent().getName());
        opponentName.setCompoundDrawablesWithIntrinsicBounds(null, null, inactiveArrow, null);

        // no reason to click on The Ghost
        if (match.getGameStatus().gameType.isGhostGame())
            opponentNameLayout.setEnabled(false);

        matchOverSnackbar = makeSnackbar(R.string.match_over, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        matchOverSnackbar.dismiss();
                    }
                });

        setToolbarTitle(match.getGameStatus().gameType);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), getMatchId());
        pager.setAdapter(adapter);
    }

    private void setToolbarTitle(GameType gameType) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_match_info, ConversionUtils.getGameTypeString(this, gameType)));
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

    @OnClick(R.id.buttonAddTurn)
    public void addInning() {
        showAddTurnDialog();
    }

    @OnClick({R.id.opponentNameLayout, R.id.playerNameLayout})
    public void showPlayerOptionsMenu(LinearLayout view) {
        String name = ((TextView) view.getChildAt(0)).getText().toString();
        PlayerTurn turn = match.getPlayer().getName().equals(name) ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT;
        showChoiceDialog(name, turn, view);
    }

    private void updatePlayerNames(String name, String newName) {
        if (opponentName.getText().toString().equals(name)) {
            opponentName.setText(newName);
            match.setOpponentName(newName);
        } else if (playerName.getText().toString().equals(name)) {
            playerName.setText(newName);
            match.setPlayerName(newName);
        }
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    private void updateViews() {
        if (mMenu != null)
            updateMenuItems();

        if (match.isMatchOver()) {
            playerName.setTextColor(ContextCompat.getColor(this, R.color.white));
            opponentName.setTextColor(ContextCompat.getColor(this, R.color.white));
            playerName.setCompoundDrawablesWithIntrinsicBounds(null, null, activeArrow, null);
            opponentName.setCompoundDrawablesWithIntrinsicBounds(null, null, activeArrow, null);
        } else if (match.getCurrentPlayersName().equals(playerName.getText().toString())) {
            playerName.setTextColor(ContextCompat.getColor(this, R.color.white));
            playerName.setCompoundDrawablesWithIntrinsicBounds(null, null, activeArrow, null);
            opponentName.setTextColor(ContextCompat.getColor(this, R.color.non_current_players_turn_text));
            opponentName.setCompoundDrawablesWithIntrinsicBounds(null, null, inactiveArrow, null);
        } else {
            playerName.setTextColor(ContextCompat.getColor(this, R.color.non_current_players_turn_text));
            playerName.setCompoundDrawablesWithIntrinsicBounds(null, null, inactiveArrow, null);
            opponentName.setTextColor(ContextCompat.getColor(this, R.color.white));
            opponentName.setCompoundDrawablesWithIntrinsicBounds(null, null, activeArrow, null);
        }

        updateFragments();

        if (match.isMatchOver()) {
            fabAddTurn.hide();
            matchOverSnackbar.show();
        } else {
            matchOverSnackbar.dismiss();
            fabAddTurn.show();
        }
    }

    private Snackbar makeSnackbar(@StringRes int resId, int duration) {
        return Snackbar.make(layout, resId, duration).setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void updateMenuItems() {
        mMenu.findItem(R.id.action_undo).setEnabled(match.isUndoTurn());
        mMenu.findItem(R.id.action_redo).setEnabled(match.isRedoTurn());
        mMenu.findItem(R.id.action_undo).getIcon().setAlpha(this.mMenu.findItem(R.id.action_undo).isEnabled() ? 255 : 97);
        mMenu.findItem(R.id.action_redo).getIcon().setAlpha(this.mMenu.findItem(R.id.action_redo).isEnabled() ? 255 : 97);
    }

    @Override
    public void addTurn(TurnBuilder turnBuilder) {
        addTurn(match.createAndAddTurn(
                turnBuilder.tableStatus,
                turnBuilder.turnEnd,
                turnBuilder.foul,
                turnBuilder.lostGame,
                turnBuilder.advStats.build()));

        Bundle bundle = new Bundle();
        bundle.putString("turn_end", turnBuilder.turnEnd.name());
        bundle.putBoolean("foul", turnBuilder.foul);
        bundle.putBoolean("lost_game", turnBuilder.lostGame);
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

    private void undoTurn() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        this.mMenu = menu;
        updateMenuItems();

        // createGuide(); todo this needs more work so I'm going to not show it until I get it where I want it

        return true;
    }

    private void createGuide() {
        if (preferences.getBoolean("first_run_tutorial_info", true)) {
            Overlay overlay = new Overlay()
                    .setStyle(Overlay.Style.Circle)
                    .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTransparent))
                    .disableClick(true)
                    .disableClickThroughHole(true);

            ChainTourGuide t1;

            if (match.isMatchOver()) {
                t1 = ChainTourGuide.init(this)
                        .setToolTip(new ToolTip()
                                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                                .setTextColor(ContextCompat.getColor(this, R.color.white))
                                .setDescription("When you finish a match this snackbar will alert you that it is over")
                                .setGravity(Gravity.TOP | Gravity.LEFT))
                        .setOverlay(overlay)
                        .playLater(matchOverSnackbar.getView());
            } else {
                t1 = ChainTourGuide.init(this)
                        .setToolTip(new ToolTip()
                                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                                .setTextColor(ContextCompat.getColor(this, R.color.white))
                                .setDescription("You can add a turn to the match by clicking here")
                                .setGravity(Gravity.TOP | Gravity.LEFT))
                        .setOverlay(overlay)
                        .playLater(fabAddTurn);
            }

            ChainTourGuide t2 = ChainTourGuide.init(this)
                    .setToolTip(new ToolTip()
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                            .setDescription("Clicking on items in the toolbar and menu will allow you to do things like " +
                                    "undo turns, change the way the match is viewed, " +
                                    "view the status of the current game and more")
                            .setGravity(Gravity.CENTER)
                            .setShadow(true))
                    .setOverlay(new Overlay()
                            .setStyle(Overlay.Style.Rectangle)
                            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTransparent))
                            .disableClick(true)
                            .disableClickThroughHole(true))
                    .playLater(toolbar);
            ChainTourGuide t5 = ChainTourGuide.init(this)
                    .setToolTip(new ToolTip()
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
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

        if (item.getItemId() == R.id.action_match_view) {
            analytics.logEvent("changed_match_view", null);
            if (pager.getCurrentItem() == 0) {
                pager.setCurrentItem(1);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_view_stream));
            } else {
                pager.setCurrentItem(0);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_view_list));
            }
        }

        return super.onOptionsItemSelected(item);
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

    private void showAddTurnDialog() {
        if (getSupportFragmentManager().findFragmentByTag("AddTurnDialog") == null) {
            AddTurnDialog addTurnDialog = AddTurnDialog.create(match);
            addTurnDialog.show(getSupportFragmentManager(), "AddTurnDialog");
        }
    }

    private void showEditPlayerNameDialog(final String name) {
        AbstractMatchEditTextDialog dialog = EditPlayerNameDialog.create(getString(R.string.edit_player_name, name), name, getMatchId());
        dialog.show(getSupportFragmentManager(), "EditPlayerName");
    }

    private void showAdvancedStatsDialog(String name, PlayerTurn turn) {
        DialogFragment dialogFragment = AdvStatsDialog.create(getMatchId(), name, turn);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    private void showEditMatchNotesDialog() {
        AbstractMatchEditTextDialog dialog = EditMatchNotesDialog.create(getString(R.string.match_notes), match.getNotes(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchNotes");
    }

    private void showEditMatchLocationDialog() {
        AbstractMatchEditTextDialog dialog = EditMatchLocationDialog.create(getString(R.string.match_location), match.getLocation(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchLocation");
    }

    private void showChoiceDialog(final String name, final PlayerTurn turn, final View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        if (MatchDialogHelperUtils.currentPlayerTurnAndAdvancedStats(turn, match.getDetails()))
            popupMenu.inflate(R.menu.menu_player_adv);
        else popupMenu.inflate(R.menu.menu_player);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_adv_stats)
                    showAdvancedStatsDialog(name, turn);

                if (id == R.id.action_edit_name)
                    showEditPlayerNameDialog(name);

                if (id == R.id.action_view_profile) {
                    Intent intent = new Intent(MatchInfoActivity.this, PlayerProfileActivity.class);
                    intent.putExtra(ARG_PLAYER_NAME, name);
                    startActivity(intent);
                }

                return true;
            }
        });

        popupMenu.show();
    }

    void registerFragment(UpdateMatchInfo info) {
        listeners.add(info);
    }

    void removeFragment(UpdateMatchInfo info) {
        listeners.remove(info);
    }

    private void updateFragments() {
        for (UpdateMatchInfo listener : listeners) {
            listener.update(match);
        }
    }

    interface UpdateMatchInfo {
        void update(Match match);
    }

    public static class EditPlayerNameDialog extends AbstractMatchEditTextDialog {

        public static AbstractMatchEditTextDialog create(String title, String preText, long matchId) {
            AbstractMatchEditTextDialog dialog = new EditPlayerNameDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }

        @Override
        protected void onPositiveButton() {
            ((MatchInfoActivity) getActivity()).updatePlayerNames(preText, input.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.editPlayerName(matchId, preText, input.getText().toString());
                }
            }).start();
        }

    }

    public static class EditMatchLocationDialog extends AbstractMatchEditTextDialog {
        public static AbstractMatchEditTextDialog create(String title, String preText, long matchId) {
            AbstractMatchEditTextDialog dialog = new EditMatchLocationDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }

        @Override
        protected void onPositiveButton() {
            match.setLocation(input.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.updateMatchLocation(input.getText().toString(), matchId);
                }
            }).start();
        }
    }

    public static class EditMatchNotesDialog extends AbstractMatchEditTextDialog {
        public static AbstractMatchEditTextDialog create(String title, String preText, long matchId) {
            AbstractMatchEditTextDialog dialog = new EditMatchNotesDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {

        }

        @Override
        protected void onPositiveButton() {
            match.setNotes(input.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.updateMatchNotes(input.getText().toString(), matchId);
                }
            });
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final long matchId;

        public PagerAdapter(FragmentManager fm, long matchId) {
            super(fm);
            this.matchId = matchId;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MatchInfoFragment.create(matchId);
                default:
                    return TurnListFragment.create(matchId);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
