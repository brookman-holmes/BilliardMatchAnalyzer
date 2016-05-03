package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchListLoader extends CursorLoader {
    public MatchListLoader(Context context) {
        super(context);
    }

    @Override public Cursor loadInBackground() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getContext());
        return databaseAdapter.getMatches();
    }
}
