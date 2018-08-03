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
    String terms="<h1>Smart PG Premium User Agreement</h1>\n" +
            "<h2>Services Offered in Premium use:</h2>\n" +
            "<ol>\n" +
            "<li>Full content access.</li>\n" +
            "<li>Ability to download the contents for offline view.</li>\n" +
            "<li>Search option.</li>\n" +
            "</ol>\n" +
            "<h2>Premium users should be aware of the following provisions</h2>\n" +
            "<ol>\n" +
            "<li>Premium users should renew their access yearly.</li>\n" +
            "<li>Access is given to only one device after payment. Each different device will need separate premium access even in a same user.</li>\n" +
            "<li>Maintenance schedules will be on a case-by-case basis.</li>\n" +
            "</ol>\n" +
            "<h2>Agreement Terms</h2>\n" +
            "<ol>\n" +
            "<li>The signatures below indicate that this agreement has been read and is understood, and represents the proper Technical Support scope of services.</li>\n" +
            "<li>Any modification or termination of this agreement will require appropriate review and approval by both parties.</li>\n" +
            "<li>This agreement will be reviewed annually at the beginning of each year to verify its currency and accuracy.</li>\n" +
            "<li>Any input, questions or concerns regarding this agreement should be brought to Smart PG team.</li>\n" +
            "</ol>";

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
                    UtilDialog.showActualUpgradeDialog(activity);
                    dismiss();
                }else{
                    Toast.makeText(activity, "Please agree terms and conditions to proceed", Toast.LENGTH_SHORT);
                }
            }
        });

    }


}
