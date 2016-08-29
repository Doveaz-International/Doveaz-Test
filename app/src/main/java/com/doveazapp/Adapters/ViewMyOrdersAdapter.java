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
import com.doveazapp.GettersSetters.OrderDetailsCustomer;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by Karthik on 7/26/2016.
 */
public class ViewMyOrdersAdapter extends ArrayAdapter<OrderDetailsCustomer> {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private ArrayList<OrderDetailsCustomer> deliver_list;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    public ViewMyOrdersAdapter(Activity a, int layoutResourceId, ArrayList<OrderDetailsCustomer> deliver_list) {
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
            convertView = inflater.inflate(R.layout.db_list_cell_review, null);

        TextView txt_id = (TextView) convertView.findViewById(R.id.txt_id);
        TextView txt_pName = (TextView) convertView.findViewById(R.id.txt_pName);
        TextView txt_pPrice = (TextView) convertView.findViewById(R.id.txt_pPrice);
        TextView txt_pQuantity = (TextView) convertView.findViewById(R.id.txt_pQuantity);

        final OrderDetailsCustomer order_details_customer = deliver_list.get(position);
        txt_id.setText(order_details_customer.getOrder_id());
        txt_pName.setText(order_details_customer.getDescription());
        txt_pPrice.setText(order_details_customer.getPrice());
        txt_pQuantity.setText(order_details_customer.getQty());

        return convertView;
    }


}
