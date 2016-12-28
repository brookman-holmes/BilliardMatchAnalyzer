package com.brookmanholmes.bma.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;

import com.brookmanholmes.billiards.match.Match;
import com.brookmanholmes.bma.data.DatabaseAdapter;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.matchinfo.UpdatesPlayerNames;

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

    public static class EditPlayerNameDialog extends AbstractMatchEditTextDialog {

        public static AbstractMatchEditTextDialog create(String title, String preText, long matchId) {
            AbstractMatchEditTextDialog dialog = new EditPlayerNameDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }

        @Override
        protected void onPositiveButton() {
            ((UpdatesPlayerNames) getActivity()).updatePlayerName(preText, input.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.editPlayerName(matchId, preText, input.getText().toString());
                }
            }).start();
        }

    }

    public static class EditMatchLocationDialog extends AbstractMatchEditTextDialog {
        public static AbstractMatchEditTextDialog create(String title, String preText, long matchId) {
            AbstractMatchEditTextDialog dialog = new EditMatchLocationDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        }

        @Override
        protected void onPositiveButton() {
            match.setLocation(input.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.updateMatchLocation(input.getText().toString(), matchId);
                }
            }).start();
        }
    }

    public static class EditMatchNotesDialog extends AbstractMatchEditTextDialog {
        public static AbstractMatchEditTextDialog create(String title, String preText, long matchId) {
            AbstractMatchEditTextDialog dialog = new EditMatchNotesDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putLong(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {

        }

        @Override
        protected void onPositiveButton() {
            match.setNotes(input.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.updateMatchNotes(input.getText().toString(), matchId);
                }
            });
        }
    }
}
