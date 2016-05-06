package com.doveazapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.doveazapp.Constants;
import com.doveazapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karthik on 12/13/2015.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Button btn_next, btn_send;

    EditText edit_cc, edit_mob;

    static EditText edit_passcode;

    TextInputLayout input_layout_country_code, input_layout_mob, input_layout_passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);


        //button init
        btn_next = (Button) findViewById(R.id.button_next);
        btn_send = (Button) findViewById(R.id.button_send);

        //edittext init
        edit_cc = (EditText) findViewById(R.id.edit_country_code);
        edit_mob = (EditText) findViewById(R.id.edit_mobilenum);
        edit_passcode = (EditText) findViewById(R.id.edit_passcode);

        //textinputlayout init for validation
        input_layout_country_code = (TextInputLayout) findViewById(R.id.input_layout_country_code);
        input_layout_mob = (TextInputLayout) findViewById(R.id.input_layout_mob);
        input_layout_passcode = (TextInputLayout) findViewById(R.id.input_layout_passcode);

        //Hide passcode field
        edit_passcode.setVisibility(View.GONE);
        input_layout_passcode.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);

        // Listners
        btn_next.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        edit_cc.addTextChangedListener(this);
        edit_mob.addTextChangedListener(this);
        edit_passcode.addTextChangedListener(this);
    }

    public static void recivedSms(String message) {
        try {
            edit_passcode.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btn_send) {
            validateMobile();
        }
        if (view == btn_next) {
            verifyOTPandRegister();
        }
    }

    private void validateMobile() {
        final String phonenumber = edit_mob.getText().toString();
        final String cc = edit_cc.getText().toString();
        if (!isValidMobile(phonenumber)) {
            input_layout_mob.setError("Please enter 10 digit mobile number");
        }
        if (cc.equals("")) {
            input_layout_mob.setError("Please fill in the appropriate fields");
            input_layout_country_code.setError(" ");
        } else {
            getOTP();
            /*Intent to_register = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(to_register);*/
        }
    }

    private void getOTP() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.v("==OTP success==", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            if (status.equals("false")) {
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                //Hide passcode field
                                edit_passcode.setVisibility(View.VISIBLE);
                                input_layout_passcode.setVisibility(View.VISIBLE);
                                btn_next.setVisibility(View.VISIBLE);
                                btn_send.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.v("==OTP Failed==", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_PHONE, edit_mob.getText().toString());
                params.put(Constants.KEY_COUNTRY_CODE, "0" + edit_cc.getText().toString());
                params.put(Constants.NUMBER_EXIST, "1");
                Log.v("INPUT PARAMS", params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void verifyOTPandRegister() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OTP_VERIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.v("==OTP success==", response);
                        // want to put condition here
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String status = obj.getString("status");
                            final String value = obj.getString("value");

                            if (status.equals("false")) {
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                                Intent to_reset = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                                startActivity(to_reset);
                            }
                        } catch (JSONException exception) {
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.v("==OTP Failed==", error.toString());
                        Intent to_register = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(to_register);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_PHONE, edit_mob.getText().toString());
                params.put(Constants.KEY_OTP, edit_passcode.getText().toString());
                params.put(Constants.KEY_COUNTRY_CODE, "0" + edit_cc.getText().toString());
                Log.v("INPUT PARAMS", params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edit_cc.getEditableText()) {
            input_layout_country_code.setError(null);
        } else if (editable == edit_mob.getEditableText()) {
            input_layout_mob.setError(null);
        }
    }
}
