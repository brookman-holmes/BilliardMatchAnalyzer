package com.brookmanholmes.bma.ui.matchinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.ui.dialog.GameStatusViewBuilder;
import com.brookmanholmes.bma.ui.profile.PlayerProfileActivity;
import com.brookmanholmes.bma.utils.ConversionUtils;
import com.brookmanholmes.bma.utils.CustomViewPager;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MatchInfoActivity extends AbstractMatchActivity {
    private static final String TAG = "MatchInfoActivity";
    private final List<UpdateMatchInfo> listeners = new ArrayList<>();

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
    @Bind(R.id.ballContainer)
    ViewGroup ballContainer;
    @Bind(R.id.playerWinPct)
    ViewGroup playerWinPct;
    @Bind(R.id.playerSpacing)
    ViewGroup playerSpacing;
    @Bind(R.id.opponentSpacing)
    ViewGroup opponentSpacing;
    @Bind(R.id.opponentWinPct)
    ViewGroup opponentWinPct;
    @Bind(R.id.winPctLayout)
    ViewGroup winPctLayout;

    private Snackbar matchOverSnackbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pager.setPagingEnabled(false);
        playerName.setText(match.getPlayer().getName());
        opponentName.setText(match.getOpponent().getName());
        // no reason to click on The Ghost
        opponentNameLayout.setEnabled(!match.getGameStatus().gameType.isGhostGame());

        matchOverSnackbar = makeSnackbar(R.string.match_over, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        matchOverSnackbar.dismiss();
                    }
                });

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), getMatchId());
        pager.setAdapter(adapter);
        setWinPctColors();
    }

    private void setWinPctColors() { // works around api 21 tinting not working?
        playerWinPct.getBackground().setColorFilter(getColor2(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);
        opponentWinPct.getBackground().setColorFilter(getColor2(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);
        ((LinearLayout) playerWinPct.getParent()).getBackground().setColorFilter(getColor2(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        ((LinearLayout) opponentWinPct.getParent()).getBackground().setColorFilter(getColor2(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
    }

    private void hideBalls() {
        if (match.getGameStatus().gameType != GameType.STRAIGHT_POOL) {
            for (int i = 0; i < ballContainer.getChildCount(); i++) {
                ballContainer.getChildAt(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setBallsOnTable() {
        if (match.getGameStatus().gameType != GameType.STRAIGHT_POOL) {
            findViewById(R.id.ballContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.ballsRemaining).setVisibility(View.GONE);
            for (int i = 0; i < match.getGameStatus().ballsOnTable.size(); i++) {
                int ball = match.getGameStatus().ballsOnTable.get(i) - 1;
                ballContainer.getChildAt(ball).setVisibility(View.VISIBLE);
            }
        } else {
            findViewById(R.id.ballContainer).setVisibility(View.GONE);
            findViewById(R.id.ballsRemaining).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.ballsRemaining)).setText(getString(R.string.balls_remaining, getBallsRemaining()));
        }
    }

    private int getBallsRemaining() {
        return 15 - ((match.getPlayer().getShootingBallsMade() + match.getOpponent().getShootingBallsMade()) % 14);
    }

    @OnClick({R.id.opponentNameLayout, R.id.playerNameLayout})
    public void showPlayerOptionsMenu(LinearLayout view) {
        String name = ((TextView) view.getChildAt(0)).getText().toString();
        PlayerTurn turn = match.getPlayer().getName().equals(name) ? PlayerTurn.PLAYER : PlayerTurn.OPPONENT;
        showChoiceDialog(name, turn, view);
    }

    @Override
    public void updatePlayerName(String name, String newName) {
        if (opponentName.getText().toString().equals(name)) {
            opponentName.setText(newName);
            match.setOpponentName(newName);
        } else if (playerName.getText().toString().equals(name)) {
            playerName.setText(newName);
            match.setPlayerName(newName);
        }
    }

    @Override
    protected void updateViews() {
        if (menu != null)
            updateMenuItems();

        if (match.isMatchOver()) {
            playerName.setAlpha(1);
            opponentName.setAlpha(1);
        } else if (match.getGameStatus().turn == PlayerTurn.PLAYER) {
            playerName.setAlpha(1);
            opponentName.setAlpha(.7f);
        } else {
            playerName.setAlpha(.7f);
            opponentName.setAlpha(1);
        }

        updateFragments();
        hideBalls();
        setBallsOnTable();
        setWinCompPct();

        if (match.isMatchOver()) {
            fabAddTurn.hide();
            matchOverSnackbar.show();
        } else {
            matchOverSnackbar.dismiss();
            fabAddTurn.show();
        }
    }

    private void setWinCompPct() {
        float scaleValue = 10;
        Transition transition = new AutoTransition();
        transition.setStartDelay(1000);
        transition.addTarget(playerWinPct)
                .addTarget(playerSpacing)
                .addTarget(opponentWinPct)
                .addTarget(opponentSpacing);

        TransitionManager.beginDelayedTransition(winPctLayout, transition);
        float playerPct = round(match.getPlayer().getMatchCompletionPct(), 2) * scaleValue;
        float opponentPct = round(match.getOpponent().getMatchCompletionPct(), 2) * scaleValue;
        setWeight(playerWinPct, playerPct);
        setWeight(playerSpacing, scaleValue - playerPct);
        setWeight(opponentWinPct, opponentPct);
        setWeight(opponentSpacing, scaleValue - opponentPct);
    }

    private void setWeight(View view, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
        params.weight = weight;
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
    }

    private float round(float d, int places) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
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

    @Override
    protected void setToolbarTitle() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_match_info, ConversionUtils.getGameTypeString(this, match.getGameStatus().gameType)));
    }

    @Override
    protected String getMimeType() {
        return "application/com.brookmanholmes.bma.matchmodel";
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_match_info;
    }

    interface UpdateMatchInfo {
        void update(Match match);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final long matchId;

        PagerAdapter(FragmentManager fm, long matchId) {
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
