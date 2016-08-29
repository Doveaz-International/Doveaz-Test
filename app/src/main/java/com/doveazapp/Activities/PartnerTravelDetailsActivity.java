package com.doveazapp.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import com.doveazapp.Dialogs.AlertDialogs;
import com.doveazapp.Dialogs.DatePickerFragment;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Karthik on 11/24/2015.
 */
public class PartnerTravelDetailsActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    RadioGroup radiochoose_region, radiochoose_service, radiochoose_way;

    Button btndate_travelon, btndate_return;

    Button button_ok;

    EditText editText_streetaddr, editText_postalcode, editText_minimumtip;

    TextInputLayout input_streetaddr, input_postalcode, input_minimumtip, input_city, input_state;

    String street_address, postal_code, minimum_tip, city, state, country;

    Spinner spinner_country, spin_state_bd, spin_city_bd;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    ProgressDialog progressDialog;

    JSONArray postjson = new JSONArray();

    JSONObject json = null;

    // Session Manager Class
    SessionManager session;

    SimpleAdapter stateAdapPROJ, cityAdapPROJ;

    ArrayList<HashMap<String, String>> stateArrayList = null;

    ArrayList<HashMap<String, String>> cityArrayList = null;

    HashMap<String, String> stateList;

    HashMap<String, String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_traveldetail_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Partner travel details");

        // Session class instance
        session = new SessionManager(getApplicationContext());

        //radio buttons
        radiochoose_region = (RadioGroup) findViewById(R.id.radioGroup);
        radiochoose_service = (RadioGroup) findViewById(R.id.radiochoose_service);
        radiochoose_way = (RadioGroup) findViewById(R.id.radioGroup1);


        //Edittext
        editText_streetaddr = (EditText) findViewById(R.id.edit_streetaddress_bd);
        editText_postalcode = (EditText) findViewById(R.id.edit_postalcode_bd);
        editText_minimumtip = (EditText) findViewById(R.id.edit_minimum_tip);

        //input layout
        input_streetaddr = (TextInputLayout) findViewById(R.id.input_streetaddress_bd);
        input_postalcode = (TextInputLayout) findViewById(R.id.input_postalcode_bd);
        input_minimumtip = (TextInputLayout) findViewById(R.id.input_minimum_tip);

        //button
        btndate_travelon = (Button) findViewById(R.id.btnpartner_travelon);
        btndate_return = (Button) findViewById(R.id.btnpartner_return);
        button_ok = (Button) findViewById(R.id.partnerbutton_ok);

        //Spinners
        spinner_country = (Spinner) findViewById(R.id.spin_country_partner);
        spin_state_bd = (Spinner) findViewById(R.id.spin_state_bd);
        spin_city_bd = (Spinner) findViewById(R.id.spin_city_bd);

        // Listeners
        btndate_travelon.setOnClickListener(this);
        btndate_return.setOnClickListener(this);
        button_ok.setOnClickListener(this);

        editText_streetaddr.addTextChangedListener(this);
        editText_postalcode.addTextChangedListener(this);
        editText_minimumtip.addTextChangedListener(this);

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Loadstates_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_state_bd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadCities_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        open_gpswhenlocal();
        loadSpinners();
        way_filter();
    }


    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(PartnerTravelDetailsActivity.this);
    }

    private void Loadstates_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT STATE--", response);
                Log.v("==state success==", response);
                // response will be like {"status":"false","value":"Username\/Password Incorrect"}
                try {

                    stateArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray states_array = value_obj.getJSONArray("states");

                    for (int i = 0; i < states_array.length(); i++) {
                        JSONObject json_data = states_array.getJSONObject(i);
                        Log.i("log_tag", " state_name" + json_data.getString("state_name"));

                        String state_name = states_array.getJSONObject(i).getString("state_name");

                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            Log.v("STATES", state_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            stateList = new HashMap<String, String>();
                            stateList.put("state_name", state_name);

                            stateArrayList.add(stateList);

                            stateAdapPROJ = new SimpleAdapter(PartnerTravelDetailsActivity.this, stateArrayList, R.layout.spinner_item,
                                    new String[]{"id", "state_name"}, new int[]{R.id.Id, R.id.Name});
                            spin_state_bd.setAdapter(stateAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        country = spinner_country.getSelectedItem().toString();

        ServiceCalls.callAPI_togetStates(this, Request.Method.POST, Constants.GET_STATES, country, listener);
    }

    private void LoadCities_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT CITY--", response);
                Log.v("==city success==", response);
                try {

                    cityArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray city_array = value_obj.getJSONArray("cities");

                    for (int i = 0; i < city_array.length(); i++) {
                        JSONObject json_data = city_array.getJSONObject(i);
                        Log.i("log_tag", " city_name" + json_data.getString("city_name"));

                        String city_name = city_array.getJSONObject(i).getString("city_name");

                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            Log.v("CITIES", city_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            cityList = new HashMap<String, String>();
                            cityList.put("city_name", city_name);

                            cityArrayList.add(cityList);

                            cityAdapPROJ = new SimpleAdapter(PartnerTravelDetailsActivity.this, cityArrayList, R.layout.spinner_item,
                                    new String[]{"id", "city_name"}, new int[]{R.id.Id, R.id.Name});
                            spin_city_bd.setAdapter(cityAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        country = spinner_country.getSelectedItem().toString();
        String state = spin_state_bd.getSelectedItem().toString();
        state = state.replace("state_name=", "");
        state = state.replaceAll("[\\[\\](){}]", "");

        Log.v("country, state", country + state);
        ServiceCalls.callAPI_togetCity(this, Request.Method.POST, Constants.GET_CITIES, country, state, listener);
    }

    private void way_filter() {
        radiochoose_way.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radio_partoneway:
                        hideReturn();
                        break;
                    case R.id.radio_partreturn:
                        showreturn();
                        break;
                    /*case R.id.radio_international:

                        break;*/
                }
            }
        });
    }

    private void showreturn() {
        btndate_return.setVisibility(View.VISIBLE);
    }

    private void hideReturn() {
        btndate_return.setVisibility(View.GONE);
    }


    private void loadSpinners() {
        //spinner adapter for country
        ArrayAdapter<CharSequence> adapter_country = ArrayAdapter.createFromResource(this,
                R.array.Countries, android.R.layout.simple_spinner_item);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter_country);
    }

    private void open_gpswhenlocal() {

        radiochoose_region.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    /*case R.id.radio_partnerlocal:
                        checkGPS_status();
                        break;*/
                    case R.id.radio_partnerdomestic:

                        break;
                    /*case R.id.radio_international:

                        break;*/
                }
            }
        });
    }

    private void checkGPS_status() {
        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            AlertDialogs.openGPS(PartnerTravelDetailsActivity.this);
        else
            Toast.makeText(getApplicationContext(), "GPS is enabled!", Toast.LENGTH_LONG).show();
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
    public void onClick(View view) {
        if (view == btndate_travelon) {
            showDatePicker();
        }
        if (view == btndate_return) {
            showreturndatepicker();
        }
        if (view == button_ok) {
            validate_gotofriendsmenu();
        }
    }

    private void validate_gotofriendsmenu() {
        // get values form edittexts
        street_address = editText_streetaddr.getText().toString();
        postal_code = editText_postalcode.getText().toString();
        minimum_tip = editText_minimumtip.getText().toString();
        country = spinner_country.getSelectedItem().toString();

        if (street_address.equals("") && postal_code.equals("") && minimum_tip.equals("")) {
            input_streetaddr.setError("street address is required");
            input_postalcode.setError("Postal code is required");
            input_minimumtip.setError("Minimum tip is required");

            if (country.equals("--select--")) {
                Toast.makeText(getApplicationContext(), "Please choose the country", Toast.LENGTH_LONG).show();
            }
        } else {
            /*Intent to_friends = new Intent(getApplicationContext(), FriendsMenuPartnerActivity.class);
            startActivity(to_friends);*/
            callServiceAPI();
            //callSmartyStreetForStreetValidation();
        }
    }

    private void callSmartyStreetForStreetValidation() {
        progressDialog = ProgressDialog.show(PartnerTravelDetailsActivity.this, "Please wait ...", "Validating Address...", true);
        progressDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.SMARTY_STREETS_FOR_VALIDATION + "&street=" + editText_streetaddr.getText().toString() + "&city=" + "" + "&state=" + "" + "&zipcode=" + editText_postalcode.getText().toString() + "&candidates=1";
        Log.v("--SMARTY INPUTS--", url);
        url = url.replaceAll(" ", "%20");
        URL sourceUrl = null;
        try {
            sourceUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, sourceUrl.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("--SMARTYSTREET LOG--", response);
                        try {
                            JSONArray obj = new JSONArray(response);
                            if (obj.toString().equals("[]")) {
                                progressDialog.dismiss();
                                Toast.makeText(PartnerTravelDetailsActivity.this, "Invalid Address contact support", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(PartnerTravelDetailsActivity.this, "Valid", Toast.LENGTH_LONG).show();
                                //goto_DeliveryAddress();
                                callServiceAPI();
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        Toast.makeText(PartnerTravelDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.v("==Login Failed==", error.toString());
                    }
                }) {
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void callServiceAPI() {
        progressDialog = ProgressDialog.show(PartnerTravelDetailsActivity.this, "Please wait ...", "Adding Service...", true);
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SAVE_BUYANDDELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("--OUTPUT PARTNER--", response.toString());
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            // {"status":"true","value":{"id":121,"message":"Service Added"}}
                            if (status.equals("false")) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                send_contacts_toserver();
                                JSONObject value_obj = obj.getJSONObject("value");
                                String message = value_obj.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
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
                        Toast.makeText(PartnerTravelDetailsActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String travel_country = spinner_country.getSelectedItem().toString().trim();
                String travel_address = editText_streetaddr.getText().toString();
                city = spin_city_bd.getSelectedItem().toString();
                city = city.replace("city_name=", "");
                city = city.replaceAll("[\\[\\](){}]", "");
                state = spin_state_bd.getSelectedItem().toString();
                state = state.replace("state_name=", "");
                state = state.replaceAll("[\\[\\](){}]", "");
                String travel_postal = editText_postalcode.getText().toString();
                String tip = editText_minimumtip.getText().toString();

                int res_id = radiochoose_region.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(res_id);
                String select_region = radioButton.getText().toString().trim();

                int res_id1 = radiochoose_service.getCheckedRadioButtonId();
                RadioButton radioButton1 = (RadioButton) findViewById(res_id1);
                String select_service = radioButton1.getText().toString().trim();

                int res_id2 = radiochoose_way.getCheckedRadioButtonId();
                RadioButton radioButton2 = (RadioButton) findViewById(res_id2);
                String select_travel_type = radioButton2.getText().toString().trim();

                String trip = null;

                if (select_travel_type.equals("One way")) {
                    trip = "0";
                } else if (select_travel_type.equals("Return")) {
                    trip = "1";
                }

                String offer = null;

                if (select_service.equals("Buy & Deliver")) {
                    offer = "A";
                } else if (select_service.equals("Collection")) {
                    offer = "B";
                } else if (select_service.equals("Send")) {
                    offer = "C";
                } else if (select_service.equals("All")) {
                    offer = "ALL";
                }

                String travel_on = btndate_travelon.getText().toString();
                String return_on = btndate_return.getText().toString();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_SERVICETYPE, "A");
                params.put(Constants.KEY_PARTNER_REGION, select_region);
                params.put(Constants.KEY_TRAVEL_TYPE, trip);
                params.put(Constants.KEY_TRAVEL_DATE, travel_on);
                params.put(Constants.KEY_RETURN_DATE, return_on);
                params.put(Constants.KEY_DESTINATION_COUNTRY, travel_country);
                params.put(Constants.KEY_DESTINATION_ADDRESS, travel_address);
                params.put(Constants.KEY_DESTINATION_CITY, city);
                params.put(Constants.KEY_DESTINATION_STATE, state);
                params.put(Constants.KEY_DESTINATION_POSTAL, travel_postal);
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

            progressDialog = ProgressDialog.show(PartnerTravelDetailsActivity.this, "Please wait ...", "We are matching your request...", true);
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
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            to_friendsMenu();
                        } else if (status.equals("true")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            to_friendsMenu();
                        }
                    } catch (JSONException exception) {
                        Log.e("--JSON EXCEPTION--", exception.toString());
                    }
                    // Toast.makeText(PartnerTravelDetailsActivity.this, response, Toast.LENGTH_LONG).show();
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

            int month = monthOfYear + 1;

            if (month < 10 && dayOfMonth < 10) {
                btndate_travelon.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
            } else if (month >= 10 && dayOfMonth < 10) {
                btndate_travelon.setText(year + "-" + month + "-" + "0" + dayOfMonth);
            } else if (month < 10 && dayOfMonth > 10) {
                btndate_travelon.setText(year + "-" + "0" + month + "-" + dayOfMonth);
            } else {
                btndate_travelon.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }
    };


    private void showreturndatepicker() {
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
        date.setCallBack(ondate1);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);

            int month = monthOfYear + 1;

            if (month < 10 && dayOfMonth < 10) {
                btndate_return.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
            } else if (month >= 10 && dayOfMonth < 10) {
                btndate_return.setText(year + "-" + month + "-" + "0" + dayOfMonth);
            } else if (month < 10 && dayOfMonth > 10) {
                btndate_return.setText(year + "-" + "0" + month + "-" + dayOfMonth);
            } else {
                btndate_return.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == editText_streetaddr.getEditableText()) {
            input_streetaddr.setError(null);
        } else if (editable == editText_postalcode.getEditableText()) {
            input_postalcode.setError(null);
        } else if (editable == editText_minimumtip.getEditableText()) {
            input_minimumtip.setError(null);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Partner travel details");
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
}