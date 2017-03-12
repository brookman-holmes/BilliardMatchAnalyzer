package com.brookmanholmes.bma.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.brookmanholmes.bma.BuildConfig;
import com.brookmanholmes.bma.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import de.cketti.mailto.EmailIntentBuilder;

/**
 * Created by Brookman Holmes on 1/16/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public final static String ARG_MATCH_ID = "matchId";
    private final static String TAG = "BaseActivity";

    protected FirebaseAnalytics analytics;
    protected SharedPreferences preferences;
    protected FirebaseAuth auth;
    protected GoogleApiClient googleApiClient;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(BaseActivity.this);

        if (BuildConfig.DEBUG)
            analytics.setAnalyticsCollectionEnabled(false);

        preferences = getPreferences();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit_feedback) {
            EmailIntentBuilder.from(this)
                    .to("brookman.holmes@gmail.com")
                    .subject("Billiard Match Analyzer Feedback")
                    .start();
            return true;
        }

        if (item.getItemId() == R.id.action_sign_out) {
            auth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient);
            startActivity(new Intent(this, SignInActivity.class));
            return true;
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

    @ColorInt
    public int getColor(@ColorRes int color, int alpha) {
        return ColorUtils.setAlphaComponent(getColor2(color), alpha);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
