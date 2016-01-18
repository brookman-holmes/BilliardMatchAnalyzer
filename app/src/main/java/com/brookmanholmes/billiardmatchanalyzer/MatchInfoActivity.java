package com.brookmanholmes.billiardmatchanalyzer;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiards.match.Match;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements MatchInfoFragment.OnListFragmentInteractionListener {
    private static final String TAG = "MatchInfoActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.addInning)
    FloatingActionButton fab;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;

    DatabaseAdapter db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        db = new DatabaseAdapter(this);
        db.open();

        Log.i("MatchInfoActivity", "Match id: " + getMatchId());
        Match<?> match = db.getMatch(getMatchId());
        playerName.setText(match.getPlayer().getName());
        opponentName.setText(match.getOpponent().getName());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, MatchInfoFragment.createMatchInfoFragment(getMatchId())).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        return true;
    }


    @OnClick(R.id.addInning)
    public void addInningToMatch() {
        Snackbar.make(coordinatorLayout, "Add inning", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onListFragmentInteraction() {
        Log.i(TAG, "Touched item in recycler view");
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }
}
