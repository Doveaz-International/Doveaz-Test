package com.doveazapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.doveazapp.Activities.HomeActivity;
import com.doveazapp.Activities.MenuActivity;
import com.doveazapp.Activities.WelcomePartnerActivity;
import com.doveazapp.Constants;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    Editor editor2;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "USER_SESSION";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    //Dummy Login
    private static final String IS_ONLINE = "isOnline";

    // User name (make variable public to access from outside)
    public static final String KEY_APITOKEN = "api_token";

    //User type
    public static final String KEY_USER_TYPE = "partner";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_USERID = "userid";

    public static final String KEY_USERNAME = "u_name";

    public static final String KEY_COUNTRY_CODE_ISO = "country_code_iso";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor2 = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String email, String api_token, String partner, String userid, String userName, String country_code) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_EMAIL, email);
        // Storing email in pref
        editor.putString(KEY_APITOKEN, api_token);
        // storing usertype
        editor.putString(KEY_USER_TYPE, partner);

        editor.putString(KEY_USERNAME, userName);

        editor.putString(KEY_USERID, userid);

        editor.putString(KEY_COUNTRY_CODE_ISO, country_code);

        // commit changes
        editor.commit();
    }

    public void checkDummySession(String currently_live) {
        /*
        * Check the dummy login by using password*/
        editor2.putString(IS_ONLINE, currently_live);

        // commit changes
        editor2.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        } else {
            HashMap<String, String> user = this.getUserDetails();
            String type = user.get(SessionManager.KEY_USER_TYPE);

            if (type.equals(Constants.KEY_TYPE_PARTNER)) {
                Intent i = new Intent(_context, WelcomePartnerActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Constants.LOGIN_PARTNER, "1");
                // Staring Login Activity
                _context.startActivity(i);
            } else if (type.equals(Constants.KEY_TYPE_DELIVER)) {
                Log.v("API_TOKEN", getUserDetails().toString());
                Intent i = new Intent(_context, MenuActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Constants.LOGIN_DELIVER, "0");
                // Staring Login Activity
                _context.startActivity(i);
            }
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_APITOKEN, pref.getString(KEY_APITOKEN, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_USER_TYPE, pref.getString(KEY_USER_TYPE, null));

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        user.put(KEY_COUNTRY_CODE_ISO, pref.getString(KEY_COUNTRY_CODE_ISO, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, HomeActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        _context.startActivity(i);

    }

   /* *//*
    * Clear live login details*//*
    public void exitApp(){
        editor2.clear();
        editor2.commit();
        System.exit(0);
    }*/

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
