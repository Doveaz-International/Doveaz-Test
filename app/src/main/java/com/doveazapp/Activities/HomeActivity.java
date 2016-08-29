package com.doveazapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.R;
import com.google.android.gms.analytics.GoogleAnalytics;

/**
 * HomeActivity.java
 * Created by Karthik on 11/25/2015.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button to_delivery, to_partner;

    //public static final String SHOW_DIALOG = "pref_terms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        getSupportActionBar().hide();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("HomeActivity");

        // menuvisibilityinAlldevices();

        // Buttons init
        to_delivery = (Button) findViewById(R.id.main_deliverybtn);
        to_delivery.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/monotype_corsiva.ttf"));
        to_partner = (Button) findViewById(R.id.main_partnerbtn);
        to_partner.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/monotype_corsiva.ttf"));
        //to_takeatour = (Button) findViewById(R.id.main_tourbtn);

        // Onclick listeners for buttons
        to_delivery.setOnClickListener(this);
        to_partner.setOnClickListener(this);

        //to_takeatour.setOnClickListener(this);

        /*SharedPreferences settings = getSharedPreferences(SHOW_DIALOG, 0);
        boolean dialogShown = settings.getBoolean("dialog_shown", false);

        if (!dialogShown) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.terms_conditions, null);

            TextView textview = (TextView) view.findViewById(R.id.textmsg);
            textview.setText(R.string.terms_condns);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("TERMS AND CONDITIONS");
            alertDialog.setView(view);
            alertDialog.setPositiveButton("ACCEPT", null);
            AlertDialog alert = alertDialog.create();
            alert.show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialog_shown", true);
            editor.commit();

        }*/
    }

    /*private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(HomeActivity.this);
    }*/

    @Override
    public void onClick(View view) {
        if (view == to_delivery) {
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("DeliveryButton", "From Home", "To Delivery");

            /*Intent todeliver = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(todeliver);*/
            /*Intent todeliver = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(todeliver);*/

            /*TO LOGIN PAGE - LOGIN OR REGISTER*/
            Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(to_login);
        }
        if (view == to_partner) {
            // track the event for google analytics
            MyApplication.getInstance().trackEvent("PartnerButton", "From Home", "To Fill Partner Details");

            /*Intent to_partner_traveldet = new Intent(getApplicationContext(), PartnerTravelDetailsActivity.class);
            startActivity(to_partner_traveldet);*/
            /*TO LOGIN PAGE - LOGIN OR REGISTER*/
            Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(to_login);
        }
       /* if (view == to_takeatour) {

        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("HomeActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
