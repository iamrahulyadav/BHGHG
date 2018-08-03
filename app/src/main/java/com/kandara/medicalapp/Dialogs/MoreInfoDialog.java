package com.kandara.medicalapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.webkit.WebView;
import android.widget.GridView;

import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.HtmlCleaner;

import java.util.ArrayList;

/**
 * Created by abina on 5/1/2018.
 */

public class MoreInfoDialog extends Dialog {

    private WebView wbMoreInfo;
    private String moreInfo;

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    private QuestionGridSelectAdapter.OnQuestionSelected onQuestionSelected;

    public void setOnQuestionSelected(QuestionGridSelectAdapter.OnQuestionSelected onQuestionSelected) {
        this.onQuestionSelected = onQuestionSelected;
    }


    public MoreInfoDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.moreinfodialog);
        wbMoreInfo=findViewById(R.id.wbMoreInfo);
        String answer=moreInfo;
        if(answer.equalsIgnoreCase("nodesc")){
            answer="<p style=\"text-align: center;\"><span style=\"color: rgb(40, 50, 78);\">Explanation of this question will be available soon. </span></p>\n" +
                    "\n" +
                    "<p style=\"text-align: center;\">\n" +
                    "\t<br>\n" +
                    "</p>\n" +
                    "\n" +
                    "<p style=\"text-align: center;\"><strong>Stay Updated!</strong></p>";
        }
        answer= HtmlCleaner.cleanThis(answer);
        answer=answer.replaceAll("font-size: 10.0pt; ", "");
        answer="\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "*{\n" +
                "  font-size:15px;\n" +
                " font-family: Arial;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n"+answer+"</body>\n" +
                "</html>";
        wbMoreInfo.getSettings().setJavaScriptEnabled(true);
        wbMoreInfo.setBackgroundColor(Color.TRANSPARENT);
        wbMoreInfo.getSettings().setBuiltInZoomControls(true);

        wbMoreInfo.loadDataWithBaseURL("", answer, "text/html", "UTF-8", "");
    }

}
