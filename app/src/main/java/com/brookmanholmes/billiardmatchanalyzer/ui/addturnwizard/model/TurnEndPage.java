package com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brookmanholmes.billiardmatchanalyzer.ui.addturnwizard.fragments.TurnEndFragment;
import com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.BranchPage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.ModelCallbacks;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.MultipleFixedChoicePage;
import com.brookmanholmes.billiardmatchanalyzer.wizard.model.SingleFixedChoicePage;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.inning.TurnEnd;
import com.brookmanholmes.billiards.inning.TurnEndOptions;
import com.brookmanholmes.billiards.inning.helpers.TurnEndHelper;

import java.util.ArrayList;
import java.util.List;

import static com.brookmanholmes.billiards.inning.TurnEnd.BREAK_MISS;
import static com.brookmanholmes.billiards.inning.TurnEnd.CHANGE_TURN;
import static com.brookmanholmes.billiards.inning.TurnEnd.CONTINUE_WITH_GAME;
import static com.brookmanholmes.billiards.inning.TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN;
import static com.brookmanholmes.billiards.inning.TurnEnd.GAME_LOST;
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
public class TurnEndPage extends BranchPage implements RequiresUpdatedTurnInfo {
    private static final String[] safetyTypes = {"Full hook", "Partial hook", "Long T", "Short T"};
    private static final String[] breakMissTypes = {"Too hard", "Too soft", "Hit too far to the left", "Hit too far to the right", "Foul", "Kicked into pocket (foul)"};
    private static final String[] illegalBreakTypes = {"Too soft", "Too thin", "CB in pocket"};
    private static final String[] whyChoices = {"Bad position", "Jacked up", "Lack of focus", "Overspin", "Unintentional english", "Too slow", "Too fast", "CB curved", "On the rail", "Forcing position"};
    TurnEndHelper turnEndHelper;
    TurnEndFragment fragment;


    public TurnEndPage(ModelCallbacks callbacks, Bundle matchData) {
        super(callbacks, "Turn end page");

        data.putAll(matchData);

        turnEndHelper = TurnEndHelper.newTurnEndHelper(GameType.valueOf(data.getString(MatchDialogHelperUtils.GAME_TYPE_KEY)));

        addBranch(SAFETY_ERROR.toString(),
                new SingleFixedChoicePage(callbacks, "You left your opponent").setChoices("Fully open", "Difficult shot"),
                new SingleFixedChoicePage(callbacks, "Why?").setChoices("Speed: too fast", "Speed: too slow", "Hit: too thick", "Hit: too thin", "Moved blocker balls"));

        addBranch(MISS.toString(),
                new BranchPage(callbacks, "What did you miss?")

                        .addBranch("Cut shot", new SingleFixedChoicePage(callbacks, "Type of cut").setChoices("Back cut", "Wing cut", "Down the rail", "Long straight shot").setRequired(true),
                                new SingleFixedChoicePage(callbacks, "Degree of cut").setChoices("Straight", "15 degrees", "30 degrees", "45 degrees", "60 degrees", "75 degrees", "90 degrees").setRequired(true),
                                new MultipleFixedChoicePage(callbacks, "How did you miss?").setChoices("Too thin", "Too thick", "Left of aim point", "Right of aim point").setRequired(true))

                        .addBranch("Bank shot", new MultipleFixedChoicePage(callbacks, "Type of bank").setChoices("Natural", "Crossover", "Long rail", "Short rail", "1 rail", "2 rail", "3 rail", "4 rail").setRequired(true),
                                new SingleFixedChoicePage(callbacks, "How did you miss?").setChoices("Too long", "Too short").setRequired(true))

                        .addBranch("Kick shot", new SingleFixedChoicePage(callbacks, "Type of kick").setChoices("1 rail", "2 rail", "3 rail", "4 rail").setRequired(true),
                                new SingleFixedChoicePage(callbacks, "How did you miss?").setChoices("Too long", "Too short").setRequired(true))

                        .addBranch("Combo", new MultipleFixedChoicePage(callbacks, "How did you miss?").setChoices("Too thin", "Too thick", "Left of aim point", "Right of aim point").setRequired(true))

                        .addBranch("Carom", new MultipleFixedChoicePage(callbacks, "How did you miss?").setChoices("Too thin", "Too thick", "Left of aim point", "Right of aim point").setRequired(true)) // these choices are probably shit

                        .addBranch("Jump", new MultipleFixedChoicePage(callbacks, "How did you miss?").setChoices("Too thin", "Too thick", "Left of aim point", "Right of aim point", "Jumped over OB", "Jumped into impeding ball").setRequired(true)),
                new MultipleFixedChoicePage(callbacks, "Why did you miss?").setChoices(whyChoices).setRequired(true))
                .setRequired(true);

        addBranch(BREAK_MISS.toString(),
                new MultipleFixedChoicePage(callbacks, "How did you miss?").setChoices(breakMissTypes),
                new MultipleFixedChoicePage(callbacks, "Why did you miss?").setChoices("Unintentional english", "Aim", "Unlucky", "Too much follow", "Too much draw", "I need to lift weights"));
        addBranch(GAME_WON.toString());
        addBranch(SAFETY.toString(),
                new MultipleFixedChoicePage(callbacks, "Safety attempt type").setChoices(safetyTypes));
        addBranch(PUSH_SHOT.toString());
        addBranch(SKIP_TURN.toString());
        addBranch(GAME_LOST.toString()); // TODO: this should be moved to an end page as a flag and replaced with the actual reason for fouling
        addBranch(ILLEGAL_BREAK.toString(),
                new MultipleFixedChoicePage(callbacks, "Illegal break type").setChoices(illegalBreakTypes));
        addBranch(CURRENT_PLAYER_BREAKS_AGAIN.toString());
        addBranch(OPPONENT_BREAKS_AGAIN.toString());
        addBranch(CONTINUE_WITH_GAME.toString()); // TODO: this should start the dialog boxes over from the beginning with reBreak option turned to false
        addBranch(CHANGE_TURN.toString()); // // TODO: this can probably be removed
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
