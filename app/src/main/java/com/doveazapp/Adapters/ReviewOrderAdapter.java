package com.doveazapp.Adapters;

/**
 * Created by Karthik on 7/4/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.OrderReviewInfo;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.List;

public class ReviewOrderAdapter extends BaseAdapter {
    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<OrderReviewInfo> order_list;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    //For GCM
    ShareExternalServer appUtil;

    public ReviewOrderAdapter(Activity activity, List<OrderReviewInfo> order_list) {
        this.activity = activity;
        this.order_list = order_list;
    }

    public ReviewOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return order_list.size();
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
            convertView = inflater.inflate(R.layout.db_list_cell_review, null);

        TextView txt_id = (TextView) convertView.findViewById(R.id.txt_id);
        txt_id.setVisibility(View.GONE);
        TextView txt_pName = (TextView) convertView.findViewById(R.id.txt_pName);
        TextView txt_pPrice = (TextView) convertView.findViewById(R.id.txt_pPrice);
        TextView txt_pQuantity = (TextView) convertView.findViewById(R.id.txt_pQuantity);

        final OrderReviewInfo order_details_customer = order_list.get(position);
        txt_id.setText(order_details_customer.getProduct_id());
        txt_pName.setText(order_details_customer.getProduct_name());
        txt_pPrice.setText(order_details_customer.getUnit_price());
        txt_pQuantity.setText(order_details_customer.getQuantity());

        return convertView;
    }

}

