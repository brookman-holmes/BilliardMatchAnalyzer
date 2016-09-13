package com.brookmanholmes.billiards.turn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brookman on 9/26/15.
 * A data class that contains the possible endings for a turn based on the game status and the
 * status of the balls for the next turn
 */
// TODO: 9/13/2016 add in tests for foul, lostgame, nofoul getters
public class TurnEndOptions {
    private final boolean foul;
    private final boolean lostGame;
    private final boolean noFoul;
    public final TurnEnd defaultCheck;
    public final List<TurnEnd> possibleEndings = new ArrayList<>();

    /**
     * Creates a new {@link com.brookmanholmes.billiards.turn.TurnEndOptions} object with the
     * specified options from it's builder
     * @param builder
     */
    private TurnEndOptions(Builder builder) {
        possibleEndings.addAll(builder.turnEnds);
        foul = builder.foul;
        defaultCheck = builder.checked;
        lostGame = builder.lostGame;
        noFoul = !builder.foul;
    }

    public boolean showFoul() {
        return true;
    }

    public boolean showNotFoul() {
        return noFoul;
    }

    public boolean showLostGame() {
        return lostGame;
    }


    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnEndOptions options = (TurnEndOptions) o;

        if (foul != options.foul) return false;
        if (lostGame != options.lostGame) return false;
        if (!possibleEndings.containsAll(options.possibleEndings)) return false;
        if (!options.possibleEndings.containsAll(possibleEndings)) return false;
        if (possibleEndings.size() != options.possibleEndings.size()) return false;
        return defaultCheck == options.defaultCheck;
    }

    @Override public int hashCode() {
        int result = (foul ? 1 : 0);
        result = 31 * result + possibleEndings.hashCode();
        result = 31 * result + defaultCheck.hashCode();
        result = 31 * result + (lostGame ? 1 : 0);
        return result;
    }

    @Override public String toString() {
        return "TurnEndOptions{" +
                "foul=" + foul +
                "\n possibleEndings=" + possibleEndings +
                "\n defaultCheck=" + defaultCheck +
                "\n lostGame=" + lostGame +
                '}';
    }

    /**
     * A builder class to facilitate creating a TurnEndOptions object
     */
    public static class Builder {
        private final List<TurnEnd> turnEnds = new ArrayList<>();
        private TurnEnd checked = TurnEnd.MISS;
        private boolean foul;
        private boolean lostGame;

        /**
         * Creates a builder for a new {@link com.brookmanholmes.billiards.turn.TurnEndOptions}
         * that uses the options selected in the builder
         */
        public Builder() {
        }

        /**
         * Sets showing won game as an option for your turn end
         * @param show true shows {@link com.brookmanholmes.billiards.turn.TurnEnd#GAME_WON}, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder wonGame(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.GAME_WON);
            }
            return this;
        }

        /**
         * Sets showing lost game as an option for your turn end
         * @param show true enables showing lost game as an additional option for how your turn ended
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder lostGame(boolean show) {
            lostGame = show;
            return this;
        }

        /**
         * Sets the default option to select
         * @param turnEnd The turn ending you would like to default to
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder defaultOption(TurnEnd turnEnd) {
            checked = turnEnd;
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#SAFETY} as an
         * option for the turn end
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder safety(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.SAFETY);
            }
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#SAFETY_ERROR} as an
         * option for the turn end
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder safetyError(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.SAFETY_ERROR);
            }
            return this;
        }

        /**
         * Sets whether a foul was possible
         * @param checked true means a foul was possible, false means it was not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder checkFoul(boolean checked) {
            foul = checked;
            return this;
        }

        /**
         * Sets whether the current player should be shown the option to break again
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder allowPlayerToBreakAgain() {
            turnEnds.add(TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN);
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#MISS} as an option
         * for the end of the turn
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder miss(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.MISS);
            }
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#BREAK_MISS} as an option
         * for the end of the turn
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder missOnBreak(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.BREAK_MISS);
            }
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#ILLEGAL_BREAK} as an option
         * for the end of the turn
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder illegalBreak(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.ILLEGAL_BREAK);
            }
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#PUSH_SHOT} as an option
         * for the end of the turn
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder push(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.PUSH_SHOT);
            }
            return this;
        }

        /**
         * Sets whether to show {@link com.brookmanholmes.billiards.turn.TurnEnd#SKIP_TURN} as an option
         * for the end of the turn
         * @param show true shows it, false does not
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder skipTurn(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.SKIP_TURN);
            }
            return this;
        }

        /**
         * Sets the options to be
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#CURRENT_PLAYER_BREAKS_AGAIN},
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#MISS},
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#SAFETY} or
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#SAFETY_ERROR}
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder allowPlayerToChooseToContinueGame() {
            turnEnds.add(TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN);
            turnEnds.add(TurnEnd.MISS);
            turnEnds.add(TurnEnd.SAFETY);
            turnEnds.add(TurnEnd.SAFETY_ERROR);

            return this;
        }

        /**
         * Sets the options to be
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#CURRENT_PLAYER_BREAKS_AGAIN},
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#OPPONENT_BREAKS_AGAIN} or
         * {@link com.brookmanholmes.billiards.turn.TurnEnd#CONTINUE_WITH_GAME}
         * @return an instance of {@link com.brookmanholmes.billiards.turn.TurnEndOptions.Builder} for
         * chaining purposes
         */
        public Builder allowPlayerToChooseWhoBreaks() {
            turnEnds.add(TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN);
            turnEnds.add(TurnEnd.OPPONENT_BREAKS_AGAIN);
            turnEnds.add(TurnEnd.CONTINUE_WITH_GAME);

            return this;
        }

        /**
         * Creates a {@link com.brookmanholmes.billiards.turn.TurnEndOptions} with the options
         * supplied to this builder
         * @return A new {@link com.brookmanholmes.billiards.turn.TurnEndOptions} object
         */
        public TurnEndOptions build() {
            return new TurnEndOptions(this);
        }
    }
}
