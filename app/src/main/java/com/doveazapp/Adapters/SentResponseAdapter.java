package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.ResponseDetails;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/01/14.
 */
public class SentResponseAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<ResponseDetails> responseDetailsList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    public SentResponseAdapter(Activity activity, List<ResponseDetails> responseDetailsList) {
        this.activity = activity;
        this.responseDetailsList = responseDetailsList;
    }

    public SentResponseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return responseDetailsList.size();
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
            convertView = inflater.inflate(R.layout.response_list_row, null);

        TextView id = (TextView) convertView.findViewById(R.id.Id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView receiver_id = (TextView) convertView.findViewById(R.id.receiver_id);
        TextView service_a = (TextView) convertView.findViewById(R.id.service_a_id);
        TextView service_b = (TextView) convertView.findViewById(R.id.service_b_id);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        final ResponseDetails responseDetails = responseDetailsList.get(position);
        session = new SessionManager(activity);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String partner = user.get(SessionManager.KEY_USER_TYPE);

        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
            date.setText(responseDetails.getDate());
        }
        if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
            date.setText(responseDetails.getTravel_date());
        }
        id.setText(responseDetails.getId());
        name.setText(responseDetails.getFullname());
        receiver_id.setText(responseDetails.getReceiver_id());
        service_a.setText(responseDetails.getService_a_id());
        service_b.setText(responseDetails.getService_b_id());


        /*// travel_date
        traveldate.setText(String.valueOf(m.getDelivery_date()));
        // risk
        risk_value.setText(String.valueOf(m.getRisk_score()));*/
        return convertView;
    }
}
