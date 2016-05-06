package com.doveazapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.doveazapp.Constants;
import com.doveazapp.R;

/**
 * Created by Karthik on 2016/03/15.
 */
public class RegisterAsActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_partner, button_deliver;

    String partner = "partner";

    String deliver = "deliver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_as_layout);

        button_partner = (Button) findViewById(R.id.button_partner);
        button_deliver = (Button) findViewById(R.id.button_deliver);

        button_partner.setOnClickListener(this);
        button_deliver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (button_partner == v) {
            Intent new_user = new Intent(getApplicationContext(), NewUserActivity.class);
            new_user.putExtra(Constants.KEY_PARTNER, "1");
            startActivity(new_user);
        }
        if (button_deliver == v) {
            Intent new_user = new Intent(getApplicationContext(), NewUserActivity.class);
            new_user.putExtra(Constants.KEY_PARTNER, "0");
            startActivity(new_user);
        }
    }
}
