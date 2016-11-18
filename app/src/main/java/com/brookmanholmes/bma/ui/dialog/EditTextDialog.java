package com.brookmanholmes.bma.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.brookmanholmes.bma.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 11/15/2016.
 */
public abstract class EditTextDialog extends DialogFragment {
    protected static final String ARG_TITLE = "arg title";
    protected static final String ARG_PRETEXT = "arg pretext";
    protected String title;
    protected String preText;
    @Bind(R.id.editText)
    protected EditText input;
    protected View view;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments().getString(ARG_TITLE);
        preText = getArguments().getString(ARG_PRETEXT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        view = createView();
        ButterKnife.bind(this, view);
        input.setText(preText);
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

        setupInput(input);
        input.setSelection(preText.length(), preText.length());

        return builder.setTitle(title)
                .setView(view)
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
                        dialog.cancel();
                    }
                })
                .create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        hideKeyboard();
        super.onCancel(dialog);
    }

    private void hideKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void showKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected abstract void setupInput(EditText input);

    protected abstract void onPositiveButton();

    protected View createView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.edit_text, null, false);
    }
}
