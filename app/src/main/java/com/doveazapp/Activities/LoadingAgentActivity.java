package com.doveazapp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * LoadingAgentActivity.java
 * Created by Karthik on 7/14/2016.
 */
public class LoadingAgentActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;

    // Session Manager Class
    static SessionManager session;

    static String profile_pic_url, username, userid, status;
    WebView w_view;

    public static String order_id;

    LinearLayout loading_layout, button_layout;

    Button button_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_agent_view);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String email = user.get(SessionManager.KEY_EMAIL);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        order_id = bundle.getString(Constants.KEY_ORDER_ID);
        Log.v("ORDER_ID", order_id);

        loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
        button_layout = (LinearLayout) findViewById(R.id.button_layout);
        button_retry = (Button) findViewById(R.id.button_retry);

        button_retry.setOnClickListener(this);

        w_view = (WebView) findViewById(R.id.loading_view);
        w_view.loadUrl("file:///android_asset/dual.gif");
        w_view.setBackgroundColor(0x00000000);

        call_api_to_getAgent();
    }

    private void call_api_to_getAgent() {
        /*progressDialog = ProgressDialog.show(LoadingAgentActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);*/
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT TRANSFER CREDIT", response);

                /*progressDialog.dismiss();*/
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_object = obj.getJSONObject("value");

                    if (status.equals("false")) {
                       /* progressDialog.dismiss();*/
                        /*{"status":"false","value":{"message":"There is no agent available"}}*/
                        String message = value_object.getString("message");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        if (message.equals("There is no agent available")) {
                            loading_layout.setVisibility(View.GONE);
                            button_layout.setVisibility(View.VISIBLE);
                        } else if (message.equals("There is no order")) {
                            loading_layout.setVisibility(View.GONE);
                            Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(myIntent);
                        }

                    } else if (status.equals("true")) {
                        /*progressDialog.dismiss();*/
                        JSONObject object_agent = value_object.getJSONObject("agent");

                        userid = object_agent.getString("userid");
                        username = object_agent.getString("fullname");
                        profile_pic_url = object_agent.getString("profile_pic_url");

                        loading_layout.setVisibility(View.GONE);

                        showDialog();

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
        Log.v("Calling API", Constants.DISPATCH_ORDER);
        ServiceCalls.callAPI_togetcategories(this, Request.Method.POST, Constants.DISPATCH_ORDER, listener, api_token);
    }

    @SuppressLint("NewApi")
    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog);
        newFragment.show(getFragmentManager(), "dialog");
    }

    void dismissDialog() {

    }

    @Override
    public void onClick(View v) {
        if (v == button_retry) {
            call_api_toretry();
        }

    }

    private void call_api_toretry() {
        progressDialog = ProgressDialog.show(LoadingAgentActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT TRANSFER CREDIT", response);

                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_object = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        /*{"status":"false","value":{"message":"There is no agent available"}}*/
                        String message = value_object.getString("message");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        if (message.equals("There is no agent available")) {
                            loading_layout.setVisibility(View.GONE);
                            button_layout.setVisibility(View.VISIBLE);
                        } else if (message.equals("There is no order")) {
                            loading_layout.setVisibility(View.GONE);
                            Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(myIntent);
                        }

                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        JSONObject object_agent = value_object.getJSONObject("agent");

                        userid = object_agent.getString("userid");
                        username = object_agent.getString("fullname");
                        profile_pic_url = object_agent.getString("profile_pic_url");

                        loading_layout.setVisibility(View.GONE);

                        showDialog();

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
        Log.v("Calling API", Constants.DISPATCH_ORDER);
        ServiceCalls.callAPI_togetcategories(this, Request.Method.POST, Constants.DISPATCH_ORDER, listener, api_token);
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
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    Intent myIntent = new Intent(getActivity(), MenuActivity.class);
                    getActivity().startActivity(myIntent);
                }
            });

            /*alert.setNegativeButton("Cancel Order", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    Call_API_toCancelOrder();
                }
            });*/

            View view = getActivity().getLayoutInflater().inflate(R.layout.loading_dialog_fragment, null);
            //getDialog().setCancelable(false);
            alert.setView(view);

            final ImageView image_agent = (ImageView) view.findViewById(R.id.image_agent);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            ImageView img_item_approved = (ImageView) view.findViewById(R.id.img_item_approved);
            ImageView img_item_picked = (ImageView) view.findViewById(R.id.img_item_picked);
            ImageView img_item_delivered = (ImageView) view.findViewById(R.id.img_item_delivered);

            // Image link from internet
            txt_name.setText(username);
            img_item_approved.setBackgroundResource(R.drawable.button_bg_round);
            new DownloadImageFromInternet((ImageView) view.findViewById(R.id.image_agent))
                    .execute(profile_pic_url);

            return alert.create();
        }

        private void Call_API_toCancelOrder() {
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("OUTPUT CANCEL ORDER", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString("status");
                        final String value = obj.getString("value");

                        if (status.equals("false")) {
                            Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        } else if (status.equals("true")) {
                            Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException exception) {
                        Log.e("--JSON EXCEPTION--", exception.toString());
                    }
                }
            };

            HashMap<String, String> user = session.getUserDetails();
            // token
            String api_token = user.get(SessionManager.KEY_APITOKEN);
            Log.v("Calling API", Constants.CANCEL_ORDER_TYPE_A);
            ServiceCalls.CallAPI_to_ViewOrder(getActivity(), Request.Method.POST, Constants.CANCEL_ORDER_TYPE_A, listener, order_id, api_token);
        }

        private void Call_viewOrderAPI() {
            /*progressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Requesting...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    Log.v("OUTPUT VIEW ORDER", response);
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString("status");
                        final String value = obj.getString("value");
                        JSONObject value_object = obj.getJSONObject("value");

                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            JSONArray order_array = value_object.getJSONArray("order");
                            for (int i = 0; i < order_array.length(); i++) {
                                JSONObject jsonobject = order_array.getJSONObject(i);
                                OrderDetailsInfo OrderDetailsInfo = new OrderDetailsInfo();
                                OrderDetailsInfo.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                                OrderDetailsInfo.setPickup_street(jsonobject.getString(Constants.KEY_PICKUP_STREET));
                                OrderDetailsInfo.setPickup_area(jsonobject.getString(Constants.KEY_PICKUP_AREA));
                                OrderDetailsInfo.setPickup_city(jsonobject.getString(Constants.KEY_PICKUP_CITY));
                                OrderDetailsInfo.setPickup_zip(jsonobject.getString(Constants.KEY_PICKUP_ZIPCODE));
                                OrderDetailsInfo.setPickup_phone(jsonobject.getString(Constants.KEY_PICKUP_PHONE));
                                OrderDetailsInfo.setDelivery_street(jsonobject.getString(Constants.KEY_DELIVERY_STREET));
                                OrderDetailsInfo.setDelivery_area(jsonobject.getString(Constants.KEY_DELIVERY_AREA));
                                OrderDetailsInfo.setDelivery_city(jsonobject.getString(Constants.KEY_DELIVERY_CITY));
                                OrderDetailsInfo.setDelivery_zip(jsonobject.getString(Constants.KEY_DELIVERY_ZIP));
                                OrderDetailsInfo.setDelivery_phone(jsonobject.getString(Constants.KEY_DELIVERY_PHONE));

                                String order_id, pickup_street, pickup_area, pickup_city, pickup_state, pickup_country, pickup_zip,
                                        pickup_phone, delivery_street, delivery_area, delivery_city, delivery_state, delivery_country,
                                        delivery_zip, delivery_phone, fee, delivery_name;
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
            Log.v("Calling API", Constants.VIEW_ORDER_TYPE_A);
            ServiceCalls.CallAPI_to_ViewOrder(getActivity(), Request.Method.POST, Constants.VIEW_ORDER_TYPE_A, listener, order_id, api_token);*/
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
