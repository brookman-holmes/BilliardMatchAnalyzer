package com.brookmanholmes.billiardmatchanalyzer.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.brookmanholmes.billiardmatchanalyzer.R;

import butterknife.OnClick;

import static com.brookmanholmes.billiardmatchanalyzer.utils.MatchDialogHelperUtils.getLayoutByGameType;

/**
 * Created by Brookman Holmes on 2/17/2016.
 */
public class BaseBallSelectionDialog extends BaseDialog {
    final DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
    final DialogInterface.OnClickListener negativeButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), getTheme());
        builder.setView(getLayoutByGameType(gameType))
                .setPositiveButton(android.R.string.ok, positiveButton)
                .setNegativeButton(android.R.string.cancel, negativeButton);
        return builder.create();
    }

    @OnClick(R.id.one_ball)
    public void onClick(View view) {
        
    }
}
