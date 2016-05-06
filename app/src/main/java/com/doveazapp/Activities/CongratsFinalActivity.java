package com.doveazapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by Karthik on 11/25/2015.
 */
public class CongratsFinalActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_upload_bill, btn_upload_item_image, check_status, btn_transfer_credit,
            btn_confirm, check_status1, check_status2, credits_final;

    // Session Manager Class
    SessionManager session;

    String reference_id, service_type, item_value, status_success, mile1_status, mile2_status, mile3_status;

    ImageButton img_button_bill, img_btn_item;

    ImageView img_itempic, img_billpic;

    final int CAMERA_CAPTURE = 1;

    final int CAMERA_CAPTURE_BILL = 2;

    Bitmap item_bmp, bill_bmp;

    // captured picture uri
    private Uri picUri;

    //RadioGroup
    RadioGroup deliver_update;

    //For GCM
    ShareExternalServer appUtil;

    //Progress bar
    ProgressDialog progressDialog;

    TextView text_photo, txt_progress, txt_progress2, text_refresh_value, text_transactionid;

    LinearLayout credit_layout, photo_layout, upload_layout, status_layout, status1_layout, status_layout2;

    ImageView upload_done, transfer_done, upload2_done;

    private static final int PERMISSIONS_REQUEST_CAMERA = 100;

    private static final int PERMISSIONS_REQUEST_CAMERA1 = 99;

    RadioButton radio_yes, radio_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congrats_activity);

        // BUTTONS
        btn_upload_bill = (Button) findViewById(R.id.upload_bill);
        btn_upload_item_image = (Button) findViewById(R.id.upload_item_image);
        check_status = (Button) findViewById(R.id.check_status);
        btn_transfer_credit = (Button) findViewById(R.id.btn_transfer_credit);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        check_status1 = (Button) findViewById(R.id.check_status1);
        check_status2 = (Button) findViewById(R.id.check_status2);
        credits_final = (Button) findViewById(R.id.credits_final);
        //button_refresh = (Button) findViewById(R.id.button_refresh);

        //Views
        credit_layout = (LinearLayout) findViewById(R.id.credit_layout);
        photo_layout = (LinearLayout) findViewById(R.id.photo_layout);
        upload_layout = (LinearLayout) findViewById(R.id.upload_layout);
        status_layout = (LinearLayout) findViewById(R.id.status_layout);
        status1_layout = (LinearLayout) findViewById(R.id.status1_layout);
        status_layout2 = (LinearLayout) findViewById(R.id.status_layout2);

        //Textviews
        text_photo = (TextView) findViewById(R.id.text_photo);
        txt_progress = (TextView) findViewById(R.id.txt_progress);
        txt_progress2 = (TextView) findViewById(R.id.txt_progress2);
        text_transactionid = (TextView) findViewById(R.id.text_transactionid);
        //text_refresh_value = (TextView) findViewById(R.id.text_refresh_value);

        //img buttons
        img_btn_item = (ImageButton) findViewById(R.id.pick_item_img);
        img_itempic = (ImageView) findViewById(R.id.pick_item_img);
        img_button_bill = (ImageButton) findViewById(R.id.pick_bill_img);
        img_billpic = (ImageView) findViewById(R.id.pick_bill_img);

        upload_done = (ImageView) findViewById(R.id.upload_done);
        transfer_done = (ImageView) findViewById(R.id.transfer_done);
        upload2_done = (ImageView) findViewById(R.id.upload2_done);


        //radiobuttons
        deliver_update = (RadioGroup) findViewById(R.id.radioGroup);

        //BUTTONS LISTENERS
        btn_upload_item_image.setOnClickListener(this);
        btn_upload_bill.setOnClickListener(this);
        img_btn_item.setOnClickListener(this);
        img_button_bill.setOnClickListener(this);
        check_status.setOnClickListener(this);
        btn_transfer_credit.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        check_status1.setOnClickListener(this);
        check_status2.setOnClickListener(this);
        credits_final.setOnClickListener(this);
        //button_refresh.setOnClickListener(this);

        session = new SessionManager(getApplicationContext());

        menuvisibilityinAlldevices();
        appUtil = new ShareExternalServer();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Milestone Activity");

        status_layout2.setVisibility(View.GONE);
        status1_layout.setVisibility(View.GONE);

        upload_done.setVisibility(View.GONE);
        transfer_done.setVisibility(View.GONE);
        upload2_done.setVisibility(View.GONE);
        //button_refresh.setVisibility(View.GONE);
        //button_refresh.setVisibility(View.GONE);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        reference_id = bundle.getString(Constants.KEY_REFERENCE_ID);
        service_type = bundle.getString(Constants.KEY_SERVICE_TYPE);
        item_value = bundle.getString(Constants.KEY_VALUE_ITEM);
        status_success = bundle.getString(Constants.KEY_SUCCESS);
        mile1_status = bundle.getString(Constants.KEY_MILESTONE1_STATUS);
        mile2_status = bundle.getString(Constants.KEY_MILESTONE2_STATUS);
        mile3_status = bundle.getString(Constants.KEY_MILESTONE3_STATUS);

        setUI();
    }

    private void setUI() {

        if (reference_id != null) {
            text_transactionid.setText(reference_id);
        }
        if (status_success != null) {
            Log.v("SUCCESS", status_success.toString());
        }

        if (service_type != null) {
            if (service_type.equals("B")) {
                photo_layout.setVisibility(View.GONE);
                upload_layout.setVisibility(View.GONE);
                status_layout.setVisibility(View.GONE);
                credit_layout.setVisibility(View.VISIBLE);
                if (mile1_status != null) {
                    if (mile1_status.equals("1")) {
                        transfer_done.setVisibility(View.VISIBLE);
                    }
                }
                if (mile2_status != null) {
                    if (mile2_status.equals("1")) {
                        upload2_done.setVisibility(View.VISIBLE);
                    }
                }
            } else if (service_type.equals("C")) {
                photo_layout.setVisibility(View.GONE);
                upload_layout.setVisibility(View.GONE);
                status_layout.setVisibility(View.GONE);
                credit_layout.setVisibility(View.VISIBLE);
                if (mile1_status != null) {
                    if (mile1_status.equals("1")) {
                        transfer_done.setVisibility(View.VISIBLE);
                        btn_transfer_credit.setEnabled(false);
                    }
                }
                if (mile2_status != null) {
                    if (mile2_status.equals("1")) {
                        upload2_done.setVisibility(View.VISIBLE);
                        btn_upload_bill.setEnabled(false);
                        btn_confirm.setEnabled(false);
                    }
                }
            } else if (service_type.equals("A")) {
                credit_layout.setVisibility(View.GONE);
                photo_layout.setVisibility(View.VISIBLE);
                upload_layout.setVisibility(View.VISIBLE);
                status_layout.setVisibility(View.VISIBLE);
                // button_refresh.setVisibility(View.VISIBLE);
                if (mile1_status != null) {
                    if (mile1_status.equals("1")) {
                        upload_done.setVisibility(View.VISIBLE);
                        btn_upload_item_image.setEnabled(false);
                    }
                    if (mile2_status != null) {
                        if (mile2_status.equals("1")) {
                            upload2_done.setVisibility(View.VISIBLE);
                            btn_upload_bill.setEnabled(false);
                        }
                    }
                }
            }
        }
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CongratsFinalActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_upload_bill) {
            if (service_type.equals("A")) {
                CallAPI_to_uploadMilestoneimg();
            } else {
                CallAPI_to_uploadBCimg();
            }
        }
        if (v == btn_upload_item_image) {
            CallAPI_to_uploadItemImg();
        }
        if (v == img_btn_item) {
            cameraActionToCapture();
        }
        if (v == img_button_bill) {
            cameraActionToCapturebill();
        }
        if (v == check_status) {
            CallAPI_tocheck_status();
        }
        if (v == btn_transfer_credit) {
            goto_acceptCredit();
        }
        if (v == btn_confirm) {
            goto_confirm();
        }
        if (v == check_status1) {
            CheckAcceptStatus();
        }
        if (v == check_status2) {
            CheckAcceptStatus2();
        }
        if (v == credits_final) {
            CheckAcceptStatus3();
        }
       /* if (v == button_refresh){
            refreshMilestone();
        }*/
    }

    private void CallAPI_to_uploadBCimg() {
        String img_bill_pick = null;
        if (bill_bmp != null) {
            img_bill_pick = getStringImage(bill_bmp);
        } else {
            Toast.makeText(getApplicationContext(), "Please upload image", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT UPLOAD BILL--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject obj_value = obj.getJSONObject("value");
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        //{"status":"true","value":{"message":"Milestone 2 added successfully","notify_userid":"1"}}
                        Log.v("UPLOAD", value.toString());

                        Toast.makeText(getApplicationContext(), "Invoice/Bill added successfully", Toast.LENGTH_SHORT).show();

                        String notify_userid = obj_value.getString(Constants.KEY_NOTIFY_USERID);
                        HashMap<String, String> user = session.getUserDetails();
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has uploaded the invoice/bill of the item!");
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
        String stage = "TWO";
        Log.v("REFERENCE_ID", reference_id);
        ServiceCalls.CallAPI_to_uploadInvoiceBC(this, Request.Method.POST, Constants.MILESTONES, listener, stage, reference_id, img_bill_pick, api_token);
    }

    private void CheckAcceptStatus() {
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("RESPONSE MILESTONE DET", response.toString());
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject userobj = obj.getJSONObject("value");
                    final String service_type = userobj.getString("service_type");
                    JSONArray detailObj = userobj.getJSONArray("milestone_details");
                    JSONArray holder_details = userobj.getJSONArray("holder_details");

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
                                /*{"status": "true","value": {"service_type": "A",
                                    "milestone_details": [{"id": "1",
                                                "reference_id": "6",
                                                "milestone_1_image_url": "http:\/\/doveaz.co.in\/\/uploads\/milestone\/6\/14545964166.jpg",
                                                "milestone_1_status": "1",
                                                "milestone_2_bill_url": null,
                                                "milestone_2_status": null,
                                                "milestone_3": null,
                                                "milestone_3_status": null,
                                                "milestone_1_date": null,
                                                "milestone_2_date": null,
                                                "milestone_3_date": null,
                                                "status": null}],
                                    "holder_details": [{"id": "10",
                                                "reference_id": "6",
                                                "credits": "1021",
                                                "userid": "2",
                                                "status": null,
                                                "fullname": "dinesh"}]
                                }}*/
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

                                if (mileStone.getMilestone_1_status().equals("1")) {
                                    Log.v("check_status1", "success");
                                    status1_layout.setVisibility(View.VISIBLE);
                                    txt_progress.setText("Item approved successfully");
                                    return;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Milestone 2 Still under progress", Toast.LENGTH_SHORT).show();
                                }

                            } else {
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
                                if (mileStone.getMilestone_1_status().equals("1")) {
                                    Log.v("check_status1", "success");
                                    status1_layout.setVisibility(View.VISIBLE);
                                    txt_progress.setText("Item approved successfully");
                                    return;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Still under progress", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        /*for (int i = 0; i < holder_details.length(); i++) {
                            if (service_type.equals("A")) {
                                *//*{"status": "true","value": {"service_type": "A",
                                    "milestone_details": [{"id": "1",
                                                "reference_id": "6",
                                                "milestone_1_image_url": "http:\/\/doveaz.co.in\/\/uploads\/milestone\/6\/14545964166.jpg",
                                                "milestone_1_status": "1",
                                                "milestone_2_bill_url": null,
                                                "milestone_2_status": null,
                                                "milestone_3": null,
                                                "milestone_3_status": null,
                                                "milestone_1_date": null,
                                                "milestone_2_date": null,
                                                "milestone_3_date": null,
                                                "status": null}],
                                    "holder_details": [{"id": "10",
                                                "reference_id": "6",
                                                "credits": "1021",
                                                "userid": "2",
                                                "status": null,
                                                "fullname": "dinesh"}]
                                }}*//*
                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = holder_details.getJSONObject(i);
                                mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                                mileStone.setCredits(jsonobject.getString(Constants.KEY_CREDITS));
                                progressDialog.dismiss();
                                String name = mileStone.getFullname();
                                String credits = mileStone.getCredits();

                                Log.v("NAME AND CREDIT", name + credits);
                                if (mileStone.getMilestone_1_status().equals("1")) {
                                    Log.v("check_status1", "success");
                                    status1_layout.setVisibility(View.VISIBLE);
                                    txt_progress.setText("Item approved successfully");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Still under progress", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }*/
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

    private void CheckAcceptStatus2() {
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("RESPONSE MILESTONE DET", response.toString());
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject userobj = obj.getJSONObject("value");
                    final String service_type = userobj.getString("service_type");
                    JSONArray detailObj = userobj.getJSONArray("milestone_details");
                    JSONArray holder_details = userobj.getJSONArray("holder_details");

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

                                if (mileStone.getMilestone_2_status().equals("1")) {
                                    Log.v("check_status3", "pressed");
                                    status_layout2.setVisibility(View.VISIBLE);
                                    txt_progress2.setText("Invoice/Bill Approved");
                                    return;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Milestone 2 Still under progress", Toast.LENGTH_SHORT).show();
                                }

                            } else {

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

                                if (mileStone.getMilestone_2_status().equals("1")) {
                                    Log.v("check_status3", "pressed");
                                    status_layout2.setVisibility(View.VISIBLE);
                                    txt_progress2.setText("Invoice/Bill Approved");
                                    return;
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

    private void CheckAcceptStatus3() {
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("RESPONSE MILESTONE DET", response.toString());
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject userobj = obj.getJSONObject("value");
                    final String service_type = userobj.getString("service_type");
                    final String credit_det = userobj.getString("credit_transfer");
                    JSONArray detailObj = userobj.getJSONArray("milestone_details");
                    //JSONArray holder_details = userobj.getJSONArray("holder_details");

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
                                if (mileStone.getMilestone_3_status().equals("1")) {
                                    Toast.makeText(CongratsFinalActivity.this, credit_det.toString(), Toast.LENGTH_LONG).show();
                                    Intent to_rating = new Intent(getApplicationContext(), RatingActivity.class);
                                    to_rating.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    startActivity(to_rating);
                                }
                            } else {

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

                                //Toast.makeText(getApplicationContext(), mileStone.getMilestone_3_status(), Toast.LENGTH_LONG).show();
                                if (mileStone.getMilestone_3_status().equals("1")) {
                                    Intent to_rating = new Intent(getApplicationContext(), RatingActivity.class);
                                    Toast.makeText(CongratsFinalActivity.this, credit_det.toString(), Toast.LENGTH_LONG).show();
                                    to_rating.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    startActivity(to_rating);
                                }
                            }
                        }
                        /*for (int i = 0; i < holder_details.length(); i++) {
                            if (service_type.equals("A")) {

                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = holder_details.getJSONObject(i);
                                mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                                mileStone.setCredits(jsonobject.getString(Constants.KEY_CREDITS));
                                progressDialog.dismiss();
                                String name = mileStone.getFullname();
                                String credits = mileStone.getCredits();
                            }
                        }*/
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

    private void refreshMilestone() {
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("RESPONSE MILESTONE DET", response.toString());
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject userobj = obj.getJSONObject("value");
                    final String service_type = userobj.getString("service_type");
                    JSONArray detailObj = userobj.getJSONArray("milestone_details");
                    JSONArray holder_details = userobj.getJSONArray("holder_details");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        //Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        /*for (int i = 0; i < detailObj.length(); i++) {
                            if (service_type.equals("A")) {
                                *//*{"status": "true","value": {"service_type": "A",
                                    "milestone_details": [{"id": "1",
                                                "reference_id": "6",
                                                "milestone_1_image_url": "http:\/\/doveaz.co.in\/\/uploads\/milestone\/6\/14545964166.jpg",
                                                "milestone_1_status": "1",
                                                "milestone_2_bill_url": null,
                                                "milestone_2_status": null,
                                                "milestone_3": null,
                                                "milestone_3_status": null,
                                                "milestone_1_date": null,
                                                "milestone_2_date": null,
                                                "milestone_3_date": null,
                                                "status": null}],
                                    "holder_details": [{"id": "10",
                                                "reference_id": "6",
                                                "credits": "1021",
                                                "userid": "2",
                                                "status": null,
                                                "fullname": "dinesh"}]
                                }}*//*
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

                                if (mileStone.getMilestone_1_status().equals("1")) {
                                    Log.v("check_status1", "success");
                                    status1_layout.setVisibility(View.VISIBLE);
                                    txt_progress.setText("Item approved successfully");
                                    return;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Milestone 2 Still under progress", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                 *//*{"status":"true","value":{"service_type":"B","milestone_details":[{"id":"2",
                                    "milestone_1_payment_status":"SUCCESS",
                                    "milestone_1_status":null,"milestone_2_image_url":null,
                                    "milestone_2_status":null,"milestone_3":null,"milestone_3_status":null,"status":null,"reference_id":"111"}]}}*//*
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
                                if (mileStone.getMilestone_1_status().equals("1")) {
                                    Log.v("check_status1", "success");
                                    status1_layout.setVisibility(View.VISIBLE);
                                    txt_progress.setText("Item approved successfully");
                                    return;
                                } else {
                                    Toast.makeText(getApplicationContext(), "Still under progress", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }*/
                        HashMap<String, String> user = session.getUserDetails();
                        String userid = user.get(SessionManager.KEY_USERID);
                        for (int i = 0; i < holder_details.length(); i++) {
                            if (service_type.equals("A")) {
                                Log.v("LOG to holder", "details");
                                /*{"status": "true","value": {"service_type": "A",
                                    "milestone_details": [{"id": "1",
                                                "reference_id": "6",
                                                "milestone_1_image_url": "http:\/\/doveaz.co.in\/\/uploads\/milestone\/6\/14545964166.jpg",
                                                "milestone_1_status": "1",
                                                "milestone_2_bill_url": null,
                                                "milestone_2_status": null,
                                                "milestone_3": null,
                                                "milestone_3_status": null,
                                                "milestone_1_date": null,
                                                "milestone_2_date": null,
                                                "milestone_3_date": null,
                                                "status": null}],
                                    "holder_details": [{"id": "10",
                                                "reference_id": "6",
                                                "credits": "1021",
                                                "userid": "2",
                                                "status": null,
                                                "fullname": "dinesh"}]
                                }}*/
                                MileStone mileStone = new MileStone();
                                JSONObject jsonobject = holder_details.getJSONObject(i);
                                mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                                mileStone.setCredits(jsonobject.getString(Constants.KEY_CREDITS));
                                mileStone.setUserid(jsonobject.getString(Constants.KEY_USERID));
                                progressDialog.dismiss();
                                String name = mileStone.getFullname();
                                String credits = mileStone.getCredits();
                                Log.v("Name and credits", name + credits);

                                if (mileStone.getUserid() != userid) {
                                    Log.v("LOG", "details");
                                    Log.v("Name1 and credits", name + credits);
                                    text_refresh_value.setText(name + " transferred " + "$" + credits + "as security deposit for your package!!!");
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


    private void to_rating() {
        Intent to_rating = new Intent(getApplicationContext(), RatingActivity.class);
        startActivity(to_rating);
    }

    private void goto_confirm() {
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT MILESTONE3--", response);
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        Log.v("MILESTONE#3", value.toString());
                        //{"status":"true","value":{"message":"Milestone 3 added successfully","notify_userid":"1"}}

                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        status1_layout.setVisibility(View.VISIBLE);
                        txt_progress.setText("Credit transferred successfully");
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
        String stage = "ONE";
        Log.v("REFERENCE_ID", reference_id);
        ServiceCalls.Call_api_to_checkMilestone(this, Request.Method.POST, Constants.MILESTONES, listener, stage, reference_id, status_success, api_token);
    }

    private void goto_acceptCredit() {
        Intent to_accept = new Intent(getApplicationContext(), AcceptCreditActivity.class);
        to_accept.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
        to_accept.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
        to_accept.putExtra(Constants.KEY_VALUE_ITEM, item_value);
        startActivity(to_accept);
        finish();
    }

    private void CallAPI_tocheck_status() {
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT MILESTONE3--", response);
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
                        Log.v("MILESTONE#3", value.toString());
                        JSONObject value_obj = obj.getJSONObject("value");
                        String message = value_obj.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
        String stage = "THREE";
        Log.v("REFERENCE_ID", reference_id);

        int res_id = deliver_update.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(res_id);
        String select_option = radioButton.getText().toString().trim();

        ServiceCalls.CalLAPI_to_get_status(this, Request.Method.POST, Constants.MILESTONES, listener, stage, reference_id, select_option, api_token);

        // Toast.makeText(getApplicationContext(), "Please select yes/no", Toast.LENGTH_SHORT).show();
    }

    private void CallAPI_to_uploadItemImg() {
        String img_item_pick = null;
        if (item_bmp != null) {
            img_item_pick = getStringImage(item_bmp);
        } else {
            Toast.makeText(getApplicationContext(), "Please upload image", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT UPLOAD--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_object = obj.getJSONObject("value");
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Log.v("UPLOAD", value.toString());
                        //{"status":"true","value":{"message":"Milestone 1 added successfully","notify_userid":"33"}}

                        Toast.makeText(getApplicationContext(), "Item image uploaded successfully", Toast.LENGTH_SHORT).show();

                        String notify_userid = value_object.getString(Constants.KEY_NOTIFY_USERID);
                        HashMap<String, String> user = session.getUserDetails();
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has uploaded the item picture!");
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
        String stage = "ONE";
        Log.v("REFERENCE_ID", reference_id);
        ServiceCalls.CallAPI_to_uploadmilestones(this, Request.Method.POST, Constants.MILESTONES, listener, stage, reference_id, img_item_pick, api_token);
    }

    private void CallAPI_to_uploadMilestoneimg() {
        String img_bill_pick = null;
        if (bill_bmp != null) {
            img_bill_pick = getStringImage(bill_bmp);
        } else {
            Toast.makeText(getApplicationContext(), "Please upload image", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog = ProgressDialog.show(CongratsFinalActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT UPLOAD BILL--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    JSONObject obj_value = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        //{"status":"true","value":{"message":"Milestone 2 added successfully","notify_userid":"1"}}
                        Log.v("UPLOAD", value.toString());
                        String notify_userid = obj_value.getString(Constants.KEY_NOTIFY_USERID);
                        HashMap<String, String> user = session.getUserDetails();
                        final String partner = user.get(SessionManager.KEY_USER_TYPE);
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(notify_userid, u_name + " " + "has uploaded the item picture!");
                        String message = obj_value.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
        String stage = "TWO";
        Log.v("REFERENCE_ID", reference_id);
        ServiceCalls.CallAPI_to_uploadInvoice(this, Request.Method.POST, Constants.MILESTONES, listener, stage, reference_id, img_bill_pick, api_token);

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

    private void cameraActionToCapturebill() {
        try {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA1);
            } else {
                // use standard intent to capture an image
                Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE_BILL);
            }
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void cameraActionToCapture() {
        try {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
            } else {
                // use standard intent to capture an image
                Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                cameraActionToCapture();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_CAMERA1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraActionToCapturebill();
            }
        } else {
            Toast.makeText(this, "Until you grant the permission, we cannot open the camera", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handle user returning from both capturing and cropping the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                picUri = data.getData();
                Bundle extras = data.getExtras();
                item_bmp = extras.getParcelable("data");
                img_itempic.setImageBitmap(item_bmp);
            }
            if (requestCode == CAMERA_CAPTURE_BILL) {
                picUri = data.getData();
                Bundle extras = data.getExtras();
                bill_bmp = extras.getParcelable("data");
                img_billpic.setImageBitmap(bill_bmp);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
        MyApplication.getInstance().trackScreenView("Milestone Activity");
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
