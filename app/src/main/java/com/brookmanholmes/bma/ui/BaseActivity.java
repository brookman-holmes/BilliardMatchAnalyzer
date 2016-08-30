package com.brookmanholmes.bma.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
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

    protected FirebaseAnalytics firebaseAnalytics;
    protected SharedPreferences preferences;
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        preferences = getPreferences();
    }

    @Override public void onDestroy() {
        Log.i(TAG, "onDestroy called");
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu called");
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected called");
        if (item.getItemId() == R.id.action_submit_feedback) {
            EmailIntentBuilder.from(this)
                    .to("brookman.holmes@gmail.com")
                    .subject("Billiard Match Analyzer Feedback")
                    .start();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onPostResume() {
        Log.i(TAG, "onPostResume called");
        super.onPostResume();
    }

    @Override protected void onStart() {
        Log.i(TAG, "onStart called");
        super.onStart();
    }

    @Override protected void onStop() {
        super.onStop();
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override protected void onResume() {
        Log.i(TAG, "onResume called");
        super.onResume();
    }

    @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onPostCreate called");
        super.onPostCreate(savedInstanceState);
    }

    protected SharedPreferences getPreferences() {
        return getSharedPreferences("com.brookmanholmes.bma", MODE_PRIVATE);
    }
}
