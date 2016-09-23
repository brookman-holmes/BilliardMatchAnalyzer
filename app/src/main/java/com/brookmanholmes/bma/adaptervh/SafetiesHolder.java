package com.brookmanholmes.bma.adaptervh;

import android.view.View;
import android.widget.TextView;

import com.brookmanholmes.billiards.player.AbstractPlayer;
import com.brookmanholmes.bma.R;

import java.util.Locale;

/**
 * Created by helios on 4/12/2016.
 */
@SuppressWarnings("WeakerAccess")
public class SafetiesHolder extends MatchInfoHolder {
    TextView tvSafetiesAttemptedPlayer;
    TextView tvSafetiesAttemptedOpponent;
    TextView tvSafetyPctPlayer;
    TextView tvSafetyPctOpponent;
    TextView tvSafetyScratchesPlayer;
    TextView tvSafetyScratchesOpponent;
    TextView tvSafetyReturnsPlayer;
    TextView tvSafetyReturnsOpponent;
    TextView tvSafetyEscapesPlayer;
    TextView tvSafetyEscapesOpponent;
    TextView tvForcedErrorsPlayer;
    TextView tvForcedErrorsOpponent;

    public SafetiesHolder(View view) {
        super(view);
        title.setText(view.getContext().getString(R.string.title_safeties));
    }

    @Override public void bind(AbstractPlayer player, AbstractPlayer opponent) {
        // Safety Percentage
        tvSafetyPctPlayer.setText(player.getSafetyPct());
        tvSafetyPctOpponent.setText(opponent.getSafetyPct());

        // highlighting of the player who's doing better in this stat
        highlightBetterPlayerStats(tvSafetyPctPlayer, tvSafetyPctOpponent, Double.parseDouble(player.getSafetyPct()), Double.parseDouble(opponent.getSafetyPct()));

        // Safeties made / safeties attempted
        tvSafetiesAttemptedPlayer.setText(itemView.getContext().getString(R.string.out_of, player.getSafetySuccesses(), player.getSafetyAttempts()));
        tvSafetiesAttemptedOpponent.setText(itemView.getContext().getString(R.string.out_of, opponent.getSafetySuccesses(), opponent.getSafetyAttempts()));

        tvSafetyScratchesOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getSafetyFouls()));
        tvSafetyScratchesPlayer.setText(String.format(Locale.getDefault(), "%d", player.getSafetyFouls()));

        highlightPlayerStat(tvSafetyScratchesPlayer, tvSafetyScratchesOpponent, player.getSafetyFouls(), opponent.getSafetyFouls());

        tvSafetyReturnsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getSafetyReturns()));
        tvSafetyReturnsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getSafetyReturns()));

        highlightBetterPlayerStats(tvSafetyReturnsPlayer, tvSafetyReturnsOpponent, player.getSafetyReturns(), opponent.getSafetyReturns());

        tvSafetyEscapesPlayer.setText(String.format(Locale.getDefault(), "%d", player.getSafetyEscapes()));
        tvSafetyEscapesOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getSafetyEscapes()));
        highlightBetterPlayerStats(tvSafetyEscapesPlayer, tvSafetyEscapesOpponent, player.getSafetyEscapes(), opponent.getSafetyEscapes());

        tvForcedErrorsOpponent.setText(String.format(Locale.getDefault(), "%d", opponent.getSafetyForcedErrors()));
        tvForcedErrorsPlayer.setText(String.format(Locale.getDefault(), "%d", player.getSafetyForcedErrors()));
        highlightPlayerStat(tvForcedErrorsPlayer, tvForcedErrorsOpponent, player.getSafetyForcedErrors(), opponent.getSafetyForcedErrors());
    }

    @Override int getLayoutRes() {
        return R.layout.dialog_help_safeties;
    }
}
