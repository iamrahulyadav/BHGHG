package com.kandara.medicalapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
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
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;
import com.kandara.medicalapp.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abina on 5/1/2018.
 */

public class FlagDialog extends Dialog {


    RadioGroup reportType;
    EditText feedbackField;
    Button sendBtn;
    String questionId;
    CatLoadingView catLoadingView;
    AppCompatActivity activity;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    private QuestionGridSelectAdapter.OnQuestionSelected onQuestionSelected;

    public void setOnQuestionSelected(QuestionGridSelectAdapter.OnQuestionSelected onQuestionSelected) {
        this.onQuestionSelected = onQuestionSelected;
    }


    public FlagDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feedback_dialog);
        catLoadingView=new CatLoadingView();
        reportType=findViewById(R.id.reportType);
        feedbackField=findViewById(R.id.feedbackField);
        sendBtn=findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = reportType.getCheckedRadioButtonId();
                RadioButton selected =  findViewById(selectedId);
                postFeedback(questionId, selected.getText().toString(), feedbackField.getText().toString());
                dismiss();
            }
        });

    }

    private void postFeedback(String questionId, String reportType, String feedback) {
        JSONObject params = new JSONObject();

        catLoadingView.show(activity.getSupportFragmentManager(), "");
        try {
            params.put("questionId", questionId);
            params.put("topic", reportType);
            params.put("feedback", feedback);
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
        }
        String tag_string_req = "post_feedback";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.MAIN_URL+"/api/feedback", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Photo Post Reponse", response.toString());

                try {
                    boolean success = response.getBoolean("success");
                    if (success) {

                        Toast.makeText(activity, "Your feedback has been received", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Your feedback could not be posted!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("Json Error", e.getMessage() + "");
                }
                catLoadingView.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                catLoadingView.dismiss();
            }
        });
        MedicalApplication.getInstance().addToRequestQueue(jsonObjectRequest, tag_string_req);
    }

}
