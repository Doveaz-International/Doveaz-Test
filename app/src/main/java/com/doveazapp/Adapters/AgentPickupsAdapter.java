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
import com.doveazapp.GettersSetters.AgentPickups;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by Karthik on 7/18/2016.
 */
public class AgentPickupsAdapter extends ArrayAdapter<AgentPickups> {
    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private ArrayList<AgentPickups> pickup_list;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    /*public AgentPickupsAdapter(Activity context, List<AgentPickups> pickup_list) {
        this.context = activity;
        this.pickup_list = pickup_list;
    }*/

    public AgentPickupsAdapter(Activity a, int layoutResourceId, ArrayList<AgentPickups> pickup_list) {
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
            convertView = inflater.inflate(R.layout.agent_pickups_row, null);

        TextView area = (TextView) convertView.findViewById(R.id.areas);
        TextView orders = (TextView) convertView.findViewById(R.id.count);

        final AgentPickups pickup_area = pickup_list.get(position);

        area.setText(pickup_area.getPickup_area());
        orders.setText(pickup_area.getOrders());

        return convertView;
    }

}
