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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Adapters.ListGroupsBuyDeliverAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.UserGroupInfo;
import com.doveazapp.Groups.GroupDetailsBuyandDeliverActivity;
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
 * FriendsMenuBuyDeliverActivity.java
 * Created by Karthik on 11/20/2015.
 */

/*Delivery -> buy& delivery -> buy& delivery (OK) -> this class*/
public class FriendsMenuBuyDeliverActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_contacts, button_1stconnection, button_unknown;

    TextView text_contacts, text_1stconnection, text_unknown, text_contacts1, text_1stconnection1, text_unknown1;

    Button verified_tab, courier_tab;

    String service_a_id;

    // Session Manager Class
    SessionManager session;

    //Progress dialog
    ProgressDialog progressDialog;

    LinearLayout verified_tab_layout, courier_tab_layout;

    private List<UserGroupInfo> GroupListDetails = new ArrayList<UserGroupInfo>();

    private ListView listView;

    private ListGroupsBuyDeliverAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendsmenu_buy_activity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            service_a_id = bundle.getString(Constants.KEY_SERVICE_A_ID);
        }

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Friends Menu Buy & Delivery");

        //Tab
        verified_tab = (Button) findViewById(R.id.verified_tab);
        courier_tab = (Button) findViewById(R.id.courier_tab);

        //buttons
        button_contacts = (Button) findViewById(R.id.button_contacts);
        button_1stconnection = (Button) findViewById(R.id.button_1stconnection);
        button_unknown = (Button) findViewById(R.id.button_unknown);
        /*button_contacts1 = (Button) findViewById(R.id.button_contacts1);
        button_1stconnection1 = (Button) findViewById(R.id.button_1stconnection1);
        button_unknown1 = (Button) findViewById(R.id.button_unknown1);*/

        //Textviews
        text_contacts = (TextView) findViewById(R.id.text_contacts);
        text_1stconnection = (TextView) findViewById(R.id.text_1stconnection);
        text_unknown = (TextView) findViewById(R.id.text_unknown);
        /*text_contacts1 = (TextView) findViewById(R.id.text_contacts1);
        text_1stconnection1 = (TextView) findViewById(R.id.text_1stconnection1);
        text_unknown1 = (TextView) findViewById(R.id.text_unknown1);*/
        verified_tab_layout = (LinearLayout) findViewById(R.id.verified_tab_layout);
        courier_tab_layout = (LinearLayout) findViewById(R.id.courier_tab_layout);

        //button onclicks
        button_contacts.setOnClickListener(this);
        button_1stconnection.setOnClickListener(this);
        button_unknown.setOnClickListener(this);
       /* button_contacts1.setOnClickListener(this);
        button_1stconnection1.setOnClickListener(this);
        button_unknown1.setOnClickListener(this);*/
        verified_tab.setOnClickListener(this);
        courier_tab.setOnClickListener(this);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Toast.makeText(getApplicationContext(), api_token + email, Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.group_list);
        adapter = new ListGroupsBuyDeliverAdapter(FriendsMenuBuyDeliverActivity.this, GroupListDetails);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");

                TextView groupId = (TextView) v.findViewById(R.id.group_id);
                String group_id = groupId.getText().toString();

                TextView business_status = (TextView) v.findViewById(R.id.text_business);
                String status_business = business_status.getText().toString();

                Intent to_groupDetails = new Intent(getApplicationContext(), GroupDetailsBuyandDeliverActivity.class);
                to_groupDetails.putExtra(Constants.KEY_GROUP_ID, group_id);
                to_groupDetails.putExtra(Constants.KEY_SERVICE_A_ID, service_a_id);
                to_groupDetails.putExtra(Constants.KEY_BUSINESS, status_business);
                startActivity(to_groupDetails);
            }
        });

        GroupListDetails.clear();
        callAPI_toGetCounts();

    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(FriendsMenuBuyDeliverActivity.this);
    }

    private void callAPI_toGetCounts() {
        progressDialog = ProgressDialog.show(FriendsMenuBuyDeliverActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT CALCULATION--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        JSONObject contact_object = obj.getJSONObject("value");
                        if (contact_object != null) {
                            String contacts_verfied = contact_object.getString(Constants.KEY_CONTACTS_VERIFIED);
                            String first_contact_verified = contact_object.getString(Constants.KEY_FIRST_CONTACT);
                            String unknown_contacts_verified = contact_object.getString(Constants.KEY_UNKNOWN);

                            /*String contacts_not_verified = contact_object.getString("contacts_not_verified");
                            String first_contacts_not_verified = contact_object.getString("first_contacts_not_verified");
                            String unknown_contacts_not_verified = contact_object.getString("unknown_contacts_not_verified");*/

                            text_contacts.setText(contacts_verfied);
                            text_1stconnection.setText(first_contact_verified);
                            text_unknown.setText(unknown_contacts_verified);

                           /* text_contacts1.setText(contacts_not_verified);
                            text_1stconnection1.setText(first_contacts_not_verified);
                            text_unknown1.setText(unknown_contacts_not_verified);*/
                        }
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling API", Constants.DISPATCH_COUNTS);
        ServiceCalls.callAPI_togetfriendscount(this, Request.Method.POST, Constants.DISPATCH_COUNTS, listener, api_token);
    }

    @Override
    public void onClick(View view) {
        if (view == verified_tab) {
            verified_tab.setFocusable(true);
            verified_tab.setFocusableInTouchMode(true);
            courier_tab.setFocusable(false);
            courier_tab.setFocusableInTouchMode(false);
            verified_tab_layout.setVisibility(View.VISIBLE);
            courier_tab_layout.setVisibility(View.GONE);
        }
        if (view == courier_tab) {
            courier_tab.setFocusable(true);
            courier_tab.setFocusableInTouchMode(true);
            verified_tab.setFocusable(false);
            verified_tab.setFocusableInTouchMode(false);
            courier_tab_layout.setVisibility(View.VISIBLE);
            verified_tab_layout.setVisibility(View.GONE);
            GroupListDetails.clear();
            callAPI_toGetGroupList();
        }
        if (view == button_contacts) {
            Intent to_getContactList = new Intent(getApplicationContext(), CheckPartnerListActivity.class);
            to_getContactList.putExtra(Constants.KEY_CONTACT_VERIFIED, Constants.KEY_CONTACT_VERIFIED);
            startActivity(to_getContactList);
        }
        if (view == button_1stconnection) {
            Intent to_getContactList = new Intent(getApplicationContext(), CheckPartnerListActivity.class);
            to_getContactList.putExtra(Constants.KEY_FIRST_CONTACT_VERIFIED, Constants.KEY_FIRST_CONTACT_VERIFIED);
            startActivity(to_getContactList);
        }
        if (view == button_unknown) {
            Intent to_getContactList = new Intent(getApplicationContext(), CheckPartnerListActivity.class);
            to_getContactList.putExtra(Constants.KEY_UNKNOWN_VERIFIED, Constants.KEY_UNKNOWN_VERIFIED);
            startActivity(to_getContactList);
        }
       /* if (view == button_contacts1) {
            Intent to_getContactList = new Intent(getApplicationContext(), CheckPartnerListActivity.class);
            to_getContactList.putExtra(Constants.KEY_CONTACTS_NOT_VERIFIED, Constants.KEY_CONTACTS_NOT_VERIFIED);
            startActivity(to_getContactList);
        }
        if (view == button_1stconnection1) {
            Intent to_getContactList = new Intent(getApplicationContext(), CheckPartnerListActivity.class);
            to_getContactList.putExtra(Constants.KEY_FIRST_CONTACT_NOT_VERIFIED, Constants.KEY_FIRST_CONTACT_NOT_VERIFIED);
            startActivity(to_getContactList);
        }
        if (view == button_unknown1) {
            Intent to_getContactList = new Intent(getApplicationContext(), CheckPartnerListActivity.class);
            to_getContactList.putExtra(Constants.KEY_UNKNOWN_NOT_VERIFIED, Constants.KEY_UNKNOWN_NOT_VERIFIED);
            startActivity(to_getContactList);
        }*/
    }

    private void callAPI_toGetGroupList() {
        progressDialog = ProgressDialog.show(FriendsMenuBuyDeliverActivity.this, "Please wait ...", "Loading groups...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                progressDialog.dismiss();
                Log.v("--OUTPUT CALCULATION--", response);
                try {
                    progressDialog.dismiss();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray group_array = value_obj.getJSONArray("groups");

                    for (int i = 0; i < group_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            JSONObject jsonobject = group_array.getJSONObject(i);
                            UserGroupInfo groupInfo = new UserGroupInfo();
                            groupInfo.setId(jsonobject.getString(Constants.KEY_ID));
                            groupInfo.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            groupInfo.setGroup_name(jsonobject.getString(Constants.KEY_GROUP_NAME));
                            groupInfo.setGroup_slogan(jsonobject.getString(Constants.KEY_GROUP_SLOGAN));
                            groupInfo.setImage(jsonobject.getString(Constants.KEY_IMAGE));
                            groupInfo.setStatus(jsonobject.getString(Constants.KEY_STATUS));
                            progressDialog.dismiss();

                            GroupListDetails.add(groupInfo);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling API", Constants.DISPATCH_COUNTS);
        ServiceCalls.callAPI_togetfriendscount(this, Request.Method.POST, Constants.DISPATCH_COUNTS, listener, api_token);
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
