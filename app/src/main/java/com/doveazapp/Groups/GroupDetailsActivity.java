package com.doveazapp.Groups;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.PartnerDetailActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.GroupMembersAdapter;
import com.doveazapp.Adapters.GroupViewServiceAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.GroupMembers;
import com.doveazapp.GettersSetters.GroupServiceInfo;
import com.doveazapp.GettersSetters.GroupServices;
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
 * Created by Karthik on 2016/02/18.
 */
public class GroupDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_view_members, btn_view_service_tab, btn_stat_tab, button_delete_group;

    LinearLayout members_tab, service_tab, stat_tab;

    String groupid, userid, user_id, admin;

    //session manager
    SessionManager session;

    TextView text_risk, text_admin, text_total, group_name;

    NetworkImageView networkImageView;

    String group_id, service_a_id;

    //progress dialog
    ProgressDialog progressDialog;

    private List<GroupServices> groupServicesList = new ArrayList<GroupServices>();

    private ListView listView;

    private GroupViewServiceAdapter adapter;

    private List<GroupMembers> groupMembersList = new ArrayList<GroupMembers>();

    private ListView listView_members;

    private GroupMembersAdapter adapter_members;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    String serviceId;

    String where_buy, one_way, delivery_date, deliver_address, deliver_country, deliver_city, deliver_state, deliver_postalcode, minimum_tip, offer_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_details_activity);

        menuvisibilityinAlldevices();
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        final HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        final String my_userId = user.get(SessionManager.KEY_USERID);

        text_risk = (TextView) findViewById(R.id.text_risk);
        text_admin = (TextView) findViewById(R.id.text_admin);
        text_total = (TextView) findViewById(R.id.text_total);
        group_name = (TextView) findViewById(R.id.group_name);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        networkImageView = (NetworkImageView) findViewById(R.id.group_image);

        //LinearLayouts
        members_tab = (LinearLayout) findViewById(R.id.members_tab);
        service_tab = (LinearLayout) findViewById(R.id.service_tab);
        stat_tab = (LinearLayout) findViewById(R.id.stat_tab);

        /*Group members list*/
        btn_view_members = (Button) findViewById(R.id.btn_view_members);
        btn_view_service_tab = (Button) findViewById(R.id.btn_view_service_tab);
        btn_stat_tab = (Button) findViewById(R.id.btn_stat_tab);
        button_delete_group = (Button) findViewById(R.id.button_delete_group);

        btn_view_members.setOnClickListener(this);
        btn_view_service_tab.setOnClickListener(this);
        btn_stat_tab.setOnClickListener(this);
        button_delete_group.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        groupid = bundle.getString(Constants.KEY_GROUP_ID);
        user_id = bundle.getString(Constants.KEY_USERID);
        admin = bundle.getString(Constants.KEY_ADMIN);

        if (admin.equals("Admin")) {
            button_delete_group.setVisibility(View.VISIBLE);
        } else {
            button_delete_group.setVisibility(View.GONE);
        }

        //Showing view_members tab alone
        members_tab.setVisibility(View.VISIBLE);
        service_tab.setVisibility(View.GONE);
        stat_tab.setVisibility(View.GONE);

        listView_members = (ListView) findViewById(R.id.group_members_list);
        adapter_members = new GroupMembersAdapter(GroupDetailsActivity.this, groupMembersList);
        listView_members.setAdapter(adapter_members);

        listView_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
            }
        });

        listView = (ListView) findViewById(R.id.service_list_group);
        adapter = new GroupViewServiceAdapter(GroupDetailsActivity.this, groupServicesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");
                TextView service_id = (TextView) findViewById(R.id.service_id);
                TextView type_a_userid = (TextView) findViewById(R.id.type_a_userid);
                serviceId = service_id.getText().toString();
                userid = type_a_userid.getText().toString();
                callAPI_toget_info();
            }
        });

        CallAPI_toGetMembers();
    }

    private void callAPI_toget_info() {
        progressDialog = ProgressDialog.show(GroupDetailsActivity.this, "Please wait ...", "Loading List...", true);
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
                    JSONArray service_array = value_obj.getJSONArray("service");

                    for (int i = 0; i < service_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            final GroupServiceInfo groupServices = new GroupServiceInfo();
                            JSONObject jsonobject = service_array.getJSONObject(i);
                            groupServices.setCategory_name(jsonobject.getString(Constants.KEY_CATEGORY_NAME));
                            groupServices.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            groupServices.setItem_short_description(jsonobject.getString(Constants.KEY_ITEM_SHORT_DESC));
                            groupServices.setWhere_to_buy(jsonobject.getString(Constants.KEY_REGION));
                            groupServices.setPick_up_address(jsonobject.getString(Constants.KEY_PICK_ADDRESS));
                            groupServices.setPick_up_city(jsonobject.getString(Constants.KEY_PICK_CITY));
                            groupServices.setPick_up_country(jsonobject.getString(Constants.KEY_PICK_COUNTRY));
                            groupServices.setPick_up_state(jsonobject.getString(Constants.KEY_PICK_STATE));
                            groupServices.setPick_up_postalcode(jsonobject.getString(Constants.KEY_PICK_POSTALCODE));
                            groupServices.setDelivery_address(jsonobject.getString(Constants.KEY_DELIVERY_ADDRESS));
                            groupServices.setDelivery_city(jsonobject.getString(Constants.KEY_DELIVERY_CITY));
                            groupServices.setDelivery_country(jsonobject.getString(Constants.KEY_DELIVERY_COUNTRY));
                            groupServices.setDelivery_state(jsonobject.getString(Constants.KEY_DELIVERY_STATE));
                            groupServices.setDelivery_postalcode(jsonobject.getString(Constants.KEY_DELIVERY_POSTAL));
                            groupServices.setDate(jsonobject.getString(Constants.KEY_DATE));

                            AlertDialog alertDialog = new AlertDialog.Builder(GroupDetailsActivity.this).create();
                            alertDialog.setTitle("Service Details");
                            alertDialog.setMessage("Name: " + groupServices.getFullname() + "\n\n" +
                                    "Category: " + groupServices.getCategory_name() + "\n\n" +
                                    "Item Description: " + groupServices.getItem_short_description() + "\n\n" +
                                    "Pickup Address: " + groupServices.getPick_up_address() + "," + groupServices.getPick_up_city() + "," +
                                    groupServices.getPick_up_state() + "," + groupServices.getPick_up_country() + "," + groupServices.getPick_up_postalcode() + "\n\n" +
                                    "Delivery Address: " + groupServices.getDelivery_address() + "," + groupServices.getDelivery_city() + "," +
                                    groupServices.getDelivery_state() + "," + groupServices.getDelivery_country() + "," + groupServices.getDelivery_postalcode() + "\n\n" +
                                    "Delivery Date: " + groupServices.getDate());
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            where_buy = groupServices.getWhere_to_buy();
                                            delivery_date = groupServices.getDate();
                                            deliver_country = groupServices.getDelivery_country();
                                            deliver_address = groupServices.getDelivery_address();
                                            deliver_city = groupServices.getDelivery_city();
                                            deliver_state = groupServices.getDelivery_state();
                                            deliver_postalcode = groupServices.getDelivery_postalcode();
                                            CallAPI_toAddService();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
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
        String service_type = "A";
        Log.v("calling api", Constants.GROUP_SERVICE_DETAILS);
        ServiceCalls.CallAPI_togetInfo(this, Request.Method.POST, Constants.GROUP_SERVICE_DETAILS, listener, serviceId, service_type, api_token);
    }

    private void CallAPI_toAddService() {
        progressDialog = ProgressDialog.show(GroupDetailsActivity.this, "Please wait ...", "Adding service...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT ADD SERVICE", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        String service_b_id = value_obj.getString(Constants.KEY_ID);

                        Intent to_details = new Intent(getApplicationContext(), PartnerDetailActivity.class);
                        to_details.putExtra(Constants.KEY_USERID, userid);
                        to_details.putExtra(Constants.KEY_SERVICEID, serviceId);
                        to_details.putExtra(Constants.KEY_SERVICE_B_ID, service_b_id);
                        startActivity(to_details);
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
        Log.v("Calling API", Constants.SAVE_BUYANDDELIVERY);
        String service_type = "A";
        String one_way = "0";
        String tip = "5";
        String offer = "ALL";
        String return_date = "";
        ServiceCalls.CallAPI_to_AddservicefromGroup(this, Request.Method.POST, Constants.SAVE_BUYANDDELIVERY,
                listener, service_type, where_buy, one_way, delivery_date, return_date, deliver_country, deliver_address, deliver_city,
                deliver_state, deliver_postalcode, tip, offer, api_token);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_view_members) {
            btn_view_members.setFocusable(true);
            btn_view_members.setFocusableInTouchMode(true);
            btn_view_service_tab.setFocusable(false);
            btn_view_service_tab.setFocusableInTouchMode(false);
            btn_stat_tab.setFocusable(false);
            btn_stat_tab.setFocusableInTouchMode(false);
            members_tab.setVisibility(View.VISIBLE);
            service_tab.setVisibility(View.GONE);
            stat_tab.setVisibility(View.GONE);

            groupMembersList.clear();
            CallAPI_toGetMembers();
        }
        if (view == btn_view_service_tab) {
            btn_view_service_tab.setFocusable(true);
            btn_view_service_tab.setFocusableInTouchMode(true);
            btn_view_members.setFocusable(false);
            btn_view_members.setFocusableInTouchMode(false);
            btn_stat_tab.setFocusable(false);
            btn_stat_tab.setFocusableInTouchMode(false);
            members_tab.setVisibility(View.GONE);
            service_tab.setVisibility(View.VISIBLE);
            stat_tab.setVisibility(View.GONE);

            CallAPI_toget_services();
        }
        if (view == btn_stat_tab) {
            btn_stat_tab.setFocusable(true);
            btn_stat_tab.setFocusableInTouchMode(true);
            btn_view_members.setFocusable(false);
            btn_view_members.setFocusableInTouchMode(false);
            btn_view_service_tab.setFocusable(false);
            btn_view_service_tab.setFocusableInTouchMode(false);
            members_tab.setVisibility(View.GONE);
            service_tab.setVisibility(View.GONE);
            stat_tab.setVisibility(View.VISIBLE);

            callAPI_toGetDetails();
        }
        if (view == button_delete_group) {
            callAPI_toDelete_group();
        }
    }

    private void callAPI_toDelete_group() {
        progressDialog = ProgressDialog.show(GroupDetailsActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT GROUP DET - A", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    }
                    if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        Intent to_groups = new Intent(getApplicationContext(), CreateGroupListActivity.class);
                        startActivity(to_groups);
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
        Log.v("Calling API", Constants.DELETE_GROUP);
        ServiceCalls.CallAPI_toGetGroupDetailsA(this, Request.Method.POST, Constants.DELETE_GROUP, listener, groupid, api_token);
    }

    /*2nd TAB (SERVICES)*/
    private void CallAPI_toget_services() {
        progressDialog = ProgressDialog.show(GroupDetailsActivity.this, "Please wait ...", "Loading List...", true);
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
                    JSONArray service_array = value_obj.getJSONArray("services");

                    for (int i = 0; i < service_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            GroupServices groupServices = new GroupServices();
                            JSONObject jsonobject = service_array.getJSONObject(i);
                            groupServices.setType_a_userid(jsonobject.getString(Constants.TYPE_A_USERID));
                            groupServices.setName(jsonobject.getString(Constants.KEY_NAME));
                            groupServices.setProfile_pic(jsonobject.getString(Constants.KEY_PROFILE_PIC));
                            groupServices.setService_id(jsonobject.getString(Constants.KEY_SERVICEID));

                            groupServicesList.clear();
                            groupServicesList.add(groupServices);
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
        Log.v("calling api", Constants.LIST_GROUP_SERVICES);
        ServiceCalls.CallAPI_tolistservice_group(this, Request.Method.POST, Constants.LIST_GROUP_SERVICES, listener, groupid, api_token);
    }

    /*3rd TAB (STAT)*/
    private void callAPI_toGetDetails() {
        progressDialog = ProgressDialog.show(GroupDetailsActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT GROUP DET - A", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray group_array = value_obj.getJSONArray("group_details");

                    for (int i = 0; i < group_array.length(); i++) {

                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        }
                        if (status.equals("true")) {
                            JSONObject jsonobject = group_array.getJSONObject(i);
                            UserGroupInfo groupInfo = new UserGroupInfo();
                            groupInfo.setId(jsonobject.getString(Constants.KEY_ID));
                            groupInfo.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            groupInfo.setGroup_name(jsonobject.getString(Constants.KEY_GROUP_NAME));
                            groupInfo.setGroup_slogan(jsonobject.getString(Constants.KEY_GROUP_SLOGAN));
                            groupInfo.setImage(jsonobject.getString(Constants.KEY_IMAGE));
                            groupInfo.setStatus(jsonobject.getString(Constants.KEY_STATUS));
                            groupInfo.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            groupInfo.setRisk_score(jsonobject.getString(Constants.KEY_RISKSCORE));
                            groupInfo.setMember_count(jsonobject.getString(Constants.KEY_GROUP_COUNT));

                            text_risk.setText(groupInfo.getRisk_score());
                            text_admin.setText(groupInfo.getFullname());
                            text_total.setText(groupInfo.getMember_count());
                            group_name.setText(groupInfo.getGroup_name());
                            networkImageView.setImageUrl(groupInfo.getImage(), imageLoader);
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
        Log.v("Calling API", Constants.GROUP_DETAILS_TYPE_A);
        ServiceCalls.CallAPI_toGetGroupDetailsA(this, Request.Method.POST, Constants.GROUP_DETAILS_TYPE_A, listener, groupid, api_token);
    }

    /*1ST TAB (MEMBERS)*/
    private void CallAPI_toGetMembers() {
        progressDialog = ProgressDialog.show(GroupDetailsActivity.this, "Please wait ...", "Loading List...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                progressDialog.dismiss();
                Log.v("--RESPONSE MEMBERS--", response.toString());
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray moderator_array = value_obj.getJSONArray("moderator");
                    JSONArray service_array = value_obj.getJSONArray("members");

                    /*for (int j = 0; j < moderator_array.length(); j++) {
                        progressDialog.dismiss();
                        GroupMembers groupMembers = new GroupMembers();
                        JSONObject jsonobject = service_array.getJSONObject(j);

                        groupMembers.setModerator_userid(user_id);
                        HashMap<String, String> user = session.getUserDetails();
                        // token
                        String MyuserID = user.get(SessionManager.KEY_USERID);
                        groupMembers.setMy_user_id(MyuserID);
                    }*/

                    for (int i = 0; i < service_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();

                            GroupMembers groupMembers = new GroupMembers();
                            JSONObject jsonobject = service_array.getJSONObject(i);
                            groupMembers.setUserid(jsonobject.getString(Constants.KEY_USERID));
                            groupMembers.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            groupMembers.setProfile_pic_url(jsonobject.getString(Constants.KEY_PROFILE_PICTURE));
                            groupMembers.setGroup_id(groupid);
                            groupMembers.setAdmin(admin);

                            groupMembersList.add(groupMembers);
                            adapter_members.notifyDataSetChanged();
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
        Log.v("calling api", Constants.GROUP_MEMBERS_LIST);
        ServiceCalls.CallAPI_tolistservice_group(this, Request.Method.POST, Constants.GROUP_MEMBERS_LIST, listener, groupid, api_token);
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
        MenuVisibility.menuVisible(GroupDetailsActivity.this);
    }
}
