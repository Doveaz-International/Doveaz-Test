package com.doveazapp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.SqliteManager.AddedCartDBHelper;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.doveazapp.Utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * BuyandDeliveryActivity.java
 * Created by Karthik on 11/18/2015.
 */
public class BuyandDeliveryActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final static String TAG = BuyandDeliveryActivity.class.getName();

    private LinearLayout loc_layout, loc_items_layout, store_layout, store_details_layout, deliver_layout, deliver_details_layout;
    private Animation animShow, animHide;
    private Button button_menu;
    private ImageView loc_arrow_img, store_arrow_img, deliver_arrow_img;

    private Spinner spin_pickcity, spin_pickarea, spin_pickcategory, spin_pickstore, spin_city_manual, spin_area_manual, spin_local_manual;

    private ArrayList<HashMap<String, String>> cityArrayList = null;
    private ArrayList<HashMap<String, String>> areaArrayList = null;
    private ArrayList<HashMap<String, String>> categoryArrayList = null;
    private ArrayList<HashMap<String, String>> storeArrayList = null;
    private ArrayList<HashMap<String, String>> citymArrayList = null;
    private ArrayList<HashMap<String, String>> areamArrayList = null;
    private ArrayList<HashMap<String, String>> localArrayList = null;

    HashMap<String, String> cityList;
    HashMap<String, String> areaList;
    HashMap<String, String> categoryList;
    HashMap<String, String> storeList;
    HashMap<String, String> citymList;
    HashMap<String, String> areamList;
    HashMap<String, String> localList;

    SimpleAdapter cityAdapPROJ, areaAdapPROJ, categoryAdapPROJ, storeAdapPROJ, citymAdapPROJ, areamAdapPROJ, localAdapPROJ;

    String city_text, id_category, category_name, store_id, store_name, city_mtext, local_mtext;

    // Session Manager Class
    SessionManager session;

    //Progress bar for loading
    ProgressDialog progressDialog;

    // Textviews
    TextView user_type_prime;

    //RadioGroups
    RadioGroup radio_address;

    // Radiobuttons
    RadioButton home_address, office_address, enter_manually;

    //LinearLayout for Address (if prime it will be visible with the addresses)
    LinearLayout address_layout, manual_address_layout;

    //Textview of address (if prime)
    TextView street, area, city, state, country, zipcode, mob_number, name_prime;

    AddedCartDBHelper cartDBHelper;

    SQLiteDatabase sqldb;

    String address_type, deliver_st, deliver_area, deliver_city, deliver_state, deliver_country, deliver_zip, delivery_phone,
            name, floor_num, order_name, deliver_address;

    TextInputLayout input_manual_name, input_manual_street, input_manual_floor, input_manual_pincode;

    EditText edit_manual_name, edit_manual_street, edit_manual_floor, edit_manual_pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buydeliver_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Buy & delivery");

        // Session class instance
        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        cartDBHelper = new AddedCartDBHelper(this);
        sqldb = cartDBHelper.getWritableDatabase();

        // Animating while opening and closing
        initAnimation();

        // TextInput layout
        input_manual_name = (TextInputLayout) findViewById(R.id.input_manual_name);
        input_manual_street = (TextInputLayout) findViewById(R.id.input_manual_street);
        input_manual_floor = (TextInputLayout) findViewById(R.id.input_manual_floor);
        input_manual_pincode = (TextInputLayout) findViewById(R.id.input_manual_pincode);

        edit_manual_name = (EditText) findViewById(R.id.edit_manual_name);
        edit_manual_street = (EditText) findViewById(R.id.edit_manual_street);
        edit_manual_floor = (EditText) findViewById(R.id.edit_manual_floor);
        edit_manual_pincode = (EditText) findViewById(R.id.edit_manual_pincode);

        // Textviews for address (if prime) will be visible to the prime user
        street = (TextView) findViewById(R.id.street);
        area = (TextView) findViewById(R.id.area);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        country = (TextView) findViewById(R.id.country);
        zipcode = (TextView) findViewById(R.id.zipcode);
        mob_number = (TextView) findViewById(R.id.mob_number);
        name_prime = (TextView) findViewById(R.id.name);

        //Radiobuttons
        home_address = (RadioButton) findViewById(R.id.home_address);
        office_address = (RadioButton) findViewById(R.id.office_address);
        enter_manually = (RadioButton) findViewById(R.id.enter_manually);

        // this is for expanding views
        loc_layout = (LinearLayout) findViewById(R.id.loc_layout);
        store_layout = (LinearLayout) findViewById(R.id.store_layout);
        deliver_layout = (LinearLayout) findViewById(R.id.deliver_layout);
        deliver_details_layout = (LinearLayout) findViewById(R.id.deliver_details_layout);
        loc_items_layout = (LinearLayout) findViewById(R.id.loc_items_layout);
        store_details_layout = (LinearLayout) findViewById(R.id.store_details_layout);
        button_menu = (Button) findViewById(R.id.button_menu);
        user_type_prime = (TextView) findViewById(R.id.user_type_prime);

        //RadioButtons for address
        radio_address = (RadioGroup) findViewById(R.id.radio_address);

        //Image views
        loc_arrow_img = (ImageView) findViewById(R.id.loc_arrow_img);
        store_arrow_img = (ImageView) findViewById(R.id.store_arrow_img);
        deliver_arrow_img = (ImageView) findViewById(R.id.deliver_arrow_img);

        loc_layout.setOnClickListener(this);
        store_layout.setOnClickListener(this);
        deliver_layout.setOnClickListener(this);
        button_menu.setOnClickListener(this);
        edit_manual_name.addTextChangedListener(this);
        edit_manual_floor.addTextChangedListener(this);
        edit_manual_street.addTextChangedListener(this);
        edit_manual_pincode.addTextChangedListener(this);

        spin_pickcity = (Spinner) findViewById(R.id.spin_pickcity);
        spin_pickarea = (Spinner) findViewById(R.id.spin_pickarea);
        spin_pickcategory = (Spinner) findViewById(R.id.spin_pickcategory);
        spin_pickstore = (Spinner) findViewById(R.id.spin_pickstore);
        spin_city_manual = (Spinner) findViewById(R.id.spin_city_manual);
        spin_area_manual = (Spinner) findViewById(R.id.spin_area_manual);
        spin_local_manual = (Spinner) findViewById(R.id.spin_local_manual);

        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        manual_address_layout = (LinearLayout) findViewById(R.id.manual_address_layout);

        LoadCities_fromAPI();
        LoadCitiestomanual_fromAPI();
        LoadAPI_getStorecategories();

        spin_pickcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> id1 = categoryArrayList.get(position);
                id_category = id1.get("id");
                category_name = id1.get("category_name");

                Load_storesfromcategories_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_area_manual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadLocalsformanual_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_pickcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Load_availableAreas_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_pickstore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> id1 = storeArrayList.get(position);
                store_id = id1.get("id");
                store_name = id1.get("store_name");

                if (position == 0) {

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_city_manual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Load_Areasformanual_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(BuyandDeliveryActivity.this);
    }

    private void LoadLocalsformanual_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    localArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray city_arr = value_obj.getJSONArray("streets");

                    for (int i = 0; i < city_arr.length(); i++) {
                        Log.v("streets", city_arr.toString());
                        JSONObject json_data = city_arr.getJSONObject(i);
                        Log.i("log_tag", " streets" + json_data.getString("street"));

                        String area_name = city_arr.getJSONObject(i).getString("street");

                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            Log.v("street", area_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            localList = new HashMap<String, String>();
                            localList.put("street", area_name);

                            localArrayList.add(localList);

                            localAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, localArrayList, R.layout.spinner_item,
                                    new String[]{"id", "street"}, new int[]{R.id.Id, R.id.Name});
                            spin_local_manual.setAdapter(localAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        local_mtext = spin_area_manual.getSelectedItem().toString();
        local_mtext = local_mtext.replace("city=area=", "");
        local_mtext = local_mtext.replaceAll("[\\[\\](){}]", "");

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        local_mtext = local_mtext.replace("area=", "");
        ServiceCalls.callAPI_togetlocalforArea(this, Request.Method.POST, Constants.GET_LOCALS, local_mtext, listener, api_token);
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

                            cityAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, cityArrayList, R.layout.spinner_item,
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

    private void Load_Areasformanual_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    areamArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray city_arr = value_obj.getJSONArray(Constants.KEY_AREA);

                    for (int i = 0; i < city_arr.length(); i++) {
                        Log.v("City_array", city_arr.toString());
                        JSONObject json_data = city_arr.getJSONObject(i);
                        Log.i("log_tag", " area" + json_data.getString(Constants.KEY_AREA));

                        String area_name = city_arr.getJSONObject(i).getString(Constants.KEY_AREA);

                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            Log.v("area", area_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            areamList = new HashMap<String, String>();
                            areamList.put(Constants.KEY_AREA, area_name);

                            areamArrayList.add(areamList);

                            areamAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, areamArrayList, R.layout.spinner_item,
                                    new String[]{"id", Constants.KEY_AREA}, new int[]{R.id.Id, R.id.Name});
                            spin_area_manual.setAdapter(areamAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        city_mtext = spin_city_manual.getSelectedItem().toString();
        city_mtext = city_mtext.replace("city_name=", "");
        city_mtext = city_mtext.replaceAll("[\\[\\](){}]", "");

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetAreafromcity(this, Request.Method.POST, Constants.GET_AREA, city_mtext, listener, api_token);
    }

    /*dropdown for city_manual*/
    private void LoadCitiestomanual_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {

                    citymArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray city_arr = value_obj.getJSONArray(Constants.CITIES);

                    for (int i = 0; i < city_arr.length(); i++) {
                        JSONObject json_data = city_arr.getJSONObject(i);
                        String city_name = city_arr.getJSONObject(i).getString(Constants.CITY_NAME);
                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            citymList = new HashMap<String, String>();
                            citymList.put(Constants.CITY_NAME, city_name);

                            citymArrayList.add(citymList);

                            citymAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, citymArrayList, R.layout.spinner_item,
                                    new String[]{"id", Constants.CITY_NAME}, new int[]{R.id.Id, R.id.Name});
                            spin_city_manual.setAdapter(citymAdapPROJ);
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

                            areaAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, areaArrayList, R.layout.spinner_item,
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

    /*dropdown for categories*/
    private void LoadAPI_getStorecategories() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--output store categories--", response);
                try {

                    categoryArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray city_array = value_obj.getJSONArray(Constants.CATEGORIES);

                    for (int i = 0; i < city_array.length(); i++) {
                        JSONObject json_data = city_array.getJSONObject(i);
                        Log.i("log_tag", " area" + json_data.getString("category_name"));

                        String id = city_array.getJSONObject(i).getString("id");
                        String category_name = city_array.getJSONObject(i).getString("category_name");

                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            Log.v("category_name", category_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            categoryList = new HashMap<String, String>();
                            categoryList.put("id", id);
                            categoryList.put("category_name", category_name);

                            categoryArrayList.add(categoryList);

                            categoryAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, categoryArrayList, R.layout.spinner_item,
                                    new String[]{"id", "category_name"}, new int[]{R.id.Id, R.id.Name});
                            spin_pickcategory.setAdapter(categoryAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetCategories(this, Request.Method.POST, Constants.GET_STORECATEGORIES, listener, api_token);
    }

    /*dropdown for stores*/
    private void Load_storesfromcategories_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--output store List--", response);
                try {
                    storeArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray store_array = value_obj.getJSONArray(Constants.STORES);

                    for (int i = 0; i < store_array.length(); i++) {
                        JSONObject json_data = store_array.getJSONObject(i);
                        Log.i("log_tag", " store_name" + json_data.getString("store_name"));

                        String id = store_array.getJSONObject(i).getString("id");
                        String store_name = store_array.getJSONObject(i).getString("store_name");

                        if (status.equals(Constants.KEY_FALSE)) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals(Constants.KEY_TRUE)) {
                            Log.v("store_name", store_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            storeList = new HashMap<String, String>();
                            storeList.put("id", id);
                            storeList.put("store_name", store_name);

                            storeArrayList.add(storeList);

                            storeAdapPROJ = new SimpleAdapter(BuyandDeliveryActivity.this, storeArrayList, R.layout.spinner_item,
                                    new String[]{"id", "store_name"}, new int[]{R.id.Id, R.id.Name});
                            spin_pickstore.setAdapter(storeAdapPROJ);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetStorefromcategory_id(this, Request.Method.POST, Constants.GET_STORES, id_category, listener, api_token);
    }

    private void openPopup_formobilenumber() {
        /* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(BuyandDeliveryActivity.this);
        alert.setTitle(Constants.MOBILE_NUMBER);
        alert.setIcon(R.drawable.mobile);
        alert.setMessage("Please enter your mobile number");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(BuyandDeliveryActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String entered_value = input.getEditableText().toString();
                if (!Utils.isOnline(BuyandDeliveryActivity.this)) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    openPopup_formobilenumber();
                } else if (entered_value.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the mobile number", Toast.LENGTH_LONG).show();
                    openPopup_formobilenumber();
                } else {
                    mob_number.setText(entered_value);
                    deliver_details_layout.setVisibility(View.VISIBLE);
                    deliver_details_layout.startAnimation(animShow);
                    deliver_arrow_img.setImageResource(R.drawable.up_arrow);
                    progressDialog = ProgressDialog.show(BuyandDeliveryActivity.this, Constants.PLEASE_WAIT, Constants.REQUESTING, true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("OUTPUT USER", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                final String status = obj.getString(Constants.KEY_STATUS);
                                final String value = obj.getString(Constants.KEY_VALUE);
                                JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                                final String user_type = value_obj.getString(Constants.KEY_USER_TYPE);

                                if (status.equals(Constants.KEY_FALSE)) {
                                    progressDialog.dismiss();
                                    Toast.makeText(BuyandDeliveryActivity.this, value, Toast.LENGTH_SHORT).show();
                                } else if (status.equals(Constants.KEY_TRUE)) {
                                    progressDialog.dismiss();
                                    if (user_type.equals("Prime User")) {
                                        JSONObject office_address_obj = value_obj.getJSONObject("office_address");
                                        JSONObject home_address_obj = value_obj.getJSONObject("home_address");

                                        final String home_street = home_address_obj.getString("street");
                                        final String home_area = home_address_obj.getString("area");
                                        final String home_city = home_address_obj.getString("city");
                                        final String home_state = home_address_obj.getString("state");
                                        final String home_country = home_address_obj.getString("country");
                                        final String home_zip = home_address_obj.getString("zip");
                                        final String home_name = home_address_obj.getString("name");
                                        final String ofc_street = office_address_obj.getString("street");
                                        final String ofc_area = office_address_obj.getString("area");
                                        final String ofc_city = office_address_obj.getString("city");
                                        final String ofc_state = office_address_obj.getString("state");
                                        final String ofc_country = office_address_obj.getString("country");
                                        final String ofc_zip = office_address_obj.getString("zip");
                                        final String ofc_name = office_address_obj.getString("name");

                                        radio_address.setVisibility(View.VISIBLE);
                                        user_type_prime.setVisibility(View.VISIBLE);
                                        user_type_prime.setText("Star user");
                                        home_address.setChecked(true);
                                        address_layout.setVisibility(View.VISIBLE);
                                        manual_address_layout.setVisibility(View.GONE);
                                        street.setText(home_street);
                                        area.setText(home_area);
                                        city.setText(home_city);
                                        state.setText(home_state);
                                        country.setText(home_country);
                                        zipcode.setText(home_zip);
                                        name_prime.setText(home_name);

                                        radio_address.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                switch (checkedId) {
                                                    case R.id.home_address:
                                                        address_layout.setVisibility(View.VISIBLE);
                                                        manual_address_layout.setVisibility(View.GONE);
                                                        street.setText(home_street);
                                                        area.setText(home_area);
                                                        city.setText(home_city);
                                                        state.setText(home_state);
                                                        country.setText(home_country);
                                                        zipcode.setText(home_zip);
                                                        name_prime.setText(home_name);
                                                        break;
                                                    case R.id.office_address:
                                                        manual_address_layout.setVisibility(View.GONE);
                                                        address_layout.setVisibility(View.VISIBLE);
                                                        street.setText(ofc_street);
                                                        area.setText(ofc_area);
                                                        city.setText(ofc_city);
                                                        state.setText(ofc_state);
                                                        country.setText(ofc_country);
                                                        zipcode.setText(ofc_zip);
                                                        name_prime.setText(ofc_name);
                                                        break;
                                                    case R.id.enter_manually:
                                                        address_layout.setVisibility(View.GONE);
                                                        manual_address_layout.setVisibility(View.VISIBLE);
                                                        break;
                                                }
                                            }
                                        });
                                    } else {
                                        enter_manually.setChecked(true);
                                        radio_address.setVisibility(View.GONE);
                                        address_layout.setVisibility(View.GONE);
                                        manual_address_layout.setVisibility(View.VISIBLE);
                                    }
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
                    ServiceCalls.CallAPI_to_GetPrimeinfo(BuyandDeliveryActivity.this, Request.Method.POST, Constants.GET_PRIME_OR_NOT, listener, entered_value, api_token);
                }
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == loc_layout) {
            if (loc_items_layout.getVisibility() == View.VISIBLE) {
                // Its visible
                loc_items_layout.setVisibility(View.GONE);
                loc_items_layout.startAnimation(animHide);
                loc_arrow_img.setImageResource(R.drawable.down_arrow);
            } else {
                // Either gone or invisible
                loc_items_layout.setVisibility(View.VISIBLE);
                loc_items_layout.startAnimation(animShow);
                loc_arrow_img.setImageResource(R.drawable.up_arrow);
            }
        }
        if (view == store_layout) {
            if (store_details_layout.getVisibility() == View.VISIBLE) {
                //its visible
                store_details_layout.setVisibility(View.GONE);
                store_details_layout.startAnimation(animHide);
                store_arrow_img.setImageResource(R.drawable.down_arrow);
            } else {
                //Either gone or invisible
                store_details_layout.setVisibility(View.VISIBLE);
                store_details_layout.startAnimation(animShow);
                store_arrow_img.setImageResource(R.drawable.up_arrow);
            }
        }
        if (view == deliver_layout) {
            if (deliver_details_layout.getVisibility() == View.VISIBLE) {
                //its visible
                deliver_details_layout.setVisibility(View.GONE);
                deliver_details_layout.startAnimation(animHide);
                deliver_arrow_img.setImageResource(R.drawable.down_arrow);
            } else {
                //Either gone or invisible
                if (mob_number.getText().toString().equals("")) {
                    openPopup_formobilenumber();
                } else {
                    deliver_details_layout.setVisibility(View.VISIBLE);
                    deliver_details_layout.startAnimation(animShow);
                    deliver_arrow_img.setImageResource(R.drawable.up_arrow);
                }
            }
        }
        if (view == button_menu) {
            //validate_fields();
            int res_id = radio_address.getCheckedRadioButtonId();
            if (res_id == -1) {
                Toast.makeText(getApplicationContext(), "Please select the delivery address", Toast.LENGTH_SHORT).show();
                return;
            } else {
                RadioButton radioButton = (RadioButton) findViewById(res_id);
                address_type = radioButton.getText().toString().trim();
            }

            deliver_state = state.getText().toString();
            deliver_country = "India";
            delivery_phone = mob_number.getText().toString();
            order_name = name_prime.getText().toString();

            String addr_type;

            Intent to_store_menu = new Intent(getApplicationContext(), StoreMenuActivity.class);
            to_store_menu.putExtra(Constants.KEY_STOREID, store_id);
            if (address_type.equals("Manual")) {
                addr_type = "0";
                if (edit_manual_name.getText().toString().equals("") &&
                        edit_manual_street.getText().toString().equals("") && edit_manual_floor.getText().toString().equals("") &&
                        edit_manual_pincode.getText().toString().equals("")) {
                    input_manual_name.setError("Please enter the name");
                    input_manual_street.setError("Please enter the street address");
                    input_manual_floor.setError("Please enter the floor number");
                    input_manual_pincode.setError("Please enter the pincode");
                } else {
                    name = edit_manual_name.getText().toString();
                    deliver_city = spin_city_manual.getSelectedItem().toString();
                    deliver_city = deliver_city.replace("city_name=", "");
                    deliver_city = deliver_city.replaceAll("[\\[\\](){}]", "");
                    deliver_area = spin_area_manual.getSelectedItem().toString();
                    deliver_area = deliver_area.replace("area=", "");
                    deliver_area = deliver_area.replaceAll("[\\[\\](){}]", "");
                    deliver_address = edit_manual_street.getText().toString();
                    deliver_st = spin_local_manual.getSelectedItem().toString().replace("street=", "").replaceAll("[\\[\\](){}]", "");
                    floor_num = edit_manual_floor.getText().toString();
                    deliver_zip = edit_manual_pincode.getText().toString();
                    deliver_state = "Karnataka";
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_ADDRESS, deliver_address);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_STREET, deliver_st);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_AREA, deliver_area);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_CITY, deliver_city);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_STATE, deliver_state);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_COUNTRY, deliver_country);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_POSTAL, deliver_zip);
                    to_store_menu.putExtra(Constants.KEY_NAME, name);
                    to_store_menu.putExtra(Constants.KEY_FLOOR_NUMBER, floor_num);
                    to_store_menu.putExtra(Constants.KEY_ADDRESS_TYPE, addr_type);
                    to_store_menu.putExtra(Constants.KEY_DELIVERY_PHONE, delivery_phone);
                    startActivity(to_store_menu);
                }
            } else {
                addr_type = "1";
                deliver_st = street.getText().toString();
                deliver_area = area.getText().toString();
                deliver_city = city.getText().toString();
                deliver_zip = zipcode.getText().toString();
                to_store_menu.putExtra(Constants.KEY_DELIVERY_STREET, deliver_st);
                to_store_menu.putExtra(Constants.KEY_DELIVERY_AREA, deliver_area);
                to_store_menu.putExtra(Constants.KEY_DELIVERY_CITY, deliver_city);
                to_store_menu.putExtra(Constants.KEY_DELIVERY_STATE, deliver_state);
                to_store_menu.putExtra(Constants.KEY_DELIVERY_COUNTRY, deliver_country);
                to_store_menu.putExtra(Constants.KEY_DELIVERY_POSTAL, deliver_zip);
                to_store_menu.putExtra(Constants.KEY_ADDRESS_TYPE, addr_type);
                to_store_menu.putExtra(Constants.KEY_DELIVERY_PHONE, delivery_phone);
                to_store_menu.putExtra(Constants.KEY_NAME, order_name);
                startActivity(to_store_menu);
            }
        }
    }

    private void validate_fields() {
        if (mob_number.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter mobile number in (Deliver Address) and Enter the address", Toast.LENGTH_SHORT).show();
        } else if (radio_address.getCheckedRadioButtonId() == -1) {
            //empty selection
            Toast.makeText(getApplicationContext(), "Please select the address to deliver", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "All done", Toast.LENGTH_SHORT).show();
        }
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Buy & Delivery");
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edit_manual_name.getEditableText()) {
            input_manual_name.setError(null);
        } else if (editable == edit_manual_street.getEditableText()) {
            input_manual_street.setError(null);
        } else if (editable == edit_manual_floor.getEditableText()) {
            input_manual_floor.setError(null);
        } else if (editable == edit_manual_pincode.getEditableText()) {
            input_manual_pincode.setError(null);
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
}
