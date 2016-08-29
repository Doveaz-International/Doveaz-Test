package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.Config;
import com.doveazapp.Utils.SessionManager;
import com.doveazapp.Utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LoginActivity.java
 * Created by Karthik on 11/16/2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button btn_login, btn_newuser, btn_forgot;

    EditText user_name, password;

    TextInputLayout input_layout_username, input_layout_password;

    GoogleCloudMessaging gcm;

    String gcm_regId;

    private static final String APP_VERSION = "appVersion";

    ProgressDialog progressDialog;

    // Session Manager Class
    SessionManager session;

    //STORE LAST ENTERED EMAIL ID

    private final String DefaultUnameValue = "";

    private String UnameValue;

    public static final String REG_ID = "regId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Login Activity");

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // text input layout
        input_layout_username = (TextInputLayout) findViewById(R.id.input_layout_username);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);

        //button initialization
        btn_login = (Button) findViewById(R.id.button_login);
        btn_newuser = (Button) findViewById(R.id.button_newuser);
        btn_forgot = (Button) findViewById(R.id.button_forgot);

        //edittext init
        user_name = (EditText) findViewById(R.id.edit_username);
        password = (EditText) findViewById(R.id.edit_password);

        btn_login.setOnClickListener(this);
        btn_newuser.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);

        user_name.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == btn_login) {
            // Validation part
            validateFields();
        }
        if (view == btn_newuser) {
            // intent to register activity
            Intent to_newuser = new Intent(getApplicationContext(), RegisterAsActivity.class);
            startActivity(to_newuser);
        }
        if (view == btn_forgot) {
            Intent to_forgot_password = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(to_forgot_password);
        }
    }

    private void validateFields() {

        final String email = user_name.getText().toString();
        final String pass = password.getText().toString();
        if (!isValidEmail(email)) {
            input_layout_username.setError(Constants.KEY_MAILID_VALIDATE);
        } else if (!isValidPassword(pass)) {
            input_layout_password.setError(Constants.KEY_PASSWORD_VALIDATE);
        } else if (!Utils.isOnline(LoginActivity.this)) {
            Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
        } else {
            LoginRequestAPI();
        }
    }

    /**
     * LOGIN API CALLS THE FUNCTION IN SERVICE CALLS (callAPI_toLogin)
     */
    private void LoginRequestAPI() {
        progressDialog = ProgressDialog.show(LoginActivity.this, Constants.PLEASE_WAIT, Constants.LOADING, true);
        progressDialog.setCancelable(false);
        final OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT LOGIN--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);

                    if (status.equals(Constants.KEY_FALSE)) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        progressDialog.dismiss();
                        Log.v("Login", value.toString());
                        // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        JSONObject login_response = obj.getJSONObject(Constants.KEY_VALUE);
                        JSONArray jsonArray = login_response.getJSONArray(Constants.KEY_USER);

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jobj = jsonArray.getJSONObject(j);
                            Log.i("EMAIL", jobj.getString(Constants.KEY_EMAIL));
                            Log.i("API-TOKEN", jobj.getString(Constants.KEY_TOKEN));
                            String email = jobj.getString(Constants.KEY_EMAIL);
                            String api_token = jobj.getString(Constants.KEY_TOKEN);
                            String partner = jobj.getString(Constants.KEY_PARTNER);
                            String userid = jobj.getString(Constants.KEY_USER_ID);
                            String u_name = jobj.getString(Constants.KEY_FULLNAME);
                            String cc_iso = jobj.getString(Constants.KEY_COUNTRY_CODE_ISO);
                            String phone_number = jobj.getString(Constants.KEY_PHONE);
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                                //SAVING SESSIONS TO SHARED PREFERENCES

                                session.createLoginSession(email, api_token, partner, userid, u_name, cc_iso, phone_number);
                                to_partnerMenu();
                            } else if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                                //SAVING SESSIONS TO SHARED PREFERENCES
                                session.createLoginSession(email, api_token, partner, userid, u_name, cc_iso, phone_number);
                                to_deliveryMenu();
                            }
                        }
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        if (TextUtils.isEmpty(gcm_regId)) {
            gcm_regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + gcm_regId);
            new Thread(new Runnable() {
                public void run() {
                    String msg = "";
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                        }
                        gcm_regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                        Log.d("LoginActivity", "registerInBackground - regId: "
                                + gcm_regId);
                        msg = "Device registered, registration ID=" + gcm_regId;
                        String Username = user_name.getText().toString();
                        String Password = password.getText().toString();
                        ServiceCalls.callAPI_toLogin(LoginActivity.this, Request.Method.POST, Constants.LOGIN_URL, listener, Username, Password, gcm_regId);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        Log.d("LoginActivity", "Error: " + msg);
                    }
                    Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                }
            }).start();
        } else {
            Log.v("Login", "Already registered with GCM server");
            String Username = user_name.getText().toString();
            String Password = password.getText().toString();
            ServiceCalls.callAPI_toLogin(this, Request.Method.POST, Constants.LOGIN_URL, listener, Username, Password, gcm_regId);
        }
    }

    private void to_partnerMenu() {
        Intent to_menuPartner = new Intent(getApplicationContext(), AgentLocationActivity.class);
        startActivity(to_menuPartner);
    }

    private void to_deliveryMenu() {
        Intent to_menu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(to_menu);
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 5) {
            return true;
        }
        return false;
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        gcm_regId = getRegistrationId(this);
        if (TextUtils.isEmpty(gcm_regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + gcm_regId);
        } else {
            Log.d("RegisterActivity",
                    "RegId already available. RegId: "
                            + gcm_regId);
        }
        return gcm_regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                LoginActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("Login", "Registration not found.");
            registerInBackground();
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("Login", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    gcm_regId = gcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("LoginActivity", "registerInBackground - regId: "
                            + gcm_regId);
                    msg = "Device registered, registration ID=" + gcm_regId;
                    storeRegistrationId(LoginActivity.this, gcm_regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("LoginActivity", "Error: " + msg);
                }
                Log.d("LoginActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                /*Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();*/
                Log.v("LoginActivity", "Registered with GCM Server!");
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                LoginActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i("Saving GCM ID", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (editable == user_name.getEditableText()) {
            input_layout_username.setError(null);
        } else if (editable == password.getEditableText()) {
            input_layout_password.setError(null);
        }
    }

    //STORE LAST ENTERED EMAIL ID
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = user_name.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        editor.putString(Constants.PREF_UNAME, UnameValue);
        editor.commit();
    }

    //STORE LAST ENTERED EMAIL ID
    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME,
                Context.MODE_PRIVATE);
        // Get value
        UnameValue = settings.getString(Constants.PREF_UNAME, DefaultUnameValue);
        user_name.setText(UnameValue);
        System.out.println("onResume load name: " + UnameValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Login Activity");
        loadPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }


}
