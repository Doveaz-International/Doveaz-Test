package com.doveazapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.AlertDialogs;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.doveazapp.Utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * CollectionActivity.java
 * Created by Karthik on 11/19/2015.
 */
public class CollectionActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final static String TAG = CollectionActivity.class.getName();

    Button btn_ok;

    ImageButton img_button;

    ImageView img_itempic, loc_arrow_img, pick_arrow_img, delivery_arrow_img;

    TextInputLayout input_shortdesc, input_price;

    EditText edit_itemshort_desc;

    ProgressDialog progressDialog;

    Spinner spin_pickcategory, spinner_package_type, spin_state_bd, spin_city_bd, spin_city_manual, spin_area_manual,
            spin_city_manual1, spin_area_manual1, spin_local_manual, spin_local_manual1;

    String short_desc, city_txt, pick_a_category, local_mtext, local_mtext1, pick_address, deliver_address,
            type_of_package, city_mtext, address_type, pick_street, pick_area, pick_city, pick_state, pick_country, pick_zip,
            name_manual, street_manual, floor_manual, postal_manual, deliver_state, deliver_street, delivery_country, deliver_area, deliver_pincode, deliver_city,
            pickup_address_type, name_manual_delivery, street_manual_delivery, floor_manual_delivery, postal_manual_delivery, pick_name, deliver_name;

    SimpleAdapter sAdapPROJ, citymAdapPROJ, areamAdapPROJ, citymAdapPROJ1, areamAdapPROJ1, localAdapPROJ, localAdapPROJ1;

    final int CAMERA_CAPTURE = 1;

    Bitmap item_img;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;

    // captured picture uri
    Uri picUri;

    ArrayList<HashMap<String, String>> jsonArraylist = null;

    HashMap<String, String> proList;

    String id_category, category_name, pickup_phone, delivery_phone;

    LinearLayout item_layout, item_details_layout;

    // Session Manager Class
    SessionManager session;

    EditText edit_mobilenum, edit_mobilenum1;

    LinearLayout deliver_details_layout;
    LinearLayout address_layout;
    LinearLayout manual_address_layout;
    LinearLayout pick_address_layout;
    LinearLayout pickup_details_layout;
    LinearLayout delivery_address_layout;
    LinearLayout del_details_layout;
    LinearLayout delivery_layout;
    LinearLayout address_layout_delivery;
    LinearLayout del_manual_address_layout;
    String fee, order_id, payment_type;


    int total;
    String current_credits;

    //RadioGroups
    RadioGroup radio_address, radio_address_delivery, radio_details, radio_amount_paid;

    // Textviews
    TextView user_type_prime, street, area, city, state, country, zipcode,
            user_type_prime_delivery, street1, area1, city1, state1, country1, zipcode1, name, name1, prompt;

    TextInputLayout input_manual_name, input_manual_street, input_manual_floor, input_manual_pincode,
            del_input_manual_name, input_manual_street1, input_manual_floor1, input_manual_pincode1;

    EditText edit_manual_name, edit_manual_street, edit_manual_floor, edit_manual_pincode,
            edit_manual_street1, edit_manual_floor1, edit_manual_pincode1, edit_manual_name1, edit_price;

    private Animation animShow, animHide;

    // Radiobuttons
    RadioButton home_address, office_address, enter_manually, delivery_home_address, delivery_office_address, delivery_enter_manually,
            radio_by_individual, radio_by_shop, radio_yes, radio_no;

    HashMap<String, String> citymList;
    HashMap<String, String> areamList;
    HashMap<String, String> citymList1;
    HashMap<String, String> areamList1;
    HashMap<String, String> localList;
    HashMap<String, String> localList1;

    private ArrayList<HashMap<String, String>> citymArrayList = null;
    private ArrayList<HashMap<String, String>> areamArrayList = null;
    private ArrayList<HashMap<String, String>> citymArrayList1 = null;
    private ArrayList<HashMap<String, String>> areamArrayList1 = null;
    private ArrayList<HashMap<String, String>> localArrayList = null;
    private ArrayList<HashMap<String, String>> localArrayList1 = null;
    private String price;

    /*//Paypal
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(Constants.CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Doveaz inc")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_activity);

        initAnimation();
        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Buy & delivery");

        // Session class instance
        session = new SessionManager(getApplicationContext());

        img_button = (ImageButton) findViewById(R.id.pick_img);

        img_itempic = (ImageView) findViewById(R.id.pick_img);
        loc_arrow_img = (ImageView) findViewById(R.id.loc_arrow_img);
        pick_arrow_img = (ImageView) findViewById(R.id.pick_arrow_img);
        delivery_arrow_img = (ImageView) findViewById(R.id.delivery_arrow_img);

        //Radiobuttons
        radio_details = (RadioGroup) findViewById(R.id.radio_details);
        radio_by_individual = (RadioButton) findViewById(R.id.radio_by_individual);
        radio_by_shop = (RadioButton) findViewById(R.id.radio_by_shop);
        home_address = (RadioButton) findViewById(R.id.home_address);
        office_address = (RadioButton) findViewById(R.id.office_address);
        enter_manually = (RadioButton) findViewById(R.id.enter_manually);
        delivery_home_address = (RadioButton) findViewById(R.id.delivery_home_address);
        delivery_office_address = (RadioButton) findViewById(R.id.delivery_office_address);
        delivery_enter_manually = (RadioButton) findViewById(R.id.delivery_enter_manually);
        radio_yes = (RadioButton) findViewById(R.id.radio_yes);
        radio_no = (RadioButton) findViewById(R.id.radio_no);

        //img button for ok
        btn_ok = (Button) findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(this);

        user_type_prime = (TextView) findViewById(R.id.user_type_prime);

        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        manual_address_layout = (LinearLayout) findViewById(R.id.manual_address_layout);
        item_layout = (LinearLayout) findViewById(R.id.item_layout);
        item_details_layout = (LinearLayout) findViewById(R.id.item_details_layout);
        pick_address_layout = (LinearLayout) findViewById(R.id.pick_address_layout);
        pickup_details_layout = (LinearLayout) findViewById(R.id.pickup_details_layout);
        delivery_address_layout = (LinearLayout) findViewById(R.id.delivery_address_layout);
        del_details_layout = (LinearLayout) findViewById(R.id.del_details_layout);
        delivery_layout = (LinearLayout) findViewById(R.id.delivery_layout);
        address_layout_delivery = (LinearLayout) findViewById(R.id.address_layout_delivery);
        del_manual_address_layout = (LinearLayout) findViewById(R.id.del_manual_address_layout);

        // Edittext init
        edit_itemshort_desc = (EditText) findViewById(R.id.edit_itemshortdesc);
        edit_mobilenum = (EditText) findViewById(R.id.edit_mobilenum);
        edit_mobilenum1 = (EditText) findViewById(R.id.edit_mobilenum1);

        //TextinputLayout for set error
        input_shortdesc = (TextInputLayout) findViewById(R.id.input_itemshortdesc);

        // TextInput layout
        input_manual_name = (TextInputLayout) findViewById(R.id.input_manual_name);
        input_manual_street = (TextInputLayout) findViewById(R.id.input_manual_street);
        input_manual_floor = (TextInputLayout) findViewById(R.id.input_manual_floor);
        input_manual_pincode = (TextInputLayout) findViewById(R.id.input_manual_pincode);
        del_input_manual_name = (TextInputLayout) findViewById(R.id.del_input_manual_name);
        input_manual_street1 = (TextInputLayout) findViewById(R.id.input_manual_street1);
        input_manual_floor1 = (TextInputLayout) findViewById(R.id.input_manual_floor1);
        input_manual_pincode1 = (TextInputLayout) findViewById(R.id.input_manual_pincode1);
        input_price = (TextInputLayout) findViewById(R.id.input_price);

        edit_manual_name = (EditText) findViewById(R.id.edit_manual_name);
        edit_manual_street = (EditText) findViewById(R.id.edit_manual_street);
        edit_manual_floor = (EditText) findViewById(R.id.edit_manual_floor);
        edit_manual_pincode = (EditText) findViewById(R.id.edit_manual_pincode);
        edit_manual_name1 = (EditText) findViewById(R.id.edit_manual_name1);
        edit_manual_street1 = (EditText) findViewById(R.id.edit_manual_street1);
        edit_manual_floor1 = (EditText) findViewById(R.id.edit_manual_floor1);
        edit_manual_pincode1 = (EditText) findViewById(R.id.edit_manual_pincode1);
        edit_price = (EditText) findViewById(R.id.edit_price);

        // Textviews for address (if prime) will be visible to the prime user
        street = (TextView) findViewById(R.id.street);
        area = (TextView) findViewById(R.id.area);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        country = (TextView) findViewById(R.id.country);
        zipcode = (TextView) findViewById(R.id.zipcode);
        name = (TextView) findViewById(R.id.name);
        user_type_prime_delivery = (TextView) findViewById(R.id.user_type_prime_delivery);
        street1 = (TextView) findViewById(R.id.street1);
        area1 = (TextView) findViewById(R.id.area1);
        city1 = (TextView) findViewById(R.id.city1);
        state1 = (TextView) findViewById(R.id.state1);
        country1 = (TextView) findViewById(R.id.country1);
        zipcode1 = (TextView) findViewById(R.id.zipcode1);
        name1 = (TextView) findViewById(R.id.name1);
        prompt = (TextView) findViewById(R.id.prompt);

        //spinner
        spin_pickcategory = (Spinner) findViewById(R.id.spin_pickcategory);
        spinner_package_type = (Spinner) findViewById(R.id.spinner_package_type);
        spin_state_bd = (Spinner) findViewById(R.id.spin_state_bd);
        spin_city_bd = (Spinner) findViewById(R.id.spin_city_bd);
        spin_city_manual = (Spinner) findViewById(R.id.spin_city_manual);
        spin_area_manual = (Spinner) findViewById(R.id.spin_area_manual);
        spin_city_manual1 = (Spinner) findViewById(R.id.spin_city_manual1);
        spin_area_manual1 = (Spinner) findViewById(R.id.spin_area_manual1);
        spin_local_manual = (Spinner) findViewById(R.id.spin_local_manual);
        spin_local_manual1 = (Spinner) findViewById(R.id.spin_local_manual1);

        deliver_details_layout = (LinearLayout) findViewById(R.id.deliver_details_layout);

        //RadioButtons for address
        radio_address = (RadioGroup) findViewById(R.id.radio_address);
        radio_address_delivery = (RadioGroup) findViewById(R.id.radio_address_delivery);
        radio_amount_paid = (RadioGroup) findViewById(R.id.radio_amount_paid);

        //Listeners
        edit_itemshort_desc.addTextChangedListener(this);
        img_button.setOnClickListener(this);
        edit_mobilenum.setOnClickListener(this);
        item_layout.setOnClickListener(this);
        pick_address_layout.setOnClickListener(this);
        delivery_address_layout.setOnClickListener(this);
        edit_mobilenum1.setOnClickListener(this);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String email = user.get(SessionManager.KEY_EMAIL);

        LoadSpinners();
        LoadCitiestomanual_fromAPI();
        LoadCitiestomanual_fromAPI1();
        LoadCategories_fromAPI();

        spin_pickcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> id1 = jsonArraylist.get(position);
                id_category = id1.get("id");
                category_name = id1.get("category_name");
                //Toast.makeText(getApplicationContext(), id_category, Toast.LENGTH_LONG).show();
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

        spin_area_manual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadLocalsformanual_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_area_manual1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadLocalsformanual_delivery_fromAPI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_city_manual1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Load_Areasformanual_fromAPI1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radio_details.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_by_individual:
                        radio_amount_paid.setVisibility(View.VISIBLE);
                        prompt.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_by_shop:
                        radio_amount_paid.setVisibility(View.VISIBLE);
                        prompt.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        radio_amount_paid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                        input_price.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_no:
                        input_price.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void LoadLocalsformanual_delivery_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    localArrayList1 = new ArrayList<HashMap<String, String>>();
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
                            localList1 = new HashMap<String, String>();
                            localList1.put("street", area_name);

                            localArrayList1.add(localList1);

                            localAdapPROJ1 = new SimpleAdapter(CollectionActivity.this, localArrayList1, R.layout.spinner_item,
                                    new String[]{"id", "street"}, new int[]{R.id.Id, R.id.Name});
                            spin_local_manual1.setAdapter(localAdapPROJ1);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        local_mtext1 = spin_area_manual1.getSelectedItem().toString();
        local_mtext1 = local_mtext1.replace("city=area=", "");
        local_mtext1 = local_mtext1.replaceAll("[\\[\\](){}]", "");

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        local_mtext1 = local_mtext1.replace("area=", "");
        Log.v("TEST1233", local_mtext1);
        ServiceCalls.callAPI_togetlocalforArea(this, Request.Method.POST, Constants.GET_LOCALS, local_mtext1, listener, api_token);
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

                            localAdapPROJ = new SimpleAdapter(CollectionActivity.this, localArrayList, R.layout.spinner_item,
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

    private void LoadCitiestomanual_fromAPI1() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {

                    citymArrayList1 = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray city_arr = value_obj.getJSONArray("cities");

                    for (int i = 0; i < city_arr.length(); i++) {
                        JSONObject json_data = city_arr.getJSONObject(i);
                        String city_name = city_arr.getJSONObject(i).getString("city_name");
                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            citymList1 = new HashMap<String, String>();
                            citymList1.put("city_name", city_name);

                            citymArrayList1.add(citymList1);

                            citymAdapPROJ1 = new SimpleAdapter(CollectionActivity.this, citymArrayList1, R.layout.spinner_item,
                                    new String[]{"id", "city_name"}, new int[]{R.id.Id, R.id.Name});
                            spin_city_manual1.setAdapter(citymAdapPROJ1);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        ServiceCalls.callAPI_togetCity(this, Request.Method.POST, Constants.GET_CITIES, "India", "Karnataka", listener);
    }

    private void Load_Areasformanual_fromAPI1() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    areamArrayList1 = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray city_arr = value_obj.getJSONArray("area");

                    for (int i = 0; i < city_arr.length(); i++) {
                        Log.v("City_array", city_arr.toString());
                        JSONObject json_data = city_arr.getJSONObject(i);
                        Log.i("log_tag", " area" + json_data.getString("area"));

                        String area_name = city_arr.getJSONObject(i).getString("area");

                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            Log.v("area", area_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            areamList1 = new HashMap<String, String>();
                            areamList1.put("area", area_name);

                            areamArrayList1.add(areamList1);

                            areamAdapPROJ1 = new SimpleAdapter(CollectionActivity.this, areamArrayList1, R.layout.spinner_item,
                                    new String[]{"id", "area"}, new int[]{R.id.Id, R.id.Name});
                            spin_area_manual1.setAdapter(areamAdapPROJ1);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        city_txt = spin_city_manual1.getSelectedItem().toString();
        city_txt = city_txt.replace("city_name=", "");
        city_txt = city_txt.replaceAll("[\\[\\](){}]", "");

         /*String api_token = "cade3fa343d595d72803f460c139086d";*/
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetAreafromcity(this, Request.Method.POST, Constants.GET_AREA, city_txt, listener, api_token);
    }

    private void LoadCitiestomanual_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {

                    citymArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray city_arr = value_obj.getJSONArray("cities");

                    for (int i = 0; i < city_arr.length(); i++) {
                        JSONObject json_data = city_arr.getJSONObject(i);
                        String city_name = city_arr.getJSONObject(i).getString("city_name");
                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            citymList = new HashMap<String, String>();
                            citymList.put("city_name", city_name);

                            citymArrayList.add(citymList);

                            citymAdapPROJ = new SimpleAdapter(CollectionActivity.this, citymArrayList, R.layout.spinner_item,
                                    new String[]{"id", "city_name"}, new int[]{R.id.Id, R.id.Name});
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

    private void Load_Areasformanual_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                try {
                    areamArrayList = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray city_arr = value_obj.getJSONArray("area");

                    for (int i = 0; i < city_arr.length(); i++) {
                        Log.v("City_array", city_arr.toString());
                        JSONObject json_data = city_arr.getJSONObject(i);
                        Log.i("log_tag", " area" + json_data.getString("area"));

                        String area_name = city_arr.getJSONObject(i).getString("area");

                        if (status.equals("false")) {
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            Log.v("area", area_name);

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            areamList = new HashMap<String, String>();
                            areamList.put("area", area_name);

                            areamArrayList.add(areamList);

                            areamAdapPROJ = new SimpleAdapter(CollectionActivity.this, areamArrayList, R.layout.spinner_item,
                                    new String[]{"id", "area"}, new int[]{R.id.Id, R.id.Name});
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

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CollectionActivity.this);
    }

    private void LoadCategories_fromAPI() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT LOGIN--", response);
                Log.v("==Categories success==", response);
                // response will be like {"status":"false","value":"Username\/Password Incorrect"}
                try {
                    jsonArraylist = new ArrayList<HashMap<String, String>>();
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray category_array = value_obj.getJSONArray("categories");

                    if (status.equals("false")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        for (int i = 0; i < category_array.length(); i++) {
                            JSONObject json_data = category_array.getJSONObject(i);
                            Log.i("log_tag", "_id" + json_data.getInt("id") +
                                    ", category_name" + json_data.getString("category_name") +
                                    ", order" + json_data.getString("order")
                            );
                            String category_name = category_array.getJSONObject(i).getString("category_name");
                            String category_id = category_array.getJSONObject(i).getString("id");

                            // SEND JSON DATA INTO SPINNER TITLE LIST
                            proList = new HashMap<String, String>();
                            proList.put("id", category_id);
                            proList.put("category_name", category_name);

                            jsonArraylist.add(proList);

                            sAdapPROJ = new SimpleAdapter(CollectionActivity.this, jsonArraylist, R.layout.spinner_item,
                                    new String[]{"id", "category_name"}, new int[]{R.id.Id, R.id.Name});
                            spin_pickcategory.setAdapter(sAdapPROJ);
                        }
                        //Toast.makeText(getApplicationContext(), category_array.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("--JSONARRAY--", category_array.toString());
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        ServiceCalls.callAPI_togetcategories(this, Request.Method.POST, Constants.GET_CATEGORIES, listener, api_token);
    }

    private void LoadSpinners() {
        //Type of package
        Resources resources = getApplicationContext().getResources();
        String[] textString = resources.getStringArray(R.array.type_of_package);
        spinner_package_type.setAdapter(new MyAdapter(CollectionActivity.this, R.layout.type_spinner_row, textString));
    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.type_spinner_row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.txt_package_type);
            Resources resources1 = getApplicationContext().getResources();
            String[] textString1 = resources1.getStringArray(R.array.type_of_package);
            label.setText(textString1[position]);

            TextView sub = (TextView) row.findViewById(R.id.txt_dimn);
            Resources resources2 = getApplicationContext().getResources();
            String[] textString2 = resources2.getStringArray(R.array.package_dimensions);
            sub.setText(textString2[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image_type);
            TypedArray icons;
            Resources res = getResources();
            icons = res.obtainTypedArray(R.array.image_type_array);
            Drawable drawable = icons.getDrawable(position);
            icon.setImageDrawable(drawable);

            return row;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btn_ok) {
            //later it will change to estimate screen
            validateandCheckpartner();
        }
        if (view == img_button) {
            cameraActionToCapture();
        }
        if (view == edit_mobilenum) {
            openPopup_to_entermobilenumber();
        }
        if (view == edit_mobilenum1) {
            openPopup_to_entermobilenumber1();
        }
        if (view == item_layout) {
            if (item_details_layout.getVisibility() == View.VISIBLE) {
                // Its visible
                item_details_layout.setVisibility(View.GONE);
                item_details_layout.startAnimation(animHide);
                loc_arrow_img.setImageResource(R.drawable.down_arrow);
            } else {
                // Either gone or invisible
                item_details_layout.setVisibility(View.VISIBLE);
                item_details_layout.startAnimation(animShow);
                loc_arrow_img.setImageResource(R.drawable.up_arrow);
            }
        }
        if (view == pick_address_layout) {
            if (pickup_details_layout.getVisibility() == View.VISIBLE) {
                pickup_details_layout.setVisibility(View.GONE);
                pickup_details_layout.startAnimation(animHide);
                pick_arrow_img.setImageResource(R.drawable.down_arrow);
            } else {
                // Either gone or invisible
                pickup_details_layout.setVisibility(View.VISIBLE);
                pickup_details_layout.startAnimation(animShow);
                pick_arrow_img.setImageResource(R.drawable.up_arrow);
            }
        }
        if (view == delivery_address_layout) {
            if (delivery_layout.getVisibility() == View.VISIBLE) {
                delivery_layout.setVisibility(View.GONE);
                delivery_layout.startAnimation(animHide);
                delivery_arrow_img.setImageResource(R.drawable.down_arrow);
            } else {
                delivery_layout.setVisibility(View.VISIBLE);
                delivery_layout.startAnimation(animShow);
                delivery_arrow_img.setImageResource(R.drawable.up_arrow);
            }
        }
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

    private void openPopup_to_entermobilenumber() {
/* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(CollectionActivity.this);
        alert.setTitle("Mobile Number");
        alert.setIcon(R.drawable.mobile);
        alert.setMessage("Please enter the sender mobile number");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(CollectionActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String entered_value = input.getEditableText().toString();
                if (!Utils.isOnline(CollectionActivity.this)) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    openPopup_to_entermobilenumber();
                } else if (entered_value.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the mobile number", Toast.LENGTH_LONG).show();
                    openPopup_to_entermobilenumber();
                } else {
                    edit_mobilenum.setText(entered_value);
                    deliver_details_layout.setVisibility(View.VISIBLE);
                    progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("OUTPUT USER", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                final String status = obj.getString("status");
                                final String value = obj.getString("value");
                                JSONObject value_obj = obj.getJSONObject("value");
                                final String user_type = value_obj.getString("user_type");

                                if (status.equals("false")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CollectionActivity.this, value, Toast.LENGTH_SHORT).show();
                                } else if (status.equals("true")) {
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
                                        name.setText(home_name);

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
                                                        name.setText(home_name);
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
                                                        name.setText(ofc_name);
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
                    ServiceCalls.CallAPI_to_GetPrimeinfo(CollectionActivity.this, Request.Method.POST, Constants.GET_PRIME_OR_NOT, listener, entered_value, api_token);
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


    private void openPopup_to_entermobilenumber1() {
/* Alert Dialog Code Start*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(CollectionActivity.this);
        alert.setTitle("Mobile Number");
        alert.setIcon(R.drawable.mobile);
        alert.setMessage("Please enter the receiver mobile number");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(CollectionActivity.this);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String entered_value = input.getEditableText().toString();
                if (!Utils.isOnline(CollectionActivity.this)) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    openPopup_to_entermobilenumber1();
                } else if (entered_value.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the mobile number", Toast.LENGTH_LONG).show();
                    openPopup_to_entermobilenumber1();
                } else {
                    edit_mobilenum1.setText(entered_value);
                    del_details_layout.setVisibility(View.VISIBLE);
                    progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
                    progressDialog.setCancelable(false);
                    OnRequestCompletedListener listener = new OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(String response) {
                            Log.v("OUTPUT USER", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                final String status = obj.getString("status");
                                final String value = obj.getString("value");
                                JSONObject value_obj = obj.getJSONObject("value");
                                final String user_type = value_obj.getString("user_type");

                                if (status.equals("false")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CollectionActivity.this, value, Toast.LENGTH_SHORT).show();
                                } else if (status.equals("true")) {
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

                                        radio_address_delivery.setVisibility(View.VISIBLE);
                                        user_type_prime_delivery.setVisibility(View.VISIBLE);
                                        user_type_prime_delivery.setText("Star user");
                                        delivery_home_address.setChecked(true);
                                        address_layout_delivery.setVisibility(View.VISIBLE);
                                        del_manual_address_layout.setVisibility(View.GONE);
                                        street1.setText(home_street);
                                        area1.setText(home_area);
                                        city1.setText(home_city);
                                        state1.setText(home_state);
                                        country1.setText(home_country);
                                        zipcode1.setText(home_zip);
                                        name1.setText(home_name);

                                        radio_address_delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                switch (checkedId) {
                                                    case R.id.delivery_home_address:
                                                        address_layout_delivery.setVisibility(View.VISIBLE);
                                                        del_manual_address_layout.setVisibility(View.GONE);
                                                        street1.setText(home_street);
                                                        area1.setText(home_area);
                                                        city1.setText(home_city);
                                                        state1.setText(home_state);
                                                        country1.setText(home_country);
                                                        zipcode1.setText(home_zip);
                                                        name1.setText(home_name);
                                                        break;
                                                    case R.id.delivery_office_address:
                                                        del_manual_address_layout.setVisibility(View.GONE);
                                                        address_layout_delivery.setVisibility(View.VISIBLE);
                                                        street1.setText(ofc_street);
                                                        area1.setText(ofc_area);
                                                        city1.setText(ofc_city);
                                                        state1.setText(ofc_state);
                                                        country1.setText(ofc_country);
                                                        zipcode1.setText(ofc_zip);
                                                        name1.setText(ofc_name);
                                                        break;
                                                    case R.id.delivery_enter_manually:
                                                        address_layout_delivery.setVisibility(View.GONE);
                                                        del_manual_address_layout.setVisibility(View.VISIBLE);
                                                        break;
                                                }
                                            }
                                        });
                                    } else {
                                        delivery_enter_manually.setChecked(true);
                                        radio_address_delivery.setVisibility(View.GONE);
                                        address_layout_delivery.setVisibility(View.GONE);
                                        del_manual_address_layout.setVisibility(View.VISIBLE);
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
                    ServiceCalls.CallAPI_to_GetPrimeinfo(CollectionActivity.this, Request.Method.POST, Constants.GET_PRIME_OR_NOT, listener, entered_value, api_token);
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


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void cameraActionToCapture() {
        try {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
            } else {
                // use standard intent to capture an image
                Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                cameraActionToCapture();
            }
        } else {
            Toast.makeText(this, "Until you grant the permission, we cannot open the camera", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image
                picUri = data.getData();
                Bundle extras = data.getExtras();
                item_img = extras.getParcelable("data");
                img_itempic.setImageBitmap(item_img);
            }
        }
        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/

            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                AlertDialogs.showDialogMessage("Payment Success Id : " + paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                AlertDialogs.showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        AlertDialogs.showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                AlertDialogs.showDialogMessage("User returned without login");
            }
        }
    }

    private void validateandCheckpartner() {
        String addr_type;

        short_desc = edit_itemshort_desc.getText().toString();
        type_of_package = spinner_package_type.getSelectedItem().toString();
        name_manual = edit_manual_name.getText().toString();
        street_manual = edit_manual_street.getText().toString();
        floor_manual = edit_manual_floor.getText().toString();
        pick_a_category = spin_pickcategory.getSelectedItem().toString();

        if (short_desc.equals("")) {
            input_shortdesc.setError("Description required");
            Toast.makeText(getApplicationContext(), "Description required", Toast.LENGTH_SHORT).show();
        } /*else if (type_of_package.equals("--select--")) {
            Toast.makeText(getApplicationContext(), "Please select the package type", Toast.LENGTH_LONG).show();
        }*/ else {
            callAPI_to_addorder();
        }
    }

    private void callAPI_to_addorder() {
        String img_item_pick = null;
        if (item_img != null) {
            img_item_pick = getStringImage(item_img);
        } else {
            Toast.makeText(getApplicationContext(), "Please upload an image", Toast.LENGTH_LONG).show();
            img_button.setBackgroundColor(getResources().getColor(R.color.red));
            return;
        }

        if (edit_price.getText().toString().equals("")) {
            price = "0";
        } else {
            price = edit_price.getText().toString();
        }

        type_of_package = spinner_package_type.getSelectedItem().toString();
        pickup_phone = edit_mobilenum.getText().toString();
        pick_street = street.getText().toString();
        pick_area = area.getText().toString();
        pick_city = city.getText().toString();
        pick_state = state.getText().toString();
        pick_country = "India";
        pick_zip = zipcode.getText().toString();
        pick_name = name.getText().toString();
        int res_id = radio_address.getCheckedRadioButtonId();
        if (res_id == -1) {
            Toast.makeText(getApplicationContext(), "Please select the pickup address", Toast.LENGTH_SHORT).show();
            return;
        } else {
            RadioButton radioButton = (RadioButton) findViewById(res_id);
            pickup_address_type = radioButton.getText().toString().trim();
        }

        int res_id1 = radio_address_delivery.getCheckedRadioButtonId();
        if (res_id1 == -1) {
            Toast.makeText(getApplicationContext(), "Please select the delivery address", Toast.LENGTH_SHORT).show();
            return;
        } else {
            RadioButton radioButton1 = (RadioButton) findViewById(res_id1);
            address_type = radioButton1.getText().toString().trim();
        }
        delivery_phone = edit_mobilenum1.getText().toString();
        deliver_street = street1.getText().toString();
        deliver_area = area1.getText().toString();
        deliver_city = city1.getText().toString();
        deliver_state = state1.getText().toString();
        delivery_country = "India";
        deliver_pincode = zipcode1.getText().toString();
        deliver_name = name1.getText().toString();
        String order_type = "2";
        String addr_type_deliver;
        String addr_type_pickup;
        if (address_type.equals("Manual")) {
            addr_type_deliver = "0";
        } else {
            addr_type_deliver = "1";
        }

        if (pickup_address_type.equals("Manual")) {
            addr_type_pickup = "0";
        } else {
            addr_type_pickup = "1";
        }

        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE ADD ORDER--", response);
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        /*{"status":"true","value":{"order_id":1467606460,"fee":1,"total_price":0}}*/
                        // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        JSONObject value_object = obj.getJSONObject("value");
                        fee = value_object.getString("fee");
                        order_id = value_object.getString("order_id");
                        //Toast.makeText(getApplicationContext(), fee.toString(), Toast.LENGTH_SHORT).show();
                        //choose_paymentmethod();
                        //call_checkCreditsapi();
                        choose_paymentmethod();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        if (addr_type_pickup.equals("0") || addr_type_deliver.equals("0")) {
            //for manual
            progressDialog.dismiss();
            Call_API_to_manual_fields();

        } else {
            Log.v("Calling API", Constants.ADD_ORDER);
            ServiceCalls.Call_api_toAddOrderCollection(this, Request.Method.POST, Constants.ADD_ORDER, listener, order_type, img_item_pick,
                    pick_street, pick_area, pick_city, pick_state, pick_country, pick_zip, deliver_street, deliver_area, deliver_city, deliver_state,
                    delivery_country, deliver_pincode, addr_type_deliver, addr_type_pickup, id_category, delivery_phone, pickup_phone, pick_name, deliver_name, price, api_token);
        }
    }

    private void call_checkCreditsapi() {
        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT CHECK CREDIT", response);
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        current_credits = value_obj.getString(Constants.KEY_AVAILABLE_CREDIT);

                        if (Integer.parseInt(current_credits) < Integer.parseInt(fee)) {
                            Toast.makeText(getApplicationContext(), "Your wallet balance is low please purchase credits", Toast.LENGTH_SHORT).show();
                            choose_paymentmethod();
                        } else {
                            call_DispatchAPI(); /*Calling instead of transfer credit api*/
                        }

                        progressDialog.dismiss();
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
        ServiceCalls.CallAPI_to_check_credits(this, Request.Method.POST, Constants.CHECK_USER_CREDITS, listener, api_token);
    }

    @SuppressLint("LongLogTag")
    private void Call_API_to_manual_fields() {
        String img_item_pick = null;
        if (item_img != null) {
            img_item_pick = getStringImage(item_img);
        } else {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            return;
        }

        type_of_package = spinner_package_type.getSelectedItem().toString();
        int res_id = radio_address.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(res_id);
        pickup_address_type = radioButton.getText().toString().trim();
        pickup_phone = edit_mobilenum.getText().toString();

        int res_id1 = radio_address_delivery.getCheckedRadioButtonId();
        RadioButton radioButton1 = (RadioButton) findViewById(res_id1);
        address_type = radioButton1.getText().toString().trim();
        delivery_phone = edit_mobilenum1.getText().toString();

        String order_type = "2";
        String addr_type_deliver;
        String addr_type_pickup;

        if (address_type.equals("Manual")) {
            addr_type_deliver = "0";
        } else {
            addr_type_deliver = "1";
        }

        if (pickup_address_type.equals("Manual")) {
            addr_type_pickup = "0";
        } else {
            addr_type_pickup = "1";
        }

        if (manual_address_layout.getVisibility() == View.VISIBLE) {
            Log.v("Manual address is visible", "dsds");
            name_manual = edit_manual_name.getText().toString();
            pick_address = edit_manual_street.getText().toString();
            pick_street = spin_local_manual.getSelectedItem().toString().replace("street=", "").replaceAll("[\\[\\](){}]", "");
            pick_area = spin_area_manual.getSelectedItem().toString().replace("area=", "").replaceAll("[\\[\\](){}]", "");
            pick_city = spin_city_manual.getSelectedItem().toString().replace("city_name=", "").replaceAll("[\\[\\](){}]", "");
            pick_state = "Karnataka";
            pick_country = "India";
            pick_zip = edit_manual_pincode.getText().toString();
            floor_manual = edit_manual_floor.getText().toString();
        } else {
            Log.v("Manual address is not visible", "dsds");
            pick_street = street.getText().toString();
            pick_area = area.getText().toString();
            pick_city = city.getText().toString();
            pick_state = state.getText().toString();
            pick_country = "India";
            pick_zip = zipcode.getText().toString();
            name_manual = name.getText().toString();
        }

        if (del_manual_address_layout.getVisibility() == View.VISIBLE) {
            Log.v("DEL Manual address is visible", "dsds");
            name_manual_delivery = edit_manual_name1.getText().toString();
            deliver_address = edit_manual_street1.getText().toString();
            deliver_street = spin_local_manual1.getSelectedItem().toString().replace("street=", "").replaceAll("[\\[\\](){}]", "");
            deliver_area = spin_area_manual1.getSelectedItem().toString().replace("area=", "").replaceAll("[\\[\\](){}]", "");
            deliver_city = spin_city_manual1.getSelectedItem().toString().replace("city_name=", "").replaceAll("[\\[\\](){}]", "");
            deliver_state = "Karnataka";
            floor_manual_delivery = edit_manual_floor1.getText().toString();
            delivery_country = "India";
            deliver_pincode = edit_manual_pincode1.getText().toString();
        } else {
            Log.v("DEL Manual address is not visible", "dsds");
            deliver_street = street1.getText().toString();
            deliver_area = area1.getText().toString();
            deliver_city = city1.getText().toString();
            deliver_state = state1.getText().toString();
            delivery_country = "India";
            deliver_pincode = zipcode1.getText().toString();
            name_manual_delivery = name1.getText().toString();
        }

        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE ADD ORDER COLLECTION--", response);
                // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        /*{"status":"true","value":{"order_id":1467883182,"fee":1,"total_price":0}}*/
                        JSONObject value_object = obj.getJSONObject("value");
                        fee = value_object.getString("fee");
                        order_id = value_object.getString("order_id");
                        //Toast.makeText(getApplicationContext(), fee.toString(), Toast.LENGTH_SHORT).show();
                        choose_paymentmethod();
                        // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        if (edit_price.getText().toString().equals("")) {
            price = "0";
        } else {
            price = edit_price.getText().toString();
        }

        if (edit_manual_name.getText().toString().equals("") &&
                edit_manual_street.getText().toString().equals("") && edit_manual_floor.getText().toString().equals("") &&
                edit_manual_pincode.getText().toString().equals("") && edit_manual_name1.getText().toString().equals("") &&
                edit_manual_street1.getText().toString().equals("") && edit_manual_floor1.getText().toString().equals("") &&
                edit_manual_pincode1.getText().toString().equals("")) {

            input_manual_name.setError("Please enter the name");
            input_manual_street.setError("Please enter the street address");
            input_manual_floor.setError("Please enter the floor number");
            input_manual_pincode.setError("Please enter the pincode");
            del_input_manual_name.setError("Please enter the delivery name");
            input_manual_street1.setError("Please enter the delivery street");
            input_manual_floor1.setError("Please enter the delivery floor");
            input_manual_pincode1.setError("Please enter delivery pincode");
            progressDialog.dismiss();
            return;
        } else {
            Log.v("Calling API", Constants.ADD_ORDER);
            ServiceCalls.Call_api_toAddOrderCollectionManual(this, Request.Method.POST, Constants.ADD_ORDER, listener, order_type, img_item_pick,
                    pick_address, pick_street, pick_area, pick_city, pick_state, pick_country, pick_zip, deliver_address, deliver_street, deliver_area, deliver_city, deliver_state,
                    delivery_country, deliver_pincode, addr_type_deliver, addr_type_pickup, id_category, delivery_phone, pickup_phone, price, name_manual, name_manual_delivery, floor_manual,
                    floor_manual_delivery, api_token);
        }
    }


    private void choose_paymentmethod() {
        final CharSequence[] options_payment = {"Cash on delivery - Free", "Paytm (coming soon)"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
        builder.setTitle("Choose a payment option");
        builder.setItems(options_payment, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (options_payment[item].equals("Cash on delivery - Free")) {
                    //alert_InsteadOfPaypal();
                    //call_paypal_sdk();
                    call_cod();
                } else if (options_payment[item].equals("Paytm (coming soon)")) {
                    call_paytm_sdk();
                }
            }
        });
        AlertDialog alert = builder.create();

        alert.show();
    }

    private void call_cod() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(CollectionActivity.this);
        alertbox.setTitle("Confirm Order");
        alertbox.setMessage("your order will be placed once you click yes!!!");
        alertbox.setPositiveButton("Confirm", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        call_transferCreditAPI();
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    private void Call_createOrder_API() {
        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT CREATE ORDER", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        JSONObject value_obj = obj.getJSONObject("value");
                        String order_id = value_obj.getString("order_id");

                        Intent to_loading_screen = new Intent(getApplicationContext(), LoadingAgentActivity.class);
                        to_loading_screen.putExtra(Constants.KEY_ORDER_ID, order_id);
                        startActivity(to_loading_screen);

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
        Log.v("Calling API", Constants.CREATE_ORDER);
        payment_type = "COD";
        ServiceCalls.CallAPI_to_CreateOrder(this, Request.Method.POST, Constants.CREATE_ORDER, listener, order_id, fee, payment_type, api_token);
    }

    private void goto_loading_screen() {

    }

    private void alert_InsteadOfPaypal() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(CollectionActivity.this);
        alertbox.setTitle("Insufficient funds");
        alertbox.setMessage("Please contact support@doveaz.com");
        alertbox.setPositiveButton("ok", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.show();
    }

    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }

    private void call_paytm_sdk() {
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();
        builder.setAmount(Double.valueOf(fee))
                .setTnxId(getTxnId())
                .setPhone(user.get(SessionManager.KEY_PHONE_NUM))
                .setProductName("product_name")
                .setFirstName(user.get(SessionManager.KEY_USERNAME))
                .setEmail(user.get(SessionManager.KEY_EMAIL))
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(true)
                .setKey(Constants.PAY_U_MONEY_KEY)
                .setMerchantId(Constants.PAY_U_MONEY_MERCHANT_ID);// Debug Merchant ID

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();

            /*
             server side call required to calculate hash with the help of <salt>
             <salt> is already shared along with merchant <key>
             serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc

            */

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);

           /*
            testing purpose

            String serverCalculatedHash="9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc";
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneySdkInitilizer.startPaymentActivityForResult(this, paymentParam);
            */
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(CollectionActivity.this, paymentParam);
                        } else {
                            Toast.makeText(CollectionActivity.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(CollectionActivity.this,
                            CollectionActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CollectionActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void CallAPI_to_purchase_credit() {
        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT PURCHASE CREDIT", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        //openAlert_purchase();
                        call_DispatchAPI(); /*Calling instead of openAlert_purchase(); api*/
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
        ServiceCalls.CallAPI_to_purchase_credits(this, Request.Method.POST, Constants.PURCHASE_CREDITS, listener, fee, api_token);

    }

    private void openAlert_purchase() {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(CollectionActivity.this);
        alertbox.setTitle("Confirm Transfer");
        alertbox.setMessage("Do you wish to Transfer credit and goto milestone?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                       /* call_transferCreditAPI();*/

                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    private void call_DispatchAPI() {
        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT DISPATCH", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
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
        ServiceCalls.CallAPI_to_check_credits(this, Request.Method.POST, Constants.DISPATCH_ORDER, listener, api_token);
    }

    private void call_transferCreditAPI() {
        progressDialog = ProgressDialog.show(CollectionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT TRANSFER CREDIT", response);
                //Toast.makeText(AcceptCreditActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    /*{"status":"true","value":{"message":"Your credit has been transfered",
                    "reference_id":"25","credit_holder_id":2}}*/
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_object = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Call_createOrder_API();

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
        Log.v("Calling API", Constants.TRANSFER_CREDITS);
        ServiceCalls.CallAPI_to_transfer_credit(this, Request.Method.POST, Constants.TRANSFER_CREDITS, listener, fee, order_id, api_token);
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edit_itemshort_desc.getEditableText()) {
            input_shortdesc.setError(null);
        }
        if (editable == edit_manual_name.getEditableText()) {
            input_manual_name.setError(null);
        }
        if (editable == edit_manual_street.getEditableText()) {
            input_manual_street.setError(null);
        }
        if (editable == edit_manual_floor.getEditableText()) {
            input_manual_floor.setError(null);
        }
        if (editable == edit_manual_pincode.getEditableText()) {
            input_manual_pincode.setError(null);
        }
        if (editable == edit_manual_name1.getEditableText()) {
            del_input_manual_name.setError(null);
        }
        if (editable == edit_manual_street1.getEditableText()) {
            input_manual_street1.setError(null);
        }
        if (editable == edit_manual_floor1.getEditableText()) {
            input_manual_floor1.setError(null);
        }
        if (editable == edit_manual_pincode1.getEditableText()) {
            input_manual_pincode1.setError(null);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Collection Activity");
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
