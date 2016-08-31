package com.brookmanholmes.bma.utils;

import android.app.Activity;
import android.content.Context;
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
                .putBoolean("first_run_tutorial_add_turn_balls", true)
                .apply();

    }

    public static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getSharedPreferences("com.brookmanholmes.bma", Context.MODE_PRIVATE);
    }
}
