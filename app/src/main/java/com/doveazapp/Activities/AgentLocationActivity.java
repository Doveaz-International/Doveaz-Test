package com.doveazapp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AgentLocationActivity.java
 * Created by Karthik on 7/18/2016.
 */
public class AgentLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spin_pickcity, spin_pickarea;
    private ArrayList<HashMap<String, String>> cityArrayList = null;
    private ArrayList<HashMap<String, String>> areaArrayList = null;

    HashMap<String, String> cityList;
    HashMap<String, String> areaList;
    SimpleAdapter cityAdapPROJ, areaAdapPROJ;

    String city_text, updated_area;

    // Session Manager Class
    SessionManager session;

    String partner_loggedin;

    Button btn_update;

    //Progress bar
    ProgressDialog progressDialog;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_location_update);

        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        spin_pickcity = (Spinner) findViewById(R.id.spin_pickcity);
        spin_pickarea = (Spinner) findViewById(R.id.spin_pickarea);
        btn_update = (Button) findViewById(R.id.btn_update);

        // GET INTENT FROM PREVIOUS CLASS
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            partner_loggedin = bundle.getString(Constants.LOGIN_PARTNER);
            if (partner_loggedin != null) {
                Log.v("Login_partner", partner_loggedin);
                enterPassword();
                /*order_id = bundle.getString("message");
                if (order_id != null) {
                    Log.v("Message from notification", order_id);
                }*/

            }
        }

        menuvisibilityinAlldevices();
        LoadCities_fromAPI();

        btn_update.setOnClickListener(this);

        spin_pickcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Load_availableAreas_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(AgentLocationActivity.this);
    }

    private void enterPassword() {
        /* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(AgentLocationActivity.this);
        alert.setTitle("Password");
        alert.setIcon(R.drawable.lock_login);
        alert.setMessage("Please enter your password, for security reasons");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(AgentLocationActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String entered_value = input.getEditableText().toString();

                progressDialog = ProgressDialog.show(AgentLocationActivity.this, Constants.PLEASE_WAIT, Constants.REQUESTING, true);
                progressDialog.setCancelable(false);
                OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(String response) {
                        Log.v("OUTPUT AUTHENTICATE", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString(Constants.KEY_STATUS);
                            final String value = obj.getString(Constants.KEY_VALUE);
                            if (status.equals(Constants.KEY_FALSE)) {
                                progressDialog.dismiss();
                                Toast.makeText(AgentLocationActivity.this, value, Toast.LENGTH_SHORT).show();
                                if (value.equals("You are not logged in")) {
                                    to_loginActivity();
                                } else {
                                    enterPassword();
                                }
                            } else if (status.equals(Constants.KEY_TRUE)) {
                                progressDialog.dismiss();
                                Toast.makeText(AgentLocationActivity.this, value, Toast.LENGTH_SHORT).show();
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
                ServiceCalls.CallAPI_to_sessionLogin(AgentLocationActivity.this, Request.Method.POST, Constants.AUTHENTICATE_USER, listener, entered_value, api_token);
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

    /*dropdown for city*/
    private void LoadCities_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {

                    cityArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray city_array = value_obj.getJSONArray(Constants.CITIES);

                    for (int i = 0; i < city_array.length(); i++) {
                        JSONObject json_data = city_array.getJSONObject(i);
                        String city_name = city_array.getJSONObject(i).getString(Constants.CITY_NAME);
                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            cityList = new HashMap<String, String>();
                            cityList.put(Constants.CITY_NAME, city_name);

                            cityArrayList.add(cityList);

                            cityAdapPROJ = new SimpleAdapter(AgentLocationActivity.this, cityArrayList, R.layout.spinner_item,
                                    new String[]{"id", Constants.CITY_NAME}, new int[]{R.id.Id, R.id.Name});
                            spin_pickcity.setAdapter(cityAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        ServiceCalls.callAPI_togetCity(this, Request.Method.POST, Constants.GET_CITIES, "India", "Karnataka", listener);
    }

    /*dropdown for areas*/
    private void Load_availableAreas_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {

                    areaArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray city_array = value_obj.getJSONArray(Constants.KEY_AREA);

                    for (int i = 0; i < city_array.length(); i++) {
                        Log.v("City_array", city_array.toString());
                        JSONObject json_data = city_array.getJSONObject(i);
                        Log.i("log_tag", " area" + json_data.getString(Constants.KEY_AREA));

                        String area_name = city_array.getJSONObject(i).getString(Constants.KEY_AREA);

                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            Log.v("area", area_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            areaList = new HashMap<String, String>();
                            areaList.put(Constants.KEY_AREA, area_name);

                            areaArrayList.add(areaList);

                            areaAdapPROJ = new SimpleAdapter(AgentLocationActivity.this, areaArrayList, R.layout.spinner_item,
                                    new String[]{"id", Constants.KEY_AREA}, new int[]{R.id.Id, R.id.Name});
                            spin_pickarea.setAdapter(areaAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        city_text = spin_pickcity.getSelectedItem().toString();
        city_text = city_text.replace("city_name=", "");
        city_text = city_text.replaceAll("[\\[\\](){}]", "");

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetAreafromcity(this, Request.Method.POST, Constants.GET_AREA, city_text, listener, api_token);
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
        if (v == btn_update) {
            CallAPI_to_updateArea();
            //startActivity(new Intent(AgentLocationActivity.this, AgentMenuUpdateActivity.class));
        }
    }

    private void CallAPI_to_updateArea() {
        updated_area = spin_pickarea.getSelectedItem().toString();
        updated_area = updated_area.replace("area=", "");
        updated_area = updated_area.replaceAll("[\\[\\](){}]", "");

        progressDialog = ProgressDialog.show(AgentLocationActivity.this, Constants.PLEASE_WAIT, Constants.REQUESTING, true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT UPDATE LOCATION", response);

                progressDialog.dismiss();
                try {

                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);

                    if (status.equals(Constants.KEY_FALSE)) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AgentLocationActivity.this, AgentMenuUpdateActivity.class)
                                .putExtra(Constants.KEY_AREA, updated_area));
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

        /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling API", Constants.UPDATE_AGENT_AREA);
        ServiceCalls.CallAPI_to_UpdateArea(this, Request.Method.POST, Constants.UPDATE_AGENT_AREA, listener, updated_area, api_token);
    }
}
