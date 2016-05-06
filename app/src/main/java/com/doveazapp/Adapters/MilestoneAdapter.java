package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.doveazapp.Activities.CongratsFinalActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Activities.ViewMilestoneDeliverActivity;
import com.doveazapp.Constants;
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
 * Created by Karthik on 2016/02/02.
 */
public class MilestoneAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<MileStone> mileStoneList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    public MilestoneAdapter(Activity activity, List<MileStone> mileStoneList) {
        this.activity = activity;
        this.mileStoneList = mileStoneList;
    }

    @Override
    public int getCount() {
        return mileStoneList.size();
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
            convertView = inflater.inflate(R.layout.milestone_row, null);

        //TextView id = (TextView) convertView.findViewById(R.id.Id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        //TextView receiver_id = (TextView) convertView.findViewById(R.id.receiver_id);
        TextView service_a = (TextView) convertView.findViewById(R.id.service_a_id);
        TextView service_b = (TextView) convertView.findViewById(R.id.service_b_id);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        // TextView category = (TextView) convertView.findViewById(R.id.category);
        Button btn_view = (Button) convertView.findViewById(R.id.btn_view);

        final MileStone milestone_details = mileStoneList.get(position);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Loading", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("RESPONSE MILESTONE DET", response.toString());
                        progressDialog.dismiss();
                        System.out.println(response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");
                            JSONObject userobj = obj.getJSONObject("value");
                            final String service_type = userobj.getString("service_type");
                            String reference_id = userobj.getString("reference_id");

                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                //{"status":"false","value":{"message":"No Milestone details available","service_type":"A","reference_id":"7"}}
                                String message = userobj.getString("message");
                                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                                Log.v("reference_id", reference_id);
                                HashMap<String, String> user = session.getUserDetails();
                                final String partner = user.get(SessionManager.KEY_USER_TYPE);
                                if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                                    Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                    to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_milestone.putExtra(Constants.KEY_SUCCESS, "SUCCESS");
                                    activity.startActivity(to_milestone);
                                } else if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                                    Intent to_milestone = new Intent(activity, ViewMilestoneDeliverActivity.class);
                                    to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                    to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                    activity.startActivity(to_milestone);
                                }
                                //Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            }
                            //it has it, do appropriate processing
                            JSONArray detailObj = userobj.getJSONArray("milestone_details");
                            for (int i = 0; i < detailObj.length(); i++) {
                                if (status.equals("true")) {
                                    /*{"status":"true","value":{"service_type":"B","milestone_details":[{"id":"2","milestone_1_payment_status":"SUCCESS",
                                    "milestone_1_status":null,"milestone_2_image_url":null,
                                    "milestone_2_status":null,"milestone_3":null,"milestone_3_status":null,"status":null,"reference_id":"111"}]}}*/
                                    MileStone mileStone = new MileStone();
                                    JSONObject jsonobject = detailObj.getJSONObject(i);
                                    if (jsonobject.has(Constants.KEY_REFERENCE_ID)) {
                                        mileStone.setReference_id(jsonobject.getString(Constants.KEY_REFERENCE_ID));
                                    }
                                    if (jsonobject.has(Constants.KEY_MILESTONE1_STATUS)) {
                                        mileStone.setMilestone_1_status(jsonobject.getString(Constants.KEY_MILESTONE1_STATUS));
                                    }
                                    if (jsonobject.has(Constants.KEY_MILESTONE2_STATUS)) {
                                        mileStone.setMilestone_2_status(jsonobject.getString(Constants.KEY_MILESTONE2_STATUS));
                                    }
                                    if (jsonobject.has(Constants.KEY_MILESTONE3_STATUS)) {
                                        mileStone.setMilestone_3_status(jsonobject.getString(Constants.KEY_MILESTONE3_STATUS));
                                    }

                                    //String ref_id = mileStone.getReference_id();
                                    String Mile1_status = mileStone.getMilestone_1_status();
                                    String Mile2_status = mileStone.getMilestone_2_status();
                                    String Mile3_status = mileStone.getMilestone_3_status();
                                    HashMap<String, String> user = session.getUserDetails();
                                    final String partner = user.get(SessionManager.KEY_USER_TYPE);
                                    if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
                                        Intent to_milestone = new Intent(activity, CongratsFinalActivity.class);
                                        to_milestone.putExtra(Constants.KEY_MILESTONE1_STATUS, Mile1_status);
                                        to_milestone.putExtra(Constants.KEY_MILESTONE2_STATUS, Mile2_status);
                                        to_milestone.putExtra(Constants.KEY_MILESTONE3_STATUS, Mile3_status);
                                        to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                        to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                        to_milestone.putExtra(Constants.KEY_SUCCESS, "SUCCESS");
                                        activity.startActivity(to_milestone);
                                    } else if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                                        Intent to_milestone = new Intent(activity, ViewMilestoneDeliverActivity.class);
                                        to_milestone.putExtra(Constants.KEY_MILESTONE1_STATUS, Mile1_status);
                                        to_milestone.putExtra(Constants.KEY_MILESTONE2_STATUS, Mile2_status);
                                        to_milestone.putExtra(Constants.KEY_MILESTONE3_STATUS, Mile3_status);
                                        to_milestone.putExtra(Constants.KEY_REFERENCE_ID, reference_id);
                                        to_milestone.putExtra(Constants.KEY_SERVICE_TYPE, service_type);
                                        activity.startActivity(to_milestone);
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                            progressDialog.dismiss();
                        }
                    }
                };

                session = new SessionManager(activity);
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                String reference_id = milestone_details.getId();
                Log.v("Calling API", Constants.MILESTONE_DETAILS);
                ServiceCalls.CalLAPI_to_milestone_details(activity, Request.Method.POST, Constants.MILESTONE_DETAILS, listener, reference_id, api_token);
            }
        });

        //id.setText(milestone_details.getSender_id());
        name.setText(milestone_details.getFullname());
        //receiver_id.setText(milestone_details.getReceiver_id());
        //service_a.setText(milestone_details.getService_a_id());
        //service_b.setText(milestone_details.getService_b_id());
        date.setText(milestone_details.getDate_of_acceptance());


        return convertView;
    }
}
