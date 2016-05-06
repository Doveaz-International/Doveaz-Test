package com.doveazapp.Groups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.ListGroupAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.UserGroupInfo;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/02/12.
 */
public class CreateGroupListActivity extends AppCompatActivity implements View.OnClickListener {

    // Buttons...
    Button button_create_group;

    //session manager
    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    private List<UserGroupInfo> GroupListDetails = new ArrayList<UserGroupInfo>();

    private ListView listView;

    private ListGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_list_activity);

        menuvisibilityinAlldevices();

        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        final HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // init buttons...
        button_create_group = (Button) findViewById(R.id.button_create_group);

        // listeners for buttons
        button_create_group.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.group_list);
        adapter = new ListGroupAdapter(CreateGroupListActivity.this, GroupListDetails);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
                TextView group_id = (TextView) v.findViewById(R.id.group_id);
                TextView user_id = (TextView) v.findViewById(R.id.userid);
                TextView text_admin = (TextView) v.findViewById(R.id.text_admin);
                String groupId = group_id.getText().toString();
                String userId = user_id.getText().toString();
                String admin = text_admin.getText().toString();

                Intent to_groupDetails = new Intent(getApplicationContext(), GroupDetailsActivity.class);
                to_groupDetails.putExtra(Constants.KEY_GROUP_ID, groupId);
                to_groupDetails.putExtra(Constants.KEY_USERID, userId);
                to_groupDetails.putExtra(Constants.KEY_ADMIN, admin);
                startActivity(to_groupDetails);
            }
        });

        GroupListDetails.clear();
        callAPI_togetMyGroupList();
    }

    private void callAPI_togetMyGroupList() {
        progressDialog = ProgressDialog.show(CreateGroupListActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                progressDialog.dismiss();
                Log.v("--RESPONSE LIST--", response.toString());
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray value_array = value_obj.getJSONArray("groups");

                    for (int i = 0; i < value_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            UserGroupInfo groupInfo = new UserGroupInfo();
                            JSONObject jsonobject = value_array.getJSONObject(i);
                            groupInfo.setId(jsonobject.getString(Constants.KEY_ID));
                            groupInfo.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            groupInfo.setGroup_name(jsonobject.getString(Constants.KEY_GROUP_NAME));
                            groupInfo.setGroup_slogan(jsonobject.getString(Constants.KEY_GROUP_SLOGAN));
                            groupInfo.setImage(jsonobject.getString(Constants.KEY_IMAGE));
                            groupInfo.setStatus(jsonobject.getString(Constants.KEY_STATUS));
                            groupInfo.setAdmin(jsonobject.getString(Constants.KEY_ADMIN));

                            Log.v("GROUP NAME", groupInfo.getGroup_name());

                            GroupListDetails.add(groupInfo);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("calling api", Constants.LIST_MY_GROUPS);
        ServiceCalls.CallAPI_tolistcontacts_Groups(this, Request.Method.POST, Constants.LIST_MY_GROUPS, listener, api_token);
    }

    @Override
    public void onClick(View view) {
        if (view == button_create_group) {
            goto_createGroupInfo();
        }
    }

    private void goto_createGroupInfo() {
        Intent to_create_info = new Intent(getApplicationContext(), CreateGroupInfo.class);
        startActivity(to_create_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String user_type = user.get(SessionManager.KEY_USER_TYPE);
        menu.clear();
        if (user_type.equals(Constants.KEY_TYPE_PARTNER)) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.items, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_deliver, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return BaseActivity.CommonClass.HandleMenu(this, item.getItemId());
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CreateGroupListActivity.this);
    }
}
