package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * AgentAcceptDeclineActivity.java
 * Created by Karthik on 7/25/2016.
 */
public class AgentAcceptDeclineActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_accept, button_decline;

    ProgressDialog progressDialog;

    //session manager
    SessionManager session;

    String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_accept_decline);

        button_accept = (Button) findViewById(R.id.button_accept);
        button_decline = (Button) findViewById(R.id.button_decline);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("ACCEPT DECLINE_AGENT SCREEN");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        button_accept.setOnClickListener(this);
        button_decline.setOnClickListener(this);

        Call_API_to_getDetailsB();
    }

    private void Call_API_to_getDetailsB() {
        progressDialog = ProgressDialog.show(AgentAcceptDeclineActivity.this, Constants.PLEASE_WAIT, "Getting the details...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT GET DETAILS--", response.toString());
                progressDialog.dismiss();
                //Toast.makeText(EditDescription.this, response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    /*{"status":"false","value":{"message":"No order"}}*/
                    if (status.equals(Constants.KEY_FALSE)) {
                        progressDialog.dismiss();
                        JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                        String message = value_obj.getString(Constants.MESSAGE);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (message.equals("No order")) {
                            Toast.makeText(getApplicationContext(), "order expired", Toast.LENGTH_SHORT).show();
                        }
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            /*{"status":"true","value":{"order_id":"1469107387",
                            "pickup_address":",,
                            2\/136, Teacher's colony,vilankurichi road,hope college,,",
                            "delviery_address":"Karthik,,strgggg,area=Koramangala,8147152130,"}}*/

                        JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                        order_id = value_obj.getString(Constants.KEY_ORDER_ID);


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
        Log.v("Calling API", Constants.ORDER_CHECK_TYPE_B);
        ServiceCalls.CallAPI_to_check_credits(this, Request.Method.POST, Constants.ORDER_CHECK_TYPE_B, listener, api_token);
    }

    @Override
    public void onClick(View v) {
        if (v == button_accept) {
            progressDialog = ProgressDialog.show(AgentAcceptDeclineActivity.this, Constants.PLEASE_WAIT, "Accepting the order...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("--OUTPUT ACCEPT--", response.toString());
                    progressDialog.dismiss();
                    //Toast.makeText(EditDescription.this, response, Toast.LENGTH_LONG).show();
                    System.out.println(response.toString());
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString(Constants.KEY_STATUS);
                        final String value = obj.getString(Constants.KEY_VALUE);

                        if (status.equals(Constants.KEY_FALSE)) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "order accepted", Toast.LENGTH_SHORT).show();

                            /*Intent to_agent_menu = new Intent(getApplicationContext(), AgentMenuUpdateActivity.class);
                            startActivity(to_agent_menu);*/
                            finish();
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
            Log.v("Calling API", Constants.AGENT_ACCEPT_ORDER);
            Log.v("Order_id_for_test", order_id);
            ServiceCalls.CallAPI_to_Accept_Dispatch(this, Request.Method.POST, Constants.AGENT_ACCEPT_ORDER, listener, order_id, api_token);
        }
        if (v == button_decline) {
            progressDialog = ProgressDialog.show(AgentAcceptDeclineActivity.this, Constants.PLEASE_WAIT, "Declining the order...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("--OUTPUT DECLINE--", response.toString());
                    progressDialog.dismiss();
                    //Toast.makeText(EditDescription.this, response, Toast.LENGTH_LONG).show();
                    System.out.println(response.toString());
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString(Constants.KEY_STATUS);
                        final String value = obj.getString(Constants.KEY_VALUE);

                        if (status.equals(Constants.KEY_FALSE)) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "order rejected", Toast.LENGTH_SHORT).show();
                            finish();
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
            Log.v("Calling API", Constants.AGENT_DECLINE_ORDER);
            ServiceCalls.CallAPI_to_Accept_Dispatch(this, Request.Method.POST, Constants.AGENT_DECLINE_ORDER, listener, order_id, api_token);

        }
    }
}