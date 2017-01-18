package com.brookmanholmes.bma.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.brookmanholmes.bma.BuildConfig;
import com.brookmanholmes.bma.MyApplication;
import com.brookmanholmes.bma.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(BaseActivity.this);

        if (BuildConfig.DEBUG)
            analytics.setAnalyticsCollectionEnabled(false);

        preferences = getPreferences();
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null)
                    Log.d(TAG, "onAuthStateChanged: " + user.getUid());
                else
                    Log.d(TAG, "onAuthStateChanged: user not signed in");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
            auth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onDestroy() {
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit_feedback) {
            EmailIntentBuilder.from(this)
                    .to("brookman.holmes@gmail.com")
                    .subject("Billiard Match Analyzer Feedback")
                    .start();
        }

        return super.onOptionsItemSelected(item);
    }

    public SharedPreferences getPreferences() {
        return getSharedPreferences("com.brookmanholmes.bma", MODE_PRIVATE);
    }

    @ColorInt
    public int getColor2(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

}
