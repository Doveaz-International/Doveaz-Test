package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.ProfileService;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Karthik on 2016/01/11.
 */
public class EditDescription extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    String userId, serviceId, service_b_id;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    TextInputLayout input_postalcode, input_streetaddress, input_city, input_state;

    EditText edit_postal_code_c, edit_streetaddress_c, editText_state_c, editText_city_c;

    Spinner spin_country;

    String coll_street_address, coll_postal_code, coll_country, coll_city, coll_state;

    TextInputLayout input_streetaddress_d, input_city_d, input_state_d, input_d_postalcode, input_fee;

    EditText edit_streetaddr_d, edit_city_d, edit_state_d, edit_postal_d, edit_fee;

    String deli_st_address, deli_postalcode, delivery_city, delivery_state, deliver_country, credits_fee;

    Spinner spin_d_country;

    //Serialized object
    ProfileService profile;

    //For GCM
    ShareExternalServer appUtil;

    Button btn_negotiate, btn_engage, btn_decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_partner_desc);

        menuvisibilityinAlldevices();

        appUtil = new ShareExternalServer();

        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Button
        btn_negotiate = (Button) findViewById(R.id.btn_negotiate);
        btn_engage = (Button) findViewById(R.id.btn_engage);
        btn_decline = (Button) findViewById(R.id.btn_declined);

        // Edittext init
        edit_postal_code_c = (EditText) findViewById(R.id.edit_postalcode_bd);
        edit_streetaddress_c = (EditText) findViewById(R.id.edit_streetaddress);
        editText_state_c = (EditText) findViewById(R.id.edit_state);
        editText_city_c = (EditText) findViewById(R.id.edit_city);
        edit_streetaddr_d = (EditText) findViewById(R.id.edit_streetaddress_delivery);
        edit_city_d = (EditText) findViewById(R.id.edit_city_delivery);
        edit_state_d = (EditText) findViewById(R.id.edit_state_delivery);
        edit_postal_d = (EditText) findViewById(R.id.edit_postalcode_d);
        edit_fee = (EditText) findViewById(R.id.edit_fee);


        //TextinputLayout for set error
        input_postalcode = (TextInputLayout) findViewById(R.id.input_postalcode_bd);
        input_streetaddress = (TextInputLayout) findViewById(R.id.input_streetaddress);
        input_city = (TextInputLayout) findViewById(R.id.input_city);
        input_state = (TextInputLayout) findViewById(R.id.input_state);
        input_d_postalcode = (TextInputLayout) findViewById(R.id.input_postalcode_d);
        input_streetaddress_d = (TextInputLayout) findViewById(R.id.input_streetaddress_delivery);
        input_city_d = (TextInputLayout) findViewById(R.id.input_city_delivery);
        input_state_d = (TextInputLayout) findViewById(R.id.input_state_delivery);
        input_fee = (TextInputLayout) findViewById(R.id.input_fee);

        //spinner
        spin_d_country = (Spinner) findViewById(R.id.spin_country_d);
        //spinner
        spin_country = (Spinner) findViewById(R.id.spin_country_bd);


        //Listeners
        edit_streetaddress_c.addTextChangedListener(this);
        edit_postal_code_c.addTextChangedListener(this);
        edit_streetaddr_d.addTextChangedListener(this);
        edit_postal_d.addTextChangedListener(this);
        btn_negotiate.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getString(Constants.KEY_USERID);
        serviceId = bundle.getString(Constants.KEY_SERVICEID);
        service_b_id = bundle.getString(Constants.KEY_SERVICE_B_ID);

        profile = (ProfileService) intent.getSerializableExtra("ProfileService");
        coll_street_address = profile.getPick_up_address();
        coll_city = profile.getPick_up_city();
        coll_state = profile.getPick_up_state();
        coll_country = profile.getPick_up_country();
        coll_postal_code = profile.getPick_up_postalcode();
        deli_st_address = profile.getDelivery_address();
        delivery_city = profile.getDelivery_city();
        deliver_country = profile.getDelivery_country();
        delivery_state = profile.getDelivery_state();
        deli_postalcode = profile.getDelivery_postalcode();
        credits_fee = profile.getCredits();

        LoadSpinners();
        LoadValuestoUI();
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(EditDescription.this);
    }

    private void LoadValuestoUI() {
        edit_streetaddress_c.setText(coll_street_address);
        editText_city_c.setText(coll_city);
        editText_state_c.setText(coll_state);
        edit_postal_code_c.setText(coll_postal_code);
        edit_streetaddr_d.setText(deli_st_address);
        edit_city_d.setText(delivery_city);
        edit_state_d.setText(delivery_state);
        edit_postal_d.setText(deli_postalcode);
        edit_fee.setText(credits_fee);
    }

    private void LoadSpinners() {
        //spinner adapter for country
        ArrayAdapter<CharSequence> adapter_country = ArrayAdapter.createFromResource(this,
                R.array.Country, android.R.layout.simple_spinner_item);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_country.setAdapter(adapter_country);
        if (!coll_country.equals(null)) {
            int spinnerPosition = adapter_country.getPosition(coll_country);
            spin_country.setSelection(spinnerPosition);
        }

        ArrayAdapter<CharSequence> adapter_country_d = ArrayAdapter.createFromResource(this,
                R.array.Country, android.R.layout.simple_spinner_item);
        adapter_country_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_d_country.setAdapter(adapter_country_d);
        if (!deliver_country.equals(null)) {
            int spinnerPosition = adapter_country.getPosition(deliver_country);
            spin_d_country.setSelection(spinnerPosition);
        }
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
        } else if (editable == edit_streetaddress_c.getEditableText()) {
            input_streetaddress.setError(null);
        } else if (editable == edit_postal_code_c.getEditableText()) {
            input_postalcode.setError(null);
        } else if (editable == editText_city_c.getEditableText()) {
            input_city.setError(null);
        } else if (editable == editText_state_c.getEditableText()) {
            input_state.setError(null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_negotiate) {
            callAPI_toNegotiate();
        }
        if (v == btn_engage) {
            callAPI_toEngagePartner();
        }
        if (v == btn_decline) {
            callAPI_toDecline();
        }
    }

    private void callAPI_toDecline() {
        progressDialog = ProgressDialog.show(EditDescription.this, "Please wait ...", "Validating Address...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT DECLINE--", response.toString());
                progressDialog.dismiss();
                //Toast.makeText(EditDescription.this, response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONArray userarray = obj.getJSONArray("value");

                    for (int i = 0; i < userarray.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                    progressDialog.dismiss();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String type = "DECLINE";
        ServiceCalls.CallAPI_togetPartnerEngagement(this, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
    }

    private void callAPI_toEngagePartner() {
        progressDialog = ProgressDialog.show(EditDescription.this, "Please wait ...", "Validating Address...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT ENGAGE--", response.toString());
                //Toast.makeText(EditDescription.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONArray userarray = obj.getJSONArray("value");

                    for (int i = 0; i < userarray.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                            HashMap<String, String> user = session.getUserDetails();
                            // token
                            String u_name = user.get(SessionManager.KEY_USERNAME);
                            sendMessageToGCMAppServer(userId, u_name + " " + "has engaged for your service");
                            Intent to_credit = new Intent(getApplicationContext(), FriendsMenuPartnerActivity.class);
                            startActivity(to_credit);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                    progressDialog.dismiss();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String type = "ENGAGE";
        ServiceCalls.CallAPI_togetPartnerEngagement(this, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
    }

    private void callAPI_toNegotiate() {

        progressDialog = ProgressDialog.show(EditDescription.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE NEGOTIATE--", response);
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    //JSONObject value_obj = obj.getJSONObject("value");
                    progressDialog.dismiss();
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> user = session.getUserDetails();
                        // token
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(userId, u_name + " " + "has been negotiated your service");
                        Intent to_credit = new Intent(getApplicationContext(), FriendsMenuPartnerActivity.class);
                        startActivity(to_credit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        //gettext from edittexts
        String nego_c_address = edit_streetaddress_c.getText().toString();
        String nego_c_city = editText_city_c.getText().toString();
        String nego_c_country = spin_country.getSelectedItem().toString();
        String nego_c_state = editText_state_c.getText().toString();
        String nego_c_postal = edit_postal_code_c.getText().toString();
        String nego_d_address = edit_streetaddr_d.getText().toString();
        String nego_d_city = edit_city_d.getText().toString();
        String nego_d_country = spin_d_country.getSelectedItem().toString();
        String nego_d_state = edit_state_d.getText().toString();
        String nego_d_postal = edit_postal_d.getText().toString();
        String fee = edit_fee.getText().toString();
        ServiceCalls.CallAPI_toNegotiate(this, Request.Method.POST, Constants.NEGOTIATE_SERVICE, listener, userId, serviceId, service_b_id, nego_c_address, nego_c_city, nego_c_state, nego_c_country, nego_c_postal, nego_d_address, nego_d_city, nego_d_state, nego_d_country, nego_d_postal, fee, api_token);
    }

    private void sendMessageToGCMAppServer(final String toUserId,
                                           final String messageToSend) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                String result = appUtil.sendMessage(toUserId, messageToSend, api_token);
                Log.d("MainActivity", "Result: " + result);
                return result;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("MainActivity", "Result: " + msg);
                /*Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
                        .show();*/
            }
        }.execute(null, null, null);
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
