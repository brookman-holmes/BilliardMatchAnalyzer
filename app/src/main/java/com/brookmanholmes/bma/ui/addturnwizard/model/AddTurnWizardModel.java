package com.brookmanholmes.bma.ui.addturnwizard.model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;

import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.billiards.turn.AdvStats;
import com.brookmanholmes.billiards.turn.TableStatus;
import com.brookmanholmes.billiards.turn.TurnEnd;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.utils.MatchDialogHelperUtils;
import com.brookmanholmes.bma.wizard.model.AbstractWizardModel;
import com.brookmanholmes.bma.wizard.model.BranchPage;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.PageList;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.ALLOW_BREAK_AGAIN_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.BALLS_ON_TABLE_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.CURRENT_PLAYER_NAME_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.GAME_TYPE_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.NEW_GAME_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.OPPOSING_PLAYER_NAME_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.SUCCESSFUL_SAFE_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.TURN_KEY;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertStringToAngle;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertStringToHowType;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertStringToShotType;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertStringToSubType;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertStringToTurnEnd;
import static com.brookmanholmes.bma.utils.MatchDialogHelperUtils.convertStringToWhyType;


/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class AddTurnWizardModel extends AbstractWizardModel {
    private static final String TAG = "AddTurnWizardModel";
    private static final Page[] emptyPageArray = new Page[]{};
    private final Bundle matchData;
    private final TurnBuilder turnBuilder;
    private final String playerName;
    private final PlayerTurn turn;
    private final EnumSet<Match.StatsDetail> dataCollection;

    public AddTurnWizardModel(Context context, Bundle matchData) {
        super(context);
        this.matchData = matchData;
        turn = PlayerTurn.valueOf(matchData.getString(TURN_KEY));
        playerName = matchData.getString(CURRENT_PLAYER_NAME_KEY);
        dataCollection = EnumSet.copyOf((EnumSet<Match.StatsDetail>) matchData.getSerializable(MatchDialogHelperUtils.DATA_COLLECTION_KEY));
        turnBuilder = new TurnBuilder(GameType.valueOf(matchData.getString(GAME_TYPE_KEY)),
                matchData.getIntegerArrayList(BALLS_ON_TABLE_KEY));

        turnBuilder.turnEnd = TurnEnd.MISS;
        turnBuilder.foul = false;
        turnBuilder.seriousFoul = false;
        turnBuilder.advStats.name(playerName);
        turnBuilder.advStats.shotType(AdvStats.ShotType.NONE);

        turnBuilder.advStats.use(MatchDialogHelperUtils.currentPlayerTurnAndAdvancedStats(turn, dataCollection));

        rootPageList = onNewRootPageList();
    }

    @Override
    public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof UpdatesTurnInfo) {
            ((UpdatesTurnInfo) page).updateTurnInfo(this);
        }

        updatePagesWithTurnInfo();
    }

    @Override
    public void onPageTreeChanged() {
        super.onPageTreeChanged();

        updatePagesWithTurnInfo();
    }

    public void updatePagesWithTurnInfo() {
        for (int i = 0; i < getCurrentPageSequence().size(); i++) {
            if (getCurrentPageSequence().get(i) instanceof RequiresUpdatedTurnInfo) {
                ((RequiresUpdatedTurnInfo) getCurrentPageSequence().get(i)).getNewTurnInfo(this);
            }
        }
    }

    @Override
    protected PageList onNewRootPageList() {
        GameType gameType = GameType.valueOf(matchData.getString(GAME_TYPE_KEY));
        if (gameType == GameType.STRAIGHT_POOL) // straight pool pages must be higher than the rebreak option because they handle re-break option differently
            return new PageList(getStraightPoolPage());
        else if (gameType == GameType.STRAIGHT_GHOST)
            return new PageList(getStraightPoolPage());
        else if (matchData.getBoolean(ALLOW_BREAK_AGAIN_KEY))
            return new PageList(getTurnEndPage());
        else if (gameType.isGhostGame())
            return new PageList(getGhostBreakPage(), getTurnEndPage());
        else if (matchData.getBoolean(NEW_GAME_KEY))
            return new PageList(getBreakPage(), getTurnEndPage());
        else
            return new PageList(getShotPage(), getTurnEndPage());
    }

    TableStatus getTableStatus() {
        return turnBuilder.tableStatus;
    }

    void setTurnEnd(String turnEnd, String foul) {
        turnBuilder.turnEnd = convertStringToTurnEnd(context,
                turnEnd,
                matchData.getString(CURRENT_PLAYER_NAME_KEY),
                matchData.getString(OPPOSING_PLAYER_NAME_KEY));

        if (getFoulPossible(turnBuilder.turnEnd)) {
            turnBuilder.seriousFoul = context.getString(R.string.foul_lost_game).equals(foul) || context.getString(R.string.foul_serious).equals(foul);
            turnBuilder.foul = context.getString(R.string.yes).equals(foul) || turnBuilder.seriousFoul;
        }
    }

    void setAngles(List<String> angles) {
        getAdvStats().clearAngle();
        List<AdvStats.Angle> list = new ArrayList<>();
        for (String angle : angles) {
            list.add(convertStringToAngle(context, angle));
        }

        getAdvStats().angle(list);
    }

    void setAngles(String angle) {
        getAdvStats().angle(convertStringToAngle(context, angle));
    }

    void setShotType(AdvStats.ShotType shotType) {
        getAdvStats().shotType(shotType);
        getAdvStats().clearAngle();
        getAdvStats().clearSubType();
        getAdvStats().clearHowTypes();
        getAdvStats().clearWhyTypes();
    }

    void setSubType(String subType) {
        getAdvStats().subType(convertStringToSubType(context, subType));
    }

    void setWhys(List<String> whys) {
        getAdvStats().clearWhyTypes();

        if (whys != null) {
            List<AdvStats.WhyType> list = new ArrayList<>();
            for (String why : whys) {
                list.add(convertStringToWhyType(context, why));
            }

            getAdvStats().whyTypes(list);
        }
    }

    void setHows(List<String> hows) {
        getAdvStats().clearHowTypes();

        if (hows != null) {
            List<AdvStats.HowType> list = new ArrayList<>();
            for (String how : hows) {
                list.add(convertStringToHowType(context, how));
            }

            getAdvStats().howTypes(list);
        }
    }

    private boolean getFoulPossible(TurnEnd turn) {
        return turn == TurnEnd.MISS
                || turn == TurnEnd.SAFETY_ERROR
                || turn == TurnEnd.BREAK_MISS
                || turn == TurnEnd.ILLEGAL_BREAK;
    }

    private AdvStats.Builder getAdvStats() {
        return turnBuilder.advStats;
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

    private Page getStraightPoolPage() {
        return new StraightPoolPage(this, context.getString(R.string.title_shot_straight, playerName), matchData)
                .addBranch(context.getString(R.string.turn_safety_error), getSafetyErrorBranch())
                .addBranch(context.getString(R.string.turn_miss), combineArrays(getHowMissPageAll(), getMissBranchPage(), getCueingPage()))
                .addBranch(context.getString(R.string.turn_safety), getSafetyPage())
                .addBranch(context.getString(R.string.turn_current_player_breaks, matchData.getString(CURRENT_PLAYER_NAME_KEY)))
                .addBranch(context.getString(R.string.turn_non_current_player_breaks, matchData.getString(OPPOSING_PLAYER_NAME_KEY)))
                .setValue(context.getString(R.string.turn_miss));
    }

    private Page getTurnEndPage() {
        return new TurnEndPage(this, context.getString(R.string.title_turn_end, playerName), matchData)
                .addBranch(context.getString(R.string.turn_safety_error), getSafetyErrorBranch())
                .addBranch(context.getString(R.string.turn_miss), combineArrays(getHowMissPageAll(), getMissBranchPage(), getCueingPage()))
                .addBranch(context.getString(R.string.turn_break_miss), getBreakErrorHow())
                .addBranch(context.getString(R.string.turn_illegal_break), getIllegalBreakHow())
                .addBranch(context.getString(R.string.turn_safety), getSafetyPage())
                .addBranch(context.getString(R.string.turn_won_game))
                .addBranch(context.getString(R.string.turn_push))
                .addBranch(context.getString(R.string.turn_skip))
                .addBranch(context.getString(R.string.turn_current_player_breaks, matchData.getString(CURRENT_PLAYER_NAME_KEY)))
                .addBranch(context.getString(R.string.turn_non_current_player_breaks, matchData.getString(OPPOSING_PLAYER_NAME_KEY)))
                .setValue(context.getString(R.string.turn_miss));
    }

    private Page[] getCueingPage() {
        if (isCueing() || isSpeed() || isDistances()) {
            return new Page[]{new CueBallPage(this, "Cue/object ball information")
                    .setCueing(isCueing())
                    .setDistances(isDistances())
                    .setSpeed(isSpeed())};
        } else return emptyPageArray;
    }

    private Page[] getSafetyErrorBranch() {
        if (isSafeties())
            return new Page[]{new SafetyErrorPage(this, context.getString(R.string.title_how_miss, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.how_choices_safety))
                    .setRequired(true)};
        else return emptyPageArray;
    }

    private Page[] getMissBranchPage() {
        if (isShotType()) {
            BranchPage page = new MissBranchPage(this, context.getString(R.string.title_miss, playerName))
                    .addBranch(context.getString(R.string.miss_cut), combineArrays(getCutTypePage(), getAnglePage(),
                            getHowMissPage(R.array.how_choices)))
                    .addBranch(context.getString(R.string.miss_long), getHowMissPage(R.array.how_choices))
                    .addBranch(context.getString(R.string.miss_bank), combineArrays(getBankPage(), getHowMissPage(R.array.how_choices_bank)))
                    .addBranch(context.getString(R.string.miss_kick), combineArrays(getKickPage(), getHowMissPage(R.array.how_choices_kick)))
                    .addBranch(context.getString(R.string.miss_combo), getHowMissPage(R.array.how_choices))
                    .addBranch(context.getString(R.string.miss_carom), getHowMissPage(R.array.how_choices))
                    .addBranch(context.getString(R.string.miss_jump), getHowMissPage(R.array.how_choices))
                    .addBranch(context.getString(R.string.miss_masse), getHowMissPage(R.array.how_choices_masse));

            return new Page[]{page.setValue(context.getString(R.string.miss_cut)).setRequired(true)};
        } else return emptyPageArray;
    }

    private Page[] combineArrays(Page[]... pages) {
        int length = 0;
        for (Page[] page : pages) {
            length += page.length;
        }

        Page[] result = new Page[length];
        int count = 0;
        for (Page[] page : pages) {
            for (Page subPage : page) {
                result[count] = subPage;
                count += 1;
            }
        }

        return result;
    }

    private Page[] getCutTypePage() {
        return new Page[]{new CutTypePage(this, context.getString(R.string.title_cut_type, playerName))
                .setChoices(context.getResources().getStringArray(R.array.cut_types))
                .setValue(context.getResources().getString(R.string.cut_rail))
                .setRequired(true)};
    }

    private Page[] getAnglePage() {
        if (isAngle()) {
            return new Page[]{new AngleTypePage(this, context.getString(R.string.title_angle, playerName), false)
                    .setChoices(context.getResources().getStringArray(R.array.angles))
                    .setValue(context.getResources().getStringArray(R.array.angles)[0])
                    .setRequired(true)};
        } else if (isSimpleAngle()) {
            return new Page[]{new AngleTypePage(this, context.getString(R.string.title_angle, playerName), false)
                    .setChoices(context.getResources().getStringArray(R.array.simple_angles))
                    .setValue(context.getResources().getStringArray(R.array.simple_angles)[0])
                    .setRequired(true)};
        } else return emptyPageArray;
    }

    private Page[] getBankPage() {
        if (isAngle()) {
            return new Page[]{new BankPage(this, context.getString(R.string.title_bank, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.banks))};
        } else return emptyPageArray;
    }

    private Page[] getKickPage() {
        if (isAngle()) {
            return new Page[]{new KickPage(this, context.getString(R.string.title_kick_type, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.kicks))
                    .setValue(context.getString(R.string.one_rail))
                    .setRequired(true)};
        } else return emptyPageArray;
    }

    private Page[] getBreakErrorHow() {
        if (isHowMiss())
            return new Page[]{new BreakErrorPage(this, context.getString(R.string.title_how_miss, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.how_choices_break))};
        else return emptyPageArray;
    }

    private Page[] getIllegalBreakHow() {
        if (isHowMiss())
            return new Page[]{new BreakErrorPage(this, context.getString(R.string.title_how_miss, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.how_choices_break))};
        else return emptyPageArray;
    }

    private Page[] getSafetyPage() {
        if (isSafeties())
            return new Page[]{new SafetyPage(this, context.getString(R.string.title_safety, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.safety_types))
                    .setValue(context.getString(R.string.safety_full_hook))
                    .setRequired(true)};
        else return emptyPageArray;
    }

    private Page[] getHowMissPage(@ArrayRes int choices) {
        if (isHowMiss())
            return new Page[]{new HowMissPage(this, context.getString(R.string.title_how_miss, playerName))
                    .setChoices(context.getResources().getStringArray(choices))};
        else return emptyPageArray;
    }

    private Page[] getHowMissPageAll() {
        if (isHowMiss() && !isShotType())
            return new Page[]{new HowMissPage(this, context.getString(R.string.title_how_miss, playerName))
                    .setChoices(context.getResources().getStringArray(R.array.how_choices_all))};
        else return emptyPageArray;
    }

    public TurnBuilder getTurnBuilder() {
        // make sure data is current
        for (Page page : getCurrentPageSequence())
            if (page instanceof UpdatesTurnInfo)
                ((UpdatesTurnInfo) page).updateTurnInfo(this);

        turnBuilder.advStats.startingPosition(matchData.getBoolean(SUCCESSFUL_SAFE_KEY) ? "Safe" : "Open");
        return turnBuilder;
    }

    void setObDistance(float dist) {
        turnBuilder.advStats.obDistance(dist);
    }

    void setCbDistance(float dist) {
        turnBuilder.advStats.cbDistance(dist);
    }

    void setCueing(int x, int y) {
        turnBuilder.advStats.cueing(x, y);
    }

    private boolean isShotType() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.SHOT_TYPE_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.SHOT_TYPE_OPPONENT);
        }
    }

    void setShotType(String shotType) {
        getAdvStats().shotType(convertStringToShotType(context, shotType));
        getAdvStats().clearAngle();
        getAdvStats().clearSubType();
        getAdvStats().clearHowTypes();
        getAdvStats().clearWhyTypes();
    }

    private boolean isCueing() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.CUEING_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.CUEING_OPPONENT);
        }
    }

    private boolean isHowMiss() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.HOW_MISS_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.HOW_MISS_OPPONENT);
        }
    }

    private boolean isSafeties() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.SAFETIES_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.SAFETIES_OPPONENT);
        }
    }

    private boolean isSpeed() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.SPEED_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.SPEED_OPPONENT);
        }
    }

    void setSpeed(int speed) {
        turnBuilder.advStats.speed(speed);
    }

    private boolean isDistances() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.BALL_DISTANCES_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.BALL_DISTANCES_OPPONENT);
        }
    }

    private boolean isAngle() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.ANGLE_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.ANGLE_OPPONENT);
        }
    }

    private boolean isSimpleAngle() {
        if (turn == PlayerTurn.PLAYER) {
            return dataCollection.contains(Match.StatsDetail.ANGLE_SIMPLE_PLAYER);
        } else {
            return dataCollection.contains(Match.StatsDetail.ANGLE_SIMPLE_OPPONENT);
        }
    }
}
