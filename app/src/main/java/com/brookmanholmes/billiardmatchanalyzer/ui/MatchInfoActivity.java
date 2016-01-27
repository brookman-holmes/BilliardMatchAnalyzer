package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.match.Match;
import com.flipboard.bottomsheet.BottomSheetLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddInningFragment.AddInningCallback {
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
    boolean newGame = true;

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
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, infoFragment, "listview").commit();
        addInning.setText(addInning.getText().toString() + " Brookman");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        return true;
    }

    @OnClick(R.id.addInning)
    public void addInning(View view) {
        AddInningFragment.newInning(newGame).show(getSupportFragmentManager(), R.id.bottomsheet);
        newGame = !newGame;
    }


    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    @Override
    public void addInning() {
        bottomSheetLayout.dismissSheet();
    }
}
