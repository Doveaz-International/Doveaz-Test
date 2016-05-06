package com.doveazapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.doveazapp.Constants;
import com.doveazapp.Groups.CreateGroupListActivity;
import com.doveazapp.Notifications.MilestoneNotificationActivity;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import java.util.HashMap;

/**
 * Created by Karthik on 11/19/2015.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * This is for onscreen side menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static class CommonClass {

        public static boolean HandleMenu(Context context, int MenuEntry) {
            switch (MenuEntry) {
                case R.id.Home:
                    SessionManager session_type;
                    // Session Manager
                    session_type = new SessionManager(context);
                    HashMap<String, String> user = session_type.getUserDetails();
                    String type = user.get(SessionManager.KEY_USER_TYPE);
                    if (type.equals(Constants.KEY_TYPE_PARTNER)) {
                        Intent newIncome = new Intent(context, WelcomePartnerActivity.class);
                        context.startActivity(newIncome);
                    }
                    if (type.equals(Constants.KEY_TYPE_DELIVER)) {
                        Intent newIncome = new Intent(context, MenuActivity.class);
                        context.startActivity(newIncome);
                    }
                    break;

                case R.id.pending_milestone:
                    Intent to_getmilestone = new Intent(context, MilestoneNotificationActivity.class);
                    context.startActivity(to_getmilestone);
                    break;

                case R.id.notifications:
                    Intent to_notification_menu = new Intent(context, NotificationsMenuActivity.class);
                    context.startActivity(to_notification_menu);
                    break;

                case R.id.my_wallet:
                    Intent to_wallet = new Intent(context, MywalletActivity.class);
                    context.startActivity(to_wallet);
                    break;

                case R.id.groups:
                    Intent to_group = new Intent(context, CreateGroupListActivity.class);
                    context.startActivity(to_group);
                    break;

                case R.id.connect:
                    Intent to_connect = new Intent(context, SearchTransactionActivity.class);
                    context.startActivity(to_connect);
                    break;

               /* case R.id.change_password:
                    Toast.makeText(context, "You selected change password", Toast.LENGTH_SHORT).show();
                    break;*/

                case R.id.Logout:
                    // Session Manager Class
                    SessionManager session;
                    // Session class instance
                    session = new SessionManager(context);
                    session.logoutUser();
                    break;
            }
            return true;
        }


        public static boolean isNetworkAvailable(Activity a) {
            boolean hasConnectedWifi = false;
            boolean hasConnectedMobile = false;
            try {
                ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("wifi"))
                        if (ni.isConnected())
                            hasConnectedWifi = true;
                    if (ni.getTypeName().equalsIgnoreCase("mobile"))
                        if (ni.isConnected())
                            hasConnectedMobile = true;
                }
                return hasConnectedWifi || hasConnectedMobile;
            } catch (Exception ex) {
                Log.v("Internet check", ex.toString());
            }
            return false;
        }

    }
}
