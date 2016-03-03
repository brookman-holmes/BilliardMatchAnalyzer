package com.brookmanholmes.billiards.inning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brookman on 9/26/15.
 */
public class TurnEndOptions {
    public final boolean scratch;
    public List<TurnEnd> possibleEndings = new ArrayList<>();
    public TurnEnd defaultCheck;

    TurnEndOptions(Builder builder) {
        possibleEndings = builder.turnEnds;
        scratch = builder.scratch;
        defaultCheck = builder.checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnEndOptions that = (TurnEndOptions) o;

        if (scratch != that.scratch) return false;
        // TODO: 2/20/2016 might need to make this more stringent?
        if (!possibleEndings.containsAll(that.possibleEndings)) return false;
        if (!that.possibleEndings.containsAll(possibleEndings)) return false;
        if (possibleEndings.size() != that.possibleEndings.size()) return false;

        return defaultCheck == that.defaultCheck;

    }

    @Override
    public int hashCode() {
        int result = possibleEndings.hashCode();
        result = 31 * result + defaultCheck.hashCode();
        result = 31 * result + (scratch ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TurnEndOptions{" +
                "possibleEndings=" + possibleEndings +
                "\n defaultCheck=" + defaultCheck +
                ", scratch=" + scratch +
                '}';
    }

    public static class Builder {
        private TurnEnd checked = TurnEnd.MISS;
        private boolean scratch;
        private List<TurnEnd> turnEnds = new ArrayList<>();


        public Builder() {
        }

        public Builder wonGame(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.GAME_WON);
            }
            return this;
        }

        public Builder lostGame(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.GAME_LOST);
            }
            return this;
        }

        public Builder defaultOption(TurnEnd turnEnd) {
            checked = turnEnd;
            return this;
        }

        public Builder safety(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.SAFETY);
            }
            return this;
        }

        public Builder safetyError(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.SAFETY_ERROR);
            }
            return this;
        }

        public Builder checkScratch(boolean checked) {
            scratch = checked;
            return this;
        }

        public Builder miss(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.MISS);
            }
            return this;
        }

        public Builder missOnBreak(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.BREAK_MISS);
            }
            return this;
        }

        public Builder illegalBreak(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.ILLEGAL_BREAK);
            }
            return this;
        }

        public Builder push(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.PUSH_SHOT);
            }
            return this;
        }

        public Builder skipTurn(boolean show) {
            if (show) {
                turnEnds.add(TurnEnd.SKIP_TURN);
            }
            return this;
        }

        public Builder allowPlayerToChooseToContinueGame() {
            turnEnds.add(TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN);
            turnEnds.add(TurnEnd.CONTINUE_WITH_GAME);

            return this;
        }

        public Builder allowPlayerToChooseWhoBreaks() {
            turnEnds.add(TurnEnd.CURRENT_PLAYER_BREAKS_AGAIN);
            turnEnds.add(TurnEnd.OPPONENT_BREAKS_AGAIN);
            turnEnds.add(TurnEnd.CONTINUE_WITH_GAME);

            return this;
        }

        public TurnEndOptions build() {
            return new TurnEndOptions(this);
        }
    }
}
