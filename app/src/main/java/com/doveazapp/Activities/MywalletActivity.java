package com.doveazapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Karthik on 2016/02/05.
 */
public class MywalletActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text_available_credits;

    // Session Manager Class
    SessionManager session;

    //Progress bar
    ProgressDialog progressDialog;

    Button button_withdraw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("My Wallet");

        text_available_credits = (TextView) findViewById(R.id.text_available_credits);
        button_withdraw = (Button) findViewById(R.id.button_withdraw);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        button_withdraw.setOnClickListener(this);

        call_checkCreditsapi();
    }

    private void call_checkCreditsapi() {
        progressDialog = ProgressDialog.show(MywalletActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT PURCHASE CREDIT", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        String current_credits = value_obj.getString(Constants.KEY_AVAILABLE_CREDIT);
                        text_available_credits.setText(current_credits);
                        progressDialog.dismiss();
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
        ServiceCalls.CallAPI_to_check_credits(this, Request.Method.POST, Constants.CHECK_USER_CREDITS, listener, api_token);
    }

    @Override
    public void onClick(View v) {
        if (v == button_withdraw) {
            withdraw_API();
        }
    }

    private void withdraw_API() {
        /* Alert Dialog Code Start*/
        AlertDialog.Builder alert = new AlertDialog.Builder(MywalletActivity.this);
        alert.setTitle("Credits Withdraw");
        alert.setMessage("Enter the credits you need to withdraw");

        // Set an EditText view to get user input
        final EditText input = new EditText(MywalletActivity.this);
        alert.setView(input);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String entered_value = input.getEditableText().toString();
                String available_credits = text_available_credits.getText().toString();
                int value = Integer.parseInt(entered_value);
                int avail_credits = Integer.parseInt(available_credits);
                if (value > avail_credits) {
                    Toast.makeText(getApplicationContext(), "Please enter value less than your credit balance", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog = ProgressDialog.show(MywalletActivity.this, "Please wait ...", "Requesting...", true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("OUTPUT ACCEPT IMAGE 1", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                final String status = obj.getString("status");
                                final String value = obj.getString("value");
                                if (status.equals("false")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                                } else if (status.equals("true")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
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
                    ServiceCalls.CallAPI_to_withdrawCredits(MywalletActivity.this, Request.Method.POST, Constants.WITHDRAW_CREDIT_API, listener, entered_value, api_token);
                }
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
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
        MenuVisibility.menuVisible(MywalletActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("My Wallet");
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
