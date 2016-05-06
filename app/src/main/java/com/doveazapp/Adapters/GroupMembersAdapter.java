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
import com.doveazapp.GettersSetters.GroupMembers;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/02/20.
 */
public class GroupMembersAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<GroupMembers> contactList;

    Context context = null;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public GroupMembersAdapter(Activity activity, List<GroupMembers> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }


    public GroupMembersAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.group_memberslist_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();

        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.group_image);
        TextView group_name = (TextView) convertView.findViewById(R.id.group_name);
        TextView group_id = (TextView) convertView.findViewById(R.id.group_id);
        Button remove = (Button) convertView.findViewById(R.id.button_remove);

        final GroupMembers groupInfo = contactList.get(position);

        if (groupInfo.getAdmin().equals("Admin")) {
            remove.setVisibility(View.VISIBLE);

        } else {
            remove.setVisibility(View.GONE);

        }

        group_id.setText(groupInfo.getUserid());
        networkImageView.setImageUrl(groupInfo.getProfile_pic_url(), imageLoader);
        group_name.setText(groupInfo.getFullname());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Requesting to remove...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("OUTPUT REMOVE MEMBER", response);
                        //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();

                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException exception) {
                            progressDialog.dismiss();
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                    }
                };
                session = new SessionManager(activity);
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                String userId = groupInfo.getUserid();
                String groupId = groupInfo.getGroup_id();
                Log.v("CallingAPI", Constants.DELETE_GROUP_MEMBER);
                ServiceCalls.CallAPI_toDeleteMembers(activity, Request.Method.POST, Constants.DELETE_GROUP_MEMBER,
                        listener, userId, groupId, api_token);
            }
        });
        return convertView;
    }
}
