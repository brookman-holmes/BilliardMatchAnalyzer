package com.brookmanholmes.billiards.turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by helios on 3/18/2016.
 */
public class AdvStats {
    private String player;
    private String shotType, shotSubtype;
    private List<String> howTypes = new ArrayList<>();
    private List<String> whyTypes = new ArrayList<>();
    private List<String> angles = new ArrayList<>();

    private AdvStats(Builder builder) {
        this.shotType = builder.shotType;
        this.shotSubtype = builder.shotSubtype;
        howTypes.addAll(builder.howTypes);
        whyTypes.addAll(builder.whyTypes);
        angles.addAll(builder.angles);
    }

    public String getPlayer() {
        return player;
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

    public static class Builder {
        private String player;
        private String shotType = "", shotSubtype = "";
        private List<String> angles = new ArrayList<>();
        private List<String> howTypes = new ArrayList<>();
        private List<String> whyTypes = new ArrayList<>();


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

        @Override
        public String toString() {
            return "Builder{" +
                    "shotType='" + shotType + '\'' +
                    ", shotSubtype='" + shotSubtype + '\'' +
                    ", angles=" + angles +
                    ", howTypes=" + howTypes +
                    ", whyTypes=" + whyTypes +
                    '}';
        }
    }
}
