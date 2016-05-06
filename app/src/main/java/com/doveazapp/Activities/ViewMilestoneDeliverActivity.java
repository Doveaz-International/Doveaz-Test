package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.MileStone;
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
 * Created by Karthik on 2016/01/30.
 */
public class ViewMilestoneDeliverActivity extends AppCompatActivity implements View.OnClickListener {
    // Session Manager Class
    SessionManager session;

    Button btn_view_item, btn_view_bill;

    ProgressDialog progressDialog;

    String reference_id, service_type, mile1_status, mile2_status, mile3_status;

    Button accept_stage2, accept_stage3, decline_stage2, decline_stage3, accept_stage1, decline_stage1, check_status1;

    LinearLayout status1_layout, view_layout;

    TextView txt_progress, text_transactionid;

    ImageView mile1_done, checkstatus_done, mile2_done;

    //For GCM
    ShareExternalServer appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_milestone_activity);

        //Button init
        btn_view_item = (Button) findViewById(R.id.btn_view_item);
        btn_view_bill = (Button) findViewById(R.id.btn_view_bill);
        accept_stage1 = (Button) findViewById(R.id.accept_stage1);
        accept_stage2 = (Button) findViewById(R.id.accept_stage2);
        accept_stage3 = (Button) findViewById(R.id.accept_stage3);
        decline_stage1 = (Button) findViewById(R.id.decline_stage1);
        decline_stage2 = (Button) findViewById(R.id.decline_stage2);
        decline_stage3 = (Button) findViewById(R.id.decline_stage3);
        check_status1 = (Button) findViewById(R.id.check_status1);
        text_transactionid = (TextView) findViewById(R.id.text_transactionid);
        //credits_final = (Button) findViewById(R.id.credits_final);

        //Textviews
        txt_progress = (TextView) findViewById(R.id.txt_progress);

        //Imageviews
        mile1_done = (ImageView) findViewById(R.id.mile1_done);
        checkstatus_done = (ImageView) findViewById(R.id.checkstatus_done);
        mile2_done = (ImageView) findViewById(R.id.mile2_done);

        //Views...
        status1_layout = (LinearLayout) findViewById(R.id.status1_layout);
        view_layout = (LinearLayout) findViewById(R.id.view_layout);

        //button listeners
        btn_view_item.setOnClickListener(this);
        btn_view_bill.setOnClickListener(this);
        accept_stage1.setOnClickListener(this);
        accept_stage2.setOnClickListener(this);
        accept_stage3.setOnClickListener(this);
        decline_stage1.setOnClickListener(this);
        decline_stage2.setOnClickListener(this);
        decline_stage3.setOnClickListener(this);
        check_status1.setOnClickListener(this);
        //credits_final.setOnClickListener(this);

        mile1_done.setVisibility(View.GONE);
        checkstatus_done.setVisibility(View.GONE);
        mile2_done.setVisibility(View.GONE);

        menuvisibilityinAlldevices();
        appUtil = new ShareExternalServer();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("View Milestone Activity");

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        reference_id = bundle.getString(Constants.KEY_REFERENCE_ID);
        service_type = bundle.getString(Constants.KEY_SERVICE_TYPE);
        mile1_status = bundle.getString(Constants.KEY_MILESTONE1_STATUS);
        mile2_status = bundle.getString(Constants.KEY_MILESTONE2_STATUS);
        mile3_status = bundle.getString(Constants.KEY_MILESTONE3_STATUS);

        setUI();
    }

    private void setUI() {
        if (reference_id != null) {
            text_transactionid.setText(reference_id);
        }
        if (service_type.equals("A")) {
            view_layout.setVisibility(View.VISIBLE);
            status1_layout.setVisibility(View.GONE);
            check_status1.setVisibility(View.GONE);
            if (mile1_status != null) {
                if (mile1_status.equals("1")) {
                    mile1_done.setVisibility(View.VISIBLE);
                    /*accept_stage1.setEnabled(false);
                    accept_stage1.setClickable(false);
                    decline_stage1.setEnabled(false);
                    decline_stage1.setClickable(false);*/
                    accept_stage1.setVisibility(View.GONE);
                    decline_stage1.setVisibility(View.GONE);
                }
            }
            if (mile2_status != null) {
                if (mile2_status.equals("1")) {
                    mile2_done.setVisibility(View.VISIBLE);
                    /*accept_stage2.setEnabled(false);
                    accept_stage2.setClickable(false);
                    decline_stage2.setEnabled(false);
                    decline_stage2.setClickable(false);*/
                    accept_stage2.setVisibility(View.GONE);
                    decline_stage2.setVisibility(View.GONE);
                }
            }
        }
        if (service_type.equals("B")) {
            view_layout.setVisibility(View.GONE);
            status1_layout.setVisibility(View.VISIBLE);
            check_status1.setVisibility(View.VISIBLE);
            if (mile1_status != null) {
                if (mile1_status.equals("1")) {
                    checkstatus_done.setVisibility(View.VISIBLE);
                }
            }
            if (mile2_status != null) {
                if (mile2_status.equals("1")) {
                    mile2_done.setVisibility(View.VISIBLE);
                    accept_stage2.setVisibility(View.GONE);
                    decline_stage2.setVisibility(View.GONE);
                }
            }
        }
        if (service_type.equals("C")) {
            view_layout.setVisibility(View.GONE);
            status1_layout.setVisibility(View.VISIBLE);
            check_status1.setVisibility(View.VISIBLE);
            if (mile1_status != null) {
                if (mile1_status.equals("1")) {
                    checkstatus_done.setVisibility(View.VISIBLE);
                }
            }
            if (mile2_status != null) {
                if (mile2_status.equals("1")) {
                    mile2_done.setVisibility(View.VISIBLE);
                    accept_stage2.setVisibility(View.GONE);
                    decline_stage2.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_view_item) {
            viewItem();
        }
        if (v == btn_view_bill) {
            viewBill();
        }
        if (v == accept_stage1) {
            acceptstage();
        }
        if (v == accept_stage2) {
            acceptstage();
        }
        if (v == accept_stage3) {
            acceptstage3();
        }
        if (v == decline_stage1) {
            declineStage();
        }
        if (v == decline_stage2) {
            declineStage();
        }
        if (v == decline_stage3) {
            declineStage();
        }
        if (v == check_status1) {
            CheckAcceptStatus();
        }
        /*if (v == credits_final) {
            CheckAcceptStatus3();
        }*/
    }

    private void acceptstage3() {
        progressDialog = ProgressDialog.show(ViewMilestoneDeliverActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT ACCEPT IMAGE 1", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_object = obj.getJSONObject(Constants.KEY_VALUE);
                    String notify_userid = value_object.getString(Constants.KEY_NOTIFY_USERID);
                    HashMap<String, String> user = session.getUserDetails();
                    final String partner = user.get(SessionManager.KEY_USER_TYPE);
                    String u_name = user.get(SessionManager.KEY_USERNAME);
                    sendMessageToGCMAppServer(notify_userid, u_name + " " + "Delivery accepted!");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        String message = value_object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        String message = value_object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        Intent to_rating = new Intent(getApplicationContext(), RatingActivity.class);
                        to_rating.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                        startActivity(to_rating);
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
        Log.v("accept_stage3", "accept3");
        String stage3 = "THREE";
        ServiceCalls.CallAPI_to_Approve1(this, Request.Method.POST, Constants.MILESTONE_APPROVE, listener, reference_id, stage3, api_token);
    }

    private void acceptstage() {
        progressDialog = ProgressDialog.show(ViewMilestoneDeliverActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT ACCEPT IMAGE 1", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);

                    JSONObject value_object = obj.getJSONObject(Constants.KEY_VALUE);

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        String message = value_object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        /*{"status":"true","value":{"message":"Milestone 1 approved","notify_userid":"2"}}*/
                        progressDialog.dismiss();
                        String message = value_object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        String notify_userid = value_object.getString(Constants.KEY_NOTIFY_USERID);
                        HashMap<String, String> user = session.getUserDetails();
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has accepted the image, which you have uploaded!");

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
        if (accept_stage1.isPressed()) {
            Log.v("accept_stage1", "accept1");
            String stage = "ONE";
            ServiceCalls.CallAPI_to_Approve1(this, Request.Method.POST, Constants.MILESTONE_APPROVE, listener, reference_id, stage, api_token);
        }
        if (accept_stage2.isPressed()) {
            Log.v("accept_stage2", "accept2");
            String stage2 = "TWO";
            ServiceCalls.CallAPI_to_Approve1(this, Request.Method.POST, Constants.MILESTONE_APPROVE, listener, reference_id, stage2, api_token);
        }
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

    private void declineStage() {
        progressDialog = ProgressDialog.show(ViewMilestoneDeliverActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT ACCEPT IMAGE 1", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_object = obj.getJSONObject(Constants.KEY_VALUE);

                    if (status.equals(Constants.KEY_FALSE)) {
                        progressDialog.dismiss();
                        String message = value_object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        progressDialog.dismiss();
                        String message = value_object.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        String notify_userid = value_object.getString(Constants.KEY_NOTIFY_USERID);
                        HashMap<String, String> user = session.getUserDetails();
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has declined your milestone, check your image and upload again!");
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
        if (decline_stage1.isPressed()) {
            Log.v("decline_stage1", "decline1");
            String stage = "ONE";
            ServiceCalls.CallAPI_to_Approve1(this, Request.Method.POST, Constants.MILESTONE_DELINE, listener, reference_id, stage, api_token);
        }
        if (decline_stage2.isPressed()) {
            Log.v("decline_stage2", "decline2");
            String stage2 = "TWO";
            ServiceCalls.CallAPI_to_Approve1(this, Request.Method.POST, Constants.MILESTONE_DELINE, listener, reference_id, stage2, api_token);
        }
        if (decline_stage3.isPressed()) {
            Log.v("decline_stage3", "decline3");
            String stage3 = "THREE";
            ServiceCalls.CallAPI_to_Approve1(this, Request.Method.POST, Constants.MILESTONE_DELINE, listener, reference_id, stage3, api_token);
        }
    }

    private void viewItem() {
        progressDialog = ProgressDialog.show(ViewMilestoneDeliverActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT VIEW ITEM--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject userobj = obj.getJSONObject(Constants.KEY_VALUE);
                    final String service_type = userobj.getString(Constants.KEY_SERVICE_TYPE);
                    JSONArray detailObj = userobj.getJSONArray(Constants.KEY_MILESTONE_DETAILS);

                    for (int i = 0; i < detailObj.length(); i++) {
                        if (status.equals(Constants.KEY_FALSE)) {
                            progressDialog.dismiss();
                            String message = userobj.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            progressDialog.dismiss();
                            /*{"status":"true","value":{"service_type":"A","reference_id":"4","milestone_details":[{"message":"No Milestone added"}],
                            "holder_details":[{"id":"3","reference_id":"4",
                            "credits":"857","userid":"1","status":null,"date":"2016-03-29 05:02:23","fullname":"dinesh"}]}}*/
                        /*{"status":"true","value":[{"id":"8","reference_id":"76",
                        "milestone_1_image_url":"http:\/\/doveaz.co.in\/\/uploads\/milestone\/76\/145415608676.jpg",
                        "milestone_1_status":"0","milestone_2_bill_url":"http:\/\/doveaz.co.in\/\/uploads\/milestone\/76\/145415619276.jpg",
                        "milestone_2_status":"0","milestone_3":null,
                        "milestone_3_status":"0","milestone_1_date":null,"milestone_2_date":null,"milestone_3_date":null,"status":null}]}*/
                           // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            JSONObject jsonobject = detailObj.getJSONObject(i);
                            MileStone mileStone = new MileStone();
                            mileStone.setMilestone_1_image_url(jsonobject.getString(Constants.KEY_MILESTONE1_IMAGE));
                            //mileStone.setMilestone_2_bill_url(jsonobject.getString(Constants.KEY_MILESTONE2_IMAGE));

                            LayoutInflater layoutInflater
                                    = (LayoutInflater) getBaseContext()
                                    .getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView = layoutInflater.inflate(R.layout.view_image_popup, null);
                            final PopupWindow popupWindow = new PopupWindow(popupView,
                                    LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT);
                            NetworkImageView imageView = (NetworkImageView) popupView.findViewById(R.id.img);
                            ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
                            if (imageLoader == null)
                                imageLoader = MyApplication.getInstance().getImageLoader();

                            imageView.setImageUrl(mileStone.getMilestone_1_image_url(), imageLoader);

                            Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                            btnDismiss.setOnClickListener(new Button.OnClickListener() {
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    popupWindow.dismiss();
                                }
                            });

                            // popupWindow.showAsDropDown(btn_view_item, 50, -30);
                            popupWindow.showAtLocation(btn_view_item, Gravity.CENTER, 0, 0);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
                // Toast.makeText(ViewMilestoneDeliverActivity.this, response, Toast.LENGTH_LONG).show();
            }
        };
        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.CalLAPI_to_milestone_details(this, Request.Method.POST, Constants.MILESTONE_DETAILS, listener, reference_id, api_token);
    }

    private void viewBill() {
        progressDialog = ProgressDialog.show(ViewMilestoneDeliverActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT CALCULATION--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject userobj = obj.getJSONObject(Constants.KEY_VALUE);
                    final String service_type = userobj.getString(Constants.KEY_SERVICETYPE);
                    JSONArray detailObj = userobj.getJSONArray(Constants.KEY_MILESTONE_DETAILS);

                    for (int i = 0; i < detailObj.length(); i++) {
                        if (status.equals(Constants.KEY_FALSE)) {
                            progressDialog.dismiss();
                            String message = userobj.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            progressDialog.dismiss();
                        /*{"status":"true","value":[{"id":"8","reference_id":"76",
                        "milestone_1_image_url":"http:\/\/doveaz.co.in\/\/uploads\/milestone\/76\/145415608676.jpg",
                        "milestone_1_status":"0","milestone_2_bill_url":"http:\/\/doveaz.co.in\/\/uploads\/milestone\/76\/145415619276.jpg",
                        "milestone_2_status":"0","milestone_3":null,
                        "milestone_3_status":"0","milestone_1_date":null,"milestone_2_date":null,"milestone_3_date":null,"status":null}]}*/
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            Log.v("Mileston1_image", value);
                            JSONObject jsonobject = detailObj.getJSONObject(i);
                            MileStone mileStone = new MileStone();
                            //mileStone.setMilestone_1_image_url(jsonobject.getString(Constants.KEY_MILESTONE1_IMAGE));
                            if (service_type.equals("A")) {
                                mileStone.setMilestone_2_bill_url(jsonobject.getString(Constants.KEY_MILESTONE2_IMAGE));
                            } else {
                                mileStone.setMilestone_2_image_url(jsonobject.getString(Constants.KEY_MILESTONE2_IMAGE_URL));
                            }


                            LayoutInflater layoutInflater
                                    = (LayoutInflater) getBaseContext()
                                    .getSystemService(LAYOUT_INFLATER_SERVICE);
                            View popupView = layoutInflater.inflate(R.layout.view_image_popup, null);
                            final PopupWindow popupWindow = new PopupWindow(popupView,
                                    LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT);
                            NetworkImageView imageView = (NetworkImageView) popupView.findViewById(R.id.img);
                            ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
                            if (imageLoader == null)
                                imageLoader = MyApplication.getInstance().getImageLoader();
                            if (service_type.equals("A")) {
                                imageView.setImageUrl(mileStone.getMilestone_2_bill_url(), imageLoader);
                            } else {
                                imageView.setImageUrl(mileStone.getMilestone_2_image_url(), imageLoader);
                            }


                            Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                            btnDismiss.setOnClickListener(new Button.OnClickListener() {
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    popupWindow.dismiss();
                                }
                            });

                            // popupWindow.showAsDropDown(btn_view_item, 50, -30);
                            popupWindow.showAtLocation(btn_view_item, Gravity.CENTER, 0, 0);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
                // Toast.makeText(ViewMilestoneDeliverActivity.this, response, Toast.LENGTH_LONG).show();
            }
        };
        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.CalLAPI_to_milestone_details(this, Request.Method.POST, Constants.MILESTONE_DETAILS, listener, reference_id, api_token);
    }

    private void CheckAcceptStatus() {
        progressDialog = ProgressDialog.show(ViewMilestoneDeliverActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("RESPONSE MILESTONE DET", response.toString());
                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject userobj = obj.getJSONObject(Constants.KEY_VALUE);
                    final String service_type = userobj.getString(Constants.KEY_SERVICETYPE);
                    JSONArray detailObj = userobj.getJSONArray(Constants.KEY_MILESTONE_DETAILS);
                    JSONArray holder_details = userobj.getJSONArray(Constants.KEY_HOLDER_VALUE);

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        //Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        HashMap<String, String> user = session.getUserDetails();
                        // token
                        String userid = user.get(SessionManager.KEY_USERID);
                        for (int i = 0; i < detailObj.length(); i++) {
                            if (service_type.equals("A")) {
                                Log.v("DETAILS-SER-A", detailObj.toString());

                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = detailObj.getJSONObject(i);
                                mileStone.setReference_id(jsonobject.getString(Constants.KEY_REFERENCE_ID));
                                mileStone.setMilestone_1_image_url(jsonobject.getString(Constants.KEY_MILESTONE1_IMAGE));
                                mileStone.setMilestone_1_status(jsonobject.getString(Constants.KEY_MILESTONE1_STATUS));
                                mileStone.setMilestone_2_bill_url(jsonobject.getString(Constants.KEY_MILESTONE2_IMAGE));
                                mileStone.setMilestone_2_status(jsonobject.getString(Constants.KEY_MILESTONE2_STATUS));
                                mileStone.setMilestone_3(jsonobject.getString(Constants.KEY_MILESTONE3));
                                mileStone.setMilestone_3_status(jsonobject.getString(Constants.KEY_MILESTONE3_STATUS));
                                mileStone.setMilestone_1_date(jsonobject.getString(Constants.KEY_MILESTONE1_DATE));
                                mileStone.setMilestone_2_date(jsonobject.getString(Constants.KEY_MILESTONE2_DATE));
                                mileStone.setMilestone_3_date(jsonobject.getString(Constants.KEY_MILESTONE3_DATE));
                                progressDialog.dismiss();
                               /* status1_layout.setVisibility(View.VISIBLE);
                                txt_progress.setText("Credit transferred successfully");*/
                            } else {
                                Log.v("DETAILS-SER-B&C", detailObj.toString());
                                 /*{"status":"true","value":{"service_type":"B","milestone_details":[{"id":"2",
                                    "milestone_1_payment_status":"SUCCESS",
                                    "milestone_1_status":null,"milestone_2_image_url":null,
                                    "milestone_2_status":null,"milestone_3":null,"milestone_3_status":null,"status":null,"reference_id":"111"}]}}*/
                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = detailObj.getJSONObject(i);
                                mileStone.setReference_id(jsonobject.getString(Constants.KEY_REFERENCE_ID));
                                mileStone.setMilestone_1_status(jsonobject.getString(Constants.KEY_MILESTONE1_PAYMENT_STATUS));
                                mileStone.setStatus(jsonobject.getString(Constants.KEY_MILESTONE1_STATUS));
                                mileStone.setMilestone_2_image_url(jsonobject.getString(Constants.KEY_MILESTONE2_IMAGE_URL));
                                mileStone.setMilestone_2_status(jsonobject.getString(Constants.KEY_MILESTONE2_STATUS));
                                mileStone.setMilestone_3(jsonobject.getString(Constants.KEY_MILESTONE3));
                                mileStone.setMilestone_3_status(jsonobject.getString(Constants.KEY_MILESTONE3_STATUS));

                                String reference_id = mileStone.getReference_id();
                                String Mile1_status = mileStone.getMilestone_1_status();
                                String statuss = mileStone.getStatus();
                                String Mile2_img = mileStone.getMilestone_2_image_url();
                                String Mile2_status = mileStone.getMilestone_2_status();
                                String Mile_3 = mileStone.getMilestone_3();
                                String Mile3_status = mileStone.getMilestone_3_status();
                                progressDialog.dismiss();
                            }
                        }
                        for (int i = 0; i < holder_details.length(); i++) {
                            if (service_type.equals("A")) {
                                Log.v("HOLDER-SER-A", holder_details.toString());

                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = holder_details.getJSONObject(i);
                                mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                                mileStone.setCredits(jsonobject.getString(Constants.KEY_CREDITS));
                                progressDialog.dismiss();
                                String name = mileStone.getFullname();
                                String credits = mileStone.getCredits();

                            } else {
                                Log.v("HOLDER-SER-B&C", holder_details.toString());

                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = holder_details.getJSONObject(i);
                                mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                                mileStone.setCredits(jsonobject.getString(Constants.KEY_CREDITS));
                                mileStone.setUserid(jsonobject.getString(Constants.KEY_USERID));
                                progressDialog.dismiss();
                                String name = mileStone.getFullname();
                                String credits = mileStone.getCredits();

                                Log.v("NAME AND CREDIT", name + credits);
                                if (mileStone.getUserid() != userid) {
                                    status1_layout.setVisibility(View.VISIBLE);
                                    txt_progress.setText(name + " transferred " + "$" + credits + "as security deposit for your package!!!");
                                }
                            }
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

        ServiceCalls.CalLAPI_to_milestone_details(getApplicationContext(), Request.Method.POST, Constants.MILESTONE_DETAILS, listener, reference_id, api_token);
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(ViewMilestoneDeliverActivity.this);
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
        MyApplication.getInstance().trackScreenView("Partner travel details");
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
