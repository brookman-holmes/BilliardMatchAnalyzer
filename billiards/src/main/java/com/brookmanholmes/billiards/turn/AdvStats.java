package com.brookmanholmes.billiards.turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by helios on 3/18/2016.
 */
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

    public boolean use() {
        return use;
    }

    public String getPlayer() {
        return player;
    }

    public String getStartingPosition() {
        return startingPosition;
    }

    public List<String> getAngles() {
        return angles;
    }

    public String getShotType() {
        return shotType;
    }

    public String getShotSubtype() {
        return shotSubtype;
    }

    public List<String> getHowTypes() {
        return howTypes;
    }

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

    public static class Builder {
        private final List<String> angles = new ArrayList<>();
        private final List<String> howTypes = new ArrayList<>();
        private final List<String> whyTypes = new ArrayList<>();
        private String player;
        private String shotType = "", shotSubtype = "";
        private String startingPosition = "";
        private boolean use;


        public Builder(String player) {
            this.player = player;
        }

        public Builder() {}

        public Builder name(String name) {
            player = name;
            return this;
        }

        public Builder shotType(String shotType) {
            this.shotType = shotType;
            return this;
        }

        public Builder use(boolean use) {
            this.use = use;
            return this;
        }

        public Builder startingPosition(String startingPosition) {
            this.startingPosition = startingPosition;
            return this;
        }

        public Builder subType(String shotSubtype) {
            this.shotSubtype = shotSubtype;
            return this;
        }

        public Builder angle(String... angles) {
            this.angles.addAll(Arrays.asList(angles));
            return this;
        }

        public Builder angle(List<String> angles) {
            this.angles.addAll(angles);
            return this;
        }

        public Builder howTypes(List<String> hows) {
            howTypes.addAll(hows);
            return this;
        }

        public Builder howTypes(String... hows) {
            howTypes.addAll(Arrays.asList(hows));

            return this;
        }

        public Builder whyTypes(List<String> whys) {
            whyTypes.addAll(whys);
            return this;
        }

        public Builder whyTypes(String... whys) {
            whyTypes.addAll(Arrays.asList(whys));

            return this;
        }

        public void clearSubType() {
            shotSubtype = "";
        }

        public void clearHowTypes() {
            howTypes.clear();
        }

        public void clearWhyTypes() {
            whyTypes.clear();
        }

        public void clearShotType() {
            shotType = "";
        }

        public void clearAngle() {
            angles.clear();
        }

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
