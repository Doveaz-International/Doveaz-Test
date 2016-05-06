package com.doveazapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karthik on 2015/12/23.
 */
public class SendActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final static String TAG = SendActivity.class.getName();

    Button btn_ok;

    ImageButton img_button;

    ImageView img_itempic;

    TextInputLayout input_shortdesc,
            input_postalcode, input_streetaddress, input_city, input_state;

    EditText edit_itemshort_desc, edit_postal_code, edit_streetaddress, editText_state, editText_city;

    ProgressDialog progressDialog;

    Spinner spin_pickcategory, spin_country, spinner_package_type;

    String short_desc, street_address, postal_code, country, city, state, pick_a_category, type_of_package;

    SimpleAdapter sAdapPROJ;

    RadioGroup radiogroup;

    final int CAMERA_CAPTURE = 1;

    Bitmap item_img;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    // captured picture uri
    private Uri picUri;
    //ArrayList<String> itemsList = new ArrayList<String>();

    // ArrayList<String> itemsList1 = new ArrayList<String>();

    //HashMap<String,String> category_list = new HashMap<String,String>();

    //List<String> list = new ArrayList<String>();

    ArrayList<HashMap<String, String>> jsonArraylist = null;

    HashMap<String, String> proList;

    String id_category, category_name;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_activity);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Buy & delivery");

        // Session class instance
        session = new SessionManager(getApplicationContext());

        img_button = (ImageButton) findViewById(R.id.pick_img);

        img_itempic = (ImageView) findViewById(R.id.pick_img);

        //img button for ok
        btn_ok = (Button) findViewById(R.id.button_ok);
        btn_ok.setOnClickListener(this);

        // Edittext init
        edit_itemshort_desc = (EditText) findViewById(R.id.edit_itemshortdesc);
        edit_postal_code = (EditText) findViewById(R.id.edit_postalcode_bd);
        edit_streetaddress = (EditText) findViewById(R.id.edit_streetaddress);
        editText_state = (EditText) findViewById(R.id.edit_state);
        editText_city = (EditText) findViewById(R.id.edit_city);


        //TextinputLayout for set error
        input_shortdesc = (TextInputLayout) findViewById(R.id.input_itemshortdesc);
        input_postalcode = (TextInputLayout) findViewById(R.id.input_postalcode_bd);
        input_streetaddress = (TextInputLayout) findViewById(R.id.input_streetaddress);
        input_city = (TextInputLayout) findViewById(R.id.input_city);
        input_state = (TextInputLayout) findViewById(R.id.input_state);

        //spinner
        spin_pickcategory = (Spinner) findViewById(R.id.spin_pickcategory);
        spin_country = (Spinner) findViewById(R.id.spin_country_bd);
        spinner_package_type = (Spinner) findViewById(R.id.spinner_package_type);

        //Radio Button
        radiogroup = (RadioGroup) findViewById(R.id.radioGroup_region);

        //Listeners
        edit_itemshort_desc.addTextChangedListener(this);
        edit_streetaddress.addTextChangedListener(this);
        edit_postal_code.addTextChangedListener(this);
        img_button.setOnClickListener(this);

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Toast.makeText(getApplicationContext(), api_token + email, Toast.LENGTH_LONG).show();

        LoadSpinners();
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

    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(SendActivity.this);
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

                            sAdapPROJ = new SimpleAdapter(SendActivity.this, jsonArraylist, R.layout.spinner_item,
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
        /*// Spinner for category
        ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(BuyandDeliveryActivity.this,
                android.R.layout.simple_spinner_item, itemsList);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_pickcategory.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();*/

        /*ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(BuyandDeliveryActivity.this,
                android.R.layout.simple_spinner_item, itemsList);
        spin_pickcategory.setAdapter(new MyAdapterForCategory(BuyandDeliveryActivity.this, R.layout.category_spinner_row, itemsList1));*/

        //Type of package
        Resources resources = getApplicationContext().getResources();
        String[] textString = resources.getStringArray(R.array.type_of_package);
        spinner_package_type.setAdapter(new MyAdapter(SendActivity.this, R.layout.type_spinner_row, textString));

        //spinner adapter for country
        ArrayAdapter<CharSequence> adapter_country = ArrayAdapter.createFromResource(this,
                R.array.Country, android.R.layout.simple_spinner_item);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_country.setAdapter(adapter_country);

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
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot open the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handle user returning from both capturing and cropping the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                picUri = data.getData();
                Bundle extras = data.getExtras();
                item_img = extras.getParcelable("data");
                img_itempic.setImageBitmap(item_img);
            }
        }
    }

    private void validateandCheckpartner() {
        short_desc = edit_itemshort_desc.getText().toString();
        street_address = edit_streetaddress.getText().toString();
        postal_code = edit_postal_code.getText().toString();
        state = editText_state.getText().toString();
        city = editText_city.getText().toString();
        country = spin_country.getSelectedItem().toString();
        type_of_package = spinner_package_type.getSelectedItem().toString();

        if (short_desc.equals("") && street_address.equals("") && postal_code.equals("")
                && city.equals("") && state.equals("")) {
            input_shortdesc.setError("Item short description required");
            input_postalcode.setError("Postal code is required");
            input_streetaddress.setError("Street address is required");
            input_state.setError("Your state is required");
            input_city.setError("City is required");

        } else if (country.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select the country", Toast.LENGTH_LONG).show();
        } else if (type_of_package.equals("--select--")) {
            Toast.makeText(getApplicationContext(), "Please select the package type", Toast.LENGTH_LONG).show();
        } else {
            callSmartyStreetForStreetValidation();
        }
    }

    private void callSmartyStreetForStreetValidation() {
        progressDialog = ProgressDialog.show(SendActivity.this, "Please wait ...", "Validating Address...", true);
        progressDialog.setCancelable(false);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.SMARTY_STREETS_FOR_VALIDATION + "&street=" + edit_streetaddress.getText().toString() + "&city=" + editText_city.getText().toString() + "&state=" + editText_state.getText().toString() + "&zipcode=" + edit_postal_code.getText().toString() + "&candidates=1";
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
                                Toast.makeText(SendActivity.this, "Invalid Address contact support", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SendActivity.this, "Valid", Toast.LENGTH_LONG).show();
                                goto_DeliveryAddress();
                                progressDialog.dismiss();
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
                        Toast.makeText(SendActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void goto_DeliveryAddress() {

        String img_item_pick = null;
        if (item_img != null) {
            img_item_pick = getStringImage(item_img);
        } else {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            return;
        }


        int res_id = radiogroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(res_id);

        short_desc = edit_itemshort_desc.getText().toString();
        String selected_region = radioButton.getText().toString();
        country = spin_country.getSelectedItem().toString();
        street_address = edit_streetaddress.getText().toString();
        city = editText_city.getText().toString();
        state = editText_state.getText().toString();
        postal_code = edit_postal_code.getText().toString();
        type_of_package = spinner_package_type.getSelectedItem().toString();

        String Address_tolatlong = street_address + "," + city + "," + state + "," + country + "," + postal_code;

        /*Geocoder coder = new Geocoder(this);
        List<Address> address;
        double lat = 0;
        double longi = 0;
        try {
            address = coder.getFromLocationName(Address_tolatlong, 1);
            Address location = address.get(0);
            lat = location.getLatitude();
            longi = location.getLongitude();

        } catch (Exception e) {

        }*/

        /*String lat = null;
        String longi = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(Address_tolatlong, 1);
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
        Log.v("TEST GEOCODE", lat + longi);*/

        Intent to_delivery = new Intent(getApplicationContext(), SendDeliverAddress.class);
        to_delivery.putExtra(Constants.KEY_CATEGORY_NAME, category_name);
        to_delivery.putExtra(Constants.KEY_CATEGORY, id_category);
        to_delivery.putExtra(Constants.KEY_ITEM_SHORT_DESC, short_desc);
        to_delivery.putExtra(Constants.KEY_IMAGE, img_item_pick);
        to_delivery.putExtra(Constants.KEY_REGION, selected_region);
        to_delivery.putExtra(Constants.KEY_PICK_COUNTRY, country);
        to_delivery.putExtra(Constants.KEY_PICK_ADDRESS, street_address);
        to_delivery.putExtra(Constants.KEY_PICK_CITY, city);
        to_delivery.putExtra(Constants.KEY_PICK_STATE, state);
        to_delivery.putExtra(Constants.KEY_PICK_POSTALCODE, postal_code);
        to_delivery.putExtra(Constants.KEY_PACKAGE_TYPE, type_of_package);
        //to_delivery.putExtra("pick_up_lat", lat);
        //to_delivery.putExtra("pick_up_long", longi);
        startActivity(to_delivery);
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
        } else if (editable == edit_streetaddress.getEditableText()) {
            input_streetaddress.setError(null);
        } else if (editable == edit_postal_code.getEditableText()) {
            input_postalcode.setError(null);
        } else if (editable == editText_city.getEditableText()) {
            input_city.setError(null);
        } else if (editable == editText_state.getEditableText()) {
            input_state.setError(null);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("send Activity");
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