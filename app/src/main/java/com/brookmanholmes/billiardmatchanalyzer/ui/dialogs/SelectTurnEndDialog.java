package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.ui.AddInningFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiards.game.GameStatus;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;
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
    RadioGroup turnEndSelectionRG;
    @Bind(R.id.missRB)
    RadioButton missRB;
    @Bind(R.id.safetyMissRB)
    RadioButton safetyMissRB;
    @Bind(R.id.wonGameRB)
    RadioButton wonGameRB;
    @Bind(R.id.lostGameRB)
    RadioButton lostGameRB;
    @Bind(R.id.pushShotRB)
    RadioButton pushShotRB;
    @Bind(R.id.skipTurnRB)
    RadioButton skipTurnRB;
    @Bind(R.id.safetyRB)
    RadioButton successfulSafetyRB;
    @Bind(R.id.breakMissRB)
    RadioButton breakMissRB;
    @Bind(R.id.illegalBreakRB)
    RadioButton illegalBreakRB;
    @Bind(R.id.continueGameRB)
    RadioButton continueGameRB;
    @Bind(R.id.scratchSwitch)
    Switch scratchSwitch;


    TurnEndHelper turnEndHelper;
    GameStatus gameStatus;

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
        gameStatus = MatchDialogHelperUtils.createGameStatusFromBundle(getArguments());

        turnEndHelper = TurnEndHelper.newTurnEndHelper(gameStatus.gameType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_turn_end_dialog, container, false);
        ButterKnife.bind(this, view);
        title.setText("Select how " + getArguments().getString(MatchDialogHelperUtils.PLAYER_NAME_KEY, "--") + "'s turn ended ");

        turnEndSelectionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                EventBus.getDefault().post(new TurnEndSelected(getTurnEndFromCheckedItem(checkedId), scratchSwitch.isChecked()));
            }
        });
        return view;
    }

    public void onEvent(AddInningFragment.Update update) {
        TurnEndOptions options = turnEndHelper.create(gameStatus, update.tableStatus);
        setVisibilities(options);
        scratchSwitch.setChecked(options.scratch);
        setCheckedItem(options.defaultCheck);
        Log.i(TAG, options.toString());
    }


    private void setVisibilities(TurnEndOptions options) {
        setVisibilityOfItem(missRB, options.miss);
        setVisibilityOfItem(lostGameRB, options.lostGame);
        setVisibilityOfItem(wonGameRB, options.wonGame);
        setVisibilityOfItem(breakMissRB, options.breakMiss);
        setVisibilityOfItem(pushShotRB, options.push);
        setVisibilityOfItem(skipTurnRB, options.skip);
        setVisibilityOfItem(safetyMissRB, options.safetyMiss);
        setVisibilityOfItem(successfulSafetyRB, options.safety);
        setVisibilityOfItem(illegalBreakRB, options.illegalBreak);
        setVisibilityOfItem(continueGameRB, options.continueGame);
    }

    private void setVisibilityOfItem(View view, boolean visibility) {
        if (visibility)
            view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    private void setCheckedItem(TurnEnd turnEnd) {
        switch (turnEnd) {
            case GAME_WON:
                wonGameRB.setChecked(true);
                break;
            case GAME_LOST:
                lostGameRB.setChecked(true);
                break;
            case BREAK_MISS:
                breakMissRB.setChecked(true);
                break;
            default:
                missRB.setChecked(true);
        }
    }

    // // TODO: 1/29/2016 add in re-break options and maybe some other options?
    private TurnEnd getTurnEndFromCheckedItem(int checkedId) {
        switch (checkedId) {
            case R.id.missRB:
                return TurnEnd.MISS;
            case R.id.safetyMissRB:
                return TurnEnd.SAFETY_ERROR;
            case R.id.wonGameRB:
                return TurnEnd.GAME_WON;
            case R.id.lostGameRB:
                return TurnEnd.GAME_LOST;
            case R.id.pushShotRB:
                return TurnEnd.PUSH_SHOT;
            case R.id.skipTurnRB:
                return TurnEnd.SKIP_TURN;
            case R.id.safetyRB:
                return TurnEnd.SAFETY;
            case R.id.breakMissRB:
                return TurnEnd.BREAK_MISS;
            case R.id.illegalBreakRB:
                return TurnEnd.ILLEGAL_BREAK;
            case R.id.continueGameRB:
                return TurnEnd.CONTINUE_WITH_GAME;
            default:
                throw new IllegalArgumentException("That checked item does not have a corresponding turn ending");

        }
    }

    public class TurnEndSelected {
        public final TurnEnd turnEnd;
        public final boolean scratch;

        public TurnEndSelected(TurnEnd turnEnd, boolean scratch) {
            this.turnEnd = turnEnd;
            this.scratch = scratch;
        }
    }
}
