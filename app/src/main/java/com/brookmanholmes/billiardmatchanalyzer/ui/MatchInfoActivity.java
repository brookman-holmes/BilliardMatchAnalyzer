package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.match.Match;
import com.flipboard.bottomsheet.BottomSheetLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MatchInfoActivity extends BaseActivity {
    private static final String TAG = "MatchInfoActivity";
    @Bind(R.id.bottomsheet)
    BottomSheetLayout bottomSheetLayout;
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

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
        setBottomBarText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (infoFragment == null)
            infoFragment = (MatchInfoFragment) getSupportFragmentManager().findFragmentByTag("infoFragment");
    }

    @OnClick(R.id.addInning)
    public void addInning(View view) {
        AddInningFragment.newInning(db.getMatch(getMatchId())).show(getSupportFragmentManager(), R.id.bottomsheet);
    }


    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    public void onEvent(AddInningFragment.AddTurnToMatchInfo turnEndSelected) {
        Turn turn = infoFragment.createAndAddTurnToMatch(turnEndSelected.tableStatus, turnEndSelected.turnEnd, turnEndSelected.scratch);
        db.insertInning(turn, getMatchId(), db.getMatch(getMatchId()).getTurnCount());
        bottomSheetLayout.dismissSheet();
        setBottomBarText();
    }

    public void setBottomBarText() {
        addInning.setText("Add turn for " + infoFragment.getCurrentPlayersName());
    }
}
