package com.brookmanholmes.bma.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.leakcanary.RefWatcher;

import de.cketti.mailto.EmailIntentBuilder;

/**
 * Created by Brookman Holmes on 1/16/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public final static String ARG_MATCH_ID = "matchId";
    private final static String TAG = "BaseActivity";

    protected FirebaseAnalytics analytics;
    protected SharedPreferences preferences;
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(BaseActivity.this);
        preferences = getPreferences();
    }

    @Override public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit_feedback) {
            EmailIntentBuilder.from(this)
                    .to("brookman.holmes@gmail.com")
                    .subject("Billiard Match Analyzer Feedback")
                    .start();
        }

        return super.onOptionsItemSelected(item);
    }

    protected SharedPreferences getPreferences() {
        return getSharedPreferences("com.brookmanholmes.bma", MODE_PRIVATE);
    }
}
