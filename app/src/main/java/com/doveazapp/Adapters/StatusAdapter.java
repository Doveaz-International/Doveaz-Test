package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.doveazapp.Activities.CongratsFinalActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.MileStone;
import com.doveazapp.GettersSetters.ResponseDetails;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/01/25.
 */
public class StatusAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<ResponseDetails> engageNegotiateList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    public StatusAdapter(Activity activity, List<ResponseDetails> engageNegotiateList) {
        this.activity = activity;
        this.engageNegotiateList = engageNegotiateList;
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
            convertView = inflater.inflate(R.layout.status_row, null);

        TextView id = (TextView) convertView.findViewById(R.id.Id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView receiver_id = (TextView) convertView.findViewById(R.id.receiver_id);
        TextView service_a = (TextView) convertView.findViewById(R.id.service_a_id);
        TextView service_b = (TextView) convertView.findViewById(R.id.service_b_id);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        // TextView category = (TextView) convertView.findViewById(R.id.category);
        Button btn_view = (Button) convertView.findViewById(R.id.btn_view);

        final ResponseDetails user_details = engageNegotiateList.get(position);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String partner = user.get(SessionManager.KEY_USER_TYPE);
                if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                    gotoMilestone();
                } else {
                    progressDialog = ProgressDialog.show(activity, "Please wait ...", "Loading", true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("RESPONSE MILESTONE DET", response.toString());
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
                                            MileStone mileStone = new MileStone();
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
                                                Log.v("LOG", userid);

                                                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                                                alertDialog.setTitle("Credit Information");
                                                alertDialog.setMessage(name + " transferred " + credits + " " + "points as security deposit for your package!!!");
                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //dialog.dismiss();
                                                                gotoMilestone();
                                                            }
                                                        });
                                                alertDialog.show();

                                            }
                                        } else {
                                            gotoMilestone();
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

                    ServiceCalls.CalLAPI_to_milestone_details(activity, Request.Method.POST, Constants.MILESTONE_DETAILS, listener, user_details.getId(), api_token);
                }
            }

            private void gotoMilestone() {
                HashMap<String, String> user = session.getUserDetails();
                final String partner = user.get(SessionManager.KEY_USER_TYPE);
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Requesting...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("--OUTPUT STATUS--", response.toString());
                        progressDialog.dismiss();
                        //Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
                        System.out.println(response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            //final String value = obj.getString("value");
                            // JSONArray userarray = obj.getJSONArray("value");
                            JSONObject valueobj = obj.getJSONObject("value");

                            Log.v("VALUE CHECK", valueobj.toString());

                            //for (int i = 0; i < userarray.length(); i++) {
                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                //Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                /*{"goto_milestone":1,"userid":"2","service_a_id":"163","service_b_id":"206",
                                        "value":"50","fee":"500","service_type":"A"}  --- for PARTNER*//*
                                *//*{"goto_milestone":1,"userid":"4","service_b_id":"211",
                                "service_a_id":"166","offer":"A","fee":"50"} -- for DELIVER*/
                                String user_id = valueobj.getString("userid");
                                String service_a_id = valueobj.getString("service_a_id");
                                String service_b_id = valueobj.getString("service_b_id");
                                String value_item = valueobj.getString("value");
                                String fee = valueobj.getString("fee");
                                String service_type = valueobj.getString("service_type");
                                String reference_id = valueobj.getString(Constants.KEY_REFERENCE_ID);

                                if (partner.equals(Constants.KEY_TYPE_DELIVER) && service_type.equals("A")) {
                                    Intent to_accept = new Intent(activity, AcceptCreditActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_accept.putExtra(Constants.KEY_VALUE_ITEM, value_item);
                                    to_accept.putExtra(Constants.KEY_FEE, fee);
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    activity.startActivity(to_accept);
                                }
                                if (partner.equals(Constants.KEY_TYPE_DELIVER) && service_type.equals("B")) {
                                    Intent to_accept = new Intent(activity, AcceptCreditActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_accept.putExtra(Constants.KEY_FEE, fee);
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    activity.startActivity(to_accept);
                                }
                                if (partner.equals(Constants.KEY_TYPE_DELIVER) && service_type.equals("C")) {
                                    Intent to_accept = new Intent(activity, AcceptCreditActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_accept.putExtra(Constants.KEY_FEE, fee);
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    activity.startActivity(to_accept);
                                }
                                if (partner.equals(Constants.KEY_TYPE_PARTNER) && service_type.equals("A")) {
                                    Intent to_accept = new Intent(activity, CongratsFinalActivity.class);
                                    to_accept.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_accept.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    to_accept.putExtra(Constants.KEY_VALUE_ITEM, value_item);
                                    activity.startActivity(to_accept);
                                }
                                if (partner.equals(Constants.KEY_TYPE_PARTNER) && service_type.equals("B")) {
                                    Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                    to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_milestone.putExtra(Constants.KEY_VALUE_ITEM, value_item);
                                    activity.startActivity(to_milestone);
                                }
                                if (partner.equals(Constants.KEY_TYPE_PARTNER) && service_type.equals("C")) {
                                    Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                    to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_milestone.putExtra(Constants.KEY_VALUE_ITEM, value_item);
                                    activity.startActivity(to_milestone);
                                }
                                //else if partner goto milestone we need to do that here
                            }
                            // }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                            progressDialog.dismiss();
                        }
                    }
                };
                if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String user_id = user.get(SessionManager.KEY_USERID);
                    //String userId = user_details.getType_b_userid();
                    String serviceId = user_details.getService_a_id();
                    String Service_b_id = user_details.getService_b_id();
                    ServiceCalls.CallAPI_NegotiationAccept_forpartner(activity, Request.Method.POST, Constants.NOTIFICATION_STATUS, listener, user_id, serviceId, Service_b_id, api_token);
                }
                if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String userId = user_details.getSender_id();
                    String serviceId = user_details.getService_b_id();
                    ServiceCalls.CallAPI_NegotiationAccept_fordeliver(activity, Request.Method.POST, Constants.NOTIFICATION_STATUS, listener, userId, serviceId, api_token);
                }
            }
        });

        id.setText(user_details.getSender_id());
        name.setText(user_details.getFullname());
        receiver_id.setText(user_details.getReceiver_id());
        service_a.setText(user_details.getService_a_id());
        service_b.setText(user_details.getService_b_id());
        session = new SessionManager(activity);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String partner = user.get(SessionManager.KEY_USER_TYPE);

        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
            date.setText(user_details.getDate());
        }
        if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
            date.setText(user_details.getTravel_date());
        }
        return convertView;
    }
}
