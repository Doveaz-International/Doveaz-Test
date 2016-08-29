package com.doveazapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * FriendsMenuPartnerActivity.java
 * Created by Karthik on 11/25/2015.
 */
public class FriendsMenuPartnerActivity extends AppCompatActivity implements View.OnClickListener {


    Button button_contacts, button_1stconnection, button_unknown;

    TextView text_contacts, text_1stconnection, text_unknown;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendsmenu_partner_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Partner Friends Menu");
        //buttons
        button_contacts = (Button) findViewById(R.id.button_contacts);
        button_1stconnection = (Button) findViewById(R.id.button_1stconnection);
        button_unknown = (Button) findViewById(R.id.button_unknown);
        //button_contacts1 = (Button) findViewById(R.id.button_contacts1);
        //button_1stconnection1 = (Button) findViewById(R.id.button_1stconnection1);
        //button_unknown1 = (Button) findViewById(R.id.button_unknown1);

        //Textviews
        text_contacts = (TextView) findViewById(R.id.text_contacts);
        text_1stconnection = (TextView) findViewById(R.id.text_1stconnection);
        text_unknown = (TextView) findViewById(R.id.text_unknown);
        /*text_contacts1 = (TextView) findViewById(R.id.text_contacts1);
        text_1stconnection1 = (TextView) findViewById(R.id.text_1stconnection1);
        text_unknown1 = (TextView) findViewById(R.id.text_unknown1);*/

        //button onclicks
        button_contacts.setOnClickListener(this);
        button_1stconnection.setOnClickListener(this);
        button_unknown.setOnClickListener(this);
        //button_contacts1.setOnClickListener(this);
        //button_1stconnection1.setOnClickListener(this);
        // button_unknown1.setOnClickListener(this);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        callAPI_toGetCounts();

    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(FriendsMenuPartnerActivity.this);
    }

    private void callAPI_toGetCounts() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT GET COUNTS--", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    if (status.equals("false")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        JSONObject contact_object = obj.getJSONObject("value");
                        if (contact_object != null) {
                            String contacts_verfied = contact_object.getString("contacts_verfied");
                            String first_contact_verified = contact_object.getString("first_contact_verified");
                            String unknown_contacts_verified = contact_object.getString("unknown_contacts_verified");

                            String contacts_not_verified = contact_object.getString("contacts_not_verified");
                            String first_contacts_not_verified = contact_object.getString("first_contacts_not_verified");
                            String unknown_contacts_not_verified = contact_object.getString("unknown_contacts_not_verified");

                            text_contacts.setText(contacts_verfied);
                            text_1stconnection.setText(first_contact_verified);
                            text_unknown.setText(unknown_contacts_verified);

                           /* text_contacts1.setText(contacts_not_verified);
                            text_1stconnection1.setText(first_contacts_not_verified);
                            text_unknown1.setText(unknown_contacts_not_verified);*/
                        }

                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
                // Toast.makeText(FriendsMenuPartnerActivity.this, response, Toast.LENGTH_LONG).show();
            }
        };

        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetfriendscount(this, Request.Method.POST, Constants.DISPATCH_COUNTS, listener, api_token);
    }

    @Override
    public void onClick(View view) {

        if (view == button_contacts) {
            Intent to_getContactList = new Intent(getApplicationContext(), PartnerTravelListActivity.class);
            to_getContactList.putExtra(Constants.KEY_CONTACT_VERIFIED, Constants.KEY_CONTACT_VERIFIED);
            startActivity(to_getContactList);
        }
        if (view == button_1stconnection) {
            Intent to_getContactList = new Intent(getApplicationContext(), PartnerTravelListActivity.class);
            to_getContactList.putExtra(Constants.KEY_FIRST_CONTACT_VERIFIED, Constants.KEY_FIRST_CONTACT_VERIFIED);
            startActivity(to_getContactList);
        }
        if (view == button_unknown) {
            Intent to_getContactList = new Intent(getApplicationContext(), PartnerTravelListActivity.class);
            to_getContactList.putExtra(Constants.KEY_UNKNOWN_VERIFIED, Constants.KEY_UNKNOWN_VERIFIED);
            startActivity(to_getContactList);
        }
        /*if (view == button_contacts1) {
            Intent to_getContactList = new Intent(getApplicationContext(), PartnerTravelListActivity.class);
            to_getContactList.putExtra(Constants.KEY_CONTACTS_NOT_VERIFIED, Constants.KEY_CONTACTS_NOT_VERIFIED);
            startActivity(to_getContactList);
        }*/
       /* if (view == button_1stconnection1) {
            Intent to_getContactList = new Intent(getApplicationContext(), PartnerTravelListActivity.class);
            to_getContactList.putExtra(Constants.KEY_FIRST_CONTACT_NOT_VERIFIED, Constants.KEY_FIRST_CONTACT_NOT_VERIFIED);
            startActivity(to_getContactList);
        }*/
       /* if (view == button_unknown1) {
            Intent to_getContactList = new Intent(getApplicationContext(), PartnerTravelListActivity.class);
            to_getContactList.putExtra(Constants.KEY_UNKNOWN_NOT_VERIFIED, Constants.KEY_UNKNOWN_NOT_VERIFIED);
            startActivity(to_getContactList);
        }*/
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
        MyApplication.getInstance().trackScreenView("Friends Menu Buy & Delivery");
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
}

