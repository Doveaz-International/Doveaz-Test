package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.doveazapp.Constants;
import com.doveazapp.Notifications.DeclineNotificationActivity;
import com.doveazapp.Notifications.EngagementsNotificationActivity;
import com.doveazapp.Notifications.SentNotificationActivity;
import com.doveazapp.Notifications.StatusForPartnerActivity;
import com.doveazapp.Notifications.StatusNotificationActivity;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import java.util.HashMap;

/**
 * Created by Karthik on 2016/01/14.
 */
public class NotificationsMenuActivity extends AppCompatActivity implements View.OnClickListener {

    // Session Manager Class
    SessionManager session;

    Button btn_sent, btn_declined, btn_engagements, btn_negotiations, btn_status, btn_status_for_partner;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_filter_activity);

        menuvisibilityinAlldevices();

        btn_sent = (Button) findViewById(R.id.btn_sent);
        //btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_declined = (Button) findViewById(R.id.btn_declined);
        btn_engagements = (Button) findViewById(R.id.btn_engagements);
        //btn_negotiations = (Button) findViewById(R.id.btn_negotiations);
        btn_status = (Button) findViewById(R.id.btn_status);
        btn_status_for_partner = (Button) findViewById(R.id.btn_status_for_partner);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        String usertype = user.get(SessionManager.KEY_USER_TYPE);
        Log.v("USERTYPE", usertype);

        // 1 = partner and 0 = deliver
        if (usertype.equals(Constants.KEY_TYPE_PARTNER)) {
            //btn_negotiations.setVisibility(View.GONE);
            // btn_engagements.setVisibility(View.GONE);
            btn_engagements.setText(Constants.KEY_AVAILABE_JOBS);
            btn_status_for_partner.setVisibility(View.VISIBLE);
            btn_status.setVisibility(View.GONE);
        } else if (usertype.equals(Constants.KEY_TYPE_DELIVER)) {
            btn_engagements.setVisibility(View.VISIBLE);
            //btn_negotiations.setVisibility(View.VISIBLE);
            btn_status_for_partner.setVisibility(View.GONE);
            btn_status.setVisibility(View.VISIBLE);
            btn_engagements.setText(Constants.KEY_AVAILABE_PARTNERS);
        }

        //listners
        btn_sent.setOnClickListener(this);
        //btn_accept.setOnClickListener(this);
        btn_declined.setOnClickListener(this);
        btn_engagements.setOnClickListener(this);
        //btn_negotiations.setOnClickListener(this);
        btn_status.setOnClickListener(this);
        btn_status_for_partner.setOnClickListener(this);
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(NotificationsMenuActivity.this);
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

    @Override
    public void onClick(View v) {
        if (v == btn_sent) {
            Intent to_sentlist = new Intent(getApplicationContext(), SentNotificationActivity.class);
            startActivity(to_sentlist);
        }
       /* if (v == btn_accept) {
            Intent to_acceptlist = new Intent(getApplicationContext(), AcceptNotificationActivity.class);
            startActivity(to_acceptlist);
        }*/
        if (v == btn_declined) {
            Intent to_declinelist = new Intent(getApplicationContext(), DeclineNotificationActivity.class);
            startActivity(to_declinelist);
        }
        if (v == btn_engagements) {
            Intent to_engagelist = new Intent(getApplicationContext(), EngagementsNotificationActivity.class);
            startActivity(to_engagelist);
        }
       /* if (v == btn_negotiations) {
            Intent to_negotiations = new Intent(getApplicationContext(), NegotiationsNotificationActivity.class);
            startActivity(to_negotiations);
        }*/
        if (v == btn_status) {
            getuserDetails();
        }
        if (v == btn_status_for_partner) {
            Intent to_status = new Intent(getApplicationContext(), StatusForPartnerActivity.class);
            startActivity(to_status);
        }
    }

    private void getuserDetails() {
        Intent to_status = new Intent(getApplicationContext(), StatusNotificationActivity.class);
        startActivity(to_status);
    }
}
