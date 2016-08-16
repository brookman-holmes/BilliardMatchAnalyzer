package com.brookmanholmes.bma.ui;

import android.support.v7.app.AppCompatActivity;

import com.brookmanholmes.bma.MyApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Brookman Holmes on 1/16/2016.
 */
public class BaseActivity extends AppCompatActivity {
    public final static String ARG_MATCH_ID = "matchId";

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }
}
