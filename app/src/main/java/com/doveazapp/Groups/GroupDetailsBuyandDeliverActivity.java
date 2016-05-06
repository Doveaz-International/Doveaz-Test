package com.doveazapp.Groups;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.GroupMembers;
import com.doveazapp.GettersSetters.UserGroupInfo;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Karthik on 2016/02/18.
 */
public class GroupDetailsBuyandDeliverActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text_risk, text_admin, text_total, group_name, risk_score, admin, total;

    Button button_engage_group;

    NetworkImageView networkImageView;

    String group_id, service_a_id, business_text;

    // Session Manager Class
    SessionManager session;
    //Progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    int INT_MAX = 100;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details_buy_deliver);

        menuvisibilityinAlldevices();

        appUtil = new ShareExternalServer();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Group Details");

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        text_risk = (TextView) findViewById(R.id.text_risk);
        text_admin = (TextView) findViewById(R.id.text_admin);
        text_total = (TextView) findViewById(R.id.text_total);
        group_name = (TextView) findViewById(R.id.group_name);

        risk_score = (TextView) findViewById(R.id.risk_score1);
        admin = (TextView) findViewById(R.id.admin);
        total = (TextView) findViewById(R.id.total);


        button_engage_group = (Button) findViewById(R.id.button_engage_group);

        button_engage_group.setOnClickListener(this);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        networkImageView = (NetworkImageView) findViewById(R.id.group_image);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        group_id = bundle.getString(Constants.KEY_GROUP_ID);
        service_a_id = bundle.getString(Constants.KEY_SERVICE_A_ID);
        business_text = bundle.getString(Constants.KEY_BUSINESS);

        if (business_text.equals("Business")) {
            risk_score.setText("Business Score");
            admin.setText("Business Admin");
            total.setText("Delivery Network");
        } else {
            risk_score.setText("Risk Score");
            admin.setText("Group Admin");
            total.setText("Total Members");
        }
        callAPI_toGetDetails();
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(GroupDetailsBuyandDeliverActivity.this);
    }

    private void callAPI_toGetDetails() {
        progressDialog = ProgressDialog.show(GroupDetailsBuyandDeliverActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT GROUP DET - A", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray group_array = value_obj.getJSONArray("group_details");

                    for (int i = 0; i < group_array.length(); i++) {

                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        }
                        if (status.equals("true")) {
                            JSONObject jsonobject = group_array.getJSONObject(i);
                            UserGroupInfo groupInfo = new UserGroupInfo();
                            groupInfo.setId(jsonobject.getString(Constants.KEY_ID));
                            groupInfo.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            groupInfo.setGroup_name(jsonobject.getString(Constants.KEY_GROUP_NAME));
                            groupInfo.setGroup_slogan(jsonobject.getString(Constants.KEY_GROUP_SLOGAN));
                            groupInfo.setImage(jsonobject.getString(Constants.KEY_IMAGE));
                            groupInfo.setStatus(jsonobject.getString(Constants.KEY_STATUS));
                            groupInfo.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            groupInfo.setRisk_score(jsonobject.getString(Constants.KEY_RISKSCORE));
                            groupInfo.setMember_count(jsonobject.getString(Constants.KEY_GROUP_COUNT));

                            String risk_score = groupInfo.getRisk_score();
                            if (business_text.equals("Business")) {
                                int INT_RISK_SCORE = INT_MAX - Integer.parseInt(risk_score);
                                text_risk.setText(String.valueOf(INT_RISK_SCORE));
                            } else {
                                text_risk.setText(risk_score);
                            }

                            text_admin.setText(groupInfo.getFullname());
                            text_total.setText(groupInfo.getMember_count());
                            group_name.setText(groupInfo.getGroup_name());
                            networkImageView.setImageUrl(groupInfo.getImage(), imageLoader);
                        }
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
        Log.v("Calling API", Constants.GROUP_DETAILS_TYPE_A);
        ServiceCalls.CallAPI_toGetGroupDetailsA(this, Request.Method.POST, Constants.GROUP_DETAILS_TYPE_A, listener, group_id, api_token);
    }

    @Override
    public void onClick(View view) {
        if (view == button_engage_group) {
            API_AddService_togroup();
        }
    }

    private void API_AddService_togroup() {
        progressDialog = ProgressDialog.show(GroupDetailsBuyandDeliverActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT ADD TO GROUP", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");


                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    }
                    if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        call_toGetgroupDetails();
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
        Log.v("Calling API", Constants.ADD_SERVICE_TO_GROUP);
        ServiceCalls.CallAPI_service_addtoGroup(this, Request.Method.POST, Constants.ADD_SERVICE_TO_GROUP, listener, service_a_id, group_id, api_token);
    }

    private void call_toGetgroupDetails() {
        progressDialog = ProgressDialog.show(GroupDetailsBuyandDeliverActivity.this, "Please wait ...", "Loading List...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                progressDialog.dismiss();
                Log.v("--RESPONSE MEMBERS--", response.toString());
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray moderator_array = value_obj.getJSONArray("moderator");
                    JSONArray service_array = value_obj.getJSONArray("members");

                    for (int i = 0; i < service_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();

                            GroupMembers groupMembers = new GroupMembers();
                            JSONObject jsonobject = service_array.getJSONObject(i);
                            groupMembers.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            String userIds = groupMembers.getUserid();
                            HashMap<String, String> user = session.getUserDetails();
                            // token
                            String u_name = user.get(SessionManager.KEY_USERNAME);
                            sendMessageToGCMAppServer(userIds, u_name + " " + "has posted a service to your group!");
                        }
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
        Log.v("calling api", Constants.GROUP_MEMBERS_LIST);
        ServiceCalls.CallAPI_tolistservice_group(this, Request.Method.POST, Constants.GROUP_MEMBERS_LIST, listener, group_id, api_token);

    }

    private void sendMessageToGCMAppServer(final String toUserId,
                                           final String messageToSend) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                String result = appUtil.sendMessage(toUserId, messageToSend, api_token);
                Log.d("MainActivity", "Result: " + result);
                return result;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("MainActivity", "Result: " + msg);
                /*Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
                        .show();*/
            }
        }.execute(null, null, null);
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
        MyApplication.getInstance().trackScreenView("Group Details");
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
