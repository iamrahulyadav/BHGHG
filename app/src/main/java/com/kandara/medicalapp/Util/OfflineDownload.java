package com.kandara.medicalapp.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.Study;
import com.orm.SugarRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class OfflineDownload {


    public static void downloadChapter(final Activity context, final ArrayList<String> selectedSubTopics) {


        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Download content...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);

        String query = "";
        for (int i = 0; i < selectedSubTopics.size(); i++) {
            if (i != selectedSubTopics.size() - 1) {
                query += "subCategory=" + selectedSubTopics.get(i).toUpperCase() + "&";
            } else {
                query += "subCategory=" + selectedSubTopics.get(i).toUpperCase();
            }
        }


        String localQuery = getLocalQuery(selectedSubTopics);
        int totalSaved = (int)SugarRecord.count(Study.class, localQuery, null);
        if (totalSaved > 0) {
            Toast.makeText(context, "Content already downloaded", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            String tag_string_req = "req_study";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_STUDY + "?" + query.replaceAll(" ", "%20")+"&sortBy=questionNumber",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("RESPONSE", response);
                            final int total;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                JSONArray dataArray = dataObject.getJSONArray("docs");
                                total = dataObject.getInt("total");
                                if(total==0){
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Content not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                while(SugarRecord.count(Study.class)!=0) {
                                    SugarRecord.deleteAll(Study.class);
                                }

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                    final Study study = new Study();
                                    study.setId(eachDataJsonObject.getInt("sid"));
                                    study.setStudyId(eachDataJsonObject.getString("_id"));
                                    if (eachDataJsonObject.has("imageUrl")) {
                                        study.setImageUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                    }
                                    study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                    study.setCategory(eachDataJsonObject.getString("category"));
                                    String question = eachDataJsonObject.getString("question");
                                    question=HtmlCleaner.cleanThis(question);
                                    String answer = eachDataJsonObject.getJSONArray("answers").getString(0);
                                    answer = HtmlCleaner.cleanThis(answer);
                                    study.setQuestion(question);
                                    study.setQuestionNumber(eachDataJsonObject.getInt("questionNumber"));
                                    study.setAnswer(answer);
                                    saveStudy(total, study, selectedSubTopics, progressDialog, (AppCompatActivity) context);
                                }

                            } catch (JSONException e) {
                                Log.e("Error", e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        }
    }

    private static void saveStudy(final int total, final Study study, final ArrayList<String> selectedSubTopics, final ProgressDialog progressDialog, AppCompatActivity context) {
        new BGTask(new BGTask.DoInBackground() {
            @Override
            public void DoJob() {
                study.save();
            }
        }, new BGTask.DoPostExecute() {
            @Override
            public void DoJob() {
                String localQuery = getLocalQuery(selectedSubTopics);
                int totalSaved = (int) SugarRecord.count(Study.class, localQuery, null);

                Log.e("Total Question", total + " ");
                Log.e("Total Saved", totalSaved + " ");
                int percentage = (totalSaved * 100) / total;
                if (percentage >= 100) {
                    progressDialog.dismiss();
                    return;
                } else {
                    progressDialog.setIndeterminate(false);
                    progressDialog.setProgress(percentage);
                }
            }
        }, context, false).execute();
    }

    @NonNull
    private static String getLocalQuery(ArrayList<String> selectedSubTopics) {
        String localQuery = "";
        for (int i = 0; i < selectedSubTopics.size(); i++) {
            if (i != selectedSubTopics.size() - 1) {
                localQuery += "subcategory = '" + selectedSubTopics.get(i).toUpperCase() + "' OR ";
            } else {
                localQuery += "subcategory = '" + selectedSubTopics.get(i).toUpperCase() + "'";
            }
        }
        return localQuery;
    }
}
