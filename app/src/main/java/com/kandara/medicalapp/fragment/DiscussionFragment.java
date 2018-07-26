package com.kandara.medicalapp.fragment;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.DiscussionAdapter;
import com.kandara.medicalapp.Adapter.ViewPagerAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Dialogs.DiscussionAddMcqDialog;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.DiscussionAnswer;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Qt;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.Util.QuestionIndicator.animation.type.AnimationType;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscussionFragment extends Fragment {

    static String GROUP_URL="https://www.facebook.com/groups/219861721842863/";

    ListView listView;
    ArrayList<Discussion> discussionArrayList;
    View headerView;

    EditText discussionContentField;
    Button btnAddMcq;
    Button btnPostDiscussion;

    DiscussionAddMcqDialog discussionAddMcqDialog;
    Handler handler;


    String option1 = "";
    String option2 = "";
    String option3 = "";
    String option4 = "";

    TextView tvOption1, tvOption2, tvOption3, tvOption4;

    CatLoadingView mView;

    Button btnDiscussInFb;

    public DiscussionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);
        discussionArrayList=new ArrayList<>();
        mView=new CatLoadingView();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Bundle bundle = message.getData();
                option1 = bundle.getString("option1");
                option2 = bundle.getString("option2");
                option3 = bundle.getString("option3");
                option4 = bundle.getString("option4");

                if (option1.isEmpty()) {
                    tvOption1.setVisibility(View.GONE);
                } else {
                    tvOption1.setVisibility(View.VISIBLE);
                    tvOption1.setText(option1);
                }

                if (option2.isEmpty()) {
                    tvOption2.setVisibility(View.GONE);
                } else {
                    tvOption2.setVisibility(View.VISIBLE);
                    tvOption2.setText(option2);
                }


                if (option3.isEmpty()) {
                    tvOption3.setVisibility(View.GONE);
                } else {
                    tvOption3.setVisibility(View.VISIBLE);
                    tvOption3.setText(option3);
                }

                if (option4.isEmpty()) {
                    tvOption4.setVisibility(View.GONE);
                } else {
                    tvOption4.setVisibility(View.VISIBLE);
                    tvOption4.setText(option4);
                }


                return false;
            }
        });
        discussionAddMcqDialog = new DiscussionAddMcqDialog();
        discussionAddMcqDialog.setHandler(handler);

        headerView = inflater.inflate(R.layout.discussion_header, container, false);
        discussionContentField = headerView.findViewById(R.id.discussionContentField);
        btnAddMcq = headerView.findViewById(R.id.btnAddMcq);
        tvOption1 = headerView.findViewById(R.id.option1);
        tvOption2 = headerView.findViewById(R.id.option2);
        tvOption3 = headerView.findViewById(R.id.option3);
        tvOption4 = headerView.findViewById(R.id.option4);

        if (option1.isEmpty()) {
            tvOption1.setVisibility(View.GONE);
        }


        if (option2.isEmpty()) {
            tvOption2.setVisibility(View.GONE);
        }


        if (option3.isEmpty()) {
            tvOption3.setVisibility(View.GONE);
        }


        if (option4.isEmpty()) {
            tvOption4.setVisibility(View.GONE);
        }

        btnPostDiscussion = headerView.findViewById(R.id.btnPostDiscussion);

        btnAddMcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!option1.isEmpty()) {
                    discussionAddMcqDialog.setOption1(option1);
                }
                if (!option2.isEmpty()) {
                    discussionAddMcqDialog.setOption2(option2);
                }
                if (!option3.isEmpty()) {
                    discussionAddMcqDialog.setOption3(option3);
                }
                if (!option4.isEmpty()) {
                    discussionAddMcqDialog.setOption4(option4);
                }
                discussionAddMcqDialog.show(getChildFragmentManager(), "");
            }
        });

        btnPostDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!discussionContentField.getText().toString().isEmpty()) {
                    postDiscussion();
                }else{
                    Toast.makeText(getActivity(), "Please enter your question to post", Toast.LENGTH_LONG).show();
                }
            }
        });

        listView = view.findViewById(R.id.discussionList);
        btnDiscussInFb=view.findViewById(R.id.btnDiscussInFb);
        btnDiscussInFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facebookIntent=newFacebookIntent(getActivity().getPackageManager(), GROUP_URL);
                startActivity(facebookIntent);
            }
        });
        PopulateDiscussion();
        return view;
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    private void PopulateDiscussion(){


        mView.show(getChildFragmentManager(), "");
        String tag_string_req = "req_mcqs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_DISCUSSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Data", response);
                        mView.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                Discussion discussion=new Discussion(eachDataJsonObject.getString("_id"));
                                discussion.setQuestion(eachDataJsonObject.getString("question"));
                                discussion.setUid(eachDataJsonObject.getString("uid"));
                                discussion.setType(eachDataJsonObject.getBoolean("type"));
                                JSONArray answersArray=eachDataJsonObject.getJSONArray("answers");
                                Log.e("SIZZZZZZZZ", answersArray.length()+"");
                                for(int k=0; k<answersArray.length(); k++){
                                    JSONObject eachAnswerObject=answersArray.getJSONObject(k);
                                    DiscussionAnswer discussionAnswer=new DiscussionAnswer(eachAnswerObject.getString("_id"));
                                    discussionAnswer.setOption(eachAnswerObject.getString("option"));
                                    if(eachAnswerObject.has("vote")){
                                        discussionAnswer.setVotes(eachAnswerObject.getString("vote"));
                                    }
                                    if(eachAnswerObject.has("votedBy")){
                                        JSONArray votedbyarray=eachAnswerObject.getJSONArray("votedBy");
                                        for(int m=0; m<votedbyarray.length(); m++){
                                            discussionAnswer.addVotedBy(votedbyarray.getString(m));
                                        }
                                    }
                                    discussion.addAnswer(discussionAnswer);
                                }
                                    JSONArray favoritedByJsonArray = eachDataJsonObject.getJSONArray("favoritedBy");
                                    for (int n = 0; n < favoritedByJsonArray.length(); n++) {
                                        discussion.addFavoritedPeopleId(favoritedByJsonArray.getString(n));
                                    }

                                discussionArrayList.add(discussion);
                            }

                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage()+"");
                        }

                        DiscussionAdapter discussionAdapter = new DiscussionAdapter(discussionArrayList, getActivity());
                        listView.setAdapter(discussionAdapter);
                        if(listView.getHeaderViewsCount()==0) {
                            listView.addHeaderView(headerView);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);


    }

    private void postDiscussion() {
        String tag_string_req = "req_discussion";
        Map<String, Object> params = new HashMap();
        ArrayList<String> options = new ArrayList<>();
        if (!tvOption1.getText().toString().isEmpty()) {
            options.add(tvOption1.getText().toString());
        }
        if (!tvOption2.getText().toString().isEmpty()) {
            options.add(tvOption2.getText().toString());
        }
        if (!tvOption3.getText().toString().isEmpty()) {
            options.add(tvOption3.getText().toString());
        }
        if (!tvOption4.getText().toString().isEmpty()) {
            options.add(tvOption4.getText().toString());
        }
        String[] optionss = new String[options.size()];
        int x=0;
        for(String op:options){
            optionss[x]=op;
            x++;
        }

        JSONArray jsonArray=new JSONArray();
        for(int k=0; k<optionss.length; k++){
            JSONObject optio=new JSONObject();
            try {
                optio.put("option", optionss[k]);
                JSONArray votedJsonArray=new JSONArray();
                optio.put("vote", 5);
                optio.put("votedBy", votedJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray.put(k, optio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.put("uid", AccountManager.getUserId(getContext()));
        if(jsonArray.length()==0) {
            params.put("type", false);
        }else{
            params.put("type", true);
        }
        params.put("question", discussionContentField.getText().toString());
        params.put("answers", jsonArray);
        String favoritedBy[]=new String[0];
        params.put("favoritedBy", favoritedBy);


        JSONObject parameters = new JSONObject(params);


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_DISCUSSION, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("ss", response.toString());

                discussionArrayList=new ArrayList<>();
                PopulateDiscussion();
                //TODO: handle success
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Volley Error", error.getMessage() + "");
                //TODO: handle failure
            }
        });
        MedicalApplication.getInstance().addToRequestQueue(jsonRequest, tag_string_req);

    }

}
