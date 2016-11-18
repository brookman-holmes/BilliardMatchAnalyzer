package com.brookmanholmes.bma.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseActivity;

/**
 * Created by Brookman Holmes on 11/15/2016.
 */
public abstract class AbstractMatchEditTextDialog extends EditTextDialog {
    protected static final String ARG_MATCH_ID = BaseActivity.ARG_MATCH_ID;
    protected DatabaseAdapter db;
    protected Match match;
    protected long matchId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matchId = getArguments().getLong(ARG_MATCH_ID);
        db = new DatabaseAdapter(getContext());
        match = db.getMatch(matchId);
    }
}
