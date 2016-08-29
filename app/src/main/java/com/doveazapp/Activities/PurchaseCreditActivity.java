package com.doveazapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karthik on 2016/01/29.
 */
public class PurchaseCreditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Purchase Credit";

    // Session Manager Class
    SessionManager session;

    String total_credit, reference_id, fee, service_type;

    //Progress bar
    ProgressDialog progressDialog;

    TextView total_credits, check_credit, credit_needed;

    Button button_confirm_fee, button_cancel;

    // int's for calculation
    int tot_credits = 0;

    int available_credit = 0;

    int needed_credits = 0;

    // Context
    Context _context;

    //For GCM
    ShareExternalServer appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_credit_activity);

        //Textviews
        total_credits = (TextView) findViewById(R.id.total_credit);
        check_credit = (TextView) findViewById(R.id.check_credit);
        credit_needed = (TextView) findViewById(R.id.credit_needed);

        //Buttons
        button_confirm_fee = (Button) findViewById(R.id.button_confirm_fee);
        button_cancel = (Button) findViewById(R.id.button_cancel);

        //button listeners
        button_confirm_fee.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        menuvisibilityinAlldevices();

        appUtil = new ShareExternalServer();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Purchase Credit screen");

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //get intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        total_credit = bundle.getString(Constants.KEY_TOTAL_CREDIT);
        reference_id = bundle.getString(Constants.KEY_REFERENCE_ID);
        fee = bundle.getString(Constants.KEY_FEE);
        service_type = bundle.getString(Constants.KEY_SERVICE_TYPE);

        if (reference_id != null)
            checkCredit();
    }

    private void checkCredit() {
        progressDialog = ProgressDialog.show(PurchaseCreditActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT CHECK CREDIT", response);
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
                        total_credits.setText(total_credit);
                        check_credit.setText(current_credits);

                        // calculation user_credit - total_credits
                        tot_credits = Integer.parseInt(total_credit);
                        available_credit = Integer.parseInt(current_credits);
                        needed_credits = tot_credits - available_credit;

                        credit_needed.setText(String.valueOf(needed_credits));
                        progressDialog.dismiss();
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
        ServiceCalls.CallAPI_to_check_credits(this, Request.Method.POST, Constants.CHECK_USER_CREDITS, listener, api_token);
    }


    @Override
    public void onClick(View v) {
        if (v == button_confirm_fee) {
            //CallAPI_to_purchase_credit();
            callAPI_tocheckAdminBalance();
            //call_paypal_sdk();
        }
        if (v == button_cancel) {

        }
    }

    private void callAPI_tocheckAdminBalance() {
        progressDialog = ProgressDialog.show(PurchaseCreditActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT CHECK ADMIN", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        /* for future use*/ //call_paypal_sdk();
                        //alert_InsteadOfPaypal();

                        choose_paymentmethod();

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
        ServiceCalls.CallAPI_to_check_admin_balance(this, Request.Method.POST, Constants.CHECK_ADMIN_BALANCE, listener, String.valueOf(needed_credits), api_token);
    }

    private void choose_paymentmethod() {
        final CharSequence[] options_payment = {"Paypal", "Paytm (Only for India)"};

        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseCreditActivity.this);
        builder.setTitle("Choose a payment option");
        builder.setItems(options_payment, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (options_payment[item].equals("Paypal")) {
                    //alert_InsteadOfPaypal();
                    //call_paypal_sdk();
                } else if (options_payment[item].equals("Paytm (Only for India)")) {
                    call_paytm_sdk();
                }
            }
        });
        AlertDialog alert = builder.create();

        alert.show();
    }

    private void alert_InsteadOfPaypal() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(PurchaseCreditActivity.this);
        alertbox.setTitle("Insufficient funds");
        alertbox.setMessage("Please contact support@doveaz.com");
        alertbox.setPositiveButton("ok", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.show();
    }

    private void call_paytm_sdk() {
        String needed_credits = credit_needed.getText().toString();
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", "");
        paramMap.put("MID", "WorldP64425807474247");
        paramMap.put("CUST_ID", "");
        paramMap.put("CHANNEL_ID", "");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "www.doveaz.com");
        paramMap.put("TXN_AMOUNT", needed_credits);
        paramMap.put("THEME", "merchant");
        paramMap.put("EMAIL", "support@doveaz.com");
        paramMap.put("MOBILE_NO", "");
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }

    public void CallAPI_to_purchase_credit() {
        progressDialog = ProgressDialog.show(PurchaseCreditActivity.this, "Please wait ...", "Requesting...", true);
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
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        openAlert_purchase();
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
        String needed_credits = credit_needed.getText().toString();
        ServiceCalls.CallAPI_to_purchase_credits(this, Request.Method.POST, Constants.PURCHASE_CREDITS, listener, needed_credits, api_token);

    }

    public void call_transferCreditAPI() {
        progressDialog = ProgressDialog.show(PurchaseCreditActivity.this, "Please wait ...", "Requesting...", true);
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
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_object = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        String low_credit = value_object.getString(Constants.KEY_LOW_CREDIT);
                        if (low_credit.equals("1")) {
                            Intent to_purchase = new Intent(getApplicationContext(), PurchaseCreditActivity.class);
                            to_purchase.putExtra(Constants.KEY_TOTAL_CREDIT, total_credit);
                            to_purchase.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                            startActivity(to_purchase);
                        }
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), fee, Toast.LENGTH_SHORT).show();
                        HashMap<String, String> user = session.getUserDetails();
                        String notify_userid = value_object.getString(Constants.KEY_NOTIFY_USERID);
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has transfered credit successfully!");

                        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                            Intent to_milestone = new Intent(getApplicationContext(), CongratsFinalActivity.class);
                            to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                            to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                            to_milestone.putExtra(Constants.KEY_SUCCESS, "SUCCESS");
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
        Log.v("From", "purchase credit");
        ServiceCalls.CallAPI_to_transfer_credit(this, Request.Method.POST, Constants.TRANSFER_CREDITS, listener, total_credit, reference_id, api_token);
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


    private void openAlert_purchase() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(PurchaseCreditActivity.this);
        alertbox.setTitle("Confirm Transfer");
        alertbox.setMessage("Do you wish to Transfer credit and goto milestone?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        call_transferCreditAPI();

                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
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
        MenuVisibility.menuVisible(PurchaseCreditActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Purchase Credit screen");
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
