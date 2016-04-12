package com.brookmanholmes.billiardmatchanalyzer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import com.brookmanholmes.billiardmatchanalyzer.R;
import com.brookmanholmes.billiardmatchanalyzer.data.DatabaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.createMatch) FloatingActionButton fab;

    DatabaseAdapter db;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        db = new DatabaseAdapter(this);
        db.open();
    }

    @Override protected void onResume() {
        super.onResume();
        final int animationDelay = 250; // .25 seconds

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                fab.show();
            }
        }, animationDelay); // display fab after activity starts
    }

    @OnClick(R.id.createMatch) public void createNewMatch() {
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                Intent intent = new Intent(MainActivity.this, CreateNewMatchActivity.class);
                startActivity(intent);
            }
        });

    }
}
