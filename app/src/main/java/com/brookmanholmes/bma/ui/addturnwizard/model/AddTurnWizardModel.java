package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;

import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.AbstractWizardModel;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.PageList;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.STATS_LEVEL_KEY;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class AddTurnWizardModel extends AbstractWizardModel {
    private final Bundle matchData;
    private final TurnBuilder turnBuilder;
    private final String playerName;

    public AddTurnWizardModel(Context context, Bundle matchData) {
        super(context);
        this.matchData = matchData;

        playerName = matchData.getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY);

        turnBuilder = new TurnBuilder(GameType.valueOf(matchData.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)),
                matchData.getIntegerArrayList(MatchDialogHelperUtils.BALLS_ON_TABLE_KEY));

        turnBuilder.foul = false;
        turnBuilder.lostGame = false;
        turnBuilder.advStats.name(playerName);

        turnBuilder.advStats.use(currentPlayerTurnAndAdvancedStats());

        rootPageList = onNewRootPageList();
    }

    private boolean currentPlayerTurnAndAdvancedStats() {
        PlayerTurn turn = PlayerTurn.valueOf(matchData.getString(MatchDialogHelperUtils.TURN_KEY));
        Match.StatsDetail detail = Match.StatsDetail.valueOf(matchData.getString(STATS_LEVEL_KEY));
        if (turn == PlayerTurn.PLAYER && detail == Match.StatsDetail.ADVANCED_PLAYER)
            return true;
        else if (turn == PlayerTurn.OPPONENT && detail == Match.StatsDetail.ADVANCED_OPPONENT)
            return true;
        else return detail == Match.StatsDetail.ADVANCED;
    }

    @Override public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof UpdatesTurnInfo) {
            ((UpdatesTurnInfo) page).updateTurnInfo(this);
        }

        updatePagesWithTurnInfo();
    }

    @Override public void onPageTreeChanged() {
        super.onPageTreeChanged();

        updatePagesWithTurnInfo();
    }

    public void updatePagesWithTurnInfo() {
        for (Page page : getCurrentPageSequence()) {
            if (page instanceof RequiresUpdatedTurnInfo) {
                ((RequiresUpdatedTurnInfo) page).getNewTurnInfo(this);
            }
        }
    }

    @Override protected PageList onNewRootPageList() {
        if (matchData.getBoolean(MatchDialogHelperUtils.ALLOW_BREAK_AGAIN_KEY))
            return new PageList(getTurnEndPage());
        else if (BreakType.valueOf(matchData.getString(MatchDialogHelperUtils.BREAK_TYPE_KEY)).equals(BreakType.GHOST))
            return new PageList(getGhostBreakPage(), getShotPage(), getTurnEndPage());
        else if (matchData.getBoolean(MatchDialogHelperUtils.NEW_GAME_KEY))
            return new PageList(getBreakPage(), getTurnEndPage());
        else
            return new PageList(getShotPage(), getTurnEndPage());
    }

    TableStatus getTableStatus() {
        return turnBuilder.tableStatus;
    }

    void setTurnEnd(String turnEnd, String foul) {
        turnBuilder.turnEnd = convertStringToTurnEnd(turnEnd);

        if (getFoulPossible(turnBuilder.turnEnd)) {
            turnBuilder.foul = (context.getString(R.string.yes).equals(foul) || context.getString(R.string.foul_lost_game).equals(foul));
            turnBuilder.lostGame = context.getString(R.string.foul_lost_game).equals(foul);
        }
    }

    private boolean getFoulPossible(TurnEnd turn) {
        return turn == TurnEnd.MISS
                || turn == TurnEnd.SAFETY_ERROR
                || turn == TurnEnd.BREAK_MISS
                || turn == TurnEnd.ILLEGAL_BREAK;
    }

    AdvStats.Builder getAdvStats() {
        return turnBuilder.advStats;
    }

    private TurnEnd convertStringToTurnEnd(String turnEnd) {
        if (turnEnd.equals(context.getString(R.string.turn_safety)))
            return TurnEnd.SAFETY;
        else if (turnEnd.equals(context.getString(R.string.turn_safety_error)))
            return TurnEnd.SAFETY_ERROR;
        else if (turnEnd.equals(context.getString(R.string.turn_break_miss)))
            return TurnEnd.BREAK_MISS;
        else if (turnEnd.equals(context.getString(R.string.turn_miss)))
            return TurnEnd.MISS;
        else if (turnEnd.equals(context.getString(R.string.turn_push)))
            return TurnEnd.PUSH_SHOT;
        else if (turnEnd.equals(context.getString(R.string.turn_skip)))
            return TurnEnd.SKIP_TURN;
        else if (turnEnd.equals(context.getString(R.string.turn_illegal_break)))
            return TurnEnd.ILLEGAL_BREAK;
        else if (turnEnd.equals(context.getString(R.string.turn_won_game)))
            return TurnEnd.GAME_WON;
        else if (turnEnd.equals(context.getString(R.string.turn_current_player_breaks, matchData.getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY))))
            return TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN;
        else if (turnEnd.equals(context.getString(R.string.turn_non_current_player_breaks, matchData.getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY))))
            return TurnEnd.OPPONENT_BREAKS_AGAIN;
        else if (turnEnd.equals(context.getString(R.string.turn_continue_game)))
            return TurnEnd.CONTINUE_WITH_GAME;
        else
            throw new IllegalArgumentException("No such conversion between string and StringRes: " + turnEnd);
    }

    private Page getGhostBreakPage() {
        return new GhostBreakPage(this, context.getString(R.string.title_break, playerName), context.getString(R.string.title_shot, playerName), matchData);
    }

    private Page getBreakPage() {
        return new BreakPage(this, context.getString(R.string.title_break, playerName), context.getString(R.string.title_shot, playerName), matchData);
    }

    private Page getShotPage() {
        return new ShotPage(this, context.getString(R.string.title_shot, playerName), matchData);
    }

    private Page getTurnEndPage() {
        if (currentPlayerTurnAndAdvancedStats()) {
            return getAdvTurnEndPage();
        } else
            return new TurnEndPage(this, context.getString(R.string.title_turn_end, playerName), matchData)
                    .addBranch(context.getString(R.string.turn_safety_error))
                    .addBranch(context.getString(R.string.turn_miss))
                    .addBranch(context.getString(R.string.turn_break_miss))
                    .addBranch(context.getString(R.string.turn_illegal_break))
                    .addBranch(context.getString(R.string.turn_safety))
                    .addBranch(context.getString(R.string.turn_won_game))
                    .addBranch(context.getString(R.string.turn_push))
                    .addBranch(context.getString(R.string.turn_skip))
                    .addBranch(context.getString(R.string.turn_current_player_breaks, matchData.getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)))
                    .addBranch(context.getString(R.string.turn_non_current_player_breaks, matchData.getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY)))
                    .setValue(context.getString(R.string.turn_miss));
    }

    private Page getAdvTurnEndPage() {
        return new TurnEndPage(this, context.getString(R.string.title_turn_end, playerName), matchData)
                .addBranch(context.getString(R.string.turn_safety_error), getSafetyErrorBranch())
                .addBranch(context.getString(R.string.turn_miss), getMissBranchPage(), getWhyMissPage())
                .addBranch(context.getString(R.string.turn_break_miss), getBreakErrorHow(), getBreakErrorWhy())
                .addBranch(context.getString(R.string.turn_illegal_break), getIllegalBreakHow(), getIllegalBreakWhy())
                .addBranch(context.getString(R.string.turn_safety), getSafetyPage())
                .addBranch(context.getString(R.string.turn_won_game))
                .addBranch(context.getString(R.string.turn_push))
                .addBranch(context.getString(R.string.turn_skip))
                .addBranch(context.getString(R.string.turn_current_player_breaks, matchData.getString(MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY)))
                .addBranch(context.getString(R.string.turn_non_current_player_breaks, matchData.getString(MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY)))
                .setValue(context.getString(R.string.turn_miss));
    }

    private Page getSafetyErrorBranch() {
        return new SafetyErrorPage(this, context.getString(R.string.title_how_miss, playerName))
                .setChoices(context.getResources().getStringArray(R.array.how_choices_safety))
                .setRequired(true);
    }

    private Page getMissBranchPage() {
        return new MissBranchPage(this, context.getString(R.string.title_miss, playerName))
                .addBranch(context.getString(R.string.miss_cut),
                        getCutTypePage(),
                        getAnglePage(),
                        getHowMissPage(R.array.how_choices))

                .addBranch(context.getString(R.string.miss_long),
                        getHowMissPage(R.array.how_choices))

                .addBranch(context.getString(R.string.miss_bank),
                        getBankPage(),
                        getHowMissPage(R.array.how_choices_bank))

                .addBranch(context.getString(R.string.miss_kick),
                        getKickPage(),
                        getHowMissPage(R.array.how_choices_kick))

                .addBranch(context.getString(R.string.miss_combo),
                        getHowMissPage(R.array.how_choices))

                .addBranch(context.getString(R.string.miss_carom),
                        getHowMissPage(R.array.how_choices))

                .addBranch(context.getString(R.string.miss_jump),
                        getHowMissPage(R.array.how_choices))

                .addBranch(context.getString(R.string.miss_masse),
                        getHowMissPage(R.array.how_choices_masse))

                .setValue(context.getString(R.string.miss_cut))
                .setParentKey("Miss");
    }

    private Page getWhyMissPage() {
        return new WhyMissPage(this, context.getString(R.string.title_why_miss, playerName))
                .setChoices(context.getResources().getStringArray(R.array.why_choices));
    }

    private Page getCutTypePage() {
        return new CutTypePage(this, context.getString(R.string.title_cut_type, playerName))
                .setChoices(context.getResources().getStringArray(R.array.cut_types))
                .setValue(context.getResources().getString(R.string.cut_rail))
                .setRequired(true);
    }

    private Page getAnglePage() {
        return new AngleTypePage(this, context.getString(R.string.title_angle, playerName))
                .setChoices(context.getResources().getStringArray(R.array.angles))
                .setValue(context.getResources().getStringArray(R.array.angles)[0])
                .setRequired(true);
    }

    private Page getBankPage() {
        return new BankPage(this, context.getString(R.string.title_bank, playerName))
                .setChoices(context.getResources().getStringArray(R.array.banks));
    }

    private Page getKickPage() {
        return new KickPage(this, context.getString(R.string.title_kick_type, playerName))
                .setChoices(context.getResources().getStringArray(R.array.kicks))
                .setValue(context.getString(R.string.one_rail))
                .setRequired(true);
    }

    private Page getBreakErrorHow() {
        return new BreakErrorPage(this, context.getString(R.string.title_how_miss, playerName))
                .setChoices(context.getResources().getStringArray(R.array.how_choices_break));
    }

    private Page getBreakErrorWhy() {
        return new WhyMissPage(this, context.getString(R.string.title_why_miss, playerName))
                .setChoices(context.getResources().getStringArray(R.array.why_choices_break));
    }

    private Page getIllegalBreakHow() {
        return new BreakErrorPage(this, context.getString(R.string.title_how_miss, playerName))
                .setChoices(context.getResources().getStringArray(R.array.how_choices_break));
    }

    private Page getIllegalBreakWhy() {
        return new WhyMissPage(this, context.getString(R.string.title_why_miss, playerName))
                .setChoices(context.getResources().getStringArray(R.array.why_choices_illegal_break));
    }

    private Page getSafetyPage() {
        return new SafetyPage(this, context.getString(R.string.title_safety, playerName))
                .setChoices(context.getResources().getStringArray(R.array.safety_types))
                .setValue(context.getString(R.string.safety_full_hook))
                .setRequired(true);
    }

    private Page getHowMissPage(@ArrayRes int choices) {
        return new HowMissPage(this, context.getString(R.string.title_how_miss, playerName))
                .setChoices(context.getResources().getStringArray(choices));
    }

    public TurnBuilder getTurnBuilder() {
        // make sure data is current
        for (Page page : getCurrentPageSequence())
            if (page instanceof UpdatesTurnInfo)
                ((UpdatesTurnInfo) page).updateTurnInfo(this);

        turnBuilder.advStats.startingPosition(matchData.getBoolean(MatchDialogHelperUtils.SUCCESSFUL_SAFE_KEY) ? "Safe" : "Open");
        return turnBuilder;
    }
}
