package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.match.Match;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener {
    private static final String TAG = "MatchInfoActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.addInning)
    Button addInning;

    DatabaseAdapter db;
    MatchInfoFragment infoFragment;

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

        infoFragment = MatchInfoFragment.createMatchInfoFragment(getMatchId());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, infoFragment, "infoFragment").commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (infoFragment == null)
            infoFragment = (MatchInfoFragment) getSupportFragmentManager().findFragmentByTag("infoFragment");
        setBottomBarText();
    }

    @OnClick(R.id.addInning)
    public void addInning(View view) {
        DialogFragment dialogFragment = AddTurnDialog.create(db.getMatch(getMatchId()));

        dialogFragment.show(getSupportFragmentManager(), "AddTurnDialog");
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    public void setBottomBarText() {
        addInning.setText("Add turn for " + infoFragment.getCurrentPlayersName());
    }

    @Override
    public void addTurn(TurnBuilder turnBuilder) {
        Turn turn = infoFragment.createAndAddTurnToMatch(turnBuilder.tableStatus, turnBuilder.turnEnd, turnBuilder.scratch);
        db.insertTurn(turn, getMatchId(), infoFragment.getTurnCount());

        setBottomBarText();
    }
}
