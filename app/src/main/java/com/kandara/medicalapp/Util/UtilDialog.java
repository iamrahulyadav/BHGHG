package com.kandara.medicalapp.Util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;

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
                .setTitle("Premium Only")
                .setMessage("This feature is available only for the premium users. Upgrade now to get the most out of this app")
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

        final String deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.prem)
                .setTitle("Upgrade Plan")
                .setMessage("Upgrade now to get the most out of this app\n Send Rs. 1000 to this eSewa account \n9816671050. In the transaction message send the following code\n"+deviceId+".\n\n Tap 'Copy to clipboard' button to copy the code and paste it somewhere safe. You need this code to make the transaction successful.")
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
