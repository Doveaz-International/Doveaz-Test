package com.doveazapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.CallDialogActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.AgentPickupByArea;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karthik on 7/19/2016.
 */
public class AgentPickupByAreaAdapter extends ArrayAdapter<AgentPickupByArea> {
    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private ArrayList<AgentPickupByArea> pickup_list;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    public AgentPickupByAreaAdapter(Activity a, int layoutResourceId, ArrayList<AgentPickupByArea> pickup_list) {
        super(a, layoutResourceId, pickup_list);
        this.activity = a;
        this.pickup_list = pickup_list;
    }

    @Override
    public int getCount() {
        return pickup_list.size();
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
            convertView = inflater.inflate(R.layout.agent_pickup_byarea_row, null);

        final TextView txt_order_id = (TextView) convertView.findViewById(R.id.txt_order_id);
        TextView pickup_address = (TextView) convertView.findViewById(R.id.pickup_address);
        Button btn_map = (Button) convertView.findViewById(R.id.btn_map);
        final Button btn_update_status = (Button) convertView.findViewById(R.id.btn_update_status);
        Button button_call = (Button) convertView.findViewById(R.id.button_call);
        TextView deliver_name = (TextView) convertView.findViewById(R.id.delivery_name);

        final AgentPickupByArea pickup_area = pickup_list.get(position);

        txt_order_id.setText(pickup_area.getOrder_id());

        deliver_name.setText(pickup_area.getPickup_name());

        pickup_address.setText(pickup_area.getPickup_street() + ", " + pickup_area.getPickup_area() +
                ", " + pickup_area.getPickup_city() + ", " + pickup_area.getPickup_state() + ", " + pickup_area.getPickup_country()
                + ", " + pickup_area.getPickup_zip());

        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, CallDialogActivity.class).putExtra(Constants.KEY_DELIVERY_PHONE, pickup_area.getPickup_phone()));
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + pickup_area.getPickup_street() + ", " + pickup_area.getPickup_area() +
                        ", " + pickup_area.getPickup_city() + ", " + pickup_area.getPickup_state() + ", " + pickup_area.getPickup_country()
                        + ", " + pickup_area.getPickup_zip();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                activity.startActivity(intent);
            }
        });

        btn_update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Requesting...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("OUTPUT UPDATE PICK STATUS", response);

                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");


                            if (status.equals("false")) {
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                JSONObject value_obj = obj.getJSONObject("value");
                                String message = value_obj.getString("message");

                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException exception) {
                            progressDialog.dismiss();
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                    }
                };

                session = new SessionManager(activity);
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                Log.v("Calling API", Constants.AGENT_UPDATE_ORDER_STATUS);
                ServiceCalls.CallAPI_to_UpdateStatus(activity, Request.Method.POST, Constants.AGENT_UPDATE_ORDER_STATUS, listener, txt_order_id.getText().toString(), "1", api_token); //"1" means updating the 1st status that the itme is picked
            }
        });

        return convertView;
    }
}
