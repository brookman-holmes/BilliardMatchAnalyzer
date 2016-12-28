package com.brookmanholmes.billiards.turn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by helios on 3/18/2016.
 * Data class for storing more advanced information for shooting. Provides options for storing what,
 * how and why a player missed
 */
public class AdvStats implements Serializable {
    private final String player;
    private final ShotType shotType;
    private final SubType shotSubtype;
    private final List<HowType> howTypes = new ArrayList<>();
    private final List<WhyType> whyTypes = new ArrayList<>();
    private final List<Angle> angles = new ArrayList<>();
    private final float cbToOb;
    private final float obToPocket;
    private final int speed;
    private final int cueX;
    private final int cueY;
    private final String startingPosition;
    private final boolean use;

    private AdvStats(Builder builder) {
        this.shotType = builder.shotType;
        this.shotSubtype = builder.subType;
        howTypes.addAll(builder.howTypes);
        whyTypes.addAll(builder.whyTypes);
        angles.addAll(builder.angles);
        this.startingPosition = builder.startingPosition;
        player = builder.player;
        use = builder.use;
        cbToOb = builder.cbToOb;
        obToPocket = builder.obToPocket;
        speed = builder.speed;
        cueX = builder.cueX;
        cueY = builder.cueY;
    }

    /**
     * Tells you if this AdvStats information is applicable to the turn or not (it may not be
     * if say the player won the game and has no information to store about their misses because
     * they didn't miss)
     * @return True if this AdvStats data should be used, false otherwise
     */
    public boolean use() {
        return use;
    }

    /**
     * Returns the name of the player this data belongs to
     * @return The name of the player
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Returns the starting position for this player (generally this would be whether they were
     * left an open shot or not)
     * @return The starting position for this advanced data
     */
    public String getStartingPosition() {
        return startingPosition;
    }

    /**
     * A list of angles for this shot (generally just 5 degree, or multiple items like natural,
     * 2 rail, long rail)
     * @return A list of angles for this shot
     */
    public List<Angle> getAngles() {
        return angles;
    }

    /**
     * The type of shot that was missed (cut shot, bank shot, jump shot, etc.)
     * @return A type of shot
     */
    public ShotType getShotType() {
        return shotType;
    }

    /**
     * The sub type of the shot (back cut, rail cut, wing cut)
     * @return The sub type of the shot
     */
    public SubType getShotSubtype() {
        return shotSubtype;
    }

    /**
     * A list of how the shot was missed (over cut, under cut, miscue, etc.)
     * @return A list of how the shot was missed
     */
    public List<HowType> getHowTypes() {
        return howTypes;
    }

    /**
     * A list of why the shot was missed (too much outside english, too much draw, etc.)
     * @return A list of reasons why the shot was missed
     */
    public List<WhyType> getWhyTypes() {
        return whyTypes;
    }

    /**
     * Distance from the cue ball to the object ball
     *
     * @return the distance from the cue ball to the object ball
     */
    public float getCbToOb() {
        return cbToOb;
    }

    /**
     * Distance from the object ball to the pocket
     *
     * @return the distance from the object ball to the pocket
     */
    public float getObToPocket() {
        return obToPocket;
    }

    /**
     * The speed of the shot taken (should be on a scale of 1 - 10)
     *
     * @return the speed of the shot taken
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * The x-axis position of the hit from the cue to the cue ball, 0 being the left most and 100
     * being the right most
     *
     * @return integer between 0 and 100, 0 (left) and 100 (right)
     */
    public int getCueX() {
        return cueX;
    }

    /**
     * The x-axis position of the hit from the cue to the cue ball, 0 being the top most and 100
     * being the bottom most
     *
     * @return integer between 0 and 100, 0 (top) and 100 (bottom)
     */
    public int getCueY() {
        return cueY;
    }

