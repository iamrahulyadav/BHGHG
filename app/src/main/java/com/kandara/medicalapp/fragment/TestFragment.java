package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.TestAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.Test;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.HtmlCleaner;
import com.kandara.medicalapp.Util.JsondataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    ListView testListView;

    public TestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        testListView = view.findViewById(R.id.testListView);
        getTests();

        return view;
    }

    public void getTests(){
        final ArrayList<Test> tests = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_TEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int n = 0; n < dataArray.length(); n++) {
                                JSONObject testJsonObject = dataArray.getJSONObject(n);
                                Test test=new Test();
                                test.setTid(testJsonObject.getString("_id"));
                                test.setName(testJsonObject.getString("name"));
                                test.setDescription(testJsonObject.getString("desc"));
                                test.setTestDuration(testJsonObject.getLong("totalTime"));
                                test.setTestStartTime(testJsonObject.getLong("startTime"));
                                tests.add(test);
                            }

                            TestAdapter testAdapter=new TestAdapter(tests, getActivity());
                            testListView.setAdapter(testAdapter);
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
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, "test");
    }

}
