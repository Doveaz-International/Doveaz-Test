package com.doveazapp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.AlertDialogs;
import com.doveazapp.GettersSetters.ViewAgentDelivery;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.SqliteManager.AddedCartDBHelper;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.doveazapp.Utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * MenuActivity.java
 * Created by Karthik on 11/16/2015.
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btn_collection, btn_send;

    LinearLayout btn_buy;

    // Session Manager Class
    static SessionManager session;

    //Progress bar
    ProgressDialog progressDialog;

    String deliver_loggedin;

    static String profile_pic_url, username, userid, delivery_status;

    AddedCartDBHelper cartDBHelper;
    SQLiteDatabase sqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Menu Activity");
        // Session class instance
        session = new SessionManager(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String name = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        cartDBHelper = new AddedCartDBHelper(this);
        sqldb = cartDBHelper.getWritableDatabase();

        //Toast.makeText(getApplicationContext(), name + email, Toast.LENGTH_LONG).show();

        // GET INTENT FROM PREVIOUS CLASS
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            deliver_loggedin = bundle.getString(Constants.LOGIN_DELIVER);
            if (deliver_loggedin != null) {
                Log.v("Login_partner", deliver_loggedin);
                enterPassword();
            }
        }

        // Buttons init
        btn_buy = (LinearLayout) findViewById(R.id.buy_btn);
        btn_collection = (LinearLayout) findViewById(R.id.collection_btn);
        btn_send = (LinearLayout) findViewById(R.id.send_btn);

        // Onclicks for buttons
        btn_buy.setOnClickListener(this);
        btn_collection.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    private void enterPassword() {
        /* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
        alert.setTitle(R.string.password);
        alert.setIcon(R.drawable.lock);
        alert.setMessage("Please enter your password, for security reasons");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(MenuActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String entered_value = input.getEditableText().toString();
                if (!Utils.isOnline(MenuActivity.this)) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    enterPassword();
                } else if (entered_value.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_LONG).show();
                    enterPassword();
                } else {
                    progressDialog = ProgressDialog.show(MenuActivity.this, Constants.PLEASE_WAIT, Constants.LOADING, true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("OUTPUT AUTHENTICATE", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                final String status = obj.getString(Constants.KEY_STATUS);
                                final String value = obj.getString(Constants.KEY_VALUE);
                                if (status.equals(Constants.KEY_FALSE)) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();
                                    if (value.equals("You are not logged in")) {
                                        to_loginActivity();
                                    } else {
                                        enterPassword();
                                    }
                                } else if (status.equals(Constants.KEY_TRUE)) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MenuActivity.this, value, Toast.LENGTH_SHORT).show();

                                    Call_API_to_check_orders();
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
                    ServiceCalls.CallAPI_to_sessionLogin(MenuActivity.this, Request.Method.POST, Constants.AUTHENTICATE_USER, listener, entered_value, api_token);
                }
            }

            private void Call_API_to_check_orders() {
                progressDialog = ProgressDialog.show(MenuActivity.this, Constants.PLEASE_WAIT, Constants.LOADING, true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("OUTPUT CHECK ORDER", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString(Constants.KEY_STATUS);
                            final String value = obj.getString(Constants.KEY_VALUE);

                            if (status.equals(Constants.KEY_FALSE)) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                            } else if (status.equals(Constants.KEY_TRUE)) {
                                progressDialog.dismiss();
                                JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                                String flag = value_obj.getString("flag");
                                //Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_LONG).show();
                                if (flag.equals(Constants.KEY_ONE)) {
                                    call_api_to_getAgent();
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
                Log.v("Calling API", Constants.ORDER_CHECK);
                ServiceCalls.callAPI_togetcategories(MenuActivity.this, Request.Method.POST, Constants.ORDER_CHECK, listener, api_token);
            }

        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void to_loginActivity() {
        Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(to_login);
    }

    private void Save_session() {

    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(MenuActivity.this);
    }

    @Override
    public void onClick(View view) {

        if (btn_buy == view) {
            /*getApplicationContext().deleteDatabase(AddedCartDBHelper.DATABASE_NAME);*/
            Intent buytologin = new Intent(getApplicationContext(), BuyandDeliveryActivity.class);
            startActivity(buytologin);
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("Buy&Delivery Button", "In Deliver Menu", "B&D");
            /*progressDialog = ProgressDialog.show(MenuActivity.this, "Please wait ...", "Requesting...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("OUTPUT CHECK ORDER", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString("status");
                        final String value = obj.getString("value");

                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            JSONObject value_obj = obj.getJSONObject("value");
                            String flag = value_obj.getString("flag");
                            //Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_LONG).show();
                            if (flag.equals("1")) {
                                Toast.makeText(getApplicationContext(), "your previous order is in process, please contact support", Toast.LENGTH_LONG).show();
                            } else {
                                Intent buytologin = new Intent(getApplicationContext(), BuyandDeliveryActivity.class);
                                startActivity(buytologin);
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
            Log.v("Calling API", Constants.ORDER_CHECK);
            ServiceCalls.callAPI_togetcategories(MenuActivity.this, Request.Method.POST, Constants.ORDER_CHECK, listener, api_token);*/
        }
        if (btn_collection == view) {
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("Collection Button", "In Deliver Menu", "B&D");
            /*progressDialog = ProgressDialog.show(MenuActivity.this, Constants.PLEASE_WAIT, Constants.LOADING, true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("OUTPUT CHECK ORDER", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString(Constants.KEY_STATUS);
                        final String value = obj.getString(Constants.KEY_VALUE);

                        if (status.equals(Constants.KEY_FALSE)) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            progressDialog.dismiss();
                            JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                            String flag = value_obj.getString(Constants.KEY_FLAG);
                            //Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_LONG).show();
                            if (flag.equals("1")) {
                                Toast.makeText(getApplicationContext(), "your previous order is in process, please contact support", Toast.LENGTH_LONG).show();
                            } else {
                                Intent coll_tologin1 = new Intent(getApplicationContext(), CollectionActivity.class);
                                startActivity(coll_tologin1);
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
            Log.v("Calling API", Constants.ORDER_CHECK);
            ServiceCalls.callAPI_togetcategories(MenuActivity.this, Request.Method.POST, Constants.ORDER_CHECK, listener, api_token);*/
            Intent coll_tologin1 = new Intent(getApplicationContext(), CollectionActivity.class);
            startActivity(coll_tologin1);
        }
        if (btn_send == view) {
            Intent coll_tosend = new Intent(getApplicationContext(), SendActivity.class);
            startActivity(coll_tosend);
            // track the event for google analytics
           /* MyApplication.getInstance().trackEvent("send Button", "In Deliver Menu", "B&D");
            progressDialog = ProgressDialog.show(MenuActivity.this, "Please wait ...", "Requesting...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("OUTPUT CHECK ORDER", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString("status");
                        final String value = obj.getString("value");

                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            JSONObject value_obj = obj.getJSONObject("value");
                            String flag = value_obj.getString("flag");
                            //Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_LONG).show();
                            if (flag.equals("1")) {
                                Toast.makeText(getApplicationContext(), "your previous order is in process, please contact support", Toast.LENGTH_LONG).show();
                            } else {
                                Intent coll_tosend = new Intent(getApplicationContext(), SendActivity.class);
                                startActivity(coll_tosend);
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
            Log.v("Calling API", Constants.ORDER_CHECK);
            ServiceCalls.callAPI_togetcategories(MenuActivity.this, Request.Method.POST, Constants.ORDER_CHECK, listener, api_token);*/
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
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Menu Activity");
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

    @Override
    public void onBackPressed() {
        askforLOGOUT();
    }

    private void askforLOGOUT() {
        AlertDialogs.askforExit(MenuActivity.this);
    }

    private void call_api_to_getAgent() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT AGENT CHECK", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);

                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);

                    if (status.equals(Constants.KEY_FALSE)) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        JSONArray sentJSONArray = value_obj.getJSONArray(Constants.KEY_ORDER);

                        for (int i = 0; i < sentJSONArray.length(); i++) {

                            ViewAgentDelivery orderHistory = new ViewAgentDelivery();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);

                            // adding user to user array
                            orderHistory.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            orderHistory.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                            orderHistory.setProfile_pic_url(jsonobject.getString(Constants.KEY_PROFILE_PICTURE));
                            orderHistory.setDelivery_status(jsonobject.getString(Constants.KEY_DELIVERY_STATUS));

                            userid = orderHistory.getOrder_id();
                            username = orderHistory.getFullname();
                            profile_pic_url = orderHistory.getProfile_pic_url();
                            delivery_status = orderHistory.getDelivery_status();
                            if (delivery_status.equals("Not Accepted")) {
                                Toast.makeText(getApplicationContext(), "Your Previous order not yet accepted", Toast.LENGTH_LONG).show();
                            } else {
                                showDialog();
                            }

                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling API", Constants.VIEW_ORDER_INFO);
        ServiceCalls.callAPI_togetcategories(this, Request.Method.POST, Constants.VIEW_ORDER_INFO, listener, api_token);
    }

    @SuppressLint("NewApi")
    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void dismissDialog() {

    }

    @SuppressLint("NewApi")
    public static class MyAlertDialogFragment extends DialogFragment {
        AlertDialog.Builder alert = null;

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Order Tracking");
            alert.setPositiveButton("View Order", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    Call_viewOrderAPI();
                }
            });

           /* alert.setNegativeButton("Cancel Order", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    Call_API_toCancelOrder();
                }
            });*/

            View view = getActivity().getLayoutInflater().inflate(R.layout.loading_dialog_fragment, null);
            alert.setView(view);

            final ImageView image_agent = (ImageView) view.findViewById(R.id.image_agent);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            ImageView img_item_approved = (ImageView) view.findViewById(R.id.img_item_approved);
            ImageView img_item_picked = (ImageView) view.findViewById(R.id.img_item_picked);
            ImageView img_item_delivered = (ImageView) view.findViewById(R.id.img_item_delivered);

            // Image link from internet
            txt_name.setText(username);
            if (delivery_status.equals(Constants.ACCEPTED)) {
                img_item_approved.setBackgroundResource(R.drawable.button_bg_round);
            }
            if (delivery_status.equals(Constants.PICKED)) {
                img_item_picked.setBackgroundResource(R.drawable.button_bg_round);
            }
            if (delivery_status.equals(Constants.DELIVERED)) {
                img_item_delivered.setBackgroundResource(R.drawable.button_bg_round);
            }
            new DownloadImageFromInternet((ImageView) view.findViewById(R.id.image_agent))
                    .execute(profile_pic_url);

            return alert.create();
        }

        private void Call_viewOrderAPI() {
            Intent to_notification_menu = new Intent(getActivity(), ViewOrderActivity.class);
            startActivity(to_notification_menu);
        }

        private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
            ImageView imageView;

            public DownloadImageFromInternet(ImageView imageView) {
                this.imageView = imageView;
                //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
            }

            protected Bitmap doInBackground(String... urls) {
                String imageURL = urls[0];
                Bitmap bimage = null;
                try {
                    InputStream in = new java.net.URL(imageURL).openStream();
                    bimage = BitmapFactory.decodeStream(in);

                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
                return bimage;
            }

            protected void onPostExecute(Bitmap result) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
