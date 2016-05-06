package com.doveazapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Karthik on 2015/12/23.
 */
public class CalculateCreditActivity extends AppCompatActivity implements View.OnClickListener {

    String credits_fee, service_a_id;

    //progress dialog
    ProgressDialog progressDialog;

    TextView text_fee;

    Button btn_confirm;

    JSONArray postjson = new JSONArray();

    JSONObject json = null;

    // Session Manager Class
    SessionManager session;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_credits);

        menuvisibilityinAlldevices();

        text_fee = (TextView) findViewById(R.id.text_cal_fee);
        btn_confirm = (Button) findViewById(R.id.button_confirm_fee);

        btn_confirm.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        credits_fee = bundle.getString(Constants.KEY_TIP_FEE);
        service_a_id = bundle.getString(Constants.KEY_SERVICE_A_ID);

        text_fee.setText(credits_fee);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // Toast.makeText(getApplicationContext(), api_token + email, Toast.LENGTH_LONG).show();

        calculatecredits();
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CalculateCreditActivity.this);
    }

    private void calculatecredits() {

        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT CALCULATION--", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject fee_object = obj.getJSONObject("value");
                    String fee = fee_object.getString("credit");

                    if (status.equals("false")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        //Toast.makeText(getApplicationContext(), fee, Toast.LENGTH_SHORT).show();
                        if (fee != null) {
                            text_fee.setText(fee);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
                // Toast.makeText(CalculateCreditActivity.this, response, Toast.LENGTH_LONG).show();
            }
        };

        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.CallAPI_tocalculate(this, Request.Method.POST, Constants.CALCULATE_CREDITS, listener, credits_fee, api_token);
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
        if (v == btn_confirm) {
            send_contacts_toserver();
        }
    }

    private void send_contacts_toserver() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                HashMap<String, Object> NamePhoneType = new HashMap<String, Object>();
                NamePhoneType.put("name", name);
                NamePhoneType.put("mobileno", phoneNumber);
                Log.d("name+---+number", name + "----" + phoneNumber);
                try {
                    String findMobnumb = phoneNumber;

                    // Log.v("Number sending", phoneNumber);
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    try {
                        HashMap<String, String> user = session.getUserDetails();
                        // cc_iso
                        String cc_iso = user.get(SessionManager.KEY_COUNTRY_CODE_ISO);
                        Phonenumber.PhoneNumber India = phoneUtil.parse(findMobnumb, cc_iso);

                        Log.v("NUMBER", India.toString());
                        Log.v("Country Code:", String.valueOf(India.getCountryCode()));
                        Log.v("National Number:", String.valueOf(India.getNationalNumber()));

                        boolean isValid = phoneUtil.isValidNumber(India);

                        Log.v("Valid or not", String.valueOf(isValid));

                        // Produces "+41 44 668 18 00"
                        System.out.println(phoneUtil.format(India, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                        // Produces "044 668 18 00"
                        System.out.println(phoneUtil.format(India, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                        // Produces "+41446681800"
                        System.out.println(phoneUtil.format(India, PhoneNumberUtil.PhoneNumberFormat.E164));

                        json = new JSONObject().put("name", name.trim());
                        json.put("contact_no", String.valueOf(India.getNationalNumber()));
                        json.put("country_code", "0" + String.valueOf(India.getCountryCode()));


                    } catch (NumberParseException e) {
                        System.err.println("NumberParseException was thrown: " + e.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postjson.put(json);
                postjson.put(json);
            }

            while (phones.moveToNext()) ;
            Log.d("json data new query", postjson.toString().trim());

            progressDialog = ProgressDialog.show(CalculateCreditActivity.this, "Please wait ...", "We are matching your request...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    progressDialog.dismiss();
                    Log.v("--OUTPUT SAVE CONTACT--", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString("status");
                        final String value = obj.getString("value");
                /*JSONObject fee_object = obj.getJSONObject("value");
                String fee = fee_object.getString("credit");*/

                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            to_friendsMenu();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            to_friendsMenu();
                        }
                    } catch (JSONException exception) {
                        Log.e("--JSON EXCEPTION--", exception.toString());
                    }
                }
            };

            HashMap<String, String> user = session.getUserDetails();
            // token
            String api_token = user.get(SessionManager.KEY_APITOKEN);
            String Contacts = postjson.toString();
            ServiceCalls.CallAPI_tosaveContact(this, Request.Method.POST, Constants.SAVE_CONTACTS, listener, Contacts, api_token);
            phones.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                send_contacts_toserver();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void to_friendsMenu() {
        progressDialog.dismiss();
        Intent to_friendsMenu = new Intent(getApplicationContext(), FriendsMenuBuyDeliverActivity.class);
        to_friendsMenu.putExtra(Constants.KEY_SERVICE_A_ID, service_a_id);
        startActivity(to_friendsMenu);
    }
}
