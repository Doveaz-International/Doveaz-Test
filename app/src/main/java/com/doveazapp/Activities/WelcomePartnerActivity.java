package com.doveazapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.AlertDialogs;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Karthik on 2016/01/18.
 */
public class WelcomePartnerActivity extends AppCompatActivity implements View.OnClickListener {

    //session manager
    SessionManager session;

    LinearLayout btn_update_service, btn_view_list, toggleButton1;

    Geocoder geocoder;

    String bestProvider;

    List<Address> user = null;

    double lat;

    double lng;

    String partner_loggedin;

    //Progress bar
    ProgressDialog progressDialog;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_partner);

        menuvisibilityinAlldevices();

        btn_update_service = (LinearLayout) findViewById(R.id.btn_update);
        btn_view_list = (LinearLayout) findViewById(R.id.btn_view);
        toggleButton1 = (LinearLayout) findViewById(R.id.toggleButton1);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Welcome Partner screen");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // GET INTENT FROM PREVIOUS CLASS
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            partner_loggedin = bundle.getString(Constants.LOGIN_PARTNER);
            if (partner_loggedin != null) {
                Log.v("Login_partner", partner_loggedin);
                enterPassword();
            }
        }
        btn_update_service.setOnClickListener(this);
        btn_view_list.setOnClickListener(this);
        toggleButton1.setOnClickListener(this);
    }

    private void enterPassword() {
        /* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(WelcomePartnerActivity.this);
        alert.setTitle("Password");
        alert.setIcon(R.drawable.lock);
        alert.setMessage("Please enter your password, for security reasons");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(WelcomePartnerActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String entered_value = input.getEditableText().toString();

                progressDialog = ProgressDialog.show(WelcomePartnerActivity.this, "Please wait ...", "Requesting...", true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("OUTPUT AUTHENTICATE", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");
                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                Toast.makeText(WelcomePartnerActivity.this, value, Toast.LENGTH_SHORT).show();
                                if (value.equals("You are not logged in")) {
                                    to_loginActivity();
                                } else {
                                    enterPassword();
                                }
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                Toast.makeText(WelcomePartnerActivity.this, value, Toast.LENGTH_SHORT).show();
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
                ServiceCalls.CallAPI_to_sessionLogin(WelcomePartnerActivity.this, Request.Method.POST, Constants.AUTHENTICATE_USER, listener, entered_value, api_token);
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void to_loginActivity() {
        Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(to_login);
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(WelcomePartnerActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_update_service) {
            Intent to_menuPartner = new Intent(getApplicationContext(), PartnerTravelDetailsActivity.class);
            startActivity(to_menuPartner);
        }
        if (v == btn_view_list) {
            Intent to_menuPartner = new Intent(getApplicationContext(), PartnerTravelDetailsActivity.class);
            startActivity(to_menuPartner);
        }
        if (v == toggleButton1) {
            checkGPS_status();
        }
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
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Welcome Partner screen");
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
        askforLOGOUT();
    }

    private void askforLOGOUT() {
        AlertDialogs.askforExit(WelcomePartnerActivity.this);
    }

    private void checkGPS_status() {
        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialogs.openGPS(WelcomePartnerActivity.this);
        } else {
            Toast.makeText(getApplicationContext(), "GPS is enabled!", Toast.LENGTH_LONG).show();
            locationenable();
        }
    }

    @SuppressLint("LongLogTag")
    private void locationenable() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            bestProvider = lm.getBestProvider(criteria, false);
            Location location = lm.getLastKnownLocation(bestProvider);
            if (location == null) {
                Toast.makeText(getApplicationContext(), "Location Not found! Enter your address manually", Toast.LENGTH_LONG).show();
                Intent to_partnerlocaltravelDetail = new Intent(getApplicationContext(), PartnerManualTravelDetails.class);
                startActivity(to_partnerlocaltravelDetail);
            } else {
                geocoder = new Geocoder(WelcomePartnerActivity.this);
                try {
                    user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    lat = (double) user.get(0).getLatitude();
                    lng = (double) user.get(0).getLongitude();
                    System.out.println(" DDD lat: " + lat + ",  longitude: " + lng);
                    /*Intent to_update_service = new Intent(getApplicationContext(), PartnerTravelDetailsActivity.class);
                    startActivity(to_update_service);*/

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());

                    addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.v("Address", addresses.toString());
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    // String knownName = addresses.get(0).getFeatureName();

                    /*Log.v("address", address.toString());
                    Log.v("city", city.toString());
                    Log.v("state", state.toString());
                    Log.v("country", country.toString());
                    Log.v("postalCode", postalCode.toString());*/

                    if (address == null || city == null || state == null || country == null || postalCode == null) {
                        Toast.makeText(getApplicationContext(), "Location Not found! Enter your address manually", Toast.LENGTH_LONG).show();
                        Intent to_partnerlocaltravelDetail = new Intent(getApplicationContext(), PartnerManualTravelDetails.class);
                        startActivity(to_partnerlocaltravelDetail);
                    } else {
                        Intent to_partnerlocaltravelDetail = new Intent(getApplicationContext(), PartnerLocalTravelDetailActivity.class);
                        to_partnerlocaltravelDetail.putExtra(Constants.KEY_ADDRESS, address.toString());
                        to_partnerlocaltravelDetail.putExtra(Constants.KEY_CITY, city.toString());
                        to_partnerlocaltravelDetail.putExtra(Constants.KEY_STATE, state.toString());
                        to_partnerlocaltravelDetail.putExtra(Constants.KEY_COUNTRY, country.toString());
                        to_partnerlocaltravelDetail.putExtra(Constants.KEY_POSTALCODE, postalCode.toString());
                        startActivity(to_partnerlocaltravelDetail);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationenable();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot allow to take your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
