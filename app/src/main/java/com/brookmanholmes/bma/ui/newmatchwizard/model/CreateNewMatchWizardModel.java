package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.content.Context;

import com.brookmanholmes.billiards.game.BreakType;
import com.brookmanholmes.billiards.game.GameType;
import com.brookmanholmes.billiards.game.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.AbstractWizardModel;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.PageList;

import java.util.Date;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class CreateNewMatchWizardModel extends AbstractWizardModel {
    private static final String TAG = "CreateNewMatchModel";
    private final Match.Builder builder = new Match.Builder();
    private String playerName = "Player 1";
    private String opponentName = "Player 2";
    private GameType gameType;
    private boolean playTheGhost;

    public CreateNewMatchWizardModel(Context context) {
        super(context);
        rootPageList = onNewRootPageList();
    }

    @Override
    public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof UpdatesPlayerNames) {
            playerName = ((PlayerNamePage) page).getPlayerName();
            opponentName = ((PlayerNamePage) page).getOpponentName();

            updatePlayerNames();
        }

        if (page instanceof UpdatesMatchBuilder)
            ((UpdatesMatchBuilder) page).updateMatchBuilder(this);
    }

    @Override
    public void onPageTreeChanged() {
        super.onPageTreeChanged();

        updateBuilder();
    }

    private void updateBuilder() {
        for (int i = 0; i < getCurrentPageSequence().size(); i++) {
            if (getCurrentPageSequence().get(i) instanceof UpdatesMatchBuilder)
                ((UpdatesMatchBuilder) getCurrentPageSequence().get(i)).updateMatchBuilder(this);
        }
    }

    private void updatePlayerNames() {
        for (int i = 0; i < getCurrentPageSequence().size(); i++) {
            if (getCurrentPageSequence().get(i) instanceof RequiresPlayerNames)
                ((RequiresPlayerNames) getCurrentPageSequence().get(i)).setPlayerNames(playerName, opponentName);
        }
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(getPlayerNamePage());
    }

    private Page getPlayerNamePage() {
        return new PlayerNamePage(this, context.getString(R.string.title_page_players), context, "PNG")
                .addBranch(Boolean.TRUE.toString(), getGhostGameChoicePage(), getStatDetailPageGhost())
                .addBranch(Boolean.FALSE.toString(), getGameChoicePage(), getStatDetailPage())
                .setValue(Boolean.FALSE.toString())
                .setRequired(true);
    }

    private Page getGhostGameChoicePage() {
        return new GameChoicePage(this, context.getString(R.string.title_page_games), context, "GGCP")
                .addBranch(context.getString(R.string.game_apa_eight_ghost),
                        getRaceToPage(context.getString(R.string.ranks), context.getString(R.string.bca_review), GameType.APA_EIGHT_BALL, "gapa8")
                                .setRaceToChoices(2, 8, 6))
                .addBranch(context.getString(R.string.game_apa_nine_ghost),
                        getRaceToPage(context.getString(R.string.ranks), context.getString(R.string.apa_nine_review), GameType.APA_NINE_BALL, "gapa9")
                                .setRaceToChoices(1, 10, 6))
                .addBranch(context.getString(R.string.game_bca_eight_ghost), getBcaRankPage("gbca8"))
                .addBranch(context.getString(R.string.game_bca_nine_ghost), getBcaRankPage("gbca9"))
                .addBranch(context.getString(R.string.game_bca_ten_ghost), getBcaRankPage("gbca10"))
                .setValue(context.getString(R.string.game_bca_nine_ghost))
                .setRequired(true);
    }

    private Page getGameChoicePage() {
        return new GameChoicePage(this, context.getString(R.string.title_page_games), context, "GCP")
                .addBranch(context.getString(R.string.game_apa_eight),
                        getRaceToPage(context.getString(R.string.ranks), context.getString(R.string.bca_review), GameType.APA_EIGHT_BALL, "apa8")
                                .setRaceToChoices(2, 8, 6),
                        getFirstBreakPage("apa8"))
                .addBranch(context.getString(R.string.game_apa_nine),
                        getRaceToPage(context.getString(R.string.ranks), context.getString(R.string.apa_nine_review), GameType.APA_NINE_BALL, "apa9")
                                .setRaceToChoices(1, 10, 6),
                        getFirstBreakPage("apa9"))
                .addBranch(context.getString(R.string.game_bca_eight), getBcaRankPage("bca8"), getBreakTypePage("bca8"))
                .addBranch(context.getString(R.string.game_bca_nine), getBcaRankPage("bca9"), getBreakTypePage("bca9"))
                .addBranch(context.getString(R.string.game_bca_ten), getBcaRankPage("bca10"), getBreakTypePage("bca10"))
                .setValue(context.getString(R.string.game_bca_nine))
                .setRequired(true);
    }

    private RaceToPage getRaceToPage(String title, String reviewString, GameType gameType, String parentKey) {
        return new RaceToPage(this, title, reviewString, gameType, context.getString(R.string.race), parentKey);
    }

    private Page getBcaRankPage(String parentKey) {
        return getRaceToPage(context.getString(R.string.race), context.getString(R.string.bca_review), GameType.BCA_EIGHT_BALL, parentKey)
                .setRaceToChoices(1, 21, 5);
    }

    private Page getFirstBreakPage(String parentKey) {
        return new FirstBreakPage(this, context.getString(R.string.title_page_first_break), parentKey)
                .setChoices(playerName, opponentName)
                .setValue(playerName)
                .setRequired(true);
    }

    private Page getStatDetailPage() {
        return new StatDetailPage(this, context.getString(R.string.title_page_stats), "SDP")
                .setChoices(context.getResources().getStringArray(R.array.stat_levels))
                .setValue(context.getString(R.string.detail_normal))
                .setRequired(true);
    }

    private Page getStatDetailPageGhost() {
        return new StatDetailPageGhost(this, context.getString(R.string.title_page_stats), "SDPG")
                .setChoices(context.getString(R.string.detail_advanced), context.getString(R.string.detail_normal))
                .setValue(context.getString(R.string.detail_normal))
                .setRequired(true);
    }

    private Page getBreakTypePage(String parentKey) {
        return new BreakTypePage(this, context.getString(R.string.title_page_break), context, parentKey)
                .addBranch(context.getString(R.string.break_winner),
                        getFirstBreakPage(parentKey))
                .addBranch(context.getString(R.string.break_alternate),
                        getFirstBreakPage(parentKey))
                .addBranch(context.getString(R.string.break_loser),
                        getFirstBreakPage(parentKey))
                .addBranch(context.getString(R.string.break_player, playerName))
                .addBranch(context.getString(R.string.break_player, opponentName))
                .setValue(context.getString(R.string.break_winner))
                .setRequired(true);
    }

    void setPlayerName(String playerName, String opponentName, String location, String notes, boolean playGhost) {
        this.playerName = playerName;
        this.opponentName = opponentName;

        builder.setPlayerName(playerName)
                .setOpponentName(opponentName)
                .setLocation(location)
                .setNotes(notes);

        playTheGhost = playGhost;
        if (playTheGhost) { // shim to set the breakType since the break page doesn't exist to set it
            setBreakType(context.getString(R.string.break_player, playerName));
        }
    }

    void setBreakType(String value) {
        BreakType breakType;

        if (playTheGhost)
            breakType = BreakType.PLAYER;
        else if (value.equals(context.getString(R.string.break_winner)))
            breakType = BreakType.WINNER;
        else if (value.equals(context.getString(R.string.break_alternate)))
            breakType = BreakType.ALTERNATE;
        else if (value.equals(context.getString(R.string.break_loser)))
            breakType = BreakType.LOSER;
        else if (value.equals(context.getString(R.string.break_player, playerName)))
            breakType = BreakType.PLAYER;
        else if (value.equals(context.getString(R.string.break_player, opponentName)))
            breakType = BreakType.OPPONENT;
        else throw new IllegalArgumentException("No corresponding break type to: " + value);

        builder.setBreakType(breakType);

        if (breakType == BreakType.PLAYER)
            builder.setPlayerTurn(PlayerTurn.PLAYER);
        if (breakType == BreakType.OPPONENT)
            builder.setPlayerTurn(PlayerTurn.OPPONENT);
    }

    void setPlayerRanks(int playerRank, int opponentRank) {
        builder.setPlayerRanks(playerRank, opponentRank);
    }

    void setFirstBreaker(PlayerTurn turn) {
        builder.setPlayerTurn(turn);
    }

    void setGameType(String value) {
        if (value.equals(context.getString(R.string.game_apa_eight)))
            gameType = GameType.APA_EIGHT_BALL;
        else if (value.equals(context.getString(R.string.game_apa_nine)))
            gameType = GameType.APA_NINE_BALL;
        else if (value.equals(context.getString(R.string.game_bca_eight)))
            gameType = GameType.BCA_EIGHT_BALL;
        else if (value.equals(context.getString(R.string.game_bca_nine)))
            gameType = GameType.BCA_NINE_BALL;
        else if (value.equals(context.getString(R.string.game_bca_ten)))
            gameType = GameType.BCA_TEN_BALL;
        else if (value.equals(context.getString(R.string.game_straight)))
            gameType = GameType.STRAIGHT_POOL;
        else if (value.equals(context.getString(R.string.game_american_rotation)))
            gameType = GameType.AMERICAN_ROTATION;
        else if (value.equals(context.getString(R.string.game_bca_eight_ghost)))
            gameType = GameType.BCA_GHOST_EIGHT_BALL;
        else if (value.equals(context.getString(R.string.game_bca_nine_ghost)))
            gameType = GameType.BCA_GHOST_NINE_BALL;
        else if (value.equals(context.getString(R.string.game_bca_ten_ghost)))
            gameType = GameType.BCA_GHOST_TEN_BALL;
        else if (value.equals(context.getString(R.string.game_apa_eight_ghost)))
            gameType = GameType.APA_GHOST_EIGHT_BALL;
        else if (value.equals(context.getString(R.string.game_apa_nine_ghost)))
            gameType = GameType.APA_GHOST_NINE_BALL;
        else throw new IllegalArgumentException("No such game type: " + value);
    }

    void setStatDetail(String value) {
        Match.StatsDetail detail;

        if (value.equals(context.getString(R.string.detail_normal)))
            detail = Match.StatsDetail.NORMAL;
        else if (value.equals(context.getString(R.string.detail_advanced)))
            detail = Match.StatsDetail.ADVANCED;
        else if (value.equals(context.getString(R.string.detail_player, playerName)))
            detail = Match.StatsDetail.ADVANCED_PLAYER;
        else if (value.equals(context.getString(R.string.detail_player, opponentName)))
            detail = Match.StatsDetail.ADVANCED_OPPONENT;
        else throw new IllegalArgumentException("No such stat detail level for: " + value);

        builder.setStatsDetail(detail);
    }

    public Match createMatch() {
        updateBuilder();
        return builder.setDate(new Date()).build(gameType);
    }
}
