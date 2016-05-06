package com.doveazapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.AlertDialogs;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Karthik on 11/16/2015.
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btn_collection, btn_send;

    LinearLayout btn_buy;

    // Session Manager Class
    SessionManager session;

    //Progress bar
    ProgressDialog progressDialog;

    String deliver_loggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Menu Activity");
        // Session class instance
        session = new SessionManager(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String name = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Toast.makeText(getApplicationContext(), name + email, Toast.LENGTH_LONG).show();

        // GET INTENT FROM PREVIOUS CLASS
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            deliver_loggedin = bundle.getString(Constants.LOGIN_DELIVER);
            if (deliver_loggedin != null) {
                Log.v("Login_partner", deliver_loggedin);
                enterPassword();
            }
        }


        // Buttons init
        btn_buy = (LinearLayout) findViewById(R.id.buy_btn);
        btn_collection = (LinearLayout) findViewById(R.id.collection_btn);
        btn_send = (LinearLayout) findViewById(R.id.send_btn);

        // Onclicks for buttons
        btn_buy.setOnClickListener(this);
        btn_collection.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    private void enterPassword() {
        /* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
        alert.setTitle("Password");
        alert.setIcon(R.drawable.lock);
        alert.setMessage("Please enter your password, for security reasons");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(MenuActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String entered_value = input.getEditableText().toString();
                if (!BaseActivity.CommonClass.isNetworkAvailable(MenuActivity.this)) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    enterPassword();
                } else if (entered_value.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
                    enterPassword();
                } else {
                    progressDialog = ProgressDialog.show(MenuActivity.this, "Please wait ...", "Requesting...", true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("OUTPUT AUTHENTICATE", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                final String status = obj.getString("status");
                                final String value = obj.getString("value");
                                if (status.equals("false")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();
                                    if (value.equals("You are not logged in")) {
                                        to_loginActivity();
                                    } else {
                                        enterPassword();
                                    }
                                } else if (status.equals("true")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException exception) {
                                progressDialog.dismiss();
                                Log.e("--JSON EXCEPTION--", exception.toString());
                            }
                        }
                    };
                    HashMap<String, String> user = session.getUserDetails();
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    ServiceCalls.CallAPI_to_sessionLogin(MenuActivity.this, Request.Method.POST, Constants.AUTHENTICATE_USER, listener, entered_value, api_token);
                }
            }

        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void to_loginActivity() {
        Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(to_login);
    }

    private void Save_session() {

    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(MenuActivity.this);
    }

    @Override
    public void onClick(View view) {

        if (btn_buy == view) {
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("Buy&Delivery Button", "In Deliver Menu", "B&D");

            Intent buytologin = new Intent(getApplicationContext(), BuyandDeliveryActivity.class);
            startActivity(buytologin);
        }
        if (btn_collection == view) {
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("Collection Button", "In Deliver Menu", "B&D");

            Intent coll_tologin1 = new Intent(getApplicationContext(), CollectionActivity.class);
            startActivity(coll_tologin1);
        }
        if (btn_send == view) {
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("send Button", "In Deliver Menu", "B&D");

            /*Intent send_tologin2 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(send_tologin2);*/
            Intent coll_tosend = new Intent(getApplicationContext(), SendActivity.class);
            startActivity(coll_tosend);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String user_type = user.get(SessionManager.KEY_USER_TYPE);
        menu.clear();
        if (user_type.equals(Constants.KEY_TYPE_PARTNER)) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.items, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_deliver, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return BaseActivity.CommonClass.HandleMenu(this, item.getItemId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Menu Activity");
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
    public void onBackPressed() {
        askforLOGOUT();
    }

    private void askforLOGOUT() {
        AlertDialogs.askforExit(MenuActivity.this);
    }
}
