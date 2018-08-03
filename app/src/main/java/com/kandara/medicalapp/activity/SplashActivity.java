package com.kandara.medicalapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AccountManager.isLoggedIn(getApplicationContext())) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    String password = Base64.encodeToString(deviceId.getBytes(), Base64.NO_WRAP);
                    RegisterUser(deviceId, password);
                }
            }
        }, 500);

    }

    private void RegisterUser(final String deviceId, final String password) {
        String tag_string_req = "req_register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Register Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                LoginUser(deviceId, password);
                            } else if (jsonObject.getString("message").equalsIgnoreCase("User with this deviceId already exist.")) {
                                LoginUser(deviceId, password);
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Json Error", e.getMessage() + "");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.getMessage() + "");
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", deviceId);
                params.put("password", password);
                return params;
            }
        };
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void RegisterMeta(final String userId, final String token) {
        String tag_string_req = "req_register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.URL_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Meta Response", response);

                        RetrieveUserData(userId, token);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.getMessage() + "");
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", userId);
                return params;
            }
        };
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    private void LoginUser(final String deviceId, final String password) {
        String tag_string_req = "req_register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Login Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {

                                RetrieveId(deviceId, jsonObject.getString("token"));
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Json Error", e.getMessage() + "");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.getMessage() + "");
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", deviceId);
                params.put("password", password);
                return params;
            }
        };
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    private void RetrieveId(String deviceId, final String token) {
        String tag_string_req = "req_register";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/Users" + "?deviceId=" + deviceId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ID Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("message");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject userData = dataArray.getJSONObject(i);
                                AccountManager.setId(getApplicationContext(), userData.getString("_id"));
                                Log.e("USER ID",userData.getString("_id")) ;
                                RegisterMeta(userData.getString("_id"), token);
                                break;
                            }
                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage() + "");
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


    private void RetrieveUserData(final String userId, final String token) {
        String tag_string_req = "req_register";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/usermeta/uid/" + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Data Reponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject userData = jsonObject.getJSONObject("data");
                            AccountManager.setToken(getApplicationContext(), token);
                            AccountManager.setLoggedIn(getApplicationContext(), true);


                            if (userData.has("profilePhoto")) {
                                AccountManager.setPhotoUrl(getApplicationContext(), AppConstants.MAIN_URL+"/media/" + userData.getString("profilePhoto"));
                            }
                            if (userData.has("lastname")) {
                                AccountManager.setLastName(getApplicationContext(), userData.getString("lastname"));
                            }
                            if (userData.has("firstname")) {
                                AccountManager.setFirstName(getApplicationContext(), userData.getString("firstname"));
                            }
                            if (userData.has("email")) {
                                AccountManager.setEmail(getApplicationContext(), userData.getString("email"));
                            }
                            if(userData.has("isPremium")){
                                AccountManager.setIsPremium(getApplicationContext(), userData.getBoolean("isPremium"));
                            }
                            AccountManager.setId(getApplicationContext(), userData.getString("uid"));

                            finish();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage() + "");
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

}