    /**
     * Returns whether the cueing is valid for this advanced stat
     *
     * @return true if the cueing is valid, false otherwise
     */
    public boolean isCueingValid() {
        return cueY > -200 && cueX > -200;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdvStats advStats = (AdvStats) o;

        if (Float.compare(advStats.cbToOb, cbToOb) != 0) return false;
        if (Float.compare(advStats.obToPocket, obToPocket) != 0) return false;
        if (speed != advStats.speed) return false;
        if (cueX != advStats.cueX) return false;
        if (cueY != advStats.cueY) return false;
        if (use != advStats.use) return false;
        if (!player.equals(advStats.player)) return false;
        if (shotType != advStats.shotType) return false;
        if (shotSubtype != advStats.shotSubtype) return false;
        if (!howTypes.equals(advStats.howTypes)) return false;
        if (!whyTypes.equals(advStats.whyTypes)) return false;
        if (!angles.equals(advStats.angles)) return false;
        return startingPosition.equals(advStats.startingPosition);

    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + shotType.hashCode();
        result = 31 * result + shotSubtype.hashCode();
        result = 31 * result + howTypes.hashCode();
        result = 31 * result + whyTypes.hashCode();
        result = 31 * result + angles.hashCode();
        result = 31 * result + (cbToOb != +0.0f ? Float.floatToIntBits(cbToOb) : 0);
        result = 31 * result + (obToPocket != +0.0f ? Float.floatToIntBits(obToPocket) : 0);
        result = 31 * result + speed;
        result = 31 * result + cueX;
        result = 31 * result + cueY;
        result = 31 * result + startingPosition.hashCode();
        result = 31 * result + (use ? 1 : 0);
        return result;
    }

    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
    }

    @Override
    public String toString() {
        return "AdvStats{" +
                "player='" + player + '\'' +
                ", shotType=" + shotType +
                ", shotSubtype=" + shotSubtype +
                ", howTypes=" + howTypes +
                ", whyTypes=" + whyTypes +
                ", angles=" + angles +
                ", cbToOb=" + cbToOb +
                ", obToPocket=" + obToPocket +
                ", speed=" + speed +
                ", cueX=" + cueX +
                ", cueY=" + cueY +
                ", startingPosition='" + startingPosition + '\'' +
                ", use=" + use +
                '}';
    }

    /**
     * Created by Brookman Holmes on 9/5/2016.
     */
    public enum Angle {
        ONE_RAIL,
        TWO_RAIL,
        THREE_RAIL,
        FOUR_RAIL,
        FIVE_RAIL,
        NATURAL,
        CROSSOVER,
        LONG_RAIL,
        SHORT_RAIL,
        FIVE,
        TEN,
        FIFTEEN,
        TWENTY,
        TWENTY_FIVE,
        THIRTY,
        THIRTY_FIVE,
        FORTY,
        FORTY_FIVE,
        FIFTY,
        FIFTY_FIVE,
        SIXTY,
        SIXTY_FIVE,
        SEVENTY,
        SEVENTY_FIVE,
        EIGHTY,
        SHALLOW,
        MEDIUM,
        STEEP
    }

    /**
     * Created by Brookman Holmes on 9/5/2016.
     */
    public enum ShotType {
        NONE,
        CUT,
        STRAIGHT_SHOT,
        BANK,
        KICK,
        COMBO,
        CAROM,
        JUMP,
        MASSE,
        SAFETY,
        SAFETY_ERROR,
        BREAK_SHOT;

        public static String[] getShots() {
            return new String[]{
                    NONE.name(),
                    CUT.name(),
                    STRAIGHT_SHOT.name(),
                    BANK.name(),
                    KICK.name(),
                    COMBO.name(),
                    CAROM.name(),
                    JUMP.name(),
                    MASSE.name()};
        }

        public static String[] getSafeties() {
            return new String[]{
                    SAFETY.name(),
                    SAFETY_ERROR.name()};
        }

        public static String[] getBreaks() {
            return new String[]{
                    BREAK_SHOT.name()};
        }
    }

    public enum HowType {
        MISCUE,
        TOO_HARD,
        TOO_SOFT,
        AIM_LEFT,
        AIM_RIGHT,
        KICKED_IN,
        THIN,
        THICK,
        KICK_LONG,
        KICK_SHORT,
        BANK_LONG,
        BANK_SHORT,
        CURVE_EARLY,
        CURVE_LATE
    }

    public enum WhyType {
        POSITION,
        LACK_FOCUS,
        JACK_UP,
        ENGLISH,
        TOO_SOFT,
        TOO_HARD,
        CURVED,
        RAIL,
        FORCING,
        FORCING_II,
        TOO_LITTLE_FOLLOW,
        TOO_LITTLE_DRAW,
        TOO_LITTLE_INSIDE,
        TOO_LITTLE_OUTSIDE,
        TOO_MUCH_FOLLOW,
        TOO_MUCH_DRAW,
        TOO_MUCH_INSIDE,
        TOO_MUCH_OUTSIDE,
        TABLE,
        MECHANICS,
        IMPEDING_BALL,
        MISJUDGED,
        PATTERN
    }

    public enum SubType {
        FULL_HOOK,
        PARTIAL_HOOK,
        LONG_T,
        SHORT_T,
        NO_DIRECT_SHOT,
        OPEN,
        WING_CUT,
        BACK_CUT,
        RAIL_CUT,
        NONE
    }

    /**
     * Builder for creating a new AdvStats object
     */
    public static class Builder {
        private final List<Angle> angles = new ArrayList<>();
        private final List<HowType> howTypes = new ArrayList<>();
        private final List<WhyType> whyTypes = new ArrayList<>();
        private String player;
        private ShotType shotType = ShotType.NONE;
        private SubType subType = SubType.NONE;
        private String startingPosition = "";
        // set these to -1 as default because that will signify that it's not to be used
        private float cbToOb = -1f;
        private float obToPocket = -1f;
        private int speed = -1;
        private int cueX = -200;
        private int cueY = -200;
        private boolean use;


        /**
         * Creates a new builder object with the player name set
         * @param player The name of the player for this AdvStats builder
         */
        public Builder(String player) {
            this.player = player;
        }

        /**
         * Creates a new empty builder
         */
        public Builder() {}

        /**
         * Sets the name of the player
         * @param name The name of the player for this AdvStats builder
         * @return An instance of this builder for chaining purposes
         */
        public Builder name(String name) {
            player = name;
            return this;
        }

        /**
         * Sets the type of shot
         * @param shotType The type of shot that was missed
         * @return  An instance of this builder for chaining purposes
         */
        public Builder shotType(ShotType shotType) {
            this.shotType = shotType;
            return this;
        }

        /**
         * Sets whether this data should be used for storing information
         * @param use True if this data should be used, false otherwise
         * @return An instance of this builder for chaining purposes
         */
        public Builder use(boolean use) {
            this.use = use;
            return this;
        }

        /**
         * The starting position for the player (open, safetied)
         * @param startingPosition The starting position for the player
         * @return An instance of this builder for chaining purposes
         */
        public Builder startingPosition(String startingPosition) {
            this.startingPosition = startingPosition;
            return this;
        }

        /**
         * The subtype of shot for the player (rail cut, wing cut, etc.)
         * @param subType The subtype of shot for the player
         * @return An instance of this builder for chaining purposes
         */
        public Builder subType(SubType subType) {
            this.subType = subType;
            return this;
        }

        /**
         * Adds to the list of angles for this shot
         * @param angles An array of angles to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder angle(Angle... angles) {
            this.angles.addAll(Arrays.asList(angles));
            return this;
        }

        /**
         * Adds to the list of angles for this shot
         * @param angles A list of angles to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder angle(Collection<Angle> angles) {
            this.angles.addAll(angles);
            return this;
        }

        /**
         * Adds to the list of hows for how the shot was missed
         * @param hows A list of hows to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder howTypes(Collection<HowType> hows) {
            howTypes.addAll(hows);
            return this;
        }

        /**
         * Adds to the list of hows for how the shot was missed
         * @param hows An array of hows to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder howTypes(HowType... hows) {
            howTypes.addAll(Arrays.asList(hows));

            return this;
        }

        /**
         * The x, y coordinates of the cueing on the cue ball
         *
         * @param x value between 0 and 100, 0 being left most, 100 being right most
         * @param y value between 0 and 100, 0 being top most, 100 being bottom most
         * @return An instance of this builder for chaining purposes
         */
        public Builder cueing(int x, int y) {
            cueX = x;
            cueY = y;
            return this;
        }

        /**
         * The speed of the shot, 0 being the slowest, 10 being the hardest hit possible
         *
         * @param speed The speed of the shot, 0 being the slowest, 10 being the hardest hit possible
         * @return An instance of this builder for chaining purposes
         */
        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }

        /**
         * The distance from the cue ball to the object ball
         *
         * @param dist float value (probably should be in feet?)
         * @return An instance of this builder for chaining purposes
         */
        public Builder cbDistance(float dist) {
            this.cbToOb = dist;
            return this;
        }

        /**
         * The distance from the object ball to the pocket
         *
         * @param dist float value (probably should be in feet?)
         * @return An instance of this builder for chaining purposes
         */
        public Builder obDistance(float dist) {
            this.obToPocket = dist;
            return this;
        }

        /**
         * Adds to the list of whys for why the shot was missed
         * @param whys A list of whys to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder whyTypes(Collection<WhyType> whys) {
            whyTypes.addAll(whys);
            return this;
        }

        /**
         * Adds to the list of whys for why the shot was missed
         * @param whys An array of whys to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder whyTypes(WhyType... whys) {
            whyTypes.addAll(Arrays.asList(whys));

            return this;
        }

        /**
         * Clears the shot subtype (sets it to an empty string)
         */
        public void clearSubType() {
            subType = SubType.NONE;
        }

        /**
         * Clears the list of how the shot was missed
         */
        public void clearHowTypes() {
            howTypes.clear();
        }

        /**
         * Clears the list of why the shot was missed
         */
        public void clearWhyTypes() {
            whyTypes.clear();
        }

        /**
         * Clears the shot type (sets it to an empty string)
         */
        public void clearShotType() {
            shotType = ShotType.NONE;
        }

        /**
         * Clears the list of angles
         */
        public void clearAngle() {
            angles.clear();
        }

        /**
         * Creates a new AdvStats object with the supplied arguments of this builder
         * @return A new AdvStats object with the supplised arguments of this builder
         */
        public AdvStats build() {
            return new AdvStats(this);
        }

        @Override public String toString() {
            return "Builder{" +
                    "player='" + player + '\'' +
                    "\n shotType='" + shotType + '\'' +
                    "\n shotSubtype='" + subType + '\'' +
                    "\n angles=" + angles +
                    "\n howTypes=" + howTypes +
                    "\n whyTypes=" + whyTypes +
                    "\n startingPosition='" + startingPosition + '\'' +
                    "\n use=" + use +
                    '}';
        }
    }
}
