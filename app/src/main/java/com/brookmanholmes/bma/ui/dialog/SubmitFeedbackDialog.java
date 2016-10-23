package com.brookmanholmes.bma.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.brookmanholmes.bma.BuildConfig;
import com.brookmanholmes.bma.R;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Brookman Holmes on 8/20/2016.
 */
public class SubmitFeedbackDialog extends DialogFragment {
    private InputMethodManager inputMethodManager;
    private EditText input;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_text, null, false);
        input = (EditText) view.findViewById(R.id.editText);
        input.setHint(getString(R.string.submit_feedback));
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        input.requestFocus();
        showKeyboard();
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == actionId) {
                    onPositiveButton();
                    hideKeyboard();
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        return builder.setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPositiveButton();
                        hideKeyboard();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideKeyboard();
                        dialog.cancel();
                    }
                })
                .create();
    }

    private void onPositiveButton() {
        if (!BuildConfig.DEBUG) {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, input.getText().toString());
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
        }
    }

    private void hideKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void showKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
