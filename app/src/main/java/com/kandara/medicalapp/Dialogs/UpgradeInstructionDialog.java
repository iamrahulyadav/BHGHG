package com.kandara.medicalapp.Dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.HtmlCleaner;
import com.kandara.medicalapp.Util.UtilDialog;

/**
 * Created by abina on 5/1/2018.
 */

public class UpgradeInstructionDialog extends Dialog {


    Button sendBtn;
    AppCompatActivity activity;
    WebView wvTerms;
    CheckBox cbAgree;
    String terms = "<p style=\"text-indent: 0in;\"><strong>Payment Via Esewa/Khalti </strong></p>\n" +
            "<ol>\n" +
            "<li style=\"margin-left: .5in;\">Copy the following Android Device ID by clicking &ldquo;Copy to Clipboard&rdquo; below &ndash; [code]</li>\n" +
            "<li style=\"margin-left: .5in;\">Paste the Android Device ID somewhere else in your mobile. If you do not have esewa, you can message this code to your friends.</li>\n" +
            "<li style=\"margin-left: .5in;\">Open Esewa./ Khalti</li>\n" +
            "<li style=\"margin-left: .5in;\">Go to Send Money inside Esewa/Khalti</li>\n" +
            "<li style=\"margin-left: .5in;\">Send Rs 1000 to any one of the following Esewa accounts (if there is problem with one account, use another account for payment)</li>\n" +
            "</ol>\n" +
            "<ul>\n" +
            "<li style=\"margin-left: .75in;\">9855065200 (Prem Raj Sigdel)</li>\n" +
            "<li style=\"margin-left: .75in;\">9803865413 (Jyoti Sigdel)</li>\n" +
            "<li style=\"margin-left: .75in;\">9845441515 (Surendra Sigdel)</li>\n" +
            "<li style=\"margin-left: .75in;\">9860042374 (Bikesh Raj Bhandari)</li>\n" +
            "</ul>\n" +
            "<li style=\"margin-left: .5in;\"><span style=\"font-family: 'Arial',sans-serif;\"> Send Rs 1000 to following Khalti Account </span></li>\n" +
            "<p style=\"margin-left: .5in;\"><span style=\"font-family: 'Arial',sans-serif;\">9855065200 (Prem Raj Sigdel)</span></p>\n" +
            "<ol start=\"6\">\n" +
            "<li style=\"margin-left: .5in;\">Inside send money menu, go to Remarks.</li>\n" +
            "<li style=\"margin-left: .5in;\">Paste the Android Device ID here.</li>\n" +
            "<li style=\"margin-left: .5in;\">Complete the remarks message in the pattern - Android Device ID &lt;space&gt; Full Name of User &lt;space&gt;email id of the user.</li>\n" +
            "<li style=\"margin-left: .5in;\">Proceed for send money.</li>\n" +
            "<li style=\"margin-left: .5in;\">Activation for premium user may take upto 24 hours.</li>\n" +
            "<li style=\"margin-left: .5in;\">If any problem with activation, send the PDF of payment receipt to email&nbsp; smartpgnepal@gmail.com</li>\n" +
            "</ol>\n" +
            "<p><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "<p style=\"text-indent: 0in;\"><strong>Payment Via Bank </strong></p>\n" +
            "<p style=\"margin-left: .5in;\"><span style=\"font-family: 'Arial',sans-serif;\">Deposit Rs 1000 in Nabil Bank in account number 2410017511901, Account holders name &ndash; Prem Raj Sigdel</span></p>\n" +
            "<p style=\"margin-left: .5in;\"><span style=\"font-family: 'Arial',sans-serif;\">Send email to smartpgnepal@gmail.com by attaching the deposit slip picture and Sending message in following pattern - Android Device ID &lt;space&gt; Full Name of User &lt;space&gt;email id of the user.</span></p>\n" +
            "<p style=\"margin-left: .5in; text-indent: 0in;\">Activation for premium user may take upto 24 hours.</p>\n" +
            "<p style=\"margin-left: .5in;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>";

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public UpgradeInstructionDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.terms_dialog);
        TextView tvTitle=findViewById(R.id.title);
        tvTitle.setText("Upgrade Plan");
        terms=HtmlCleaner.cleanThis(terms);
        wvTerms = findViewById(R.id.wvterms);
        wvTerms.setBackgroundColor(Color.TRANSPARENT);
        wvTerms.loadDataWithBaseURL("", terms, "text/html", "UTF-8", "");
        cbAgree = findViewById(R.id.cbAgree);
        cbAgree.setVisibility(View.GONE);
        terms = HtmlCleaner.cleanThis(terms);
        sendBtn = findViewById(R.id.btnProceed);
        sendBtn.setText("Copy to Clipboard");
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Copied!", Toast.LENGTH_SHORT);

                final String deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("deviceid", deviceId);
                clipboard.setPrimaryClip(clip);
                dismiss();
            }
        });

    }


}
