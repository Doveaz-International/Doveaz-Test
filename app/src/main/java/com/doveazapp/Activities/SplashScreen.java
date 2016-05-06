package com.doveazapp.Activities;

/**
 * Created by Karthik on 2016/01/14.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

public class SplashScreen extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        getSupportActionBar().hide();

/*// METHOD 1

        *//****** Create Thread that will sleep for 5 seconds *************//*
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),FirstScreen.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();*/

//METHOD 2

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
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
