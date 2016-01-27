package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.AddInningFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchHelperUtils;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.inning.TableStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.helpers.TurnEndHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Brookman Holmes on 1/23/2016.
 */
public class SelectTurnEndDialog extends Fragment {
    private static final String TAG = "SelectTurnEndDialog";
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.missTypes)
    RadioGroup radioGroup;

    TurnEndHelper turnEndHelper;
    GameStatus gameStatus;
    TableStatus tableStatus;

    public static SelectTurnEndDialog create(Bundle args) {
        SelectTurnEndDialog fragment = new SelectTurnEndDialog();
        fragment.setArguments(args);
        return fragment;
    }

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameStatus = MatchHelperUtils.createGameStatusFromBundle(getArguments());

        turnEndHelper = TurnEndHelper.newTurnEndHelper(gameStatus.gameType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_turn_end_dialog, container, false);
        ButterKnife.bind(this, view);
        title.setText("Select how " + getArguments().getString(MatchHelperUtils.PLAYER_NAME_KEY, "--") + "'s turn ended ");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                EventBus.getDefault().post(new TurnEndSelected(TurnEnd.MISS, tableStatus));
            }
        });
        return view;
    }

    public void onEvent(SelectBreakBallsDialog.BreakStatus newStatus) {
        Log.i(TAG, "SelectBreakBallsDialog.BreakStatus called in " + TAG);
    }

    public void onEvent(SelectBallsDialog.BallStatus newStatus) {
        Log.i(TAG, "SelectBallsDialog.BallStatus called in " + TAG);
    }

    public void onEvent(AddInningFragment.Update update) {
        Log.i(TAG, "SelectBallsDialog.Update called in " + TAG);
    }

    public class TurnEndSelected {
        public final TurnEnd turnEnd;
        public final TableStatus tableStatus;

        public TurnEndSelected(TurnEnd turnEnd, TableStatus tableStatus) {
            this.turnEnd = turnEnd;
            this.tableStatus = tableStatus;
        }
    }
}
