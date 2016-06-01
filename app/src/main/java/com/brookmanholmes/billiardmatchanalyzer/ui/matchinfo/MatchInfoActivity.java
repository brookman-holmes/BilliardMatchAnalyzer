package com.brookmanholmes.billiardmatchanalyzer.ui.matchinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.MyApplication;
import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.ApaPlayer;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.BaseViewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.BreaksHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.BreaksWithWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.FooterViewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.MatchOverviewHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.RunOutsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.RunOutsWithEarlyWinsHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.SafetiesHolder;
import com.brookmanholmes.billiardmatchanalyzer.adaptervh.ShootingPctHolder;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.BaseActivity;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.dialog.GameStatusStringBuilder;
import com.brookmanholmes.billiardmatchanalyzer.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.AdvStatsDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.stats.TurnListDialog;
import com.brookmanholmes.billiards.game.InvalidGameTypeException;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.IMatch;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.billiards.player.ApaEightBallPlayer;
import com.brookmanholmes.billiards.player.ApaNineBallPlayer;
import com.brookmanholmes.billiards.player.EightBallPlayer;
import com.brookmanholmes.billiards.player.IApa;
import com.brookmanholmes.billiards.player.NineBallPlayer;
import com.brookmanholmes.billiards.player.TenBallPlayer;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.Turn;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.squareup.leakcanary.RefWatcher;

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

        // no reason to click on The Ghost
        if (match.getGameStatus().breakType == BreakType.GHOST)
            opponentName.setEnabled(false);

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
        addTurnDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyAppTheme);
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
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyAppTheme);
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
        this.menu.findItem(R.id.action_undo).getIcon().setAlpha(this.menu.findItem(R.id.action_undo).isEnabled() ? 255 : 127);
        this.menu.findItem(R.id.action_redo).getIcon().setAlpha(this.menu.findItem(R.id.action_redo).isEnabled() ? 255 : 127);
    }

    @Override public void addTurn(TurnBuilder turnBuilder) {
        addTurn(infoFragment.createAndAddTurnToMatch(
                turnBuilder.tableStatus,
                turnBuilder.turnEnd,
                turnBuilder.foul,
                turnBuilder.lostGame,
                turnBuilder.advStats.build()));
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

    public static class MatchInfoFragment extends Fragment implements IMatch {
        @Bind(R.id.scrollView) RecyclerView recyclerView;
        private MatchInfoRecyclerAdapter<?> adapter;
        private LinearLayoutManager layoutManager;

        /**
         * Mandatory empty constructor for the fragment manager to instantiate the
         * fragment (e.g. upon screen orientation changes).
         */
        public MatchInfoFragment() {
        }


        public static MatchInfoFragment createMatchInfoFragment(long matchId) {
            MatchInfoFragment fragment = new MatchInfoFragment();

            Bundle args = new Bundle();
            args.putLong(ARG_MATCH_ID, matchId);

            fragment.setArguments(args);

            return fragment;
        }

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            long matchId = setMatchId();
            DatabaseAdapter db = new DatabaseAdapter(getContext());
            Match<?> match = db.getMatch(matchId);
            adapter = MatchInfoRecyclerAdapter.createMatchAdapter(match);
            db = null;
        }

        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list_view, container, false);
            ButterKnife.bind(this, view);

            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            recyclerView.setAdapter(adapter);

            return view;
        }

        @Override public void onDestroyView() {
            recyclerView.setAdapter(null);
            recyclerView = null;
            layoutManager = null;
            ButterKnife.unbind(this);

            RefWatcher refWatcher = MyApplication.getRefWatcher(getContext());
            refWatcher.watch(this);

            super.onDestroyView();
        }

        private long setMatchId() {
            if (getArguments().getLong(ARG_MATCH_ID) != 0L) {
                return getArguments().getLong(ARG_MATCH_ID);
            } else {
                throw new IllegalArgumentException("This fragment must be created with a match ID passed into it");
            }
        }

        @Override
        public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats) {
            return adapter.createAndAddTurnToMatch(tableStatus, turnEnd, scratch, isGameLost, advStats);
        }

        @Override public String getCurrentPlayersName() {
            return adapter.getCurrentPlayersName();
        }

        @Override public String getNonCurrentPlayersName() {
            return adapter.getNonCurrentPlayersName();
        }

        @Override public void undoTurn() {
            adapter.undoTurn();
        }

        @Override public boolean isRedoTurn() {
            return adapter.isRedoTurn();
        }

        @Override public boolean isUndoTurn() {
            return adapter.isUndoTurn();
        }

        @Override public Turn redoTurn() {
            return adapter.redoTurn();
        }

        @Override public AbstractPlayer getPlayer() {
            return adapter.getPlayer();
        }

        @Override public AbstractPlayer getOpponent() {
            return adapter.getOpponent();
        }

        @Override public String getLocation() {
            return adapter.getLocation();
        }

        @Override public int getTurnCount() {
            return adapter.getTurnCount();
        }

        @Override public long getMatchId() {
            return adapter.getMatchId();
        }

        /**
         * Created by Brookman Holmes on 1/13/2016.
         */
        static class MatchInfoRecyclerAdapter<T extends AbstractPlayer> extends RecyclerView.Adapter<BaseViewHolder<T>>
                implements IMatch<T> {
            public static final int ITEM_MATCH_OVERVIEW = 0;
            public static final int ITEM_SHOOTING_PCT = 1;
            public static final int ITEM_SAFETIES = 2;
            public static final int ITEM_BREAKS = 3;
            public static final int ITEM_RUN_OUTS = 4;
            public static final int ITEM_FOOTER = 10;
            public static final int ITEM_APA_STATS = 5;
            final int gameBall;
            Match.StatsDetail detail;
            ViewType viewTypeToggle = ViewType.CARDS;
            Match<T> match;

            MatchInfoRecyclerAdapter(Match<T> match) {
                this.match = match;
                detail = match.getAdvStats();
                this.gameBall = match.getGameStatus().GAME_BALL;
            }

            MatchInfoRecyclerAdapter(Match<T> match, ViewType viewType) {
                this(match);
                viewTypeToggle = viewType;
            }

            @SuppressWarnings("unchecked")
            public static <T extends AbstractPlayer> MatchInfoRecyclerAdapter<?> createMatchAdapter(Match<T> match) {
                // this is probably fucking retarded?
                switch (match.getGameStatus().gameType) {
                    case BCA_NINE_BALL:
                        return new BcaNineBallMatchInfoRecyclerAdapter((Match<NineBallPlayer>) match);
                    case BCA_EIGHT_BALL:
                        return new BcaEightBallMatchInfoRecyclerAdapter((Match<EightBallPlayer>) match);
                    case BCA_TEN_BALL:
                        return new BcaTenBallMatchInfoRecyclerAdapter((Match<TenBallPlayer>) match);
                    case APA_NINE_BALL:
                        return new ApaMatchInfoRecyclerAdapter<>((Match<ApaNineBallPlayer>) match);
                    case APA_EIGHT_BALL:
                        return new ApaMatchInfoRecyclerAdapter<>((Match<ApaEightBallPlayer>) match);
                    default:
                        throw new InvalidGameTypeException(match.getGameStatus().gameType.toString());
                }
            }

            @SuppressWarnings("unchecked")
            public static <T extends AbstractPlayer> MatchInfoRecyclerAdapter<?> createMatchAdapterWithCardViews(Match<T> match) {
                // this is probably fucking retarded?
                switch (match.getGameStatus().gameType) {
                    case BCA_NINE_BALL:
                        return new BcaNineBallMatchInfoRecyclerAdapter((Match<NineBallPlayer>) match, MatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                    case BCA_EIGHT_BALL:
                        return new BcaEightBallMatchInfoRecyclerAdapter((Match<EightBallPlayer>) match, MatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                    case BCA_TEN_BALL:
                        return new BcaTenBallMatchInfoRecyclerAdapter((Match<TenBallPlayer>) match, MatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                    case APA_NINE_BALL:
                        return new ApaMatchInfoRecyclerAdapter<>((Match<ApaNineBallPlayer>) match, MatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                    case APA_EIGHT_BALL:
                        return new ApaMatchInfoRecyclerAdapter<>((Match<ApaEightBallPlayer>) match, MatchInfoFragment.MatchInfoRecyclerAdapter.ViewType.CARDS);
                    default:
                        throw new InvalidGameTypeException(match.getGameStatus().gameType.toString());
                }
            }

            @Override public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(viewType), parent, false);
                view.setTag(viewType);
                return getMatchInfoHolderByViewType(view, viewType);
            }

            @Override public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
                holder.bind(getPlayer(), getOpponent());
            }

            @Override public int getItemCount() {
                if (match.getGameStatus().breakType == BreakType.GHOST)
                    return 4;
                else
                    return 6;
            }

            @Override public int getItemViewType(int position) {
                if (match.getGameStatus().breakType == BreakType.GHOST) {
                    switch (position) {
                        case 0:
                            return ITEM_MATCH_OVERVIEW;
                        case 1:
                            return ITEM_SHOOTING_PCT;
                        case 2:
                            return ITEM_BREAKS;
                        default:
                            return ITEM_FOOTER;
                    }
                }

                if (position == getItemCount() - 1)
                    return ITEM_FOOTER;
                else return position;
            }

            @Override public T getPlayer() {
                return match.getPlayer();
            }

            @Override public T getOpponent() {
                return match.getOpponent();
            }

            @Override
            public Turn createAndAddTurnToMatch(TableStatus tableStatus, TurnEnd turnEnd, boolean scratch, boolean isGameLost, AdvStats advStats) {
                Turn turn = match.createAndAddTurnToMatch(tableStatus, turnEnd, scratch, isGameLost, advStats);
                notifyDataSetChanged();
                return turn;
            }

            @Override public void undoTurn() {
                match.undoTurn();
                notifyDataSetChanged();
            }

            @Override public boolean isRedoTurn() {
                return match.isRedoTurn();
            }

            @Override public boolean isUndoTurn() {
                return match.isUndoTurn();
            }

            @Override public Turn redoTurn() {
                Turn turn = match.redoTurn();
                notifyDataSetChanged();
                return turn;
            }

            @Override public long getMatchId() {
                return match.getMatchId();
            }

            @Override public String getLocation() {
                return match.getLocation();
            }

            @Override public String getCurrentPlayersName() {
                return match.getCurrentPlayersName();
            }

            @Override public String getNonCurrentPlayersName() {
                return match.getNonCurrentPlayersName();
            }

            @Override public int getTurnCount() {
                return match.getTurnCount();
            }

            @LayoutRes int getLayoutResource(int viewType) {
                if (viewTypeToggle == ViewType.CARDS) {
                    switch (viewType) {
                        case ITEM_RUN_OUTS:
                            return R.layout.card_run_outs;
                        case ITEM_BREAKS:
                            return R.layout.card_breaks;
                        case ITEM_MATCH_OVERVIEW:
                            return R.layout.card_match_overview;
                        case ITEM_SAFETIES:
                            return R.layout.card_safeties;
                        case ITEM_SHOOTING_PCT:
                            return R.layout.card_shooting_pct;
                        case ITEM_FOOTER:
                            return R.layout.footer;
                        default:
                            throw new IllegalArgumentException("No such view type: " + viewType);
                    }
                } else {
                    switch (viewType) {
                        case ITEM_RUN_OUTS:
                            return R.layout.plain_runs;
                        case ITEM_BREAKS:
                            return R.layout.plain_breaks;
                        case ITEM_MATCH_OVERVIEW:
                            return R.layout.plain_match_overview;
                        case ITEM_SAFETIES:
                            return R.layout.plain_safeties;
                        case ITEM_SHOOTING_PCT:
                            return R.layout.plain_shooting;
                        case ITEM_FOOTER:
                            return R.layout.footer;
                        default:
                            throw new IllegalArgumentException("No such view type: " + viewType);
                    }
                }
            }

            BaseViewHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
                switch (viewType) {
                    case ITEM_MATCH_OVERVIEW:
                        return new MatchOverviewHolder<>(view, detail);
                    case ITEM_SHOOTING_PCT:
                        return new ShootingPctHolder<>(view, detail);
                    case ITEM_BREAKS:
                        return new BreaksHolder<>(view, match.getGameStatus().GAME_BALL, detail);
                    case ITEM_RUN_OUTS:
                        return new RunOutsHolder<>(view, detail);
                    case ITEM_SAFETIES:
                        return new SafetiesHolder<>(view, detail);
                    case ITEM_FOOTER:
                        return new FooterViewHolder<>(view);
                    default:
                        throw new IllegalArgumentException("No such view type");
                }
            }

            public enum ViewType {
                CARDS,
                LIST
            }

            /**
             * Created by Brookman Holmes on 1/17/2016.
             */
            static class BcaTenBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<TenBallPlayer> {
                BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match) {
                    super(match);
                }

                BcaTenBallMatchInfoRecyclerAdapter(Match<TenBallPlayer> match, ViewType viewType) {
                    super(match, viewType);
                }

                @Override
                BaseViewHolder<TenBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
                    switch (viewType) {
                        case ITEM_RUN_OUTS:
                            return new RunOutsWithEarlyWinsHolder<>(view, detail);
                        default:
                            return super.getMatchInfoHolderByViewType(view, viewType);
                    }
                }
            }

            /**
             * Created by Brookman Holmes on 1/17/2016.
             */
            static class BcaNineBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<NineBallPlayer> {
                BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match) {
                    super(match);
                }

                BcaNineBallMatchInfoRecyclerAdapter(Match<NineBallPlayer> match, ViewType viewType) {
                    super(match, viewType);
                }

                @Override
                BaseViewHolder<NineBallPlayer> getMatchInfoHolderByViewType(View view, int viewType) {
                    switch (viewType) {
                        case ITEM_BREAKS:
                            return new BreaksWithWinsHolder<>(view, gameBall, detail);
                        case ITEM_RUN_OUTS:
                            return new RunOutsWithEarlyWinsHolder<>(view, detail);
                        default:
                            return super.getMatchInfoHolderByViewType(view, viewType);
                    }
                }
            }

            /**
             * Created by Brookman Holmes on 1/17/2016.
             */
            static class BcaEightBallMatchInfoRecyclerAdapter extends MatchInfoRecyclerAdapter<EightBallPlayer> {
                BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match) {
                    super(match);
                }

                BcaEightBallMatchInfoRecyclerAdapter(Match<EightBallPlayer> match, ViewType viewType) {
                    super(match, viewType);
                }
            }

            /**
             * Created by Brookman Holmes on 1/17/2016.
             */
            static class ApaMatchInfoRecyclerAdapter<T extends AbstractPlayer & IApa> extends MatchInfoRecyclerAdapter<T> {

                ApaMatchInfoRecyclerAdapter(Match<T> match) {
                    super(match);
                }

                ApaMatchInfoRecyclerAdapter(Match<T> match, ViewType viewType) {
                    super(match, viewType);
                }

                @Override public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
                    super.onBindViewHolder(holder, position);

                    if (holder instanceof ApaPlayer)
                        ((ApaPlayer) holder).setTvInningsOpponent(match.getGameStatus().innings);
                }

                @Override BaseViewHolder<T> getMatchInfoHolderByViewType(View view, int viewType) {
                    switch (viewType) {
                        case ITEM_APA_STATS:
                            return new ApaPlayer<>(view, detail);
                        case ITEM_BREAKS:
                            return new BreaksWithWinsHolder<>(view, gameBall, detail);
                        case ITEM_RUN_OUTS:
                            return new RunOutsWithEarlyWinsHolder<>(view, detail);
                        default:
                            return super.getMatchInfoHolderByViewType(view, viewType);
                    }
                }

                @Override int getLayoutResource(int viewType) {
                    if (viewTypeToggle == ViewType.CARDS) {
                        switch (viewType) {
                            case ITEM_APA_STATS:
                                return R.layout.card_apa_stats;
                            default:
                                return super.getLayoutResource(viewType);
                        }
                    } else {
                        switch (viewType) {
                            case ITEM_APA_STATS:
                                return R.layout.plain_apa_stats;
                            default:
                                return super.getLayoutResource(viewType);
                        }
                    }
                }

                @Override public int getItemCount() {
                    return super.getItemCount() + 1;
                }

                @Override public int getItemViewType(int position) {
                    switch (position) {
                        case 0:
                            return ITEM_APA_STATS;
                        case 1:
                            return ITEM_SHOOTING_PCT;
                        case 2:
                            return ITEM_SAFETIES;
                        case 3:
                            return ITEM_BREAKS;
                        case 4:
                            return ITEM_RUN_OUTS;
                        case 5:
                            return ITEM_MATCH_OVERVIEW;
                        case 6:
                            return ITEM_FOOTER;
                        default:
                            throw new IllegalArgumentException("Cannot have position: " + position);
                    }
                }
            }
        }
    }
}
