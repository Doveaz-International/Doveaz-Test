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
import com.doveazapp.GettersSetters.AgentDeliveries;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by Karthik on 7/18/2016.
 */
public class AgentDeliveryAdapter extends ArrayAdapter<AgentDeliveries> {
    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private ArrayList<AgentDeliveries> deliver_list;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    public AgentDeliveryAdapter(Activity a, int layoutResourceId, ArrayList<AgentDeliveries> deliver_list) {
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
            convertView = inflater.inflate(R.layout.agent_deliveries_row, null);

        TextView area = (TextView) convertView.findViewById(R.id.areas);
        TextView orders = (TextView) convertView.findViewById(R.id.count);

        final AgentDeliveries deliveries_area = deliver_list.get(position);

        String area_text = deliveries_area.getPickup_area();

        area_text = area_text.replace("area=", "");
        area_text = area_text.replaceAll("[\\[\\](){}]", "");

        area.setText(area_text);
        orders.setText(deliveries_area.getOrders());

        return convertView;
    }
}
