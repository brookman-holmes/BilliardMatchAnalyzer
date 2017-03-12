package com.brookmanholmes.bma.ui.matchinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;

import com.brookmanholmes.bma.data.DataSource;
import com.brookmanholmes.bma.data.FireDataSource;
import com.brookmanholmes.bma.ui.BaseActivity;
import com.brookmanholmes.bma.ui.dialog.EditTextDialog;

/**
 * Created by Brookman Holmes on 11/15/2016.
 */
public abstract class AbstractMatchEditTextDialog extends EditTextDialog {
    protected static final String ARG_MATCH_ID = BaseActivity.ARG_MATCH_ID;
    protected DataSource db;
    protected String matchId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matchId = getArguments().getString(ARG_MATCH_ID);
        db = new FireDataSource();
    }

    public static class EditMatchLocationDialog extends AbstractMatchEditTextDialog {
        public static AbstractMatchEditTextDialog create(String title, String preText, String matchId) {
            AbstractMatchEditTextDialog dialog = new EditMatchLocationDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putString(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            input.setText(getArguments().getString(ARG_PRETEXT, ""));
        }

        @Override
        protected void onPositiveButton() {
            db.updateMatchLocation(input.getText().toString(), matchId);
            if (getActivity() instanceof AbstractMatchActivity) {
                ((AbstractMatchActivity) getActivity()).match.setLocation(input.getText().toString());
            }
        }
    }

    public static class EditMatchNotesDialog extends AbstractMatchEditTextDialog {
        public static AbstractMatchEditTextDialog create(String title, String preText, String matchId) {
            AbstractMatchEditTextDialog dialog = new EditMatchNotesDialog();
            Bundle args = new Bundle();
            args.putString(ARG_PRETEXT, preText);
            args.putString(ARG_TITLE, title);
            args.putString(ARG_MATCH_ID, matchId);

            dialog.setArguments(args);
            return dialog;
        }

        @Override
        protected void setupInput(EditText input) {
            input.setText(getArguments().getString(ARG_PRETEXT, ""));
        }

        @Override
        protected void onPositiveButton() {
            db.updateMatchNotes(input.getText().toString(), matchId);
            if (getActivity() instanceof AbstractMatchActivity) {
                ((AbstractMatchActivity) getActivity()).match.setNotes(input.getText().toString());
            }
        }
    }
}
