package com.brookmanholmes.bma.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.brookmanholmes.bma.R;

/**
 * Created by Brookman Holmes on 3/30/2016.
 */
public class HelpDialogCreator {
    private final Context context;
    private final AlertDialog.Builder builder;
    private final String playerName;

    public HelpDialogCreator(Context context, String pageTitle, String playerName) {
        this.context = context;
        this.playerName = playerName;
        builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setView(LayoutInflater.from(context).inflate(getRes(pageTitle), null))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public AlertDialog create() {
        return builder.create();
    }

    @LayoutRes private int getRes(String pageTitle) {
        if (pageTitle.equals(getAnglePageTitle()))
            return R.layout.dialog_help_angle;
        else if (pageTitle.equals(getBankPageTitle()))
            return R.layout.dialog_help_bank;
        else if (pageTitle.equals(getBreakPageTitle()))
            return R.layout.dialog_help_break;
        else if (pageTitle.equals(getCutTypePageTitle()))
            return R.layout.dialog_help_cut;
        else if (pageTitle.equals(getFoulPageTitle()))
            return R.layout.dialog_help_foul;
        else if (pageTitle.equals(getHowMissPageTitle()))
            return R.layout.dialog_help_how_miss;
        else if (pageTitle.equals(getKickPageTitle()))
            return R.layout.dialog_help_kick;
        else if (pageTitle.equals(getSafetyPageTitle()))
            return R.layout.dialog_help_safety;
        else if (pageTitle.equals(getShotPageTitle()))
            return R.layout.dialog_help_shot;
        else if (pageTitle.equals(getTurnEndPageTitle()))
            return R.layout.dialog_help_turn_end;
        else if (pageTitle.equals(getWhatMissPageTitle()))
            return R.layout.dialog_help_what_miss;
        else if (pageTitle.equals(getWhyMissPageTitle()))
            return R.layout.dialog_help_why_miss;
        else
            throw new IllegalArgumentException("Invalid page title: " + pageTitle);
    }

    private String getBreakPageTitle() {
        return context.getString(R.string.title_break, playerName);
    }

    private String getShotPageTitle() {
        return context.getString(R.string.title_shot, playerName);
    }

    private String getTurnEndPageTitle() {
        return context.getString(R.string.title_turn_end, playerName);
    }

    private String getFoulPageTitle() {
        return context.getString(R.string.title_foul, playerName);
    }

    private String getHowMissPageTitle() {
        return context.getString(R.string.title_how_miss, playerName);
    }

    private String getWhatMissPageTitle() {
        return context.getString(R.string.title_miss, playerName);
    }

    private String getWhyMissPageTitle() {
        return context.getString(R.string.title_why_miss, playerName);
    }

    private String getCutTypePageTitle() {
        return context.getString(R.string.title_cut_type, playerName);
    }

    private String getAnglePageTitle() {
        return context.getString(R.string.title_angle, playerName);
    }

    private String getBankPageTitle() {
        return context.getString(R.string.title_bank, playerName);
    }

    private String getKickPageTitle() {
        return context.getString(R.string.title_kick, playerName);
    }

    private String getSafetyPageTitle() {
        return context.getString(R.string.title_safety, playerName);
    }
}
