package com.brookmanholmes.bma.ui.newmatchwizard.model;

import android.content.Context;

import com.brookmanholmes.bma.R;
import com.brookmanholmes.bma.wizard.model.AbstractWizardModel;
import com.brookmanholmes.bma.wizard.model.Page;
import com.brookmanholmes.bma.wizard.model.PageList;
import com.brookmanholmes.billiards.game.util.BreakType;
import com.brookmanholmes.billiards.game.util.GameType;
import com.brookmanholmes.billiards.game.util.PlayerTurn;
import com.brookmanholmes.billiards.match.Match;

/**
 * Created by Brookman Holmes on 1/7/2016.
 */
public class CreateNewMatchWizardModel extends AbstractWizardModel {
    String playerName = "Player 1", opponentName = "Player 2";
    GameType gameType;
    boolean playTheGhost;

    private Match.Builder builder = new Match.Builder();

    public CreateNewMatchWizardModel(Context context) {
        super(context);
        rootPageList = onNewRootPageList();
    }

    @Override public void onPageDataChanged(Page page) {
        super.onPageDataChanged(page);

        if (page instanceof UpdatesPlayerNames) {
            playerName = ((PlayerNamePage) page).getPlayerName();
            opponentName = ((PlayerNamePage) page).getOpponentName();
        }

        updatePlayerNames();
        if (page instanceof UpdatesMatchBuilder)
            ((UpdatesMatchBuilder) page).updateMatchBuilder(this);
    }

    @Override public void onPageTreeChanged() {
        super.onPageTreeChanged();

        updateBuilder();
    }

    private void updateBuilder() {
        for (Page page : getCurrentPageSequence()) {
            if (page instanceof UpdatesMatchBuilder)
                ((UpdatesMatchBuilder) page).updateMatchBuilder(this);
        }
    }

    private void updatePlayerNames() {
        for (Page page : rootPageList) {
            if (page instanceof RequiresPlayerNames) {
                ((RequiresPlayerNames) page).setPlayerNames(playerName, opponentName);
            }
        }
    }

    @Override protected PageList onNewRootPageList() {
        return new PageList(
                getPlayerNamePage(),
                getGameChoicePage(),
                getStatDetailPage()
        );
    }

    private Page getPlayerNamePage() {
        return new PlayerNamePage(this, context.getString(R.string.title_page_players),
                context.getString(R.string.player_number), context.getString(R.string.location))
                .setRequired(true);
    }

    private Page getGhostGameChoicePage() {
        return new GameChoicePage(this, context.getString(R.string.title_page_games), context)
                .addBranch(context.getString(R.string.game_bca_eight))
                .addBranch(context.getString(R.string.game_bca_nine))
                .addBranch(context.getString(R.string.game_bca_ten))
                .setValue(context.getString(R.string.game_bca_nine))
                .setRequired(true);
    }

    private Page getGameChoicePage() {
        return new GameChoicePage(this, context.getString(R.string.title_page_games), context)
                .addBranch(context.getString(R.string.game_apa_eight),
                        getApaRankPage(GameType.APA_EIGHT_BALL, PlayerTurn.PLAYER),
                        getApaRankPage(GameType.APA_EIGHT_BALL, PlayerTurn.OPPONENT),
                        getFirstBreakPage("apa 8"))
                .addBranch(context.getString(R.string.game_apa_nine),
                        getApaRankPage(GameType.APA_NINE_BALL, PlayerTurn.PLAYER),
                        getApaRankPage(GameType.APA_NINE_BALL, PlayerTurn.OPPONENT),
                        getFirstBreakPage("apa 9"))
                .addBranch(context.getString(R.string.game_bca_eight),
                        getBreakTypePage("bca 8"))
                .addBranch(context.getString(R.string.game_bca_nine),
                        getBreakTypePage("bca 9"))
                .addBranch(context.getString(R.string.game_bca_ten),
                        getBreakTypePage("bca 10"))
                .setValue(context.getString(R.string.game_bca_nine))
                .setRequired(true);
    }

    private Page getFirstBreakPage(String parentKey) {
        return new FirstBreakPage(this, context.getString(R.string.title_page_first_break), parentKey)
                .setChoices(playerName, opponentName)
                .setValue(playerName)
                .setRequired(true);
    }

    private Page getApaRankPage(GameType gameType, PlayerTurn playerTurn) {
        if (gameType == GameType.APA_EIGHT_BALL) {
            return new Apa8BallRankPage(this, context.getString(R.string.title_page_rank), playerTurn)
                    .setRequired(true);
        } else if (gameType == GameType.APA_NINE_BALL) {
            return new Apa9BallRankPage(this, context.getString(R.string.title_page_rank), playerTurn);
        } else throw new IllegalArgumentException("That game type is not supported: " + gameType);
    }

    private Page getStatDetailPage() {
        return new StatDetailPage(this, context.getString(R.string.title_page_stats))
                .setChoices(context.getResources().getStringArray(R.array.stat_levels))
                .setValue(context.getString(R.string.detail_normal))
                .setRequired(true);
    }

    private Page getBreakTypePage(String parentPage) {
        return new BreakTypePage(this, context.getString(R.string.title_page_break), context)
                .addBranch(context.getString(R.string.break_winner),
                        getFirstBreakPage(parentPage))
                .addBranch(context.getString(R.string.break_alternate),
                        getFirstBreakPage(parentPage))
                .addBranch(context.getString(R.string.break_loser),
                        getFirstBreakPage(parentPage))
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
    }

    void setBreakType(String value) {
        BreakType breakType;

        if (playTheGhost)
            breakType = BreakType.GHOST;
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

    void setPlayerRank(PlayerTurn playerTurn, String playerRank) {
        if (playerTurn == PlayerTurn.PLAYER)
            builder.setPlayerRank(Integer.valueOf(playerRank));
        else if (playerTurn == PlayerTurn.OPPONENT)
            builder.setOpponentRank(Integer.valueOf(playerRank));
        else throw new IllegalArgumentException("No such PlayerTurn: " + playerTurn);
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
        else throw new IllegalArgumentException("No such game type: " + value);
    }

    void setStatDetail(String value) {
        Match.StatsDetail detail;

        if (value.equals(context.getString(R.string.detail_simple)))
            detail = Match.StatsDetail.SIMPLE;
        else if (value.equals(context.getString(R.string.detail_normal)))
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
        return builder.build(gameType);
    }
}
