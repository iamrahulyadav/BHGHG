package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.SearchAdapter;
import com.kandara.medicalapp.Adapter.StudySearchAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;
import com.orm.SugarRecord;
import com.orm.query.Select;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    ArrayList<Object> searchItemList;
    RecyclerView searchListView;
    private String searchTxt;
    private TextView tvNoResult;

    CatLoadingView mView;

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchItemList = new ArrayList<>();
        searchListView = view.findViewById(R.id.searchList);
        tvNoResult = view.findViewById(R.id.tvNoResult);
        mView = new CatLoadingView();
        performSearch(searchTxt);
        return view;
    }


    public void performSearch(final String searchTxt) {

        mView.show(getChildFragmentManager(), "");
        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mView.dismiss();
                        searchItemList=new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray dataArray = jsonObject.getJSONArray("message");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final Study study = new Study();
                                study.setId(eachDataJsonObject.getInt("sid"));
                                study.setQuestionNumber(i + 1);
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                if (eachDataJsonObject.has("imageUrl")) {
                                    study.setImageUrl("http://163.172.172.57:5000" + eachDataJsonObject.getString("imageUrl"));
                                }
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                study.setQuestion(eachDataJsonObject.getString("question"));
                                JSONArray answersArray = eachDataJsonObject.getJSONArray("answers");
                                for (int k = 0; k < answersArray.length(); k++) {
                                    study.setAnswer(answersArray.getString(k));
                                }
                                if(!searchItemList.contains(study)) {
                                    searchItemList.add(study);
                                }
                            }
                            if (searchItemList.size() > 0) {
                                StudySearchAdapter searchAdapter = new StudySearchAdapter(searchItemList, getActivity());
                                searchAdapter.setSearchTxt(searchTxt);
                                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                searchListView.setLayoutManager(llm);
                                searchListView.setAdapter(searchAdapter);

                                tvNoResult.setVisibility(View.GONE);
                            } else {
                                tvNoResult.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                            tvNoResult.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mView.dismiss();
                        tvNoResult.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("question", searchTxt);
                return params;
            }
        };
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

}
