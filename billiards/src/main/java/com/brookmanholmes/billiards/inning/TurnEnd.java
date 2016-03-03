package com.brookmanholmes.billiards.inning;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum TurnEnd {
    SAFETY_ERROR("Safety Miss"),
    MISS("Miss"),
    BREAK_MISS("Miss on Break"),
    GAME_WON("Game won"),
    SAFETY("Safety"),
    PUSH_SHOT("Push"),
    SKIP_TURN("Give shot back"),
    GAME_LOST("Game lost"),
    CURRENT_PLAYER_BREAKS_AGAIN("Current player breaks again"),
    OPPONENT_BREAKS_AGAIN("Opposing player breaks again"),
    CONTINUE_WITH_GAME("Continue game"),
    CHANGE_TURN("Change turn"),
    ILLEGAL_BREAK("Illegal break");

    private final String name;

    TurnEnd(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
