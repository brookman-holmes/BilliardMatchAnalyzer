package com.brookmanholmes.billiardmatchanalyzer.ui.matchinfo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.MyApplication;
import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.BaseActivity;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialog.GameStatusViewBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvStatsDialog;
import com.brookmanholmes.billiardmatchanalyzer.utils.ConversionUtils;
import com.brookmanholmes.billiardmatchanalyzer.utils.CustomViewPager;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.Turn;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener {
    public static final String TAG_INFO_FRAGMENT = "infoFragment";
    public static final String TAG_TURNS_FRAGMENT = "turnsFragment";
    private static final String TAG = "MatchInfoActivity";
    private static final String ARG_PLAYER_NAME = PlayerProfileActivity.ARG_PLAYER_NAME;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.playerName) TextView playerName;
    @Bind(R.id.opponentName) TextView opponentName;
    @Bind(R.id.pager) CustomViewPager pager;
    @Bind(R.id.coordinatorLayout) CoordinatorLayout layout;

    DatabaseAdapter db;
    Match<?> match;
    Menu menu;
    PagerAdapter adapter;
    List<UpdateMatchInfo> listeners = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        ButterKnife.bind(this);
        pager.setPagingEnabled(false);

        setSupportActionBar(toolbar);

        db = new DatabaseAdapter(this);

        match = db.getMatch(getMatchId());
        playerName.setText(match.getPlayer().getName());
        opponentName.setText(match.getOpponent().getName());

        // no reason to click on The Ghost
        if (match.getGameStatus().breakType == BreakType.GHOST)
            opponentName.setEnabled(false);

        setToolbarTitle(match.getGameStatus().gameType);
        adapter = new PagerAdapter(getSupportFragmentManager(), getMatchId());
        pager.setAdapter(adapter);
    }

    private void setToolbarTitle(GameType gameType) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_match_info, ConversionUtils.getGameTypeString(this, gameType)));
    }

    @Override protected void onResume() {
        super.onResume();
        updateViews();
    }

    @OnClick(R.id.buttonAddTurn) public void addInning(View view) {
        showAddTurnDialog();
    }

    @OnClick({R.id.opponentName, R.id.playerName})
    public void showPlayerOptionsMenu(TextView view) {
        String name = view.getText().toString();
        PlayerTurn turn = match.getPlayer().getName().equals(name) ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT;
        showChoiceDialog(name, turn);
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

    public void updateViews() {
        if (menu != null)
            updateMenuItems();

        if (match.getCurrentPlayersName().equals(playerName.getText().toString())) {
            playerName.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            opponentName.setTextColor(ContextCompat.getColor(this, R.color.non_current_players_turn));
        } else {
            playerName.setTextColor(ContextCompat.getColor(this, R.color.non_current_players_turn));
            opponentName.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        }

        updateFragments();
    }

    private void updateMenuItems() {
        menu.findItem(R.id.action_undo).setEnabled(match.isUndoTurn());
        menu.findItem(R.id.action_redo).setEnabled(match.isRedoTurn());
        menu.findItem(R.id.action_undo).getIcon().setAlpha(this.menu.findItem(R.id.action_undo).isEnabled() ? 255 : 127);
        menu.findItem(R.id.action_redo).getIcon().setAlpha(this.menu.findItem(R.id.action_redo).isEnabled() ? 255 : 127);
    }

    @Override public void addTurn(TurnBuilder turnBuilder) {
        addTurn(match.createAndAddTurn(
                turnBuilder.tableStatus,
                turnBuilder.turnEnd,
                turnBuilder.foul,
                turnBuilder.lostGame,
                turnBuilder.advStats.build()));
    }

    private void addTurn(Turn turn) {
        db.insertTurn(turn, getMatchId(), match.getTurnCount());
        //turnListFragment.updateMatch(match);
        updateViews();
    }

    private void undoTurn() {
        match.undoTurn();
        db.undoTurn(getMatchId(), match.getTurnCount() + 1);
        //turnListFragment.updateMatch(match);
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
            showEditMatchNotesDialog();
        }

        if (id == R.id.action_location) {
            showEditMatchLocationDialog();
        }

        if (id == R.id.action_game_status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            View view = getLayoutInflater().inflate(R.layout.dialog_game_status, null);
            GameStatusViewBuilder.bindView(match, view);
            builder.setView(view)
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
            addTurn(match.redoTurn());
        }

        if (item.getItemId() == R.id.action_match_view) {
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

    private void showAddTurnDialog() {
        AddTurnDialog addTurnDialog = AddTurnDialog.create(match);
        addTurnDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyAppTheme);
        addTurnDialog.show(getSupportFragmentManager(), "AddTurnDialog");
    }

    private void showEditPlayerNameDialog(final String name) {
        EditTextDialog dialog = EditPlayerNameDialog.create(getString(R.string.edit_player_name, name), name, getMatchId());
        dialog.show(getSupportFragmentManager(), "EditPlayerName");
    }

    private void showAdvancedStatsDialog(String name, PlayerTurn turn) {
        DialogFragment dialogFragment =
                AdvStatsDialog.create(getMatchId(), name, turn);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyAppTheme);
        dialogFragment.show(getSupportFragmentManager(), "AdvStatsDialog");
    }

    private void showEditMatchNotesDialog() {
        EditTextDialog dialog = EditMatchNotesDialog.create(getString(R.string.match_notes), match.getNotes(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchNotes");
    }

    private void showEditMatchLocationDialog() {
        EditTextDialog dialog = EditMatchLocationDialog.create(getString(R.string.match_location), match.getLocation(), getMatchId());
        dialog.show(getSupportFragmentManager(), "EditMatchLocation");
    }

    private void showChoiceDialog(final String name, final PlayerTurn turn) {
        ChoiceDialog dialog = ChoiceDialog.create(getMatchId(), turn, name);
        dialog.show(getSupportFragmentManager(), "ChoiceDialog");
    }

    void registerFragment(UpdateMatchInfo info) {
        listeners.add(info);
    }

    void removeFragment(UpdateMatchInfo info) {
        listeners.remove(info);
    }

    void updateFragments() {
        for (UpdateMatchInfo listener : listeners) {
            listener.update(match);
        }
    }

    interface UpdateMatchInfo {
        void update(Match<?> match);
    }

    public static class ChoiceDialog extends DialogFragment {
        private static final String ARG_PLAYER_TURN = "arg player turn";
        String[] items;
        long matchId;
        String name;
        PlayerTurn turn;

        static ChoiceDialog create(long matchId, PlayerTurn turn, String name) {
            ChoiceDialog dialog = new ChoiceDialog();
            Bundle args = new Bundle();
            args.putLong(ARG_MATCH_ID, matchId);
            args.putString(ARG_PLAYER_TURN, turn.name());
            args.putString(ARG_PLAYER_NAME, name);

            dialog.setArguments(args);
            return dialog;
        }

        @Override public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            DatabaseAdapter db = new DatabaseAdapter(getContext());
            turn = PlayerTurn.valueOf(getArguments().getString(ARG_PLAYER_TURN));
            name = getArguments().getString(ARG_PLAYER_NAME);
            matchId = getArguments().getLong(ARG_MATCH_ID);
            if (playerHasAdvancedStats(turn, db.getMatch(matchId).getAdvStats())) {
                items = new String[]{getString(R.string.view_profile), getString(R.string.view_adv_stats), getString(R.string.edit_name)};
            } else {
                items = new String[]{getString(R.string.view_profile), getString(R.string.edit_name)};
            }
        }

        @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                    .setTitle(name)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            if (items[which].equals(getString(R.string.view_adv_stats)))
                                ((MatchInfoActivity) getActivity()).showAdvancedStatsDialog(name, turn);
                            else if (items[which].equals(getString(R.string.view_profile))) {
                                Intent intent = new Intent(getContext(), PlayerProfileActivity.class);
                                intent.putExtra(ARG_PLAYER_NAME, name);
                                startActivity(intent);
                            } else if (items[which].equals(getString(R.string.edit_name))) {
                                ((MatchInfoActivity) getActivity()).showEditPlayerNameDialog(name);
                            }
                        }
                    }).create();
        }

        private boolean playerHasAdvancedStats(PlayerTurn turn, Match.StatsDetail detail) {
            if (turn == PlayerTurn.PLAYER && detail == Match.StatsDetail.ADVANCED_PLAYER)
                return true;
            else if (turn == PlayerTurn.OPPONENT && detail == Match.StatsDetail.ADVANCED_OPPONENT)
                return true;
            else return detail == Match.StatsDetail.ADVANCED;
        }
    }

    public static abstract class EditTextDialog extends DialogFragment {
        static final String ARG_TITLE = "arg title";
        static final String ARG_PRETEXT = "arg pretext";
        String title;
        String preText;
        EditText input;
        DatabaseAdapter db;
        Match<?> match;
        long matchId;
        private InputMethodManager inputMethodManager;

        @Override public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            title = getArguments().getString(ARG_TITLE);
            preText = getArguments().getString(ARG_PRETEXT);
            matchId = getArguments().getLong(ARG_MATCH_ID);
            db = new DatabaseAdapter(getContext());
            match = ((MatchInfoActivity) getActivity()).match;
        }

        @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_text, null, false);
            input = (EditText) view.findViewById(R.id.editText);
            input.setText(preText);
            inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            input.requestFocus();
            showKeyboard();
            input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (EditorInfo.IME_ACTION_DONE == actionId) {
                        onPositiveButton();
                        hideKeyboard();
                        dismiss();
                        return true;
                    }
                    return false;
                }
            });

            setupInput(input);
            input.setSelection(preText.length(), preText.length());

            return builder.setTitle(title)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            onPositiveButton();
                            hideKeyboard();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
        }

        @Override public void dismiss() {
            super.dismiss();
        }

        @Override public void onCancel(DialogInterface dialog) {
            hideKeyboard();
            super.onCancel(dialog);
        }

        private void hideKeyboard() {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        private void showKeyboard() {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        abstract void setupInput(EditText input);
        abstract void onPositiveButton();
    }

    public static class EditPlayerNameDialog extends EditTextDialog {

        public static EditTextDialog create(String title, String preText, long matchId) {
            EditTextDialog dialog = new EditPlayerNameDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }
        @Override void onPositiveButton() {
            ((MatchInfoActivity)getActivity()).updatePlayerNames(preText, input.getText().toString());
            db.editPlayerName(matchId, preText, input.getText().toString());
        }

    }

    public static class EditMatchLocationDialog extends EditTextDialog {
        public static EditTextDialog create(String title, String preText, long matchId) {
            EditTextDialog dialog = new EditMatchLocationDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }

        @Override void onPositiveButton() {
            match.setLocation(input.getText().toString());
            db.updateMatchLocation(input.getText().toString(), matchId);
        }
    }

    public static class EditMatchNotesDialog extends EditTextDialog {
        public static EditTextDialog create(String title, String preText, long matchId) {
            EditTextDialog dialog = new EditMatchNotesDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }
        @Override void setupInput(EditText input) {

        }

        @Override void onPositiveButton() {
            match.setNotes(input.getText().toString());
            db.updateMatchNotes(input.getText().toString(), matchId);
        }
    }

    public static class MatchInfoFragment extends Fragment implements UpdateMatchInfo {
        private static final String TAG = "MatchInfoFragment";
        @Bind(R.id.scrollView) RecyclerView recyclerView;
        private LinearLayoutManager layoutManager;
        private MatchInfoRecyclerAdapter adapter;

        /**
         * Mandatory empty constructor for the fragment manager to instantiate the
         * fragment (e.g. upon screen orientation changes).
         */
        public MatchInfoFragment() {
        }


        public static MatchInfoFragment create(long matchId) {
            MatchInfoFragment fragment = new MatchInfoFragment();

            Bundle args = new Bundle();
            args.putLong(ARG_MATCH_ID, matchId);

            fragment.setArguments(args);

            return fragment;
        }

        @Override public void onAttach(Context context) {
            super.onAttach(context);
            ((MatchInfoActivity)getActivity()).registerFragment(this);
        }

        @Override public void onDetach() {
            ((MatchInfoActivity)getActivity()).removeFragment(this);
            super.onDetach();
        }

        @Override public void update(Match<?> match) {
            adapter.updatePlayers(match);
        }

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            long matchId;

            if (getArguments().getLong(ARG_MATCH_ID, -1L) != -1L) {
                 matchId = getArguments().getLong(ARG_MATCH_ID);
            } else {
                throw new IllegalArgumentException("This fragment must be created with a match ID passed into it");
            }

            DatabaseAdapter db = new DatabaseAdapter(getContext());
            Match<?> match = db.getMatch(matchId);
            adapter = MatchInfoRecyclerAdapter.createMatchAdapter(match);
        }

        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list_view, container, false);
            ButterKnife.bind(this, view);

            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            return view;
        }

        @Override public void onDestroyView() {
            recyclerView.setAdapter(null);
            recyclerView = null;
            layoutManager = null;
            ButterKnife.unbind(this);
            super.onDestroyView();
        }

        @Override public void onDestroy() {
            RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
            refWatcher.watch(this);
            super.onDestroy();
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private long matchId;

        public PagerAdapter(FragmentManager fm, long matchId) {
            super(fm);
            this.matchId = matchId;
        }

        @Override public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MatchInfoFragment.create(matchId);
                default:
                    return TurnListFragment.create(matchId);
            }
        }

        @Override public int getCount() {
            return 2;
        }
    }
}
