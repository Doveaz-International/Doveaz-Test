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
import com.doveazapp.Adapters.SentResponseAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.ResponseDetails;
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
 * Created by Karthik on 2016/01/14.
 */
public class SentNotificationActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private List<ResponseDetails> responseDetailsList = new ArrayList<ResponseDetails>();

    private ListView listView;

    private SentResponseAdapter adapter;

    //session manager
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_notify_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Sent Notification screen");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        listView = (ListView) findViewById(R.id.friend_list);
        adapter = new SentResponseAdapter(SentNotificationActivity.this, responseDetailsList);
        listView.setAdapter(adapter);

        callAPI_togetResponseList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");

            }
        });
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(SentNotificationActivity.this);
    }

    private void callAPI_togetResponseList() {
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
                    JSONObject sent = values.getJSONObject("send");
                    JSONArray sentJSONArray = sent.getJSONArray("engagements");
                    Log.v("Negotiations!!!", sentJSONArray.toString());

                    for (int i = 0; i < sentJSONArray.length(); i++) {
                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            ResponseDetails responseDetails = new ResponseDetails();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);
                            // adding user to user array
                            responseDetails.setId(jsonobject.getString(Constants.KEY_ID));
                            responseDetails.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            responseDetails.setReceiver_id(jsonobject.getString(Constants.KEY_RECEIVER_ID));
                            responseDetails.setService_a_id(jsonobject.getString(Constants.KEY_SERVICE_A_ID));
                            responseDetails.setService_b_id(jsonobject.getString(Constants.KEY_SERVICE_B_ID));
                            // get user data from session
                            HashMap<String, String> user = session.getUserDetails();
                            String partner = user.get(SessionManager.KEY_USER_TYPE);
                            if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                                responseDetails.setDate(jsonobject.getString(Constants.KEY_DATE));
                            }
                            if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                                responseDetails.setTravel_date(jsonobject.getString(Constants.KEY_TRAVEL_DATE));
                            }
                            responseDetailsList.add(responseDetails);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Sent Notification screen");
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
