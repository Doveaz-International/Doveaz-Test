package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Adapters.UserDetailsAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.UserDetails;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * CheckPartnerListActivity.java
 * Created by Karthik on 11/23/2015.
 */


public class CheckPartnerListActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private List<UserDetails> userDetailsList = new ArrayList<UserDetails>();

    private ListView listView;

    private UserDetailsAdapter adapter;

    SessionManager session;

    String contact_verified, first_contact_verified, unknown_verified, contact_not_verified, first_contact_not_verified, unknown_not_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkpartnerlist_activity);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Check Partner List");

        menuvisibilityinAlldevices();

        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // GET INTENT FROM PREVIOUS CLASS
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        contact_verified = bundle.getString(Constants.KEY_CONTACT_VERIFIED);
        first_contact_verified = bundle.getString(Constants.KEY_FIRST_CONTACT_VERIFIED);
        unknown_verified = bundle.getString(Constants.KEY_UNKNOWN_VERIFIED);
        contact_not_verified = bundle.getString(Constants.KEY_CONTACTS_NOT_VERIFIED);
        first_contact_not_verified = bundle.getString(Constants.KEY_FIRST_CONTACT_NOT_VERIFIED);
        unknown_not_verified = bundle.getString(Constants.KEY_UNKNOWN_NOT_VERIFIED);

        if (contact_verified != null) {
            callAPI_togetcontactsNotVerified();
        } else if (first_contact_verified != null) {
            callAPI_togetcontactsNotVerified();
        } else if (unknown_verified != null) {
            callAPI_togetcontactsNotVerified();
        } else if (contact_not_verified != null) {
            callAPI_togetcontactsNotVerified();
        } else if (first_contact_not_verified != null) {
            callAPI_togetcontactsNotVerified();
        } else if (unknown_not_verified != null) {
            callAPI_togetcontactsNotVerified();
        }


        listView = (ListView) findViewById(R.id.friend_list);
        adapter = new UserDetailsAdapter(CheckPartnerListActivity.this, userDetailsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
               /* Toast.makeText(CheckPartnerListActivity.this,
                        "List View Clicked:" + position, Toast.LENGTH_LONG)
                        .show();*/
                TextView user_id = (TextView) v.findViewById(R.id.userid);
                TextView service_id = (TextView) v.findViewById(R.id.service_id);
                //TextView risk_score = (TextView) v.findViewById(R.id.txt_risk);
                String userId = user_id.getText().toString();
                String serviceId = service_id.getText().toString();
                //String riskscore = risk_score.getText().toString();

                Intent to_details = new Intent(getApplicationContext(), CheckPartnerDetailsActivity.class);
                to_details.putExtra(Constants.KEY_USERID, userId);
                to_details.putExtra(Constants.KEY_SERVICEID, serviceId);
                //to_details.putExtra(Constants.KEY_RISKSCORE, riskscore);
                startActivity(to_details);
            }
        });
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CheckPartnerListActivity.this);
    }

    private void callAPI_togetcontactsNotVerified() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT CONTACT LIST--", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONArray userarray = obj.getJSONArray("value");

                    for (int i = 0; i < userarray.length(); i++) {
                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            JSONObject jsonobject = userarray.getJSONObject(i);
                            UserDetails userDetails = new UserDetails();
                            userDetails.setName(jsonobject.getString(Constants.KEY_NAME));
                            userDetails.setTravel_date(jsonobject.getString(Constants.KEY_TRAVEL_DATE));
                            userDetails.setRisk_score(jsonobject.getString(Constants.KEY_RISKSCORE));
                            userDetails.setProfile_pic(jsonobject.getString(Constants.KEY_PROFILE_PIC));
                            userDetails.setId(jsonobject.getString(Constants.KEY_ID));
                            userDetails.setService_id(jsonobject.getString(Constants.KEY_SERVICEID));
                            // adding movie to movies array
                            userDetailsList.add(userDetails);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
                adapter.notifyDataSetChanged();
            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        if (contact_verified != null) {
            ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, contact_verified, api_token);
        } else if (first_contact_verified != null) {
            ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, first_contact_verified, api_token);
        } else if (unknown_verified != null) {
            ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, unknown_verified, api_token);
        } else if (contact_not_verified != null) {
            ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, contact_not_verified, api_token);
        } else if (first_contact_not_verified != null) {
            ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, first_contact_not_verified, api_token);
        } else if (unknown_not_verified != null) {
            ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, unknown_not_verified, api_token);
        }
        // ServiceCalls.CallAPI_togetuserList(this, Request.Method.POST, Constants.GET_LIST_B_PROFILES, listener, contact_not_verified, api_token);
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
        MyApplication.getInstance().trackScreenView("Check Partner List");
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


