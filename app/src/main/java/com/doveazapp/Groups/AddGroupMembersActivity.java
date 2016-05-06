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
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.AddGroupMembersAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.GroupInfo;
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
 * Created by Karthik on 2016/02/17.
 */
public class AddGroupMembersActivity extends AppCompatActivity implements View.OnClickListener {

    private List<GroupInfo> GroupListDetails = new ArrayList<GroupInfo>();

    private ListView listView;

    private AddGroupMembersAdapter adapter;

    //session manager
    SessionManager session;

    public String group_id;

    //progress dialog
    ProgressDialog progressDialog;

    Button button_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_add_members_list);

        menuvisibilityinAlldevices();
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        button_finish = (Button) findViewById(R.id.button_finish);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        group_id = bundle.getString(Constants.KEY_GROUP_ID);

        listView = (ListView) findViewById(R.id.add_memberList);
        adapter = new AddGroupMembersAdapter(AddGroupMembersActivity.this, GroupListDetails);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
            }
        });

        button_finish.setOnClickListener(this);
        callAPI_togetfriendsList();
    }

    private void callAPI_togetfriendsList() {
        progressDialog = ProgressDialog.show(AddGroupMembersActivity.this, "Please wait ...", "Validating Address...", true);
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
                    JSONArray value_array = value_obj.getJSONArray(Constants.KEY_VERIFIED_PROFILES);
                    //JSONArray value_array = obj.getJSONArray("value");

                    for (int i = 0; i < value_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            GroupInfo groupInfo = new GroupInfo();
                            JSONObject jsonobject = value_array.getJSONObject(i);
                            groupInfo.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            groupInfo.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            groupInfo.setProfile_pic_url(jsonobject.getString(Constants.KEY_PROFILE_PICTURE));
                            groupInfo.setGroup_id(group_id);

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
        Log.v("calling api", Constants.GET_PARTNER_CONTACTS_TO_CREATE_GROUP);
        ServiceCalls.CallAPI_tolistcontacts_Groups(this, Request.Method.POST, Constants.GET_PARTNER_CONTACTS_TO_CREATE_GROUP, listener, api_token);
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
        MenuVisibility.menuVisible(AddGroupMembersActivity.this);
    }

    @Override
    public void onClick(View v) {
        Intent to_groups = new Intent(getApplicationContext(), CreateGroupListActivity.class);
        startActivity(to_groups);
    }
}
