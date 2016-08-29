package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * AcceptCreditActivity.java
 * Created by Karthik on 2016/01/21.
 */
public class AcceptCreditActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_cconfirm, button_cancel;

    TextView text_value, text_fee, text_total;

    //session manager
    SessionManager session;

    String item_value, fee, reference_id, service_type;

    int fee_value = 0;

    int value_item = 0;

    int total_credits = 0;

    // Context
    Context _context;

    //Progress bar
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_credit_activity);

        menuvisibilityinAlldevices();
        appUtil = new ShareExternalServer();
        button_cconfirm = (Button) findViewById(R.id.button_confirm_fee);
        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cconfirm.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        text_value = (TextView) findViewById(R.id.text_value);
        text_fee = (TextView) findViewById(R.id.text_fee);
        text_total = (TextView) findViewById(R.id.text_total);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Transfer credit");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //get intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        item_value = bundle.getString(Constants.KEY_VALUE_ITEM);
        fee = bundle.getString(Constants.KEY_FEE);
        reference_id = bundle.getString(Constants.KEY_REFERENCE_ID);
        service_type = bundle.getString(Constants.KEY_SERVICE_TYPE);
        if (reference_id != null)
            Log.v("refereencee_idd", reference_id);

        setCreditsValue();
    }

    private void setCreditsValue() {
        if (item_value != null) {
            text_value.setText(item_value);
        } else {
            text_value.setText(Constants.NOT_AVAILABLE);
        }
        if (fee != null) {
            text_fee.setText(fee);
        } else {
            text_fee.setText(Constants.NOT_AVAILABLE);
        }
        if (fee != null) {
            fee_value = Integer.parseInt(fee);
        }
        if (item_value != null) {
            value_item = Integer.parseInt(item_value);
            total_credits = fee_value + value_item;
            text_total.setText(String.valueOf(total_credits));
        } else {
            text_total.setText(String.valueOf(fee));
        }
        if (String.valueOf(total_credits) == null) {
            text_total.setText(item_value);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == button_cconfirm) {
            call_transferCreditAPI();
        }
        if (v == button_cancel) {

        }
    }

    private void call_transferCreditAPI() {
        progressDialog = ProgressDialog.show(AcceptCreditActivity.this, Constants.PLEASE_WAIT, Constants.REQUESTING, true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT TRANSFER CREDIT", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_object = obj.getJSONObject(Constants.KEY_VALUE);

                    if (status.equals(Constants.KEY_FALSE)) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        String low_credit = value_object.getString(Constants.KEY_LOW_CREDIT);
                        if (low_credit.equals(Constants.KEY_ONE)) {
                            Intent to_purchase = new Intent(getApplicationContext(), PurchaseCreditActivity.class);
                            to_purchase.putExtra(Constants.KEY_TOTAL_CREDIT, text_total.getText().toString());
                            to_purchase.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                            to_purchase.putExtra(Constants.KEY_FEE, fee);
                            to_purchase.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                            startActivity(to_purchase);
                        }
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        progressDialog.dismiss();
                        String notify_userid = value_object.getString(Constants.KEY_NOTIFY_USERID);
                        HashMap<String, String> user = session.getUserDetails();
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has transfered credit successfully!");

                        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                            Intent to_milestone = new Intent(getApplicationContext(), CongratsFinalActivity.class);
                            to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                            to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                            to_milestone.putExtra(Constants.KEY_VALUE_ITEM, item_value);
                            to_milestone.putExtra(Constants.KEY_SUCCESS, Constants.KEY_SUCCESS);
                            startActivity(to_milestone);
                        } else if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                            Intent to_milestone = new Intent(getApplicationContext(), ViewMilestoneDeliverActivity.class);
                            to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                            to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                            startActivity(to_milestone);
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
        Log.v("Calling API", Constants.TRANSFER_CREDITS);
        ServiceCalls.CallAPI_to_transfer_credit(this, Request.Method.POST, Constants.TRANSFER_CREDITS, listener, text_total.getText().toString(), reference_id, api_token);
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

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(AcceptCreditActivity.this);
    }

    public void goto_home() {
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String partner = user.get(SessionManager.KEY_USER_TYPE);
        if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
            Intent i = new Intent(_context, MenuActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
            Intent i = new Intent(_context, AgentLocationActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /*@Override
    public void onBackPressed() {
        askfor_gohome();
    }

    public void askfor_gohome() {
        AlertDialogs.AcceptBack(AcceptCreditActivity.this);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Transfer credit");
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
