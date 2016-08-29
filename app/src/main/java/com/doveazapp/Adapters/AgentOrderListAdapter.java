package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.AgentOrderHistory;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by Karthik on 7/19/2016.
 */
public class AgentOrderListAdapter extends ArrayAdapter<AgentOrderHistory> {
    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private ArrayList<AgentOrderHistory> deliver_list;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    public AgentOrderListAdapter(Activity a, int layoutResourceId, ArrayList<AgentOrderHistory> deliver_list) {
        super(a, layoutResourceId, deliver_list);
        this.activity = a;
        this.deliver_list = deliver_list;
    }

    @Override
    public int getCount() {
        return deliver_list.size();
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
            convertView = inflater.inflate(R.layout.agent_order_row, null);

        TextView txt_order_id = (TextView) convertView.findViewById(R.id.txt_order_id);
        TextView pickup_name = (TextView) convertView.findViewById(R.id.pickup_name);
        TextView delivery_name = (TextView) convertView.findViewById(R.id.delivery_name);
        TextView status = (TextView) convertView.findViewById(R.id.status);

        final AgentOrderHistory deliveries_area = deliver_list.get(position);

        txt_order_id.setText(deliveries_area.getOrder_id());
        pickup_name.setText(deliveries_area.getPickup_name());
        delivery_name.setText(deliveries_area.getDelivery_name());
        status.setText(deliveries_area.getStatus());

        return convertView;
    }
}
