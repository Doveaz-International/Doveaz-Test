package com.doveazapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Adapters.ReviewOrderAdapter;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.AlertDialogs;
import com.doveazapp.GettersSetters.OrderReviewInfo;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.SqliteManager.AddedCartDBHelper;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * OrderReviewActivity.java
 * Created by Karthik on 6/16/2016.
 */

public class OrderReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = OrderReviewActivity.class.getName();

    private AlertDialog.Builder build;
    private String price, fee, order_id, order_type, deliver_st, deliver_area, deliver_city, deliver_state, deliver_zip,
            address_type, result, store_id, delivery_phone, delivery_name, delivery_address;

    TextView txtprice, txtfee, text_total;

    Button checkout_button, button_cancel;

    ProgressDialog progressDialog;

    // Session Manager Class
    SessionManager session;

    int total;
    String current_credits;

    private ArrayList<OrderReviewInfo> responseDetailsList = new ArrayList<OrderReviewInfo>();

    private ListView listView;

    private ReviewOrderAdapter adapter;

    AddedCartDBHelper cartDBHelper;
    SQLiteDatabase sqldb;
    private String payment_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_review_activity);

        menuvisibilityinAlldevices();

        listView = (ListView) findViewById(R.id.List);
        txtprice = (TextView) findViewById(R.id.price);
        txtfee = (TextView) findViewById(R.id.fee);
        text_total = (TextView) findViewById(R.id.text_total);
        checkout_button = (Button) findViewById(R.id.checkout_button);
        button_cancel = (Button) findViewById(R.id.button_cancel);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        /*price = bundle.getString(Constants.KEY_TOTAL_PRICE);
        fee = bundle.getString(Constants.KEY_TIP_FEE);
        order_id = bundle.getString(Constants.KEY_ORDER_ID);*/
        order_type = bundle.getString(Constants.KEY_ORDER_TYPE, order_type);
        deliver_st = bundle.getString(Constants.KEY_DELIVERY_STREET, deliver_st);
        deliver_area = bundle.getString(Constants.KEY_DELIVERY_AREA, deliver_area);
        deliver_city = bundle.getString(Constants.KEY_DELIVERY_CITY, deliver_city);
        deliver_state = bundle.getString(Constants.KEY_DELIVERY_STATE, deliver_state);
        deliver_zip = bundle.getString(Constants.KEY_DELIVERY_ZIP, deliver_zip);
        address_type = bundle.getString(Constants.KEY_ADDRESS_TYPE, address_type);
        result = bundle.getString(Constants.KEY_RESULT, result);
        store_id = bundle.getString(Constants.KEY_STOREID, store_id);
        delivery_phone = bundle.getString(Constants.KEY_DELIVERY_PHONE, delivery_phone);
        delivery_name = bundle.getString(Constants.KEY_DELIVERY_NAME, delivery_name);
        delivery_address = bundle.getString(Constants.KEY_DELIVERY_ADDRESS, delivery_address);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String email = user.get(SessionManager.KEY_EMAIL);

        adapter = new ReviewOrderAdapter(OrderReviewActivity.this, responseDetailsList);
        listView.setAdapter(adapter);

        checkout_button.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        cartDBHelper = new AddedCartDBHelper(this);
        sqldb = cartDBHelper.getWritableDatabase();

        CallAPI_to_addorder();
    }

    private void CallAPI_to_addorder() {
        progressDialog = ProgressDialog.show(OrderReviewActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE ADD ORDER--", response);
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                        JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                        String price = value_obj.getString(Constants.KEY_TOTAL_PRICE);
                        String fee = value_obj.getString(Constants.KEY_TIP_FEE);
                        order_id = value_obj.getString(Constants.KEY_ORDER_ID);

                        txtprice.setText(price);
                        txtfee.setText(fee);
                        total = Integer.parseInt(price) + Integer.parseInt(fee);
                        text_total.setText(String.valueOf(total));

                        JSONArray order_array = value_obj.getJSONArray(Constants.KEY_ORDER_ITEMS);
                        for (int i = 0; i < order_array.length(); i++) {
                            JSONObject jsonobject = order_array.getJSONObject(i);
                            OrderReviewInfo order_details = new OrderReviewInfo();
                            // adding user to user array
                            order_details.setProduct_id(jsonobject.getString(Constants.KEY_PRODUCT_ID));
                            order_details.setProduct_name(jsonobject.getString(Constants.KEY_PRODUCT_NAME));
                            order_details.setQuantity(jsonobject.getString(Constants.KEY_PRODUCT_QUANTITY));
                            order_details.setUnit_price(jsonobject.getString(Constants.KEY_UNIT_PRICE));

                            responseDetailsList.add(order_details);
                            adapter.notifyDataSetChanged();

                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        if (address_type.equals("0")) { //for manualpay
            Log.v("Calling API", Constants.ADD_ORDER);
            ServiceCalls.Call_api_toAddOrderBD(this, Request.Method.POST, Constants.ADD_ORDER, listener, order_type, delivery_address, deliver_st,
                    deliver_area, deliver_city, deliver_state, "India", deliver_zip,
                    address_type, result, store_id, delivery_phone, delivery_name, api_token);
        } else {
            Log.v("Calling API", Constants.ADD_ORDER);
            ServiceCalls.Call_api_toAddOrderBD(this, Request.Method.POST, Constants.ADD_ORDER, listener, order_type, delivery_address, deliver_st,
                    deliver_area, deliver_city, deliver_state, "India", deliver_zip,
                    address_type, result, store_id, delivery_phone, delivery_name, api_token);
        }
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(OrderReviewActivity.this);

    }

    private void calculate_billing_price() {


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == checkout_button) {
            //choose_paymentmethod();
            call_checkCreditsapi();
        }
        if (v == button_cancel) {
            Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(myIntent);
        }
    }

    private void call_checkCreditsapi() {
        progressDialog = ProgressDialog.show(OrderReviewActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT CHECK CREDIT", response);
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
                        current_credits = value_obj.getString(Constants.KEY_AVAILABLE_CREDIT);

                        if (Integer.parseInt(current_credits) < total) {
                            Toast.makeText(getApplicationContext(), "Your wallet balance is low please purchase credits", Toast.LENGTH_SHORT).show();
                            CallAPI_to_purchase_credit();
                        } else {

                            call_transferCreditAPI();
                            //call_transferCreditAPI();
                        }

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
        Log.v("Calling Api", Constants.CHECK_USER_CREDITS);
        ServiceCalls.CallAPI_to_check_credits(this, Request.Method.POST, Constants.CHECK_USER_CREDITS, listener, api_token);
    }

    private void choose_paymentmethod() {
        final CharSequence[] options_payment = {"Cash on delivery (COD)", "Paytm (Only for India)"};

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderReviewActivity.this);
        builder.setTitle("Choose a payment option");
        builder.setItems(options_payment, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (options_payment[item].equals("Cash on delivery (COD)")) {
                    call_cod();
                } else if (options_payment[item].equals("Paytm (Only for India)")) {
                    call_paytm_sdk();
                }
            }
        });
        AlertDialog alert = builder.create();

        alert.show();
    }

    private void call_cod() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(OrderReviewActivity.this);
        alertbox.setTitle("Confirm Order");
        alertbox.setMessage("your order will be placed once you click yes!!!");
        alertbox.setPositiveButton("Confirm", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Call_createOrder_API();
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    private void Call_createOrder_API() {
        progressDialog = ProgressDialog.show(OrderReviewActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT CREATE ORDER", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                        goto_loading_screen();

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
        Log.v("Calling API", Constants.CREATE_ORDER);
        payment_type = "COD";
        ServiceCalls.CallAPI_to_CreateOrder(this, Request.Method.POST, Constants.CREATE_ORDER, listener, order_id, text_total.getText().toString(), payment_type, api_token);
    }

    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }

    private double getAmount() {
        int total = Integer.parseInt(price) + Integer.parseInt(fee);
        Double amount = Double.valueOf(total);
        return amount;
    }

    private void call_paytm_sdk() {
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String phone = user.get(SessionManager.KEY_PHONE_NUM);
        Log.v("phone", phone);
        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();
        builder.setAmount(Double.valueOf(fee))
                .setTnxId(getTxnId())
                .setPhone(phone)
                .setProductName("product_name")
                .setFirstName(user.get(SessionManager.KEY_USERNAME))
                .setEmail(user.get(SessionManager.KEY_EMAIL))
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(true)
                .setKey(Constants.PAY_U_MONEY_KEY)
                .setMerchantId(Constants.PAY_U_MONEY_MERCHANT_ID);// Debug Merchant ID

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);
    }

    public void CallAPI_to_purchase_credit() {
        progressDialog = ProgressDialog.show(OrderReviewActivity.this, "Please wait ...", "Requesting...", true);
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
        ServiceCalls.CallAPI_to_purchase_credits(this, Request.Method.POST, Constants.PURCHASE_CREDITS, listener, String.valueOf(total), api_token);
    }

    private void openAlert_purchase() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(OrderReviewActivity.this);
        alertbox.setTitle("Confirm Purchase");
        alertbox.setMessage("Do you wish to buy " + String.valueOf(total) + " credits?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                       /* call_transferCreditAPI();*/
                        choose_paymentmethod();
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    private void call_transferCreditAPI() {
        progressDialog = ProgressDialog.show(OrderReviewActivity.this, "Please wait ...", "Requesting...", true);
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
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    } else if (status.equals("true")) {

                        String message = value_object.getString("message");
                        String reference_id = value_object.getString("reference_id");
                        String credit_holder_id = value_object.getString("credit_holder_id");

                        //goto_loading_screen();
                        Call_createOrder_API();

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
        Log.v("Calling API", Constants.TRANSFER_CREDITS);
        ServiceCalls.CallAPI_to_transfer_credit(this, Request.Method.POST, Constants.TRANSFER_CREDITS, listener, text_total.getText().toString(), order_id, api_token);
    }

    private void goto_loading_screen() {
        Intent to_loading_screen = new Intent(getApplicationContext(), LoadingAgentActivity.class);
        to_loading_screen.putExtra(Constants.KEY_ORDER_ID, order_id);
        startActivity(to_loading_screen);
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        /*progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);*/
        AlertDialogs.showProgress(OrderReviewActivity.this);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT HASH CALCULATION", response);
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
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        //Call_createOrder_API();

                        String hash = value_object.getString("hash");
                        Log.i("app_activity", "Server calculated Hash :  " + hash);
                        paymentParam.setMerchantHash(hash);

                        PayUmoneySdkInitilizer.startPaymentActivityForResult(OrderReviewActivity.this, paymentParam);

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
        Log.v("Calling API", Constants.CALCULATE_HASH);
        ServiceCalls.CallAPI_to_Calculate_hash(this, Request.Method.POST, Constants.CALCULATE_HASH, listener, Constants.PAY_U_MONEY_KEY, getTxnId(), fee, "collection_category", Constants.PAY_U_MONEY_SALT_KEY, api_token);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                AlertDialogs.showDialogMessage("Payment Success Id : " + paymentId, OrderReviewActivity.this);
                call_transferCreditAPI();
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                AlertDialogs.showDialogMessage("cancelled", OrderReviewActivity.this);
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        AlertDialogs.showDialogMessage("failure", OrderReviewActivity.this);
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                AlertDialogs.showDialogMessage("User returned without login", OrderReviewActivity.this);
            }
        }
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
    public void onBackPressed() {

    }
}
