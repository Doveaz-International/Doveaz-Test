package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.GroupInfo;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/02/17.
 */
public class AddGroupMembersAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<GroupInfo> contactList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();


    public AddGroupMembersAdapter(Activity activity, List<GroupInfo> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }


    public AddGroupMembersAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.group_add_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.img_profilepic);
        TextView text_groupid = (TextView) convertView.findViewById(R.id.group_id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        Button btn_add = (Button) convertView.findViewById(R.id.button_add);

        final GroupInfo groupInfo = contactList.get(position);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Requesting...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("OUTPUT ADD MEMBER", response);
                        //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        try {
                    /*{"status":"true","value":"Member added"}*/
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();

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
                String group_id = groupInfo.getGroup_id();
                String userId = groupInfo.getUserid();
                Log.v("Calling Api", Constants.ADD_MEMBER_TO_GROUP);
                ServiceCalls.CallAPI_toAddMembers(activity, Request.Method.POST, Constants.ADD_MEMBER_TO_GROUP, listener, group_id, userId, api_token);
            }
        });

        text_groupid.setText(groupInfo.getUserid());
        name.setText(groupInfo.getFullname());
        thumbNail.setImageUrl(groupInfo.getProfile_pic_url(), imageLoader);
        return convertView;
    }
}
