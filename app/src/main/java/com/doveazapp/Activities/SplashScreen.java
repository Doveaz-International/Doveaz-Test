package com.doveazapp.Activities;

/**
 * Created by Karthik on 2016/01/14.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;
import com.doveazapp.Utils.Utils;

public class SplashScreen extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;
    LinearLayout connection_layout;
    Button button_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        connection_layout = (LinearLayout) findViewById(R.id.connection_layout);
        button_retry = (Button) findViewById(R.id.button_retry);
        getSupportActionBar().hide();
        if (Utils.isOnline(SplashScreen.this)) {

        /*
        * HANDLER 2 SEC DELAY
        * */
            new Handler().postDelayed(new Runnable() {

                // Using handler with postDelayed called runnable run method

                @Override
                public void run() {

                    session.checkLogin();

                /*Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);*/

                    // close this activity
                    finish();
                }
            }, 2 * 1000); // wait for 5 seconds
        } else {
            connection_layout.setVisibility(View.VISIBLE);
            button_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(getIntent());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
