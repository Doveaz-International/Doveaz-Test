package com.doveazapp.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.doveazapp.Constants;
import com.doveazapp.Fragments.AgentDeliveriesListFragment;
import com.doveazapp.Fragments.AgentOrderHistoryFragment;
import com.doveazapp.Fragments.AgentPickupsListFragment;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.util.HashMap;

/**
 * AgentMenuUpdateActivity.java
 * Created by Karthik on 7/18/2016.
 */
public class AgentMenuUpdateActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;

    String agent_updated_area;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_menu_update);

        menuvisibilityinAlldevices();

        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            agent_updated_area = bundle.getString(Constants.KEY_AREA);

        Bundle bundle_1 = new Bundle();
        bundle_1.putString(Constants.KEY_AREA, agent_updated_area);
        AgentPickupsListFragment menuListFragment = new AgentPickupsListFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        menuListFragment.setArguments(bundle_1);
        ft.replace(R.id.three_buttons_activity, menuListFragment);
        ft.commit();

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.three_buttons_menu, new

                OnMenuTabSelectedListener() {
                    @Override
                    public void onMenuItemSelected(int itemId) {
                        switch (itemId) {
                            case R.id.pick_ups:
                                Snackbar.make(coordinatorLayout, Constants.PLEASE_WAIT, Snackbar.LENGTH_LONG).show();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.KEY_AREA, agent_updated_area);
                                AgentPickupsListFragment menuListFragment = new AgentPickupsListFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                menuListFragment.setArguments(bundle);
                                ft.replace(R.id.three_buttons_activity, menuListFragment);
                                ft.commit();
                                break;

                            case R.id.deliveries:
                                Snackbar.make(coordinatorLayout, Constants.PLEASE_WAIT, Snackbar.LENGTH_LONG).show();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString(Constants.KEY_AREA, agent_updated_area);
                                AgentDeliveriesListFragment DmenuListFragment = new AgentDeliveriesListFragment();
                                FragmentManager fm1 = getFragmentManager();
                                FragmentTransaction ft1 = fm1.beginTransaction();
                                DmenuListFragment.setArguments(bundle1);
                                ft1.replace(R.id.three_buttons_activity, DmenuListFragment);
                                ft1.commit();
                                break;

                            case R.id.reports:
                                Snackbar.make(coordinatorLayout, Constants.PLEASE_WAIT, Snackbar.LENGTH_LONG).show();
                                Bundle bundle2 = new Bundle();
                                bundle2.putString(Constants.KEY_AREA, agent_updated_area);
                                AgentOrderHistoryFragment OmenuListFragment = new AgentOrderHistoryFragment();
                                FragmentManager fm2 = getFragmentManager();
                                FragmentTransaction ft2 = fm2.beginTransaction();
                                OmenuListFragment.setArguments(bundle2);
                                ft2.replace(R.id.three_buttons_activity, OmenuListFragment);
                                ft2.addToBackStack(null);
                                ft2.commit();
                                break;
                        }
                    }
                }

        );

        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#009899");

        // Use the dark theme. Ignored on mobile when there are more than three tabs.
        //bottomBar.useDarkTheme(true);

        // Use custom text appearance in tab titles.
        //bottomBar.setTextAppearance(R.style.MyTextAppearance);

        // Use custom typeface that's located at the "/src/main/assets" directory. If using with
        // custom text appearance, set the text appearance first.
        /*bottomBar.setTypeFace("fonts/viga.otf");*/
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(AgentMenuUpdateActivity.this);
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

   /* @Override
    public void onBackPressed() {

       *//* int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
            askforLOGOUT();
        } else {
            getFragmentManager().popBackStack();
        }*//*

        *//*AgentPickupsListFragment fragment = (AgentPickupsListFragment) this.getFragmentManager().findFragmentById(R.id.pickups);
        if (fragment != null && fragment.isVisible()) {
            //VISIBLE! =)
            askforLOGOUT();
        }*//*


    }

    private void askforLOGOUT() {
        AlertDialogs.askforExit(AgentMenuUpdateActivity.this);
    }*/

}

