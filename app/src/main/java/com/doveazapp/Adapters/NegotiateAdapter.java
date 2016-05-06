package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.EngageNegotiate;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/01/16.
 */
public class NegotiateAdapter extends BaseAdapter {

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

    public NegotiateAdapter(Activity activity, List<EngageNegotiate> engageNegotiateList) {
        this.activity = activity;
        this.engageNegotiateList = engageNegotiateList;
    }


    public NegotiateAdapter(Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.negotiation_row, null);

        TextView id = (TextView) convertView.findViewById(R.id.Id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView receiver_id = (TextView) convertView.findViewById(R.id.receiver_id);
        TextView service_a = (TextView) convertView.findViewById(R.id.service_a_id);
        TextView service_b = (TextView) convertView.findViewById(R.id.service_b_id);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView caddress = (TextView) convertView.findViewById(R.id.negotiated_collection_address);
        TextView daddress = (TextView) convertView.findViewById(R.id.negotiated_delivery_address);
        TextView fee = (TextView) convertView.findViewById(R.id.negotiated_fee);
        Button btn_view = (Button) convertView.findViewById(R.id.btn_view);
        Button btn_accept = (Button) convertView.findViewById(R.id.btn_engage);
        Button btn_decline = (Button) convertView.findViewById(R.id.btn_decline);

        final EngageNegotiate engageNegotiate = engageNegotiateList.get(position);

        date.setText(engageNegotiate.getTravel_date());
        id.setText(engageNegotiate.getId());
        name.setText(engageNegotiate.getFullname());
        receiver_id.setText(engageNegotiate.getReceiver_id());
        service_a.setText(engageNegotiate.getService_a_id());
        service_b.setText(engageNegotiate.getService_b_id());
        fee.setText(engageNegotiate.getNegotiated_fee());
        daddress.setText(engageNegotiate.getNegotiated_delivery_address() + "," + engageNegotiate.getNegotiated_delivery_city() + "," +
                engageNegotiate.getNegotiated_delivery_postalcode()
                + "," + engageNegotiate.getNegotiated_delivery_state() + "," + engageNegotiate.getNegotiated_delivery_country());
        caddress.setText(engageNegotiate.getNegotiated_collection_address() + "," + engageNegotiate.getNegotiated_collection_city() + "," +
                engageNegotiate.getNegotiated_collection_postalcode()
                + "," + engageNegotiate.getNegotiated_collection_state() + "," + engageNegotiate.getNegotiated_collection_country());

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Log.v("--OUTPUT ENGAGE--", response.toString());
                        progressDialog.dismiss();
                        Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
                        System.out.println(response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");
                            JSONArray userarray = obj.getJSONArray("value");

                            for (int i = 0; i < userarray.length(); i++) {
                                if (status.equals("false")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                                } else if (status.equals("true")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                                    HashMap<String, String> user = session.getUserDetails();
                                    // token
                                    String u_name = user.get(SessionManager.KEY_USERNAME);
                                    String userId = engageNegotiate.getSender_id();
                                    sendMessageToGCMAppServer(userId, u_name + " " + "has engaged for your service");
                                }
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
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_a_id();
                    String Service_b_id = engageNegotiate.getService_b_id();
                    ServiceCalls.CallAPI_NegotiationAccept_forpartner(activity, Request.Method.POST, Constants.NEGOTIATE_SERVICE_ACCEPT, listener, userId, serviceId, Service_b_id, api_token);
                }
                if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_b_id();
                    ServiceCalls.CallAPI_NegotiationAccept_fordeliver(activity, Request.Method.POST, Constants.NEGOTIATE_SERVICE_ACCEPT, listener, userId, serviceId, api_token);
                }
            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        System.out.println(response.toString());
                        Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");
                            JSONArray userarray = obj.getJSONArray("value");

                            for (int i = 0; i < userarray.length(); i++) {
                                if (status.equals("false")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                                } else if (status.equals("true")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                                    HashMap<String, String> user = session.getUserDetails();
                                    // token
                                    String u_name = user.get(SessionManager.KEY_USERNAME);
                                    String userId = engageNegotiate.getSender_id();
                                    sendMessageToGCMAppServer(userId, u_name + " " + "has declined your Negotiation!");
                                }
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
                    ServiceCalls.CallAPI_NegotiationAccept_forpartner(activity, Request.Method.POST, Constants.NEGOTIATE_SERVICE_DECLINE, listener, userId, serviceId, Service_b_id, api_token);
                }
                if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
                    // token
                    String api_token = user.get(SessionManager.KEY_APITOKEN);
                    String type = "DECLINE";
                    String userId = engageNegotiate.getSender_id();
                    String serviceId = engageNegotiate.getService_b_id();
                    ServiceCalls.CallAPI_NegotiationAccept_fordeliver(activity, Request.Method.POST, Constants.NEGOTIATE_SERVICE_DECLINE, listener, userId, serviceId, api_token);
                }
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Original Address");
                alertDialog.setMessage("Pickup Address:" + "\n" + engageNegotiate.getPick_up_address() + "," + engageNegotiate.getPick_up_city() + "," + engageNegotiate.getPick_up_postalcode()
                        + "," + engageNegotiate.getPick_up_state() + "," + engageNegotiate.getPick_up_country() + "\n " +
                        "Delivery Address:" + "\n" + engageNegotiate.getDelivery_address() + "," + engageNegotiate.getDelivery_city() + "," +
                        engageNegotiate.getDelivery_postalcode() + "," + engageNegotiate.getDelivery_state() + "," +
                        engageNegotiate.getDelivery_country() + "\n" + "Fee" + "\n" + engageNegotiate.getFee());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
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
