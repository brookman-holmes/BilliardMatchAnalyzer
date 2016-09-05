package com.brookmanholmes.billiards.turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by helios on 3/18/2016.
 * Data class for storing more advanced information for shooting. Provides options for storing what,
 * how and why a player missed
 */
// TODO: 9/4/2016 Convert a lot of this stuff to enums
public class AdvStats {
    private final String player;
    private final String shotType;
    private final String shotSubtype;
    private final List<String> howTypes = new ArrayList<>();
    private final List<String> whyTypes = new ArrayList<>();
    private final List<String> angles = new ArrayList<>();
    private final String startingPosition;
    private final boolean use;

    private AdvStats(Builder builder) {
        this.shotType = builder.shotType;
        this.shotSubtype = builder.shotSubtype;
        howTypes.addAll(builder.howTypes);
        whyTypes.addAll(builder.whyTypes);
        angles.addAll(builder.angles);
        this.startingPosition = builder.startingPosition;
        player = builder.player;
        use = builder.use;
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
    public List<String> getAngles() {
        return angles;
    }

    /**
     * The type of shot that was missed (cut shot, bank shot, jump shot, etc.)
     * @return A type of shot
     */
    public String getShotType() {
        return shotType;
    }

    /**
     * The sub type of the shot (back cut, rail cut, wing cut)
     * @return The sub type of the shot
     */
    public String getShotSubtype() {
        return shotSubtype;
    }

    /**
     * A list of how the shot was missed (over cut, under cut, miscue, etc.)
     * @return A list of how the shot was missed
     */
    public List<String> getHowTypes() {
        return howTypes;
    }

    /**
     * A list of why the shot was missed (too much outside english, too much draw, etc.)
     * @return A list of reasons why the shot was missed
     */
    public List<String> getWhyTypes() {
        return whyTypes;
    }

    @Override public String toString() {
        return "AdvStats{" +
                "player='" + player + '\'' +
                "\n shotType='" + shotType + '\'' +
                "\n shotSubtype='" + shotSubtype + '\'' +
                "\n howTypes=" + howTypes +
                "\n whyTypes=" + whyTypes +
                "\n angles=" + angles +
                "\n startingPosition='" + startingPosition + '\'' +
                "\n use=" + use +
                '}';
    }

    /**
     * Builder for creating a new AdvStats object
     */
    public static class Builder {
        private final List<String> angles = new ArrayList<>();
        private final List<String> howTypes = new ArrayList<>();
        private final List<String> whyTypes = new ArrayList<>();
        private String player;
        private String shotType = "", shotSubtype = "";
        private String startingPosition = "";
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
        public Builder shotType(String shotType) {
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
         * @param shotSubtype The subtype of shot for the player
         * @return An instance of this builder for chaining purposes
         */
        public Builder subType(String shotSubtype) {
            this.shotSubtype = shotSubtype;
            return this;
        }

        /**
         * Adds to the list of angles for this shot
         * @param angles An array of angles to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder angle(String... angles) {
            this.angles.addAll(Arrays.asList(angles));
            return this;
        }

        /**
         * Adds to the list of angles for this shot
         * @param angles A list of angles to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder angle(List<String> angles) {
            this.angles.addAll(angles);
            return this;
        }

        /**
         * Adds to the list of hows for how the shot was missed
         * @param hows A list of hows to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder howTypes(List<String> hows) {
            howTypes.addAll(hows);
            return this;
        }

        /**
         * Adds to the list of hows for how the shot was missed
         * @param hows An array of hows to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder howTypes(String... hows) {
            howTypes.addAll(Arrays.asList(hows));

            return this;
        }

        /**
         * Adds to the list of whys for why the shot was missed
         * @param whys A list of whys to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder whyTypes(List<String> whys) {
            whyTypes.addAll(whys);
            return this;
        }

        /**
         * Adds to the list of whys for why the shot was missed
         * @param whys An array of whys to add
         * @return An instance of this builder for chaining purposes
         */
        public Builder whyTypes(String... whys) {
            whyTypes.addAll(Arrays.asList(whys));

            return this;
        }

        /**
         * Clears the shot subtype (sets it to an empty string)
         */
        public void clearSubType() {
            shotSubtype = "";
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
            shotType = "";
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
                    "\n shotSubtype='" + shotSubtype + '\'' +
                    "\n angles=" + angles +
                    "\n howTypes=" + howTypes +
                    "\n whyTypes=" + whyTypes +
                    "\n startingPosition='" + startingPosition + '\'' +
                    "\n use=" + use +
                    '}';
        }
    }
}
