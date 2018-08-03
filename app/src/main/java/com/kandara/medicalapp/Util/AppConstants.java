package com.kandara.medicalapp.Util;

/**
 * Created by abina on 2/11/2018.
 */

public class AppConstants {
    
    public static String MAIN_URL="http://163.172.172.57:5000";

    public static String URL_MCQ = MAIN_URL+"/api/mcq";
    public static String URL_STUDY = MAIN_URL+"/api/study";
    public static String URL_TEST = MAIN_URL+"/api/test";
    public static String URL_TEST_POSTSCORE = MAIN_URL+"/api/testscore";
    public static String URL_REGISTER = MAIN_URL+"/api/register";
    public static String URL_LOGIN = MAIN_URL+"/api/authenticate";


    public static String URL_DISCUSSION = MAIN_URL+"/api/Discussion";

    public static String URL_COMMENT = MAIN_URL+"/api/Comment";

    public static String URL_USERS = MAIN_URL+"/api/usermeta";
    public static String URL_PHOTO_UPLOAD = MAIN_URL+"/api/upload-image";
    public static String URL_SEARCH = MAIN_URL+"/api/study-search";
    public static int FREE_USERS_DATA_LIMIT = 10;

}
