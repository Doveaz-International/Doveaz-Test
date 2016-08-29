package com.doveazapp.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.Dialogs.DatePickerFragment;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.CircleImageUtil;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Karthik on 11/17/2015.
 */
public class RegisterActivity extends AppCompatActivity implements OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int PERMISSIONS_REQUEST_CAMERA1 = 99;
    private static final int PERMISSIONS_REQUEST_CAMERA2 = 98;
    // keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    // keep track of cropping intent
    final int PIC_CROP = 2;
    //without CROP
    final int CAMERA_NO_CROP = 3;
    //without CROP2 for proof
    final int CAMERA_NO_CROP2 = 4;
    //Edittext
    EditText editText_fullname, editText_usrname, editText_pass,
            editText_email, editText_street, editText_postal;
    //spinners
    Spinner spinner_gender, spinner_university, spinner_employer, spinner_nationality;
    Spinner spinner_country, spin_state_bd, spin_city_bd;
    //textLayouts
    TextInputLayout input_fullname, input_username, input_password, input_email,
            input_streetaddress, input_postalcode;
    //Img Buttons
    ImageButton captureBtn;
    //Img views
    ImageView picView;
    //Buttons
    Button btn_done, btn_pickdate;
    String full_name, username, password, email, street, postal, gender, university, profession, nationality, country, city, state;
    Bitmap thePic;
    //Progress dialog
    ProgressDialog progressDialog;
    TextView text_cc, text_mob, text_tc;
    String CC_fromnewuser, mobile_numfromnewuser, user_type;
    String be_a_partner = null;
    CheckBox check_termsandcond;

    ArrayList<HashMap<String, String>> stateArrayList = null;

    ArrayList<HashMap<String, String>> cityArrayList = null;

    HashMap<String, String> stateList;

    HashMap<String, String> cityList;

    SimpleAdapter stateAdapPROJ, cityAdapPROJ;


    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                Toast.makeText(getApplicationContext(), "18+ age only allowed", Toast.LENGTH_SHORT).show();
            }
            /*Toast.makeText(
                    RegisterActivity.this,
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
    // captured picture uri
    private Uri picUri;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Register Activity");

        // retrieve a reference to the UI button
        captureBtn = (ImageButton) findViewById(R.id.picture);
        /*Proof1 = (ImageButton) findViewById(R.id.proof1);
        Proof2 = (ImageButton) findViewById(R.id.proof2);*/

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.user_img_upload);

        CircleImageUtil graphicUtil = new CircleImageUtil();
        //picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
        captureBtn.setImageBitmap(graphicUtil.getCircleBitmap(bm, 16));
        // handle button click
        captureBtn.setOnClickListener(this);
        /*Proof1.setOnClickListener(this);
        Proof2.setOnClickListener(this);*/

        // input layout for validation
        input_fullname = (TextInputLayout) findViewById(R.id.input_fullname);
        input_username = (TextInputLayout) findViewById(R.id.input_username);
        input_password = (TextInputLayout) findViewById(R.id.input_password);
        input_email = (TextInputLayout) findViewById(R.id.input_email);
        //input_presentaddr = (TextInputLayout) findViewById(R.id.input_presentaddr);
        input_streetaddress = (TextInputLayout) findViewById(R.id.input_streetaddress);
        input_postalcode = (TextInputLayout) findViewById(R.id.input_postalcode);
        text_tc = (TextView) findViewById(R.id.text_tc);
        check_termsandcond = (CheckBox) findViewById(R.id.check_termsandcond);

        //editText init
        editText_fullname = (EditText) findViewById(R.id.edit_fullname);
        editText_usrname = (EditText) findViewById(R.id.edit_username);
        editText_pass = (EditText) findViewById(R.id.edit_password_reg);
        editText_email = (EditText) findViewById(R.id.edit_email);
        //editText_presentaddr = (EditText) findViewById(R.id.edit_presentaddr);
        editText_street = (EditText) findViewById(R.id.edit_streetaddress);
        editText_postal = (EditText) findViewById(R.id.edit_postalcode);

        //spinner init
        /*spinner_gender = (Spinner) findViewById(R.id.spin_gender);
        spinner_university = (Spinner) findViewById(R.id.spin_university);
        spinner_employer = (Spinner) findViewById(R.id.spin_employer);
        spinner_nationality = (Spinner) findViewById(R.id.spin_nationality);*/
        spinner_country = (Spinner) findViewById(R.id.spin_country);
        spin_state_bd = (Spinner) findViewById(R.id.spin_state_bd);
        spin_city_bd = (Spinner) findViewById(R.id.spin_city_bd);

        //button init
        btn_done = (Button) findViewById(R.id.button_done);
        btn_pickdate = (Button) findViewById(R.id.datepick);

        //Listeners
        btn_pickdate.setOnClickListener(this);
        btn_done.setOnClickListener(this);

        //for edittext
        editText_fullname.addTextChangedListener(this);
        editText_usrname.addTextChangedListener(this);
        editText_pass.addTextChangedListener(this);
        editText_email.addTextChangedListener(this);
        //editText_presentaddr.addTextChangedListener(this);
        editText_street.addTextChangedListener(this);
        editText_postal.addTextChangedListener(this);

        //for spinners
        /*spinner_gender.setOnItemSelectedListener(this);
        spinner_university.setOnItemSelectedListener(this);
        spinner_employer.setOnItemSelectedListener(this);
        spinner_nationality.setOnItemSelectedListener(this);
        spinner_country.setOnItemSelectedListener(this);*/
        text_tc.setOnClickListener(this);

        //ImageView
        picView = (ImageView) findViewById(R.id.picture);
        /*proof_pic1 = (ImageView) findViewById(R.id.proof1);
        proof_pic2 = (ImageView) findViewById(R.id.proof2);*/

        //checkbox
        //check_bepartner = (CheckBox) findViewById(R.id.check_bepartner);

        //TextView
        text_cc = (TextView) findViewById(R.id.text_cc);
        text_mob = (TextView) findViewById(R.id.text_mobilenum);

        // Intent intent = getIntent();
        Intent intent = getIntent();
        CC_fromnewuser = intent.getStringExtra("cc");
        mobile_numfromnewuser = intent.getStringExtra("mob");
        user_type = intent.getStringExtra(Constants.KEY_PARTNER);

        if (CC_fromnewuser != null && mobile_numfromnewuser != null) {
            /*text_cc.setText(CC_fromnewuser);
            text_mob.setText(mobile_numfromnewuser);*/
        }
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

        LoadSpinners();
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

                            stateAdapPROJ = new SimpleAdapter(RegisterActivity.this, stateArrayList, R.layout.spinner_item,
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

                            cityAdapPROJ = new SimpleAdapter(RegisterActivity.this, cityArrayList, R.layout.spinner_item,
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

    /**
     * Load spinner adapters for gender, education, profession, nationality, country
     */
    private void LoadSpinners() {
        // Spinner for gender
        /*ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);

        //Spinner for Education
        ArrayAdapter<CharSequence> adapter_education = ArrayAdapter.createFromResource(this,
                R.array.Education, android.R.layout.simple_spinner_item);
        adapter_education.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_university.setAdapter(adapter_education);

        //spinner adapter for profession
        ArrayAdapter<CharSequence> adapter_profession = ArrayAdapter.createFromResource(this,
                R.array.Profession, android.R.layout.simple_spinner_item);
        adapter_profession.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_employer.setAdapter(adapter_profession);

        //spinner adapter for nationality
        ArrayAdapter<CharSequence> adapter_nationality = ArrayAdapter.createFromResource(this,
                R.array.Nationality, android.R.layout.simple_spinner_item);
        adapter_nationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nationality.setAdapter(adapter_nationality);*/

        //spinner adapter for country
        ArrayAdapter<CharSequence> adapter_country = ArrayAdapter.createFromResource(this,
                R.array.Countries, android.R.layout.simple_spinner_item);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter_country);
    }

    /**
     * Click method to handle user pressing button to launch camera
     */
    public void onClick(View v) {
        if (v == captureBtn) {
            cameraAction();
        }
        /*if (v == Proof1) {
            cameraAction1();
        }
        if (v == Proof2) {
            cameraAction2();
        }*/
        if (v == btn_done) {
            validateForm();
        }
        if (v == btn_pickdate) {
            showDatePicker();
        }
        if (v == text_tc) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view1 = inflater.inflate(R.layout.terms_conditions, null, false);

            TextView textview = (TextView) view1.findViewById(R.id.textmsg);
            textview.setText(R.string.term_new);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("TERMS AND CONDITIONS");
            alertDialog.setView(view1);
            alertDialog.setPositiveButton("ACCEPT", null);
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    private void cameraAction2() {
        try {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA2);
            } else {
                // use standard intent to capture an image
                Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_NO_CROP2);
            }

        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void cameraAction1() {
        try {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA1);
            } else {
                // use standard intent to capture an image
                Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_NO_CROP);
            }
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void cameraAction() {
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
                cameraAction();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_CAMERA1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraAction1();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_CAMERA2) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraAction2();
            }
        } else {
            Toast.makeText(this, "Until you grant the permission, we cannot open the camera", Toast.LENGTH_SHORT).show();
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

    private void validateForm() {
        //gettext for validation and request
        full_name = editText_fullname.getText().toString();
        username = editText_usrname.getText().toString();
        password = editText_pass.getText().toString();
        email = editText_email.getText().toString();
        //present_address = editText_presentaddr.getText().toString();
        street = editText_street.getText().toString();
        postal = editText_postal.getText().toString();
       /* gender = spinner_gender.getSelectedItem().toString().trim();
        university = spinner_university.getSelectedItem().toString().trim();
        profession = spinner_employer.getSelectedItem().toString().trim();
        nationality = spinner_nationality.getSelectedItem().toString().trim();*/
        country = spinner_country.getSelectedItem().toString().trim();

        if (full_name.equals("") && username.equals("") && password.equals("") && email.equals("") && street.equals("") && postal.equals("")) {

            input_fullname.setError("You should enter fullname");
            input_username.setError("You should enter username");
            input_password.setError("Password Required");
            input_email.setError("Email required");
            //input_presentaddr.setError("Present Address is required");
            input_streetaddress.setError("Street Address is required");
            input_postalcode.setError("Postal code is required");
        } else if (!isValidEmail(email)) {
            input_email.setError("Please enter valid email ID");
        } /*else if (gender.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select the gender", Toast.LENGTH_LONG).show();
        } else if (university.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select your Education", Toast.LENGTH_LONG).show();
        } else if (profession.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select your profession", Toast.LENGTH_LONG).show();
        } else if (nationality.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select your nationality", Toast.LENGTH_LONG).show();
        }*/ else if (country.equals("--Select--")) {
            Toast.makeText(getApplicationContext(), "Please select your country", Toast.LENGTH_LONG).show();
        } else if (!check_termsandcond.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please accept the Terms & Conditions", Toast.LENGTH_LONG).show();
        } else {
            //callSmartyStreetForStreetValidation();
            CallSignUpAPI();
            // Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT).show();
        }
    }

    private void CallSignUpAPI() {
        progressDialog = ProgressDialog.show(RegisterActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE REGISTER--", response);
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
                        Toast.makeText(getApplicationContext(), "Registration Successful... Please Login...", Toast.LENGTH_SHORT).show();
                        Intent Login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(Login);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        String fullname = editText_fullname.getText().toString().trim();
        String username = editText_usrname.getText().toString().trim();
        String password = editText_pass.getText().toString().trim();
        String email = editText_email.getText().toString().trim();
        //String pressent_address = editText_presentaddr.getText().toString().trim();
        String street_address = editText_street.getText().toString().trim();
        String postal_code = editText_postal.getText().toString().trim();
       /* String gender = spinner_gender.getSelectedItem().toString().trim();
        String edu = spinner_university.getSelectedItem().toString().trim();
        String profession = spinner_employer.getSelectedItem().toString().trim();
        String nationality = spinner_nationality.getSelectedItem().toString().trim();*/
        String country = spinner_country.getSelectedItem().toString().trim();
        city = spin_city_bd.getSelectedItem().toString();
        city = city.replace("city_name=", "");
        city = city.replaceAll("[\\[\\](){}]", "");
        state = spin_state_bd.getSelectedItem().toString();
        state = state.replace("state_name=", "");
        state = state.replaceAll("[\\[\\](){}]", "");
        //String area = editText_area.getText().toString().trim();
        String date = btn_pickdate.getText().toString();

        //String be_a_partner = null;

        Log.v("MOB TEST", mobile_numfromnewuser);
        Log.v("CC TEST", CC_fromnewuser);

        /* if (check_bepartner.isChecked() == true) {
        be_a_partner = "1";
    } else if (check_bepartner.isChecked() == false) {
        be_a_partner = "0";
    }*/
        if (user_type.equals(Constants.KEY_TYPE_PARTNER)) {
            be_a_partner = "1";
        } else {
            be_a_partner = "0";
        }
        String img_proof1 = null;
        String img_proof2 = null;
        String img_profile = null;

        /*if (proof1 != null) {
            img_proof1 = getStringImage(proof1);
        } else {
            Toast.makeText(getApplicationContext(), "Please select proof from gallery or from camera", Toast.LENGTH_LONG).show();
            return;
        }

        if (proof2 != null) {
            img_proof2 = getStringImage(proof2);
        } else {
            Toast.makeText(getApplicationContext(), "Please select Image from gallery or from camera", Toast.LENGTH_LONG).show();
            return;
        }*/

        /*ServiceCalls.Call_api_toRegister(this, Request.Method.POST, Constants.REGISTER_URL, listener, fullname, username,
                email, password, gender, date, state, city, "area", country, street_address, be_a_partner,
                nationality, edu, profession, "presentaddress", img_proof1, img_proof2, postal_code, mobile_numfromnewuser, CC_fromnewuser, img_profile);*/

        if (thePic != null) {
            img_profile = getStringImage(thePic);
            ServiceCalls.Call_api_toRegister(this, Request.Method.POST, Constants.REGISTER_URL, listener, fullname, username,
                    email, password, date, state, city, "area", country, street_address, be_a_partner,
                    "presentaddress", postal_code, mobile_numfromnewuser, CC_fromnewuser, img_profile);
        } else {
            ServiceCalls.Call_api_toRegister1(this, Request.Method.POST, Constants.REGISTER_URL, listener, fullname, username,
                    email, password, date, state, city, "area", country, street_address, be_a_partner,
                    "presentaddress", postal_code, mobile_numfromnewuser, CC_fromnewuser);
        }

    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     * Handle user returning from both capturing and cropping the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // user is returning from capturing an image using the camera
            if (requestCode == CAMERA_NO_CROP) {
                // get the Uri for the captured image
                picUri = data.getData();

                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
               /* proof2 = extras.getParcelable("data");

                proof_pic1.setImageBitmap(proof2);*/
            }

            if (requestCode == CAMERA_NO_CROP2) {
                // get the Uri for the captured image
                picUri = data.getData();

                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                /*proof1 = extras.getParcelable("data");

                proof_pic2.setImageBitmap(proof1);*/
            }
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image
                picUri = data.getData();
                // carry out the crop operation
                // get the cropped bitmap
                Bundle extras = data.getExtras();
                thePic = extras.getParcelable("data");

                // display the returned cropped image
                CircleImageUtil graphicUtil = new CircleImageUtil();
                picView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
            }
            // user is returning from cropping the image
           /* else if (requestCode == PIC_CROP) {
                // get the returned data
                Bundle extras = data.getExtras();

                //picView.setImageBitmap(graphicUtil.getCircleBitmap(thePic, 16));

               *//* ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = shre.edit();
                edit.putString("profile_pic", encodedImage);
                edit.commit();*//*
            }*/
        }
    }

    /**
     * Helper method to carry out crop operation
     *//*
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image*//*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == editText_fullname.getEditableText()) {
            input_fullname.setError(null);
        } else if (editable == editText_usrname.getEditableText()) {
            input_username.setError(null);
        } else if (editable == editText_email.getEditableText()) {
            input_email.setError(null);
        } else if (editable == editText_pass.getEditableText()) {
            input_password.setError(null);
        } /*else if (editable == editText_presentaddr.getEditableText()) {
            input_presentaddr.setError(null);
        }*/ else if (editable == editText_street.getEditableText()) {
            input_streetaddress.setError(null);
        } else if (editable == editText_postal.getEditableText()) {
            input_postalcode.setError(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Register Activity");

        /*SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
        String previouslyEncodedImage = shre.getString("profile_pic", "");

        if (!previouslyEncodedImage.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            CircleImageUtil graphicUtil = new CircleImageUtil();
            picView.setImageBitmap(graphicUtil.getRoundedShape(bitmap));
        }*/
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
           /* case R.id.spin_gender:
                break;
            case R.id.spin_university:
                break;
            case R.id.spin_employer:
                break;
            case R.id.spin_nationality:
                break;*/
            case R.id.spin_country:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}