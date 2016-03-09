package com.brookmanholmes.billiards.inning;

/**
 * Created by Brookman Holmes on 10/26/2015.
 */
public enum TurnEnd {
    SAFETY_ERROR("Safety Miss"),
    MISS("Miss"),
    BREAK_MISS("Miss on break"),
    GAME_WON("Game won"),
    SAFETY("Safety"),
    PUSH_SHOT("Push"),
    SKIP_TURN("Give shot back"),
    CURRENT_PLAYER_BREAKS_AGAIN("Current player breaks again"),
    OPPONENT_BREAKS_AGAIN("Opposing player breaks again"),
    CONTINUE_WITH_GAME("Continue game"),
    CHANGE_TURN("Change turn"),
    ILLEGAL_BREAK("Illegal break");

    private final String name;

    TurnEnd(String name) {
        this.name = name;
    }

    public static TurnEnd fromString(String value) {
        switch (value) {
            case "Safety Miss":
                return SAFETY_ERROR;
            case "Miss":
                return MISS;
            case "Miss on break":
                return BREAK_MISS;
            case "Game won":
                return GAME_WON;
            case "Safety":
                return SAFETY;
            case "Push":
                return PUSH_SHOT;
            case "Give shot back":
                return SKIP_TURN;
            case "Current player breaks again":
                return CURRENT_PLAYER_BREAKS_AGAIN;
            case "Opposing player breaks again":
                return OPPONENT_BREAKS_AGAIN;
            case "Continue game":
                return CONTINUE_WITH_GAME;
            case "Change turn":
                return CHANGE_TURN;
            case "Illegal break":
                return ILLEGAL_BREAK;
            default:
                throw new IllegalArgumentException(value + " is not a valid Turn End");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
