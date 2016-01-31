package com.brookmanholmes.billiards.inning;

/**
 * Created by brookman on 9/26/15.
 */
public class TurnEndOptions {
    public final boolean safety;
    public final boolean safetyMiss;
    public final boolean miss;
    public final boolean wonGame;
    public final boolean lostGame;
    public final boolean push;
    public final boolean skip;
    public final boolean illegalBreak;
    public final boolean scratch;
    public final boolean continueGame;
    public final boolean restartGame;
    public final boolean reBreak;
    public final boolean breakMiss;

    public TurnEnd defaultCheck;

    TurnEndOptions(Builder builder) {
        safety = builder.safety;
        safetyMiss = builder.safetyMiss;
        miss = builder.miss;
        wonGame = builder.wonGame;
        lostGame = builder.lostGame;
        push = builder.push;
        skip = builder.skip;
        scratch = builder.scratch;
        illegalBreak = builder.illegalBreak;
        defaultCheck = builder.checked;
        continueGame = builder.continueGame;
        restartGame = builder.opponentBreaksAgain;
        reBreak = builder.playerBreaksAgain;
        breakMiss = builder.breakMiss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TurnEndOptions that = (TurnEndOptions) o;

        if (safety != that.safety) return false;
        if (safetyMiss != that.safetyMiss) return false;
        if (miss != that.miss) return false;
        if (wonGame != that.wonGame) return false;
        if (lostGame != that.lostGame) return false;
        if (push != that.push) return false;
        if (skip != that.skip) return false;
        if (illegalBreak != that.illegalBreak) return false;
        if (scratch != that.scratch) return false;
        if (continueGame != that.continueGame) return false;
        if (restartGame != that.restartGame) return false;
        if (reBreak != that.reBreak) return false;
        if (breakMiss != that.breakMiss) return false;
        return defaultCheck == that.defaultCheck;

    }

    @Override
    public int hashCode() {
        int result = (safety ? 1 : 0);
        result = 31 * result + (safetyMiss ? 1 : 0);
        result = 31 * result + (miss ? 1 : 0);
        result = 31 * result + (wonGame ? 1 : 0);
        result = 31 * result + (lostGame ? 1 : 0);
        result = 31 * result + (push ? 1 : 0);
        result = 31 * result + (skip ? 1 : 0);
        result = 31 * result + (illegalBreak ? 1 : 0);
        result = 31 * result + (scratch ? 1 : 0);
        result = 31 * result + (continueGame ? 1 : 0);
        result = 31 * result + (restartGame ? 1 : 0);
        result = 31 * result + (reBreak ? 1 : 0);
        result = 31 * result + (breakMiss ? 1 : 0);
        result = 31 * result + defaultCheck.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TurnEndOptions{" +
                "safety=" + safety +
                "\n safetyMiss=" + safetyMiss +
                "\n miss=" + miss +
                "\n wonGame=" + wonGame +
                "\n lostGame=" + lostGame +
                "\n push=" + push +
                "\n skip=" + skip +
                "\n illegalBreak=" + illegalBreak +
                "\n scratch=" + scratch +
                "\n continueGame=" + continueGame +
                "\n opponentBreaksAgain=" + restartGame +
                "\n playerBreaksAgain=" + reBreak +
                "\n breakMiss=" + breakMiss +
                "\n defaultCheck=" + defaultCheck +
                '}';
    }

    public static class Builder {
        TurnEnd checked = TurnEnd.MISS;
        private boolean safety = false;
        private boolean safetyMiss = false;
        private boolean miss = false;
        private boolean wonGame = false;
        private boolean lostGame = false;
        private boolean push = false;
        private boolean skip = false;
        private boolean illegalBreak = false;
        private boolean continueGame = false;
        private boolean opponentBreaksAgain = false;
        private boolean playerBreaksAgain = false;
        private boolean breakMiss = false;
        private boolean scratch = false;


        public Builder() {
        }

        public Builder wonGame(boolean show) {
            wonGame = show;
            return this;
        }

        public Builder lostGame(boolean show) {
            lostGame = show;
            return this;
        }

        public Builder defaultOption(TurnEnd turnEnd) {
            checked = turnEnd;
            return this;
        }

        public Builder safety(boolean show) {
            safety = show;
            return this;
        }

        public Builder safetyError(boolean show) {
            safetyMiss = show;
            return this;
        }

        public Builder checkScratch(boolean checked) {
            scratch = checked;
            return this;
        }

        public Builder miss(boolean show) {
            miss = show;
            return this;
        }

        public Builder missOnBreak(boolean show) {
            breakMiss = show;
            return this;
        }

        public Builder illegalBreak(boolean show) {
            illegalBreak = show;
            return this;
        }

        public Builder push(boolean show) {
            push = show;
            return this;
        }

        public Builder skipTurn(boolean show) {
            skip = show;
            return this;
        }

        public Builder allowPlayerToChooseToContinueGame(boolean show) {
            playerBreaksAgain = true;
            continueGame = true;

            return this;
        }

        public Builder allowPlayerToChooseWhoBreaks(boolean show) {
            playerBreaksAgain = true;
            opponentBreaksAgain = true;
            continueGame = true;

            return this;
        }

        public TurnEndOptions build() {
            return new TurnEndOptions(this);
        }
    }
}
