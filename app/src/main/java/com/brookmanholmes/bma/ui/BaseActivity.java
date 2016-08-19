package com.brookmanholmes.bma.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.brookmanholmes.bma.MyApplication;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Brookman Holmes on 1/16/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public final static String ARG_MATCH_ID = "matchId";
    protected FirebaseAnalytics firebaseAnalytics;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }
}
