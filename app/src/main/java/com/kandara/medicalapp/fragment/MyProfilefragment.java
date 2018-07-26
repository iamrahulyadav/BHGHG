package com.kandara.medicalapp.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.DiscussionAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.DiscussionAnswer;
import com.kandara.medicalapp.Model.User;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AndroidMultiPartEntity;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.DataManager;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.kandara.medicalapp.activity.MainActivity;
import com.kandara.medicalapp.activity.SplashActivity;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfilefragment extends Fragment {

    RoundedImageView imgProfile;
    EditText firstNameField, lastNameField, emailField;
    Button saveProfileBtn;
    CheckBox cbIsPremium;

    CatLoadingView mView;

    private int SELECT_PHOTO = 245;
    private String imagePath = "";

    public MyProfilefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profilefragment, container, false);
        mView = new CatLoadingView();
        imgProfile = view.findViewById(R.id.img_profile);
        cbIsPremium = view.findViewById(R.id.cbIsPremium);
        firstNameField = view.findViewById(R.id.firstNameField);
        lastNameField = view.findViewById(R.id.lastNameField);
        emailField = view.findViewById(R.id.emailField);
        saveProfileBtn = view.findViewById(R.id.saveProfileBtn);

        if (!AccountManager.getFirstName(getContext()).isEmpty()) {
            firstNameField.setText(AccountManager.getFirstName(getContext()));
        }

        if (!AccountManager.getLastName(getContext()).isEmpty()) {
            lastNameField.setText(AccountManager.getLastName(getContext()));
        }

        cbIsPremium.setChecked(AccountManager.isUserPremium(getActivity()));


        if (!AccountManager.getEmail(getContext()).isEmpty()) {
            emailField.setText(AccountManager.getEmail(getContext()));
        }

        if (!AccountManager.getUserPhotourl(getContext()).isEmpty()) {
            Picasso.with(getActivity()).load(AccountManager.getUserPhotourl(getContext())).into(imgProfile);
        }


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else {
                    requestPermission();
                }
            }
        });

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameField.getText().toString().isEmpty() || lastNameField.getText().toString().isEmpty() || emailField.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all field", Toast.LENGTH_SHORT).show();
                } else {
                    AccountManager.setFirstName(getActivity(), firstNameField.getText().toString());
                    AccountManager.setLastName(getActivity(), lastNameField.getText().toString());
                    AccountManager.setEmail(getActivity(), emailField.getText().toString());
                    AccountManager.setIsPremium(getActivity(), cbIsPremium.isChecked());
                    saveProfile(firstNameField.getText().toString(), lastNameField.getText().toString(), emailField.getText().toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            new UploadFileToServer().execute();

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            mView.show(getChildFragmentManager(), "");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConstants.URL_PHOTO_UPLOAD);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity();

                File sourceFile = new File(imagePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));


                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject fileObject = jsonObject.getJSONObject("file");
                final String filename = fileObject.getString("filename");
                AccountManager.setPhotoUrl(getActivity(), "http://163.172.172.57:5000/media/" + filename);

                try {
                    Picasso.with(getActivity()).load(AccountManager.getUserPhotourl(getActivity())).into(imgProfile);
                } catch (IllegalArgumentException e) {
                    imgProfile.setImageResource(R.drawable.ic_addphoto);
                }
                String tag_string_req = "req_photopost";

                Log.e("User ID", AccountManager.getUserId(getContext()));
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, AppConstants.URL_USERS + "/uid/" + AccountManager.getUserId(getContext()),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Photo Post Reponse", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getActivity(), "Photo Saved", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Photo could not be saved", Toast.LENGTH_LONG).show();
                                    }

                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                } catch (JSONException e) {
                                    Log.e("Json Error", e.getMessage() + "");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Cache-Control", "no-cache");
                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                        headers.put("Authorization", AccountManager.getToken(getActivity()));
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("profilePhoto", filename);
                        return params;
                    }

                };
                MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mView.dismiss();

            super.onPostExecute(result);
        }

    }

    public void saveProfile(final String firstname, final String lastname, final String email) {
        Toast.makeText(getActivity(), "Saving data...", Toast.LENGTH_LONG).show();


        JSONObject params = new JSONObject();
        try {
            params.put("firstname", firstname);
            params.put("lastname", lastname);
            params.put("email", email);
            params.put("isPremium", cbIsPremium.isChecked());
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
        }
        String tag_string_req = "post_mcq";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, AppConstants.URL_USERS + "/uid/" + AccountManager.getUserId(getContext()), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean success = response.getBoolean("success");
                    if (success) {

                        Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Data could not be saved", Toast.LENGTH_LONG).show();
                    }

                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } catch (JSONException e) {
                    Log.e("Json Error", e.getMessage() + "");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MedicalApplication.getInstance().addToRequestQueue(jsonObjectRequest, tag_string_req);



    }

}
