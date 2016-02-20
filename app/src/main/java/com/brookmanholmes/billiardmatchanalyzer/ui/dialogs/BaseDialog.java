package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.match.Match;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY;
import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.createBundleFromMatch;

/**
 * Created by Brookman Holmes on 2/17/2016.
 */
public class BaseDialog extends DialogFragment {
    String currentPlayersName, opposingPlayersName;
    GameType gameType;

    public static BaseDialog createBreakBallsDialog(Match<?> match) {
        Bundle args = createBundleFromMatch(match);

        BaseDialog fragment = createBreakDialog();

        fragment.setArguments(args);

        return fragment;
    }

    private static BaseDialog createBreakDialog() {
        return new BreakBallsDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPlayersName = getArguments().getString(CURRENT_PLAYER_NAME_KEY);
        opposingPlayersName = getArguments().getString(OPPOSING_PLAYER_NAME_KEY);
        gameType = GameType.valueOf(getArguments().getString(GAME_TYPE_KEY));
    }

    @Override
    public int getTheme() {
        return R.style.CustomDialogTransitionTheme;
    }
}
