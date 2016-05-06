package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.AcceptCreditActivity;
import com.doveazapp.Activities.CheckPartnerDetailsActivity;
import com.doveazapp.Activities.CongratsFinalActivity;
import com.doveazapp.Activities.PartnerDetailActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.EngageNegotiate;
import com.doveazapp.GettersSetters.MileStone;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/01/15.
 */
public class EngageAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<EngageNegotiate> engageNegotiateList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    public EngageAdapter(Activity activity, List<EngageNegotiate> engageNegotiateList) {
        this.activity = activity;
        this.engageNegotiateList = engageNegotiateList;
    }

    public EngageAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return engageNegotiateList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.engagenegotiate_row, null);

        TextView id = (TextView) convertView.findViewById(R.id.Id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView receiver_id = (TextView) convertView.findViewById(R.id.receiver_id);
        TextView service_a = (TextView) convertView.findViewById(R.id.service_a_id);
        TextView service_b = (TextView) convertView.findViewById(R.id.service_b_id);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        // TextView category = (TextView) convertView.findViewById(R.id.category);
        Button btn_view = (Button) convertView.findViewById(R.id.btn_view);
        Button btn_engage = (Button) convertView.findViewById(R.id.btn_engage);
        Button btn_decline = (Button) convertView.findViewById(R.id.btn_decline);

        final EngageNegotiate engageNegotiate = engageNegotiateList.get(position);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO AUTO GENERATED
                session = new SessionManager(activity);
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String name = user.get(SessionManager.KEY_APITOKEN);
                String email = user.get(SessionManager.KEY_EMAIL);
                String partner = user.get(SessionManager.KEY_USER_TYPE);

                if (partner.equals("1")) {
                    Log.i("view btn clicked", "at positiion" + position);
                    Intent to_details = new Intent(activity, PartnerDetailActivity.class);
                    to_details.putExtra(Constants.KEY_USERID, engageNegotiate.getSender_id());
                    to_details.putExtra(Constants.KEY_SERVICEID, engageNegotiate.getService_a_id());
                    to_details.putExtra(Constants.KEY_SERVICE_B_ID, engageNegotiate.getService_b_id());
                    activity.startActivity(to_details);
                }
                if (partner.equals("0")) {
                    Log.i("view btn clicked", "at positiion" + position);
                    Intent to_details = new Intent(activity, CheckPartnerDetailsActivity.class);
                    to_details.putExtra(Constants.KEY_USERID, engageNegotiate.getSender_id());
                    to_details.putExtra(Constants.KEY_SERVICEID, engageNegotiate.getService_b_id());
                    activity.startActivity(to_details);
                }
            }
        });

        btn_engage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO AUTO GENERATED
                Log.i("engage btn clicked", "at positiion" + position);
                // Session Manager
                session = new SessionManager(activity);
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String name = user.get(SessionManager.KEY_APITOKEN);
                String email = user.get(SessionManager.KEY_EMAIL);
                final String partner = user.get(SessionManager.KEY_USER_TYPE);
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Requesting...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("--OUTPUT ENGAGE--", response.toString());
                        progressDialog.dismiss();
                        //Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                        System.out.println(response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            //for (int i = 0; i < userarray.length(); i++) {
                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Engaged successfully", Toast.LENGTH_SHORT).show();
                                    /*INPUT -- {type=ENGAGE, service_id=7, userid=4}*/
                                /*OUTPUT -- {"status":"true","value":{"goto_milestone":1,"reference_id":12,"userid":"4",
                                "service_b_id":"192","service_a_id":"178","fee":"50","value":"54","service_type":"A"}}*/
                                JSONObject valueObj = obj.getJSONObject("value");
                                final MileStone mileStone = new MileStone();
                                mileStone.setGoto_milestone(valueObj.getString(Constants.KEY_GOTO_MILESTONE));
                                mileStone.setReference_id(valueObj.getString(Constants.KEY_REFERENCE_ID));
                                mileStone.setService_type(valueObj.getString(Constants.KEY_SERVICE_TYPE));
                                mileStone.setUserid(valueObj.getString(Constants.KEY_USERID));
                                mileStone.setValue(valueObj.getString(Constants.KEY_VALUE_ITEM));
                                mileStone.setFee(valueObj.getString(Constants.KEY_FEE));
                                String service_type = mileStone.getService_type();
                                String userId = mileStone.getUserid();
                                HashMap<String, String> user = session.getUserDetails();
                                // token
                                String u_name = user.get(SessionManager.KEY_USERNAME);
                                sendMessageToGCMAppServer(userId, u_name + " " + "has engaged for your service");
                                if (partner.equals(Constants.KEY_TYPE_DELIVER) && service_type.equals("A")) {
                                    Intent to_accept = new Intent(activity, AcceptCreditActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    to_accept.putExtra(Constants.KEY_VALUE_ITEM, mileStone.getValue());
                                    to_accept.putExtra(Constants.KEY_FEE, mileStone.getFee());
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                    activity.startActivity(to_accept);
                                }

                                if (partner.equals(Constants.KEY_TYPE_DELIVER) && service_type.equals("B")) {
                                    Intent to_accept = new Intent(activity, AcceptCreditActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    to_accept.putExtra(Constants.KEY_FEE, mileStone.getFee());
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                    activity.startActivity(to_accept);
                                }
                                if (partner.equals(Constants.KEY_TYPE_DELIVER) && service_type.equals("C")) {
                                    Intent to_accept = new Intent(activity, AcceptCreditActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    to_accept.putExtra(Constants.KEY_FEE, mileStone.getFee());
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                    activity.startActivity(to_accept);
                                }
                                if (partner.equals(Constants.KEY_TYPE_PARTNER) && service_type.equals("A")) {
                                    progressDialog = ProgressDialog.show(activity, "Please wait ...", "Loading", true);
                                    progressDialog.setCancelable(false);
                                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                                        @Override
                                        public void onRequestCompleted(String response) {
                                            Log.v("RESPONSE MILESTONE DET", response.toString());
                                            //Toast.makeText(activity, response.toString(), Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                            System.out.println(response.toString());
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                final String status = obj.getString("status");
                                                final String value = obj.getString("value");
                                                JSONObject userobj = obj.getJSONObject("value");
                                                final String service_type = userobj.getString("service_type");
                                                //JSONArray detailObj = userobj.getJSONArray("milestone_details");
                                                JSONArray holder_details = userobj.getJSONArray("holder_details");

                                                if (status.equals("false")) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(activity, value, Toast.LENGTH_LONG).show();
                                                } else if (status.equals("true")) {
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
                                                            final MileStone mileStone = new MileStone();
                                                            JSONObject jsonobject = holder_details.getJSONObject(i);
                                                            mileStone.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                                                            mileStone.setCredits(jsonobject.getString(Constants.KEY_CREDITS));
                                                            mileStone.setUserid(jsonobject.getString(Constants.KEY_USERID));
                                                            mileStone.setReference_id(jsonobject.getString(Constants.KEY_REFERENCE_ID));
                                                            progressDialog.dismiss();
                                                            String name = mileStone.getFullname();
                                                            String credits = mileStone.getCredits();
                                                            Log.v("Name and credits", name + credits);

                                                            if (mileStone.getUserid() != userid) {
                                                                Log.v("LOG", "details");

                                                                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                                                                alertDialog.setTitle("Credit Information");
                                                                alertDialog.setMessage(name + " transferred " + credits + " " + "points as security deposit for your package!!!");
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                //dialog.dismiss();
                                                                                Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                                                                to_milestone.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                                                                to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                                                                activity.startActivity(to_milestone);
                                                                            }
                                                                        });
                                                                alertDialog.show();
                                                            }
                                                        } else {
                                                            Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                                            to_milestone.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                                            to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                                            activity.startActivity(to_milestone);
                                                        }
                                                    }
                                                }
                                            } catch (JSONException exception) {
                                                Log.e("--JSON EXCEPTION--", exception.toString());
                                                progressDialog.dismiss();
                                            }
                                        }
                                    };

                                    // token
                                    String api_token = user.get(SessionManager.KEY_APITOKEN);

                                    ServiceCalls.CalLAPI_to_milestone_details(activity, Request.Method.POST, Constants.MILESTONE_DETAILS, listener, mileStone.getReference_id(), api_token);
                                }
                                if (partner.equals(Constants.KEY_TYPE_PARTNER) && service_type.equals("B")) {
                                    Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                    to_milestone.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                    to_milestone.putExtra(Constants.KEY_VALUE_ITEM, mileStone.getValue());
                                    activity.startActivity(to_milestone);
                                }
                                if (partner.equals(Constants.KEY_TYPE_PARTNER) && service_type.equals("C")) {
                                    Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                    to_milestone.putExtra(Constants.KEY_REFERENCE_ID, mileStone.getReference_id());
                                    to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, mileStone.getService_type());
                                    to_milestone.putExtra(Constants.KEY_VALUE_ITEM, mileStone.getValue());
                                    activity.startActivity(to_milestone);
                                }
                                //else if partner goto milestone we need to do that here
                            }
                            //}
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                            progressDialog.dismiss();
                        }
                    }
                };
                if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String type = "ENGAGE";
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_a_id();
                    String Service_b_id = engageNegotiate.getService_b_id();
                    Log.v("Calling api", Constants.GET_ENGAGEMENT);
                    ServiceCalls.CallAPI_togetEngagement(activity, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, Service_b_id, api_token);
                }
                if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String type = "ENGAGE";
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_b_id();
                    Log.v("Calling api", Constants.GET_ENGAGEMENT);
                    ServiceCalls.CallAPI_togetPartnerEngagement(activity, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
                }
            }

            private void check_forCreditTransfer() {
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO AUTO GENERATED
                Log.i("decline btn clicked", "at positiion" + position);
                // Session Manager
                session = new SessionManager(activity);
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String name = user.get(SessionManager.KEY_APITOKEN);
                String email = user.get(SessionManager.KEY_EMAIL);
                String partner = user.get(SessionManager.KEY_USER_TYPE);
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Requesting...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("--OUTPUT DECLINE--", response.toString());
                        progressDialog.dismiss();
                        //Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                        System.out.println(response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");
                            //JSONArray userarray = obj.getJSONArray("value");

                            //for (int i = 0; i < userarray.length(); i++) {
                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                //{"status":"true","value":"Declined successfully"}
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                                // }
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                            progressDialog.dismiss();
                        }
                    }
                };
                if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String type = "DECLINE";
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_a_id();
                    String Service_b_id = engageNegotiate.getService_b_id();
                    ServiceCalls.CallAPI_togetEngagement(activity, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, Service_b_id, api_token);
                }
                if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String type = "DECLINE";
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_b_id();
                    ServiceCalls.CallAPI_togetPartnerEngagement(activity, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
                }
            }
        });
        session = new SessionManager(activity);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String partner = user.get(SessionManager.KEY_USER_TYPE);
        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
            date.setText(engageNegotiate.getDate());
        }
        if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
            date.setText(engageNegotiate.getTravel_date());
        }
        //date.setText(engageNegotiate.getTravel_date());
        id.setText(engageNegotiate.getId());
        name.setText(engageNegotiate.getFullname());
        receiver_id.setText(engageNegotiate.getReceiver_id());
        service_a.setText(engageNegotiate.getService_a_id());
        service_b.setText(engageNegotiate.getService_b_id());
        //category.setText(engageNegotiate.getCategory_name());
        return convertView;
    }

    private void sendMessageToGCMAppServer(final String toUserId,
                                           final String messageToSend) {
        appUtil = new ShareExternalServer();
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
}
