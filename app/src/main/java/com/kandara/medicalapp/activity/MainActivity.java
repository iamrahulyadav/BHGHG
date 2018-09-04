package com.kandara.medicalapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RecoverySystem;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.SubTopic;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.Model.User;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AndroidMultiPartEntity;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.Util.NavDrawerAdapter;
import com.kandara.medicalapp.Util.UtilDialog;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.kandara.medicalapp.fragment.AboutUsFragment;
import com.kandara.medicalapp.fragment.ContactUsFragment;
import com.kandara.medicalapp.fragment.ContributionFragment;
import com.kandara.medicalapp.fragment.DiscussionFragment;
import com.kandara.medicalapp.fragment.DownloadFragment;
import com.kandara.medicalapp.fragment.FAQFragment;
import com.kandara.medicalapp.fragment.HomeFragment;
import com.kandara.medicalapp.fragment.SearchFragment;
import com.kandara.medicalapp.fragment.LeaderboardFragment;
import com.kandara.medicalapp.fragment.MCQFragment;
import com.kandara.medicalapp.fragment.MainStudyFragment;
import com.kandara.medicalapp.fragment.MyProfilefragment;
import com.kandara.medicalapp.fragment.RevisionFragment;
import com.kandara.medicalapp.fragment.TestFragment;
import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private RoundedImageView imgProfile;
    private TextView txtName, txtEmail;

    public static int navItemIndex = 0;
    private static final String TAG_HOME = "Home";
    private static final String TAG_STUDY = "Read Book";
    private static final String TAG_TEST = "Online Test";
    private static final String TAG_MCQ = "Past Questions";
    private static final String TAG_REVISION = "MCQRevision";
    private static final String TAG_MY_PROFILE = "My Profile";
    private static final String TAG_DISCUSSION = "Discussion";
    private static final String TAG_DOWNLOADS = "Downloads";
    private static final String TAG_FAQ = "FAQ";
    private static final String TAG_ABOUT_US= "About Us";
    private static final String TAG_CONTACT_US= "Contact Us";
    public static String CURRENT_TAG = TAG_HOME;
    private Handler mHandler;
    private User currentUser;
    private ImageView menuIcon;
    private ImageView searchIcon;
    private EditText searchEdittext;
    private RelativeLayout toolbarLayout;
    private TextView actionBarTitle;
    private String imagePath = "";
    private boolean isSearchBar = true;
    ListView listView;
    NavDrawerAdapter navDrawerAdapter;
    String navs[] = {"Home", "Read Book", "Online Test", "Past Questions", "Revision", "My Profile", "Discussion", "Downloads", "FAQ", "About Us", "Contact Us"};

    CatLoadingView mView;
    private int SELECT_PHOTO = 233;
    ArrayList<Topic> topicArray;
    ArrayList<Topic> mcqtopicArray;
    ArrayList<Integer> mcqyearArray;
    ArrayList<String> mcqboardArray;
    ArrayList<SubTopic> subTopicArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RetrieveUserData(AccountManager.getUserId(getApplicationContext()), AccountManager.getToken(getApplicationContext()));
        mView = new CatLoadingView();
        mcqtopicArray = new ArrayList<>();
        mcqyearArray = new ArrayList<>();
        mcqboardArray = new ArrayList<>();
        topicArray = new ArrayList<>();
        subTopicArray = new ArrayList<>();
        Log.e("Count", SugarRecord.count(Study.class) + "");
        currentUser = JsondataUtil.getUserDetail(getApplicationContext());
        mHandler = new Handler();
        navDrawerAdapter = new NavDrawerAdapter(MainActivity.this, R.layout.item_nav_drawer, navs, 0);
        drawer = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menuIcon);
        searchIcon = findViewById(R.id.searchIcon);
        searchEdittext = findViewById(R.id.searchEdittext);
        toolbarLayout = findViewById(R.id.toolbar);
        listView = findViewById(R.id.lst_menu_items);
        listView.setAdapter(navDrawerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavigateDrawer(i);
            }
        });
        actionBarTitle = findViewById(R.id.actionBarTitle);
        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.website);
        imgProfile = findViewById(R.id.img_profile);
        loadNavHeader();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIcon.setEnabled(true);
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIcon.setEnabled(true);
                String searchTxt = searchEdittext.getText().toString();
                if (!searchTxt.isEmpty()) {
                    performSearch();
                }
            }
        });

        searchEdittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchTxt = searchEdittext.getText().toString();
                    if (!searchTxt.isEmpty()) {
                        performSearch();
                    }
                    return true;
                }
                return false;
            }
        });
        updateToolbarAndStatusBar("Main");

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


    private void performSearch() {
        if(!AccountManager.isUserPremium(getApplicationContext())){
            UtilDialog.showUpgradeDialog(MainActivity.this);
            return;
        }
        loadSearchFragment(searchEdittext.getText().toString());
    }

    public void NavigateDrawer(int i) {
        switch (i) {
            case 0:
                navItemIndex = 0;
                isSearchBar = true;
                updateToolbarAndStatusBar("Home");
                CURRENT_TAG = TAG_HOME;
                break;
            case 1:
                navItemIndex = 1;
                isSearchBar = false;
                updateToolbarAndStatusBar("Read Book");
                CURRENT_TAG = TAG_STUDY;
                break;
            case 2:
                navItemIndex = 2;
                isSearchBar = false;
                updateToolbarAndStatusBar("Online Test");
                CURRENT_TAG = TAG_TEST;
                break;
            case 3:
                navItemIndex = 3;
                isSearchBar = false;
                updateToolbarAndStatusBar("Past Questions");
                CURRENT_TAG = TAG_MCQ;
                break;
            case 4:
                navItemIndex = 4;
                isSearchBar = false;
                updateToolbarAndStatusBar("MCQRevision");
                CURRENT_TAG = TAG_REVISION;
                break;
            case 5:
                navItemIndex = 5;
                isSearchBar = false;
                updateToolbarAndStatusBar("My profile");
                CURRENT_TAG = TAG_MY_PROFILE;
                break;
            case 6:
                navItemIndex = 6;
                isSearchBar = false;
                updateToolbarAndStatusBar("Discussion");
                CURRENT_TAG = TAG_DISCUSSION;
                break;

            case 7:
                navItemIndex = 7;
                isSearchBar = false;
                updateToolbarAndStatusBar("Downloads");
                CURRENT_TAG = TAG_DOWNLOADS;
                break;


            case 8:
                navItemIndex = 8;
                isSearchBar = false;
                updateToolbarAndStatusBar("FAQ");
                CURRENT_TAG = TAG_FAQ;
                break;

            case 9:
                navItemIndex = 9;
                isSearchBar = false;
                updateToolbarAndStatusBar("About Us");
                CURRENT_TAG = TAG_ABOUT_US;
                break;


            case 10:
                navItemIndex = 10;
                isSearchBar = false;
                updateToolbarAndStatusBar("Contact Us");
                CURRENT_TAG = TAG_CONTACT_US;
                break;


            default:
                navItemIndex = 0;
        }
        navDrawerAdapter.setSelected(i);
        listView.setAdapter(navDrawerAdapter);
        listView.invalidate();
        loadHomeFragment();

    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        if (AccountManager.getFirstName(getApplicationContext()).isEmpty()) {
            txtName.setText("Guest");
        } else {
            txtName.setText(AccountManager.getFirstName(getApplicationContext()) + " " + AccountManager.getLastName(getApplicationContext()));
        }

        String email="";
        if (AccountManager.getEmail(getApplicationContext()).isEmpty()) {
            email="guest@guest.com";
        } else {
            email=AccountManager.getEmail(getApplicationContext());
        }
        if(AccountManager.isUserPremium(getApplicationContext())){
            email+="\nPremium User";
        }else{
            email+="\nFree User";
        }
        txtEmail.setText(email);
        try {
            Picasso.with(MainActivity.this).load(AccountManager.getUserPhotourl(getApplicationContext())).into(imgProfile);
        } catch (IllegalArgumentException e) {
            imgProfile.setImageResource(R.drawable.ic_addphoto);
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
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
    }


    private void loadSearchFragment(final String searchTxt) {

        navItemIndex = 199;
        navDrawerAdapter.setSelected(navItemIndex);
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setSearchTxt(searchTxt);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, searchFragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        invalidateOptionsMenu();
    }

    private void loadHomeFragment() {
        searchEdittext.setText("");
        navDrawerAdapter.setSelected(navItemIndex);
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                MainStudyFragment studyFragment = new MainStudyFragment();
                return studyFragment;

            case 2:
                TestFragment testFragment = new TestFragment();
                return testFragment;

            case 3:
                MCQFragment mcqFragment = new MCQFragment();
                return mcqFragment;

            case 4:
                RevisionFragment revisionFragment = new RevisionFragment();
                return revisionFragment;


            case 5:
                MyProfilefragment myProfilefragment = new MyProfilefragment();
                return myProfilefragment;

            case 6:
                DiscussionFragment discussionFragment = new DiscussionFragment();
                return discussionFragment;


            case 7:
                DownloadFragment downloadFragment=new DownloadFragment();
                return downloadFragment;


            case 8:
                FAQFragment faqFragment=new FAQFragment();
                return faqFragment;

            case 9:
                AboutUsFragment aboutUsFragment=new AboutUsFragment();
                return aboutUsFragment;

            case 10:
                ContactUsFragment contactUsFragment=new ContactUsFragment();
                return contactUsFragment;

            default:
                return new HomeFragment();
        }
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (navItemIndex != 0) {
            navItemIndex = 0;
            NavigateDrawer(navItemIndex);
        } else {
            finish();
        }
        return;

    }


    public void updateToolbarAndStatusBar(String title) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.main_bg_color));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (isSearchBar) {
            menuIcon.setVisibility(View.VISIBLE);
            searchIcon.setVisibility(View.VISIBLE);
            toolbarLayout.setBackgroundResource(R.drawable.bg_toolbar_top);
            actionBarTitle.setVisibility(View.GONE);
            searchEdittext.setVisibility(View.VISIBLE);
        } else {
            menuIcon.setVisibility(View.VISIBLE);
            searchIcon.setVisibility(View.GONE);
            toolbarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            actionBarTitle.setVisibility(View.VISIBLE);
            actionBarTitle.setText(title);
            searchEdittext.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            new UploadFileToServer().execute();
            cursor.close();
        }
    }



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            mView.show(getSupportFragmentManager(), "");
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
                AccountManager.setPhotoUrl(MainActivity.this, AppConstants.MAIN_URL+"/media/" + filename);

                try {
                    Picasso.with(MainActivity.this).load(AccountManager.getUserPhotourl(MainActivity.this)).into(imgProfile);
                } catch (IllegalArgumentException e) {
                    imgProfile.setImageResource(R.drawable.ic_addphoto);
                }
                String tag_string_req = "req_photopost";

                Log.e("User ID", AccountManager.getUserId(MainActivity.this));
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, AppConstants.URL_USERS + "/uid/" + AccountManager.getUserId(MainActivity.this),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Photo Post Reponse", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(MainActivity.this, "Photo Saved", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Photo could not be saved", Toast.LENGTH_LONG).show();
                                    }
                                    finish();
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
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
                        headers.put("Authorization", AccountManager.getToken(MainActivity.this));
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


}
