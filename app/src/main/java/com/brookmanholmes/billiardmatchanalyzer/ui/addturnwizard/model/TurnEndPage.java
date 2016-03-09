package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.TurnEndFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;
import com.brookmanholmes.billiards.inning.helpers.TurnEndHelper;
import com.brookmanholmes.billiards.match.Match;

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.STATS_LEVEL_KEY;
import static com.brookmanholmes.billiards.inning.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.inning.TurnEnd.CHANGE_TURN;
import static com.brookmanholmes.billiards.inning.TurnEnd.CONTINUE_WITH_GAME;
import static com.brookmanholmes.billiards.inning.TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN;
import static com.brookmanholmes.billiards.inning.TurnEnd.GAME_WON;
import static com.brookmanholmes.billiards.inning.TurnEnd.ILLEGAL_BREAK;
import static com.brookmanholmes.billiards.inning.TurnEnd.MISS;
import static com.brookmanholmes.billiards.inning.TurnEnd.OPPONENT_BREAKS_AGAIN;
import static com.brookmanholmes.billiards.inning.TurnEnd.PUSH_SHOT;
import static com.brookmanholmes.billiards.inning.TurnEnd.SAFETY;
import static com.brookmanholmes.billiards.inning.TurnEnd.SAFETY_ERROR;
import static com.brookmanholmes.billiards.inning.TurnEnd.SKIP_TURN;

/**
 * Created by Brookman Holmes on 2/20/2016.
 */
public class TurnEndPage extends BranchPage implements RequiresUpdatedTurnInfo, UpdatesTurnInfo {

    private static final String[] breakMissTypes = {"Too hard", "Too soft", "Hit too far to the left", "Hit too far to the right", "Foul", "Kicked into pocket (foul)"};
    private static final String[] illegalBreakTypes = {"Too soft", "Too thin", "CB in pocket"};
    private static final String[] whyChoices = {"Bad position", "Jacked up", "Lack of focus", "Over spin", "Unintentional english", "Too slow", "Too fast", "CB curved", "On the rail", "Forcing position"};
    private static final String[] whyChoicesSafety = {"Too fast", "Too slow", "Too thick", "Too thin"};
    private static final String[] whyChoicesBreak = {"Unintentional english", "Aim", "Unlucky", "Too much follow", "Too much draw", "I need to lift weights"};
    TurnEndHelper turnEndHelper;
    TurnEndFragment fragment;


    public TurnEndPage(ModelCallbacks callbacks, Bundle matchData) {
        super(callbacks, "How did your turn end?");

        data.putAll(matchData);

        turnEndHelper = TurnEndHelper.newTurnEndHelper(GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)));


        addBranch(SAFETY_ERROR.toString(), new FoulPage(callbacks, matchData));
        addBranch(MISS.toString(), new FoulPage(callbacks, matchData));
        addBranch(BREAK_MISS.toString(), new FoulPage(callbacks, matchData));
        addBranch(SAFETY.toString());
        addBranch(ILLEGAL_BREAK.toString(), new FoulPage(callbacks, matchData));
        addBranch(GAME_WON.toString());
        addBranch(PUSH_SHOT.toString());
        addBranch(SKIP_TURN.toString());
        addBranch(CURRENT_PLAYER_BREAKS_AGAIN.toString());
        addBranch(OPPONENT_BREAKS_AGAIN.toString());
        addBranch(CONTINUE_WITH_GAME.toString()); // TODO: this should start the dialog boxes over from the beginning with reBreak option turned to false
        addBranch(CHANGE_TURN.toString()); // // TODO: this can probably be removed

        if (Match.StatsDetail.valueOf(data.getString(STATS_LEVEL_KEY)) == Match.StatsDetail.ADVANCED) {
            // Safety Error branch
            branches.get(0).childPageList.add(new WhyMissPage(callbacks, whyChoicesSafety, "safety error branch"));

            // Miss branch
            branches.get(1).childPageList.add(new MissBranchPage(callbacks));
            branches.get(1).childPageList.add(new WhyMissPage(callbacks, whyChoices, "miss branch"));

            // Break Miss branch
            branches.get(2).childPageList.add(new HowMissPage(callbacks, breakMissTypes, "break miss"));
            branches.get(2).childPageList.add(new WhyMissPage(callbacks, whyChoicesBreak, "break miss branch"));

            // Safety branch
            branches.get(3).childPageList.add(new SafetyPage(callbacks));

            // Illegal break branch
            branches.get(4).childPageList.add(new HowMissPage(callbacks, illegalBreakTypes, "illegal break"));
            branches.get(4).childPageList.add(new WhyMissPage(callbacks, whyChoicesBreak, "illegal break branch"));
        }
    }

    @Override
    public Fragment createFragment() {
        return TurnEndFragment.create(getKey());
    }

    @Override
    public void getNewTurnInfo(TurnBuilder turnBuilder) {
        TurnEndOptions options = turnEndHelper.create(MatchDialogHelperUtils.createGameStatusFromBundle(data),
                turnBuilder.tableStatus);
        updateFragment(options);
    }

    @Override
    public void updateTurnInfo(TurnBuilder turnBuilder) {
        turnBuilder.turnEnd = getTurnEndFromPageData();
    }

    private TurnEnd getTurnEndFromPageData() {
        return TurnEnd.fromString(data.getString(SIMPLE_DATA_KEY, "Miss"));
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    public void registerListener(TurnEndFragment fragment) {
        this.fragment = fragment;
    }

    public void unregisterListener() {
        fragment = null;
    }

    public void updateFragment(TurnEndOptions options) {
        if (fragment != null) {
            List<String> optionsInString = new ArrayList<>();

            for (TurnEnd turnEnd : options.possibleEndings) {
                optionsInString.add(turnEnd.toString());
            }

            fragment.updateOptions(optionsInString, options.defaultCheck.toString());
        }
    }
}
