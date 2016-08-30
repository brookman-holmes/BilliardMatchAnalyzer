package com.brookmanholmes.bma.utils;

import android.content.SharedPreferences;

/**
 * Created by Brookman Holmes on 8/30/2016.
 */
public class PreferencesUtil {
    private PreferencesUtil(){}

    public static void resetTutorial(SharedPreferences preferences) {
        preferences.edit().putBoolean("first_run_tutorial_intro", true)
                .putBoolean("first_run_tutorial_info", true)
                .putBoolean("first_run_tutorial_create", true)
                .putBoolean("first_run_tutorial", true)
                .apply();

    }
}
