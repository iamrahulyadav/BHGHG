package com.kandara.medicalapp.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.DiscussionAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.Comment;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.DiscussionAnswer;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.DataManager;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.View.catloadinglibrary.EyelidView;
import com.kandara.medicalapp.fragment.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CommentDialog extends DialogFragment {

    public CommentDialog() {
    }

    public void setDiscussionId(String discussionId) {
        this.discussionId = discussionId;
    }

    String discussionId;
    Dialog mDialog;
    EditText commentField;
    Button btnPostComment;
    ArrayList<Comment> commentArrayList;
    ListView listView;
    View headerView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setContentView(R.layout.dialog_comment);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.getWindow().setGravity(Gravity.CENTER);
            View view = mDialog.getWindow().getDecorView();
            headerView = getLayoutInflater().inflate(R.layout.comment_header, null, false);
            commentField = headerView.findViewById(R.id.commentField);
            btnPostComment = headerView.findViewById(R.id.btnPostComment);
            btnPostComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postComment();
                }
            });
            listView = view.findViewById(R.id.listView);
            commentArrayList=new ArrayList<>();
            PopulateComments();
        }
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDialog = null;
        System.gc();
    }

    private void PopulateComments() {


        String tag_string_req = "req_mcqs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_COMMENT+"?did="+discussionId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Data", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                Comment comment = new Comment();
                                comment.setUid(eachDataJsonObject.getString("uid"));
                                comment.setComment(eachDataJsonObject.getString("comment"));
                                commentArrayList.add(comment);
                            }

                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage() + "");
                        }

                        CommentAdapter commentAdapter = new CommentAdapter(getActivity(), commentArrayList);
                        listView.setAdapter(commentAdapter);
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

    private void postComment() {
        String tag_string_req = "req_comment";
        Map<String, Object> params = new HashMap();
        params.put("uid", AccountManager.getUserId(getContext()));
        params.put("did", discussionId);
        params.put("comment", commentField.getText().toString());
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_COMMENT, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("ss", response.toString());

                commentArrayList=new ArrayList<>();
                PopulateComments();
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
