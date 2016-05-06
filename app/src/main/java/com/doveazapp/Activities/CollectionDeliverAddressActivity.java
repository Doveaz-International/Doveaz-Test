package com.doveazapp.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RadioGroup;
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
import com.doveazapp.Dialogs.DatePickerFragment;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Karthik on 2015/12/23.
 */
public class CollectionDeliverAddressActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    private final static String TAG = CollectionDeliverAddressActivity.class.getName();

    Button btn_pickdate;

    TextInputLayout input_streetaddress_d, input_city_d, input_state_d, input_d_postalcode, input_valueofitem, input_tipserv, input_quantity;

    EditText editText_valueofitem, editText_tipserv, edit_streetaddr_d, edit_city_d, edit_state_d, edit_postal_d, edit_quantity;

    String value_of_item, tip_for_serv, deli_st_address, deli_postalcode, delivery_city, delivery_state, deliver_country;

    Spinner spin_d_country;

    //RadioGroup forvalue_filter;

    //progress dialog
    ProgressDialog progressDialog;

    Button button_ok;

    private String date;

    RadioGroup radiovalue;

    //from buyanddelivery
    String category_id, category_name, item_short, image, pick_region, pick_country,
            pick_address, pick_city, pick_state, pick_postal, package_type, item_price;

    //String pickup_lat, pickup_long;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_deliver_address);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Collection Delivery");

        menuvisibilityinAlldevices();

        //button init
        btn_pickdate = (Button) findViewById(R.id.datepick);

        edit_streetaddr_d = (EditText) findViewById(R.id.edit_streetaddress_delivery);
        edit_city_d = (EditText) findViewById(R.id.edit_city_delivery);
        edit_state_d = (EditText) findViewById(R.id.edit_state_delivery);
        edit_postal_d = (EditText) findViewById(R.id.edit_postalcode_d);
        editText_valueofitem = (EditText) findViewById(R.id.edit_valueofitem);
        editText_tipserv = (EditText) findViewById(R.id.edit_tipservice);
        edit_quantity = (EditText) findViewById(R.id.edit_quantity);

        //InputText Layout
        input_d_postalcode = (TextInputLayout) findViewById(R.id.input_postalcode_d);
        input_valueofitem = (TextInputLayout) findViewById(R.id.input_valueofitem);
        input_tipserv = (TextInputLayout) findViewById(R.id.input_tipservice);
        input_streetaddress_d = (TextInputLayout) findViewById(R.id.input_streetaddress_delivery);
        input_city_d = (TextInputLayout) findViewById(R.id.input_city_delivery);
        input_state_d = (TextInputLayout) findViewById(R.id.input_state_delivery);
        input_quantity = (TextInputLayout) findViewById(R.id.input_quantity);

        //spinner
        spin_d_country = (Spinner) findViewById(R.id.spin_country_d);

        //radiobutton init
        //forvalue_filter = (RadioGroup) findViewById(R.id.radiovalue);

        // set Button OnClicks
        btn_pickdate.setOnClickListener(this);
        edit_streetaddr_d.addTextChangedListener(this);
        edit_postal_d.addTextChangedListener(this);
        editText_tipserv.addTextChangedListener(this);
        editText_valueofitem.addTextChangedListener(this);
        edit_quantity.addTextChangedListener(this);

        //Radio value
        //radiovalue = (RadioGroup) findViewById(R.id.radiovalue);

        //button
        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        category_name = bundle.getString(Constants.KEY_CATEGORY_NAME);
        category_id = bundle.getString(Constants.KEY_CATEGORY);
        item_short = bundle.getString(Constants.KEY_ITEM_SHORT_DESC);
        image = bundle.getString(Constants.KEY_IMAGE);
        pick_region = bundle.getString(Constants.KEY_REGION);
        pick_country = bundle.getString(Constants.KEY_PICK_COUNTRY);
        pick_address = bundle.getString(Constants.KEY_PICK_ADDRESS);
        pick_city = bundle.getString(Constants.KEY_PICK_CITY);
        pick_state = bundle.getString(Constants.KEY_PICK_STATE);
        pick_postal = bundle.getString(Constants.KEY_PICK_POSTALCODE);
        package_type = bundle.getString(Constants.KEY_PACKAGE_TYPE);
        // pickup_lat = bundle.getString("pick_up_lat");
        //pickup_long = bundle.getString("pick_up_long");

        // Session class instance
        session = new SessionManager(getApplicationContext());

        // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Toast.makeText(getApplicationContext(), api_token + email, Toast.LENGTH_LONG).show();

        LoadSpinners();
        if (pick_region.equals(Constants.KEY_LOCAL)) {
            loadPricesForLocal();
        }
        if (pick_region.equals(Constants.KEY_INTERNATIONAL)) {
            loadPrices();
        }
        if (pick_region.equals(Constants.KEY_DOMESTIC)) {
            loadPricesForDomestic();
        }

    }

    private void LoadSpinners() {
        ArrayAdapter<CharSequence> adapter_country_d = ArrayAdapter.createFromResource(this,
                R.array.Country, android.R.layout.simple_spinner_item);
        adapter_country_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_d_country.setAdapter(adapter_country_d);
    }

    private void loadPricesForLocal() {
        if (category_name.equals("Document") || category_name.equals("Clothing") || category_name.equals("Groceries") || category_name.equals("Medicines") || category_name.equals("Food") || category_name.equals("Spirits")) {
            item_price = "8";
        }
        if (category_name.equals("Electronics - Laptop") || category_name.equals("Electronics - Playstation") || category_name.equals("Electronics - Mobile") || category_name.equals("Electronics - Accessories") || category_name.equals("Books & Media") || category_name.equals("Shoes") || category_name.equals("Beauty & Health") || category_name.equals("Shoes") || category_name.equals("Toys & Baby Products")) {
            item_price = "18";
        }
        if (category_name.equals("Home & Furniture")) {
            item_price = "75";
        }
    }

    private void loadPrices() {
        if (category_name.equals("Electronics - Laptop") || category_name.equals("Electronics - Playstation")) {
            item_price = "49";
        }
        if (category_name.equals("Electronics - Mobile")) {
            item_price = "29";
        }
        if (category_name.equals("Electronics - Accessories")) {
            item_price = "19";
        }
        if (category_name.equals("Groceries") || category_name.equals("Food")) {
            item_price = "59";
        }
        if (category_name.equals("Spirits")) {
            item_price = "8";
        }
        if (category_name.equals("Document")) {
            item_price = "35";
        }
        if (category_name.equals("Clothing") || category_name.equals("Beauty & Health")) {
            item_price = "15";
        }
        if (category_name.equals("Toys & Baby Products") || category_name.equals("Shoes") || category_name.equals("Sports")) {
            item_price = "19";
        }
    }

    private void loadPricesForDomestic() {
        if (package_type.equals(Constants.KEY_ENVELOPE)) {
            item_price = "30";
        }
        if (package_type.equals(Constants.KEY_BAG)) {
            item_price = "45";
        }
        if (package_type.equals(Constants.KEY_SMALL_BOX)) {
            item_price = "85";
        }
        if (package_type.equals(Constants.KEY_LARGE_BOX)) {
            item_price = "140";
        }
        if (package_type.equals(Constants.KEY_EXTRA_LARGE)) {
            item_price = "280";
        }
    }

   /* private void initwithfilter() {
        editText_valueofitem.setVisibility(View.GONE);
    }

    private void valueFilter() {

        forvalue_filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioButtonID) {
                switch (radioButtonID) {
                    case R.id.radio_itemvalue:
                        setItemsVisible();
                        break;
                    case R.id.radio_requestvalue:
                        setItemsInvisible();
                        break;

                }
            }
        });
    }

    private void setItemsVisible() {
        editText_valueofitem.setVisibility(View.VISIBLE);
    }

    private void setItemsInvisible() {
        editText_valueofitem.setVisibility(View.GONE);
    }*/


    private void validateandCheckpartner() {
        deli_st_address = edit_streetaddr_d.getText().toString();
        deli_postalcode = edit_postal_d.getText().toString();
        value_of_item = editText_valueofitem.getText().toString();
        tip_for_serv = editText_tipserv.getText().toString();
        delivery_city = edit_city_d.getText().toString();
        delivery_state = edit_state_d.getText().toString();
        deliver_country = spin_d_country.getSelectedItem().toString();

       /* Log.v("ADDRESS TEST", deli_st_address + "," + delivery_city + "," + delivery_state + "," + pick_country + "," + deli_postalcode);

        String address_toconvert = deli_st_address + "," + delivery_city + "," + delivery_state + "," + pick_country + "," + deli_postalcode;*/

      /*  String lat = null;
        String longi = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address_toconvert, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                *//*StringBuilder sb = new StringBuilder();
                sb.append(address.getLatitude()).append(",");
                sb.append(address.getLongitude()).append("");
                result = sb.toString();*//*
                lat = String.valueOf(address.getLatitude());
                longi = String.valueOf(address.getLongitude());
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to connect to Geocoder", e);
        }
        Toast.makeText(getApplicationContext(), lat + longi, Toast.LENGTH_LONG).show();
        Log.v("TEST GEOCODE", lat + longi);

        Log.v("PICKUPLAT", String.valueOf(pickup_lat));
        Log.v("PICKUPLONG", String.valueOf(pickup_long));

        Log.v("DELIVERYLAT", lat);
        Log.v("DeliverLONG", longi);

        float[] results = new float[1];
        Location.distanceBetween(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long), Double.parseDouble(lat), Double.parseDouble(longi), results);
        float distanceInMeters = results[0];
        float distanceInMiles = distanceInMeters * 0.00062137f;

        Toast.makeText(getApplicationContext(), String.valueOf(distanceInMiles), Toast.LENGTH_LONG).show();
        Log.v("DISTANCE", String.valueOf(distanceInMiles));
*/

        int fee = 0;
        if (tip_for_serv.length() > 0) {
            fee = Integer.parseInt(tip_for_serv.trim());
        } else {
            input_tipserv.setError("Tip for the service is required");
        }

        if (deli_st_address.equals("") && deli_postalcode.equals("")
                && delivery_city.equals("") && delivery_state.equals("")) {
            input_d_postalcode.setError("Delivery postal code is required");
            input_streetaddress_d.setError("Delivery street address required");
            input_city_d.setError("Delivery city is required");
            input_state_d.setError("Delivery state is required");
        } /*else if (tip_for_serv.length() == 0) {
            input_tipserv.setError("Tip for the service is required");
        } */ /*else if (pick_region.equals("Local") && fee <= distanceInMiles && distanceInMiles <= 50) {
            Log.v("DISTANCE1", String.valueOf(distanceInMiles));
            //Log.v("Rounded1", String.valueOf(rounded));
            input_tipserv.setError("Please enter the tip more than" + distanceInMiles);
        } else if (pick_region.equals("Local") && distanceInMiles > 50 && Integer.parseInt(tip_for_serv) < 50) {
            input_tipserv.setError("Please enter the tip more than 50");
        }*/ else if (deliver_country.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select the country", Toast.LENGTH_LONG).show();
        } else {
            callSmartyStreetForStreetValidation();
        }
    }


    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CollectionDeliverAddressActivity.this);
    }

    private void callSmartyStreetForStreetValidation() {
        progressDialog = ProgressDialog.show(CollectionDeliverAddressActivity.this, "Please wait ...", "Validating Address...", true);
        progressDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.SMARTY_STREETS_FOR_VALIDATION + "&street=" + edit_streetaddr_d.getText().toString() + "&city=" + edit_city_d.getText().toString() + "&state=" + edit_state_d.getText().toString() + "&zipcode=" + edit_postal_d.getText().toString() + "&candidates=1";
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
                                Toast.makeText(CollectionDeliverAddressActivity.this, "Invalid Address contact support", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CollectionDeliverAddressActivity.this, "Address valid", Toast.LENGTH_LONG).show();
                                //goto_DeliveryAddress();
                                callBuyandDeliverAPI();
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
                        Toast.makeText(CollectionDeliverAddressActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void callBuyandDeliverAPI() {
        progressDialog = ProgressDialog.show(CollectionDeliverAddressActivity.this, "Please wait ...", "Adding Service...", true);
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SAVE_BUYANDDELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("--OUTPUT DELIVERY--", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            if (status.equals("false")) {
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                JSONObject value_obj = obj.getJSONObject("value");
                                String message = value_obj.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                to_caluculatecredit();
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                        //Toast.makeText(CollectionDeliverAddressActivity.this, response, Toast.LENGTH_LONG).show();
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
                                // Show timeout error message
                                Toast.makeText(getApplicationContext(),
                                        "Oops. Timeout error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        Toast.makeText(CollectionDeliverAddressActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.v("CHECK", category_id + item_short + image);
                Log.v("CHECK2", pick_region + pick_country + pick_address + pick_city + pick_state + pick_postal + package_type);

                String deliver_country = spin_d_country.getSelectedItem().toString().trim();
                String deliver_address = edit_streetaddr_d.getText().toString();
                String deliver_city = edit_city_d.getText().toString();
                String deliver_state = edit_state_d.getText().toString();
                String deliver_postal = edit_postal_d.getText().toString();
                String item_value = editText_valueofitem.getText().toString();
                String tip = editText_tipserv.getText().toString();
                String date_pick = btn_pickdate.getText().toString();

               /* int res_id = radiovalue.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(res_id);
                String value_select = radioButton.getText().toString().trim();*/

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_CATEGORY, category_id);
                params.put(Constants.KEY_SERVICETYPE, "B");
                params.put(Constants.KEY_ITEM_SHORT_DESC, item_short);
                params.put(Constants.KEY_IMAGE, image);
                params.put(Constants.KEY_REGION, pick_region);
                params.put(Constants.KEY_PICK_ADDRESS, pick_address);
                params.put(Constants.KEY_PICK_CITY, pick_city);
                params.put(Constants.KEY_PICK_STATE, pick_state);
                params.put(Constants.KEY_PICK_COUNTRY, pick_country);
                params.put(Constants.KEY_PICK_POSTALCODE, pick_postal);
                params.put(Constants.KEY_PACKAGE_TYPE, package_type);
                params.put(Constants.KEY_DELIVERY_COUNTRY, deliver_country);
                params.put(Constants.KEY_DELIVERY_ADDRESS, deliver_address);
                params.put(Constants.KEY_DELIVERY_CITY, deliver_city);
                params.put(Constants.KEY_DELIVERY_STATE, deliver_state);
                params.put(Constants.KEY_DELIVERY_POSTAL, deliver_postal);
                params.put(Constants.KEY_DELIVERY_DATE, date_pick);
                params.put(Constants.KEY_IKNOW_VALUE, "I know the value");
                if (item_value.equals("")) {
                    params.put(Constants.KEY_KNOWN_VALUE, "0");
                } else {
                    params.put(Constants.KEY_KNOWN_VALUE, item_value);
                }
                params.put(Constants.KEY_TIP_FEE, tip);
                if (item_value.equals("")) {
                    params.put(Constants.KEY_CREDITS, "0");
                } else {
                    params.put(Constants.KEY_CREDITS, item_value);
                }

                Log.v("==DELIVER PARAMS==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
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

    private void to_caluculatecredit() {
        String tip = editText_tipserv.getText().toString();
        Intent to_cal_credit = new Intent(getApplicationContext(), CalculateCreditActivity.class);
        to_cal_credit.putExtra(Constants.KEY_TIP_FEE, tip);
        startActivity(to_cal_credit);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_pickdate) {
            showDatePicker();
        }
        if (v == button_ok) {
            validateandCheckpartner();
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

            Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            /*if (minAdultAge.before(userAge)) {
                Toast.makeText(getApplicationContext(), "18+ age only allowed", Toast.LENGTH_SHORT).show();
            }*/
            /*btn_pickdate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                    + "-" + String.valueOf(dayOfMonth));
            Toast.makeText(
                    DeliveryAddressActivity.this,
                    String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                            + "-" + String.valueOf(dayOfMonth),
                    Toast.LENGTH_LONG).show();
            date = String.valueOf(year) + "-" + String.valueOf(monthOfYear)
                    + "-" + String.valueOf(dayOfMonth);*/

            int month = monthOfYear + 1;

            if (month < 10 && dayOfMonth < 10) {
                btn_pickdate.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
            } else if (month >= 10 && dayOfMonth < 10) {
                btn_pickdate.setText(year + "-" + month + "-" + "0" + dayOfMonth);
            } else if (month < 10 && dayOfMonth > 10) {
                btn_pickdate.setText(year + "-" + "0" + month + "-" + dayOfMonth);
            } else {
                btn_pickdate.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edit_postal_d.getEditableText()) {
            input_d_postalcode.setError(null);
        } else if (editable == edit_city_d.getEditableText()) {
            input_city_d.setError(null);
        } else if (editable == edit_state_d.getEditableText()) {
            input_state_d.setError(null);
        } else if (editable == editText_tipserv.getEditableText()) {
            input_tipserv.setError(null);
        } else if (editable == edit_quantity.getEditableText()) {
            String edit_qty = edit_quantity.getText().toString();
            int total = 0;
            if (!edit_qty.isEmpty()) {
                int price_caluculation = Integer.parseInt(edit_quantity.getText().toString());
                int price = Integer.parseInt(item_price);
                total = price * price_caluculation;
            }
            editText_tipserv.setText(String.valueOf(total));
            editText_tipserv.setEnabled(false);
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
        MyApplication.getInstance().trackScreenView("Collection Delivery");
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