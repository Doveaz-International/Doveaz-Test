package com.doveazapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.UserTransaction;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Karthik on 2016/03/28.
 */
public class SearchTransactionActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    SessionManager session;

    //Layouts
    LinearLayout img_name_layout;

    //network img
    NetworkImageView img_check_frnd;

    TextView txt_check_name;

    Button button_call, button_search;

    TextInputLayout input_layout_transactionid;

    EditText edit_transactionid;

    //Progress bar
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;

    private static String[] PERMISSIONS_PHONECALL = {Manifest.permission.CALL_PHONE};

    private String mobile_number;

    private String edit_transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_transaction_id_activity);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        //layout init
        img_name_layout = (LinearLayout) findViewById(R.id.img_name_layout);
        //button_layout = (LinearLayout) findViewById(R.id.button_layout);

        //image
        img_check_frnd = (NetworkImageView) findViewById(R.id.img_check_frnd);
        txt_check_name = (TextView) findViewById(R.id.txt_check_name);

        //button
        button_call = (Button) findViewById(R.id.button_call);
        button_search = (Button) findViewById(R.id.button_search);

        /*
        * INPUT LAYOUT*/
        input_layout_transactionid = (TextInputLayout) findViewById(R.id.input_layout_transactionid);
        edit_transactionid = (EditText) findViewById(R.id.edit_transactionid);

        /*
        * Listeners*/
        edit_transactionid.addTextChangedListener(this);
        button_call.setOnClickListener(this);
        button_search.setOnClickListener(this);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();

        img_name_layout.setVisibility(View.GONE);
        //button_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == button_search) {
            validate();
        }
        if (v == button_call) {
            Call_phone();
        }
    }

    private void Call_phone() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mobile_number));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Call_phone();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validate() {
        edit_transaction = edit_transactionid.getText().toString();

        if (edit_transaction.equals("")) {
            input_layout_transactionid.setError("Please enter the transaction Id");
        } else {
            callAPI_togetTran_details();
        }
    }

    /*
    * Calling Api to get the transaction details-- need to parse image, name and phone number*/
    private void callAPI_togetTran_details() {
        progressDialog = ProgressDialog.show(SearchTransactionActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);

        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT SEARCH TRANSACTION--", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");

                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONArray user_details_array = value_obj.getJSONArray(Constants.KEY_USER_DETAILS);

                    for (int i = 0; i < user_details_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                            img_name_layout.setVisibility(View.VISIBLE);
                            // button_layout.setVisibility(View.VISIBLE);

                            UserTransaction userDetails = new UserTransaction();
                            JSONObject jsonobject = user_details_array.getJSONObject(i);

                            userDetails.setFullname(jsonobject.getString(Constants.KEY_FULLNAME));
                            userDetails.setProfile_pic(jsonobject.getString(Constants.KEY_PROFILE_PICTURE));
                            userDetails.setPhone(jsonobject.getString(Constants.KEY_PHONE));

                            txt_check_name.setText(userDetails.getFullname());
                            img_check_frnd.setImageUrl(userDetails.getProfile_pic(), imageLoader);

                            mobile_number = userDetails.getPhone();
                        }
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
        Log.d("Calling API", Constants.GET_TRANSACTION_DETAILS);
        ServiceCalls.CallAPI_to_getTransactionDetails(this, Request.Method.POST, Constants.GET_TRANSACTION_DETAILS, listener, edit_transaction, api_token);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edit_transactionid.getEditableText()) {
            input_layout_transactionid.setError(null);
        }
    }
}
