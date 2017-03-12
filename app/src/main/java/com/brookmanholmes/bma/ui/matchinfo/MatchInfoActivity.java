package com.brookmanholmes.bma.ui.matchinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.dialog.GameStatusViewBuilder;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.utils.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MatchInfoActivity extends AbstractMatchActivity {
    private static final String TAG = "MatchInfoActivity";
    private final List<MatchInfoListener> listeners = new ArrayList<>();

    @Bind(R.id.playerName)
    TextView playerName;
    @Bind(R.id.opponentName)
    TextView opponentName;
    @Bind(R.id.playerTurnIndicator)
    ViewGroup playerTurnIndicator;
    @Bind(R.id.opponentTurnIndicator)
    ViewGroup opponentTurnIndicator;

    @Nullable
    @Bind(R.id.playerTurnIndicatorSec)
    ViewGroup playerTurnIndicatorSec;
    @Nullable
    @Bind(R.id.opponentTurnIndicatorSec)
    ViewGroup opponentTurnIndicatorSec;

    @Nullable
    @Bind(R.id.playerNameSecondary)
    TextView playerNameSec;
    @Nullable
    @Bind(R.id.opponentNameSecondary)
    TextView opponentNameSec;
    @Bind(R.id.pager)
    CustomViewPager pager;

    private Snackbar matchOverSnackbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager.setPagingEnabled(false);
        playerName.setText(match.getPlayer().getName());
        opponentName.setText(match.getOpponent().getName());
        if (playerNameSec != null && opponentNameSec != null) {
            playerNameSec.setText(match.getPlayer().getName());
            opponentNameSec.setText(match.getOpponent().getName());
        }
        matchOverSnackbar = makeSnackbar(R.string.match_over, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        matchOverSnackbar.dismiss();
                    }
                });

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("show_match_over_snackbar", false))
                matchOverSnackbar.show();
        }

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), match.getGameStatus().gameType);
        pager.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("show_match_over_snackbar", matchOverSnackbar.isShown());
    }

    @Override
    public void updatePlayerName(String name, String newName) {
        if (opponentName.getText().toString().equals(name)) {
            opponentName.setText(newName);
            match.setOpponentId(newName);
        } else if (playerName.getText().toString().equals(name)) {
            playerName.setText(newName);
            match.setPlayerId(newName);
        }
    }

    @Override
    protected void updateViews() {
        if (menu != null)
            updateMenuItems();

        if (match.isMatchOver()) {
            setNameEnabled(true, true);
        } else if (match.getGameStatus().turn == PlayerTurn.PLAYER) {
            setNameEnabled(true, false);
        } else {
            setNameEnabled(false, true);
        }

        updateFragments();

        if (match.isMatchOver()) {
            fabAddTurn.hide();
            if (!matchOverSnackbar.isShown())
                matchOverSnackbar.show();
        } else {
            fabAddTurn.show();
            if (matchOverSnackbar.isShown())
                matchOverSnackbar.dismiss();
        }
    }

    private void setNameEnabled(boolean player, boolean opponent) {
        setPlayerTurnIndicator(playerTurnIndicator, player);
        setPlayerTurnIndicator(opponentTurnIndicator, opponent);

        if (opponentTurnIndicatorSec != null)
            setPlayerTurnIndicator(opponentTurnIndicatorSec, opponent);
        if (playerTurnIndicatorSec != null)
            setPlayerTurnIndicator(playerTurnIndicatorSec, player);

        isPlayerTurn(playerName, player);
        isPlayerTurn(opponentName, opponent);
        if (opponentNameSec != null)
            isPlayerTurn(opponentNameSec, opponent);
        if (playerNameSec != null)
            isPlayerTurn(playerNameSec, player);

    }

    private void setPlayerTurnIndicator(ViewGroup turnIndicator, boolean enabled) {
        if (enabled)
            turnIndicator.setBackgroundColor(getColor2(R.color.colorPrimary));
        else turnIndicator.setBackgroundColor(getColor2(R.color.colorPrimary));
    }

    private void isPlayerTurn(TextView textView, boolean enabled) {
        if (enabled)
            textView.setTextColor(getColor2(R.color.white));
        else
            textView.setTextColor(getColor(R.color.white, 159));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

    void registerFragment(MatchInfoListener info) {
        listeners.add(info);
        info.update(match);
    }

    void removeFragment(MatchInfoListener info) {
        listeners.remove(info);
    }

    private void updateFragments() {
        for (MatchInfoListener listener : listeners) {
            listener.update(match);
        }
    }

    @Override
    protected void setToolbarTitle() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_match_info, ConversionUtils.getGameTypeString(this, match.getGameStatus().gameType)));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_match_info;
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final GameType gameType;

        PagerAdapter(FragmentManager fm, GameType gameType) {
            super(fm);
            this.gameType = gameType;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MatchInfoFragment.newInstance(gameType);
                default:
                    return new TurnListFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
