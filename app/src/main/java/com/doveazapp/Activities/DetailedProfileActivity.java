package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.ProfileService;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.HashMap;

/**
 * Created by Karthik on 2016/01/09.
 */
public class DetailedProfileActivity extends AppCompatActivity {

    private ProgressBar progBar;

    NetworkImageView img_profile;

    //session manager
    SessionManager session;

    //Serialized object
    ProfileService profile;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    private Handler mHandler = new Handler();

    TextView txt_check_name, prog_risk, txt_dob, txt_gender, txt_edu, txt_profession, txt_mobile, txt_nationality,
            txt_street, txt_city, txt_postal, txt_country;

    //progress dialog
    ProgressDialog progressDialog;

    String risk_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_profile);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Total Profile View");

        // text views
        txt_check_name = (TextView) findViewById(R.id.txt_check_name);
        prog_risk = (TextView) findViewById(R.id.prog_risk);
        txt_dob = (TextView) findViewById(R.id.txt_dob);
        txt_gender = (TextView) findViewById(R.id.txt_gender);
        txt_edu = (TextView) findViewById(R.id.txt_edu);
        txt_profession = (TextView) findViewById(R.id.txt_profession);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_nationality = (TextView) findViewById(R.id.txt_nationality);
        txt_street = (TextView) findViewById(R.id.txt_street);
        txt_city = (TextView) findViewById(R.id.txt_city);
        txt_postal = (TextView) findViewById(R.id.txt_postal);
        txt_country = (TextView) findViewById(R.id.txt_country);

        //progress for risk
        progBar = (ProgressBar) findViewById(R.id.progress_checkrisk);

        //Network Img
        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        // profile image
        img_profile = (NetworkImageView) findViewById(R.id.img_check_frnd);

        // 1. get passed intent
        Intent intent = getIntent();
        // 2. get ProfileService object from intent
        profile = (ProfileService) intent.getSerializableExtra("ProfileService");
        risk_score = profile.getRisk_score();
        Log.v("risk_score", risk_score);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        setProfileValues();
        loadrisk();
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(DetailedProfileActivity.this);
    }

    private void loadrisk() {

        new Thread(new Runnable() {
            public void run() {
                final int presentage = 0;
                // while (mProgressStatus < 63) {
                // mProgressStatus += 1;
                // Update the progress bar
                mHandler.post(new Runnable() {
                    public void run() {
                        progBar.setProgress((int) Double.parseDouble(risk_score));
                        prog_risk.setText("" + risk_score + "%");
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //   }
            }
        }).start();

    }

    private void setProfileValues() {
        progressDialog = ProgressDialog.show(DetailedProfileActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        txt_check_name.setText(profile.getFullname());
        txt_dob.setText(profile.getDob());
        txt_gender.setText(profile.getGender());
        txt_edu.setText(profile.getEducation());
        txt_profession.setText(profile.getProfession());
        txt_mobile.setText(profile.getPhone());
        txt_nationality.setText(profile.getNationality());
        txt_street.setText(profile.getStreetaddress());
        txt_city.setText(profile.getCity());
        txt_postal.setText(profile.getPostalcode());
        txt_country.setText(profile.getCountry());
        img_profile.setImageUrl(profile.getProfile_pic_url(), imageLoader);
        progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Check Partner Details Activity");
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