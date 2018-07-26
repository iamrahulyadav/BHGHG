package com.kandara.medicalapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abina on 2/26/2018.
 */

public class AccountManager {
    private static String USERDATA ="loggingPreference";
    private static String IS_LOGGED_IN="isLoggedIn";
    private static String USER_ID="userId";
    private static String TOPICS="topics";
    private static String MCQTOPICS="mcqtopics";
    private static String MCQYEAR="mcqyear";
    private static String MCQBOARD="mcqboard";
    private static String SUBTOPICS="subtopics";
    private static String USER_TOKEN="userToken";
    private static String USER_FIRSTNAME="userFirstName";
    private static String USER_LASTNAME="userLastName";
    private static String USER_EMAIL="userEmail";
    private static String USER_ISPREMIUM="isPremium";
    private static String USER_PHOTOURL="userPhotoUrl";
    /**
     * Checks if an user is logged In Or Not
     * @param context
     * @return a boolean value: true for logged in, false for logged out
     */
    public static boolean isLoggedIn(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Logs In or Log Out an user
     * @param context
     * @param isLoggedIn
     */
    public static void setLoggedIn(Context context, boolean isLoggedIn){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }


    public static void setIsPremium(Context context, boolean isPremium){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(USER_ISPREMIUM, isPremium);
        editor.commit();
    }

    public static void setFirstName(Context context, String firstName){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_FIRSTNAME, firstName);
        editor.commit();
    }


    public static void setLastName(Context context, String lastName){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_LASTNAME, lastName);
        editor.commit();
    }
    public static void setId(Context context, String id){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_ID, id);
        editor.commit();
    }


    public static void setSubTopics(Context context, String subTopics){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(SUBTOPICS, subTopics);
        editor.commit();
    }


    public static void setTopic(Context context, String topics){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(TOPICS, topics);
        editor.commit();
    }


    public static void setMCQTOPICS(Context context, String mcqTopics){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(MCQTOPICS, mcqTopics);
        editor.commit();
    }

    public static void setMCQYEAR(Context context, String mcqYear){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(MCQYEAR, mcqYear);
        editor.commit();
    }


    public static void setMCQBOARD(Context context, String mcqBoard){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(MCQBOARD, mcqBoard);
        editor.commit();
    }



    public static void setToken(Context context, String id){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_TOKEN, id);
        editor.commit();
    }

    public static void setEmail(Context context, String email){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public static void setPhotoUrl(Context context, String photourl){

        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_PHOTOURL, photourl);
        editor.commit();
    }

    public static String getFirstName(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(USER_FIRSTNAME, "");
    }

    public static boolean isUserPremium(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getBoolean(USER_ISPREMIUM, false);
    }


    public static String getLastName(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(USER_LASTNAME, "");
    }


    public static String getUserId(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(USER_ID, "");
    }


    public static String getSubTopics(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(SUBTOPICS, "");
    }


    public static String getMCQTOPICS(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(MCQTOPICS, "");
    }


    public static String getMCQYEAR(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(MCQYEAR, "");
    }

    public static String getMCQBOARD(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(MCQBOARD, "");
    }


    public static String getTopics(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(TOPICS, "");
    }



    public static String getToken(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(USER_TOKEN, "");
    }


    public static String getEmail(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(USER_EMAIL, "");
    }


    public static String getUserPhotourl(Context context){
        SharedPreferences preferences=context.getSharedPreferences(USERDATA, 0);
        return preferences.getString(USER_PHOTOURL, "");
    }






}
