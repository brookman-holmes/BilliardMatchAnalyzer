package com.brookmanholmes.billiardmatchanalyzer.data;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

/**
 * Created by Brookman Holmes on 1/13/2016.
 */
public class MatchListLoader extends CursorLoader {
    public MatchListLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getContext());
        databaseAdapter.open();
        return databaseAdapter.getMatches();
    }
}
