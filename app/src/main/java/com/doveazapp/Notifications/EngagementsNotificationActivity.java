package com.doveazapp.Notifications;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.EngageAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.EngageNegotiate;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/01/15.
 */
public class EngagementsNotificationActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private List<EngageNegotiate> responseDetailsList = new ArrayList<EngageNegotiate>();

    private ListView listView;

    private EngageAdapter adapter;

    //session manager
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engagements_notify_listview);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Engage Notification screen");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        listView = (ListView) findViewById(R.id.engage_list);
        adapter = new EngageAdapter(EngagementsNotificationActivity.this, responseDetailsList);
        listView.setAdapter(adapter);

        callAPI_togetEngagementlist();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");

            }
        });
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(EngagementsNotificationActivity.this);
    }

    private void callAPI_togetEngagementlist() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE LIST--", response.toString());
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject values = obj.getJSONObject("value");
                    Log.v("VALUEEE", values.toString());
                    JSONObject sent = values.getJSONObject("response");
                    JSONArray sentJSONArray = sent.getJSONArray("engagements");
                    Log.v("Negotiations!!!", sentJSONArray.toString());

                    for (int i = 0; i < sentJSONArray.length(); i++) {
                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            EngageNegotiate engageNegotiate = new EngageNegotiate();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);
                            // adding user to user array
                            engageNegotiate.setId(jsonobject.getString(Constants.KEY_ID));
                            engageNegotiate.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            engageNegotiate.setReceiver_id(jsonobject.getString(Constants.KEY_RECEIVER_ID));
                            engageNegotiate.setService_a_id(jsonobject.getString(Constants.KEY_SERVICE_A_ID));
                            engageNegotiate.setService_b_id(jsonobject.getString(Constants.KEY_SERVICE_B_ID));
                            engageNegotiate.setSender_id(jsonobject.getString(Constants.KEY_SENDER_ID));
                            // get user data from session
                            HashMap<String, String> user = session.getUserDetails();
                            String partner = user.get(SessionManager.KEY_USER_TYPE);
                            if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                                engageNegotiate.setDate(jsonobject.getString(Constants.KEY_DATE));
                            }
                            if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                                engageNegotiate.setTravel_date(jsonobject.getString(Constants.KEY_TRAVEL_DATE));
                            }
                            responseDetailsList.add(engageNegotiate);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }

            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("fomighm", api_token);
        ServiceCalls.CallAPI_togetResponse(this, Request.Method.POST, Constants.NOTIFICATION_RESPONSE, listener, api_token);
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
}
