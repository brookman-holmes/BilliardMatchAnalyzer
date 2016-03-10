package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.AddTurnDialog;
import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model.TurnBuilder;
import com.brookmanholmes.billiards.game.Turn;
import com.brookmanholmes.billiards.match.Match;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchInfoActivity extends BaseActivity implements AddTurnDialog.AddTurnListener {
    private static final String TAG = "MatchInfoActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
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

        Log.i(TAG, match.getNotes());

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

    @OnClick(R.id.playerSide)
    public void showTestLayout() {
        String[] shotTypes = new String[] {"Shot type"};
        String[] angleList = new String[] {"Angle"};
        String[] howList = new String[] {"How?"};
        String[] whyList = new String[] {"Why?"};

        View view = getLayoutInflater().inflate(R.layout.content_shooting_stats, null);
        setupSpinner((Spinner)view.findViewById(R.id.spinner), Arrays.asList(shotTypes));
        setupSpinner((Spinner)view.findViewById(R.id.spinner2), Arrays.asList(angleList));
        setupSpinner((Spinner) view.findViewById(R.id.spinner3), Arrays.asList(howList));
        setupSpinner((Spinner) view.findViewById(R.id.spinner4), Arrays.asList(whyList));
        new AlertDialog.Builder(this, R.style.AlertDialogTheme).setView(view).setTitle("Shooting Errors").create().show();
    }

    private void setupSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private long getMatchId() {
        return getIntent().getExtras().getLong(ARG_MATCH_ID);
    }

    public void setBottomBarText() {
        addInning.setText("Add turn for " + infoFragment.getCurrentPlayersName());
    }

    @Override
    public void addTurn(TurnBuilder turnBuilder) {
        Turn turn = infoFragment.createAndAddTurnToMatch(
                turnBuilder.tableStatus,
                turnBuilder.turnEnd,
                turnBuilder.scratch,
                turnBuilder.lostGame);
        db.insertTurn(turn, getMatchId(), infoFragment.getTurnCount());

        setBottomBarText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_notes) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            builder.setTitle("Match Notes")
                    .setMessage(db.getMatch(getMatchId()).getNotes())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}
