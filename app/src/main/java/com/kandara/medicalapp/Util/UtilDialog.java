package com.kandara.medicalapp.Util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import com.kandara.medicalapp.Dialogs.TermsAndAgreementDialog;
import com.kandara.medicalapp.Dialogs.UpgradeInstructionDialog;
import com.kandara.medicalapp.R;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * Created by abina on 7/16/2018.
 */

public class UtilDialog {

    public static void showUpgradeDialog(final Activity activity){
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_trending_up_black_24dp)
                .setTitle("Premium User only")
                .setMessage("This feature is available only for the premium users. Upgrade now to get the following features\n1.\tFull content access. Free users can only use around 10% of the contents. \n" +
                        "2.\tAbility to download the contents for offline view. \n" +
                        "3.\tSearch option. \n")
                .setPositiveButton("Upgrade", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpgradeDescDialog(activity);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void showLimitReachedDialog(final Activity activity){
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_trending_up_black_24dp)
                .setTitle("Premium Only")
                .setMessage("You have reached the limit as a free user.  Upgrade now to get the most out of this app")
                .setPositiveButton("Upgrade", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpgradeDescDialog(activity);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void showUpgradeDescDialog(final Activity activity){

        TermsAndAgreementDialog termsAndAgreementDialog=new TermsAndAgreementDialog(activity);
        termsAndAgreementDialog.setActivity((AppCompatActivity) activity);
        termsAndAgreementDialog.show();

    }


    public static void showUpgradeInstruction(final Activity activity){
        UpgradeInstructionDialog upgradeInstructionDialog=new UpgradeInstructionDialog(activity);
        upgradeInstructionDialog.setActivity((AppCompatActivity) activity);
        upgradeInstructionDialog.show();

    }

    public static void showActualUpgradeDialog(final Activity activity){

        final String deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.prem)
                .setTitle("Upgrade Plan")
                
                .setMessage("Steps to upgrade\n Copy the following transaction code - "+deviceId+"\nGo to send money inside Esewa and send Rs. 1000 to esewa account 9852050660.\nInside send money menu, go to Remarks and send the message in following pattern \nTransaction code<space>Full Name<space>email id of the user.\nActivation for premium user may take upto 24 hours.\nYou will receive an email after activation.")
                .setPositiveButton("Copy to Clipboard", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("deviceid", deviceId);
                        clipboard.setPrimaryClip(clip);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



}
