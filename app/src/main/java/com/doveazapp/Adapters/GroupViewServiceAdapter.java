package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.GettersSetters.GroupServices;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.List;

/**
 * Created by Karthik on 2016/02/19.
 */
public class GroupViewServiceAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<GroupServices> servicesList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public GroupViewServiceAdapter(Activity activity, List<GroupServices> servicesList) {
        this.activity = activity;
        this.servicesList = servicesList;
    }
    
    public GroupViewServiceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return servicesList.size();
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
            convertView = inflater.inflate(R.layout.group_serviceslist_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();

        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.profile_image);
        TextView name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView service_id = (TextView) convertView.findViewById(R.id.service_id);
        TextView type_a_userid = (TextView) convertView.findViewById(R.id.type_a_userid);

        final GroupServices groupInfo = servicesList.get(position);

        name.setText(groupInfo.getName());
        networkImageView.setImageUrl(groupInfo.getProfile_pic(), imageLoader);
        service_id.setText(groupInfo.getService_id());
        type_a_userid.setText(groupInfo.getType_a_userid());
        return convertView;
    }
}
