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

import com.android.volley.Request;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.MilestoneAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.MileStone;
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
 * Created by Karthik on 2016/02/02.
 */
public class MilestoneNotificationActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private List<MileStone> responseDetailsList = new ArrayList<MileStone>();

    private ListView listView;

    private MilestoneAdapter adapter;

    //session manager
    SessionManager session;

    //Serialized object
    //ResponseDetails user_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_notify_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Status Notification screen");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);
       /* // 1. get passed intent
        Intent intent = getIntent();
        // 2. get ProfileService object from intent
        user_details = (ResponseDetails) intent.getSerializableExtra("StatusDetails");*/


        listView = (ListView) findViewById(R.id.friend_list);
        adapter = new MilestoneAdapter(MilestoneNotificationActivity.this, responseDetailsList);
        listView.setAdapter(adapter);

        callAPI_togetStatusList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");

            }
        });
    }

    private void callAPI_togetStatusList() {
        progressDialog = ProgressDialog.show(MilestoneNotificationActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT MILESTONE--", response.toString());
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject userarray = obj.getJSONObject("value");
                    JSONArray valueobj = userarray.getJSONArray("final_agreement");

                    for (int i = 0; i < valueobj.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            //Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            MileStone mileStone = new MileStone();
                            JSONObject jsonobject = valueobj.getJSONObject(i);
                            mileStone.setId(jsonobject.getString(Constants.KEY_ID));
                            mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            mileStone.setDate_of_acceptance(jsonobject.getString(Constants.DATE_OF_ACCEPTANCE));
                            mileStone.setType_a_userid(jsonobject.getString(Constants.TYPE_A_USERID));
                            mileStone.setType_b_userid(jsonobject.getString(Constants.TYPE_B_USERID));
                            mileStone.setType_a_service_id(jsonobject.getString(Constants.TYPE_A_SERVICEID));
                            mileStone.setType_b_service_id(jsonobject.getString(Constants.TYPE_B_SERVICEID));
                            progressDialog.dismiss();
                            responseDetailsList.add(mileStone);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                    progressDialog.dismiss();
                }
            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("CallingAPI", Constants.GET_MILESTONE_LIST);
        ServiceCalls.callAPI_togetcategories(this, Request.Method.POST, Constants.GET_MILESTONE_LIST, listener, api_token);
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

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(MilestoneNotificationActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Status Notification screen");
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
