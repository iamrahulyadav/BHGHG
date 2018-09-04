package com.kandara.medicalapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.HtmlCleaner;
import com.kandara.medicalapp.Util.UtilDialog;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abina on 5/1/2018.
 */

public class TermsAndAgreementDialog extends Dialog {


    Button sendBtn;
    AppCompatActivity activity;
    WebView wvTerms;
    CheckBox cbAgree;
    String terms="<h2>Smart PG Premium User Agreement</h2>\n" +
            "<h4>Premium users should be aware of the following provisions</h4>\n" +
            "<ol>\n" +
            "<li>Premium users should renew their access yearly.</li>\n" +
            "<li>Access is given to only one device after payment. A different device will need separate premium access even in the same user.</li>\n" +
            "<li>Maintenance schedules will be on a case-by-case basis.</li>\n" +
            "</ol>\n" +
            "<h4>Agreement Terms</h4>\n" +
            "<ol>\n" +
            "<li>The signatures below indicate that this agreement has been read and is understood, and represents the proper Technical Support scope of services.</li>\n" +
            "<li>Any modification or termination of this agreement will require appropriate review and approval by both parties.</li>\n" +
            "<li>This agreement will be reviewed annually at the beginning of each year to verify its currency and accuracy.</li>\n" +
            "<li>Any input, questions or concerns regarding this agreement should be brought to Smart PG team.</li>\n" +
            "</ol>\n" +
            "<h4>Approval</h4>\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">The undersigned hereby understand and agree to the terms of this agreement.</span></p>\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "<table style=\"border-collapse: collapse; border: none;\">\n" +
            "<tbody>\n" +
            "<tr style=\"height: .3in;\">\n" +
            "<td style=\"width: 270.9pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" colspan=\"3\" width=\"348\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">[Department name]</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 13.5pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"18\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 3.7in; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" colspan=\"3\" width=\"342\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">[Company name]</span></p>\n" +
            "</td>\n" +
            "</tr>\n" +
            "<tr style=\"height: .3in;\">\n" +
            "<td style=\"width: 110.15pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" colspan=\"2\" width=\"143\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">Name of Signatory:</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 160.75pt; border: none; border-bottom: solid windowtext 1.0pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"205\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 13.5pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"18\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 1.75in; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" colspan=\"2\" width=\"163\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">Name of Signatory:</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 1.95in; border: none; border-bottom: solid windowtext 1.0pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"179\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "</tr>\n" +
            "<tr style=\"height: .3in;\">\n" +
            "<td style=\"width: 41.4pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"55\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">Date:</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 229.5pt; border: none; border-bottom: solid windowtext 1.0pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" colspan=\"2\" width=\"293\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 13.5pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"18\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 58.5pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" width=\"78\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">Date:</span></p>\n" +
            "</td>\n" +
            "<td style=\"width: 207.9pt; border: none; border-bottom: solid windowtext 1.0pt; padding: 0in 5.4pt 0in 5.4pt; height: .3in;\" colspan=\"2\" width=\"264\">\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>\n" +
            "</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td style=\"border: none;\" width=\"55\">&nbsp;</td>\n" +
            "<td style=\"border: none;\" width=\"88\">&nbsp;</td>\n" +
            "<td style=\"border: none;\" width=\"205\">&nbsp;</td>\n" +
            "<td style=\"border: none;\" width=\"18\">&nbsp;</td>\n" +
            "<td style=\"border: none;\" width=\"78\">&nbsp;</td>\n" +
            "<td style=\"border: none;\" width=\"86\">&nbsp;</td>\n" +
            "<td style=\"border: none;\" width=\"179\">&nbsp;</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "<p style=\"margin-bottom: .0001pt; line-height: normal;\"><span style=\"font-family: 'Arial',sans-serif;\">&nbsp;</span></p>";

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public TermsAndAgreementDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.terms_dialog);
        TextView tvTitle=findViewById(R.id.title);
        tvTitle.setText("Terms and Condition");
        wvTerms=findViewById(R.id.wvterms);
        wvTerms.setBackgroundColor(Color.TRANSPARENT);
        wvTerms.loadDataWithBaseURL("", terms, "text/html", "UTF-8", "");
        cbAgree=findViewById(R.id.cbAgree);
        terms= HtmlCleaner.cleanThis(terms);
        sendBtn=findViewById(R.id.btnProceed);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbAgree.isChecked()){
                    UtilDialog.showUpgradeInstruction(activity);
                    dismiss();
                }else{
                    Toast.makeText(activity, "Please agree terms and conditions to proceed", Toast.LENGTH_SHORT);
                }
            }
        });

    }


}
