package com.doveazapp.Activities;

import android.app.ProgressDialog;
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
import com.android.volley.RetryPolicy;
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
 * Created by Karthik on 2015/12/19.
 */
public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText editText_phone, editText_password, editText_Cpassword;

    TextInputLayout input_phone, input_pass, input_cpass;

    Button reset_password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_forgotpassword);

        editText_phone = (EditText) findViewById(R.id.edit_mobilenum);
        editText_password = (EditText) findViewById(R.id.edit_password);
        editText_Cpassword = (EditText) findViewById(R.id.edit_Cpassword);

        input_phone = (TextInputLayout) findViewById(R.id.input_layout_mob);
        input_pass = (TextInputLayout) findViewById(R.id.input_layout_password);
        input_cpass = (TextInputLayout) findViewById(R.id.input_layout_Cpassword);

        reset_password = (Button) findViewById(R.id.button_reset);

        //Listeners
        reset_password.setOnClickListener(this);
        editText_phone.addTextChangedListener(this);
        editText_password.addTextChangedListener(this);
        editText_Cpassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == reset_password) {
            validateEdittexts();
        }

    }

    private void validateEdittexts() {
        final String phonenumber = editText_phone.getText().toString();
        final String password = editText_password.getText().toString();
        final String cpassword = editText_Cpassword.getText().toString();

        if (phonenumber.equals("") && phonenumber.length() <= 10) {
            input_phone.setError("Please enter 10 digit mobile number");
        } else if (password.equals("") && password.length() <= 8) {
            input_pass.setError("Minimum 8 characters");
        } else if (!cpassword.equals(password)) {
            input_cpass.setError("Passwords does not match");
        } else {
            CallResetPasswordAPI();
        }
    }

    private void CallResetPasswordAPI() {
        progressDialog = ProgressDialog.show(ResetPasswordActivity.this, "Please wait ...", "Loading", true);
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FORGOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(ResetPasswordActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.v("==OTP success==", response);
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
                                Toast.makeText(getApplicationContext(), "Password reset successful please try login now ", Toast.LENGTH_SHORT).show();
                                Intent to_login = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(to_login);
                            }
                        } catch (JSONException exception) {
                            progressDialog.dismiss();
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ResetPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.v("==OTP Failed==", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_PHONE, editText_phone.getText().toString().trim());
                params.put(Constants.KEY_PASSWORD, editText_password.getText().toString().trim());
                params.put(Constants.KEY_CONFIRM_PASSWORD, editText_Cpassword.getText().toString().trim());
                Log.v("__INPUT PARAMS__", params.toString());
                return params;
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == editText_phone.getEditableText()) {
            input_phone.setError(null);
        } else if (editable == editText_password.getEditableText()) {
            input_pass.setError(null);
        } else if (editable == editText_Cpassword.getEditableText()) {
            input_cpass.setError(null);
        }
    }
}