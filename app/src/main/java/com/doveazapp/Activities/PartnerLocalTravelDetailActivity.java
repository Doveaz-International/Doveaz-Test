package com.doveazapp.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.DatePickerFragment;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Karthik on 2016/02/01.
 */
public class PartnerLocalTravelDetailActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    RadioGroup radiochoose_service;

    Button btndate_travelon, button_ok;

    String street_address, postal_code, minimum_tip, city, state, country;

    TextInputLayout input_minimumtip;

    TextView text_address, text_city, text_state, text_country, text_postal;

    EditText editText_minimumtip;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    ProgressDialog progressDialog;

    JSONArray postjson = new JSONArray();

    JSONObject json = null;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_local_details);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Partner travel details");

        // Session class instance
        session = new SessionManager(getApplicationContext());

        //radio buttons
        radiochoose_service = (RadioGroup) findViewById(R.id.radiochoose_service);

        editText_minimumtip = (EditText) findViewById(R.id.edit_minimum_tip);
        input_minimumtip = (TextInputLayout) findViewById(R.id.input_minimum_tip);
        editText_minimumtip.addTextChangedListener(this);

        //Textviews
        text_address = (TextView) findViewById(R.id.text_address);
        text_city = (TextView) findViewById(R.id.text_city);
        text_state = (TextView) findViewById(R.id.text_state);
        text_country = (TextView) findViewById(R.id.text_country);
        text_postal = (TextView) findViewById(R.id.text_postal);

        //button
        btndate_travelon = (Button) findViewById(R.id.btnpartner_travelon);
        button_ok = (Button) findViewById(R.id.partnerbutton_ok);

        // Listeners
        btndate_travelon.setOnClickListener(this);
        btndate_travelon.setOnClickListener(this);
        button_ok.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        street_address = bundle.getString(Constants.KEY_ADDRESS);
        city = bundle.getString(Constants.KEY_CITY);
        state = bundle.getString(Constants.KEY_STATE);
        country = bundle.getString(Constants.KEY_COUNTRY);
        postal_code = bundle.getString(Constants.KEY_POSTALCODE);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        fix_values();
    }

    private void fix_values() {
        text_address.setText(street_address);
        text_city.setText(city);
        text_state.setText(state);
        text_country.setText(country);
        text_postal.setText(postal_code);
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(PartnerLocalTravelDetailActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view == btndate_travelon) {
            showDatePicker();
        }
        if (view == button_ok) {
            validate_gotofriendsMenu();
        }

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String date;
            Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            /*if (minAdultAge.before(userAge)) {
                Toast.makeText(getApplicationContext(), "18+ age only allowed", Toast.LENGTH_SHORT).show();
            }*/
            date = String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                    + "-" + String.valueOf(dayOfMonth);

            if (monthOfYear < 10 && dayOfMonth < 10) {
                btndate_travelon.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth);
            } else if (monthOfYear > 10 && dayOfMonth < 10) {
                btndate_travelon.setText(year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth);
            } else if (monthOfYear < 10 && dayOfMonth > 10) {
                btndate_travelon.setText(year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth);
            } else {
                btndate_travelon.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
    };

    private void validate_gotofriendsMenu() {
        minimum_tip = editText_minimumtip.getText().toString();
        if (minimum_tip.equals("")) {
            input_minimumtip.setError("Minimum tip is required");
        } else {
            callServiceAPI();
        }

    }

    private void callServiceAPI() {
        progressDialog = ProgressDialog.show(PartnerLocalTravelDetailActivity.this, "Please wait ...", "Adding Service...", true);
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SAVE_BUYANDDELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("--OUTPUT PARTNER--", response.toString());
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString(Constants.KEY_STATUS);
                            final String value = obj.getString(Constants.KEY_VALUE);

                            if (status.equals(Constants.KEY_FALSE)) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                send_contacts_toserver();
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                        //Toast.makeText(PartnerLocalTravelDetailActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        progressDialog.dismiss();
                        //Showing toast
                        if (volleyError.networkResponse == null) {
                            if (volleyError.getClass().equals(TimeoutError.class)) {
                                progressDialog.dismiss();
                                // Show timeout error message
                                Toast.makeText(getApplicationContext(),
                                        "Oops. Timeout error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        Toast.makeText(PartnerLocalTravelDetailActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String tip = editText_minimumtip.getText().toString();

                int res_id1 = radiochoose_service.getCheckedRadioButtonId();
                RadioButton radioButton1 = (RadioButton) findViewById(res_id1);
                String select_service = radioButton1.getText().toString().trim();


               /* String trip = null;

                if (select_travel_type.equals("One way")) {
                    trip = "0";
                } else if (select_travel_type.equals("Return")) {
                    trip = "1";
                }*/

                String offer = null;

                if (select_service.equals("Buy & Deliver")) {
                    offer = "A";
                } else if (select_service.equals("Collection")) {
                    offer = "B";
                } else if (select_service.equals("send")) {
                    offer = "C";
                } else if (select_service.equals("All")) {
                    offer = "All";
                }

                String travel_on = btndate_travelon.getText().toString();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_SERVICETYPE, "A");
                params.put(Constants.KEY_PARTNER_REGION, "Local");
                params.put(Constants.KEY_TRAVEL_TYPE, "one way");
                params.put(Constants.KEY_TRAVEL_DATE, travel_on);
                params.put(Constants.KEY_DESTINATION_COUNTRY, country);
                params.put(Constants.KEY_DESTINATION_ADDRESS, street_address);
                params.put(Constants.KEY_DESTINATION_CITY, city);
                params.put(Constants.KEY_DESTINATION_STATE, state);
                params.put(Constants.KEY_DESTINATION_POSTAL, postal_code);
                params.put(Constants.KEY_TIP_FEE, tip);
                params.put(Constants.KEY_OFFER, offer);

                Log.v("--PARTNER PARAMS--", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                HashMap<String, String> user = session.getUserDetails();
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("--INPUT HEADERS--", headers.toString());
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 500000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 500000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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

            progressDialog = ProgressDialog.show(PartnerLocalTravelDetailActivity.this, "Please wait ...", "We are matching your request...", true);
            progressDialog.setCancelable(false);
            OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                @Override
                public void onRequestCompleted(String response) {
                    progressDialog.dismiss();
                    Log.v("--OUTPUT SAVE CONTACT--", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        final String status = obj.getString(Constants.KEY_STATUS);
                        final String value = obj.getString(Constants.KEY_VALUE);
                /*JSONObject fee_object = obj.getJSONObject("value");
                String fee = fee_object.getString("credit");*/

                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            to_friendsMenu();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            to_friendsMenu();
                        }
                    } catch (JSONException exception) {
                        Log.e("--JSON EXCEPTION--", exception.toString());
                    }
                    //Toast.makeText(PartnerLocalTravelDetailActivity.this, response, Toast.LENGTH_LONG).show();
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

    private void to_friendsMenu() {
        Intent to_friendsMenu = new Intent(getApplicationContext(), FriendsMenuPartnerActivity.class);
        startActivity(to_friendsMenu);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
