package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.GettersSetters.UserGroupInfo;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.List;

/**
 * Created by Karthik on 2016/02/17.
 */
public class ListGroupsBuyDeliverAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<UserGroupInfo> contactList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public ListGroupsBuyDeliverAdapter(Activity activity, List<UserGroupInfo> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }


    public ListGroupsBuyDeliverAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return contactList.size();
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
            convertView = inflater.inflate(R.layout.group_list_buy_deliver, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();

        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.group_image);
        TextView group_name = (TextView) convertView.findViewById(R.id.group_name);
        TextView group_id = (TextView) convertView.findViewById(R.id.group_id);
        TextView userId = (TextView) convertView.findViewById(R.id.userid);
        TextView text_business = (TextView) convertView.findViewById(R.id.text_business);
        ImageView imageView_network = (ImageView) convertView.findViewById(R.id.img_verified_group);

        final UserGroupInfo groupInfo = contactList.get(position);

        if (groupInfo.getId() != null) {
            group_id.setText(groupInfo.getId());
        } else {
            group_id.setText("");
        }

        if (groupInfo.getStatus().equals("1")) {
            text_business.setText("Business");
            imageView_network.setVisibility(View.GONE);
        } else {
            text_business.setText("");
            imageView_network.setVisibility(View.VISIBLE);
        }

        networkImageView.setImageUrl(groupInfo.getImage(), imageLoader);
        group_name.setText(groupInfo.getGroup_name());
        userId.setText(groupInfo.getUserid());
        return convertView;
    }
}
