package com.doveazapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import java.util.HashMap;

/**
 * Created by Karthik on 6/7/2016.
 */
public class UpdateLocationPartnerActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_update;
    //session manager
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_loc_partner_activity);

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("show area list");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_update) {
            goto_arealist();
        }
    }

    private void goto_arealist() {
        Intent to_show_area = new Intent(getApplicationContext(), ShowAreaListActivity.class);
        startActivity(to_show_area);
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(UpdateLocationPartnerActivity.this);
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
}
