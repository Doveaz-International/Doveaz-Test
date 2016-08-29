package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.ProfileService;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * CheckPartnerDetailsActivity.java
 * Created by Karthik on 11/24/2015.
 */
public class CheckPartnerDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progBar;

    private TextView text;

    private Handler mHandler = new Handler();

    private int mProgressStatus = 0;

    NetworkImageView img_profile;

    TextView txt_name, txt_distancepickup, txt_distancedelivery, txt_traveldate, txt_fee;

    Button button_view_profile, button_engage, btn_decline;

    String userId, serviceId, risk_score, service_b_id;

    SessionManager session;

    String risk_score_fromapi = null;

    //For GCM
    ShareExternalServer appUtil;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    ProfileService profileService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_friend_details);

        menuvisibilityinAlldevices();

        appUtil = new ShareExternalServer();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Check Partner Details Activity");

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        // profile image
        img_profile = (NetworkImageView) findViewById(R.id.img_check_frnd);

        // textviews
        txt_name = (TextView) findViewById(R.id.txt_check_name);
        //txt_creditvalues = (TextView) findViewById(R.id.txt_check_credits_values);
        txt_distancepickup = (TextView) findViewById(R.id.txt_check_miles);
        txt_distancedelivery = (TextView) findViewById(R.id.txt_check_mile);
        txt_traveldate = (TextView) findViewById(R.id.txt_check_date);
        txt_fee = (TextView) findViewById(R.id.txt_fee);

        //Buttons
        button_view_profile = (Button) findViewById(R.id.button_view_pro);
        button_engage = (Button) findViewById(R.id.btn_engage);
        btn_decline = (Button) findViewById(R.id.btn_decline);

        progBar = (ProgressBar) findViewById(R.id.progress_checkrisk);
        text = (TextView) findViewById(R.id.prog_risk);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // token
        String name = user.get(SessionManager.KEY_APITOKEN);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //Toast.makeText(getApplicationContext(), name + email, Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getString(Constants.KEY_USERID);
        serviceId = bundle.getString(Constants.KEY_SERVICEID);
        risk_score = bundle.getString(Constants.KEY_RISKSCORE);
        service_b_id = bundle.getString(Constants.KEY_SERVICE_B_ID);

        button_view_profile.setOnClickListener(this);
        button_engage.setOnClickListener(this);
        btn_decline.setOnClickListener(this);

        dosomething();
        callAPI_togetDetails();
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CheckPartnerDetailsActivity.this);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void callAPI_togetDetails() {
        progressDialog = ProgressDialog.show(CheckPartnerDetailsActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE OF DETAILS--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString(Constants.KEY_STATUS);
                    final String value = obj.getString(Constants.KEY_VALUE);
                    JSONObject value_obj = obj.getJSONObject(Constants.KEY_VALUE);
                    JSONObject profile_obj = value_obj.getJSONObject("profile");
                    JSONObject service_obj = value_obj.getJSONObject("service_details");
                    String dist_from_pickup = value_obj.getString("distace_from_pickup");
                    String dist_from_delivery = value_obj.getString("distace_from_delivery");
                    risk_score_fromapi = value_obj.getString("risk_score");
                    String earnings = value_obj.getString("total_eranings");
                    progressDialog.dismiss();
                    if (status.equals(Constants.KEY_FALSE)) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals(Constants.KEY_TRUE)) {
                        progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        // calling Profile service POJO to fill data
                        profileService = new ProfileService();

                        //for profile details
                        profileService.setRisk_score(risk_score_fromapi);
                        profileService.setUserid(profile_obj.getString("userid"));
                        profileService.setEmail(profile_obj.getString("email"));
                        profileService.setFullname(profile_obj.getString("fullname"));
                        profileService.setLastname(profile_obj.getString("lastname"));
                        profileService.setGender(profile_obj.getString("gender"));
                        profileService.setDob(profile_obj.getString("dob"));
                        profileService.setCountry(profile_obj.getString("country"));
                        profileService.setPresentaddress(profile_obj.getString("presentaddress"));
                        profileService.setArea(profile_obj.getString("area"));
                        profileService.setStreetaddress(profile_obj.getString("streetaddress"));
                        profileService.setPartner(profile_obj.getString("partner"));
                        /*profileService.setIdproof1(profile_obj.getString("idproof1"));
                        profileService.setIdproof2(profile_obj.getString("idproof2"));*/
                        profileService.setNationality(profile_obj.getString("nationality"));
                        profileService.setState(profile_obj.getString("state"));
                        profileService.setEducation(profile_obj.getString("education"));
                        profileService.setProfession(profile_obj.getString("profession"));
                        profileService.setCity(profile_obj.getString("city"));
                        profileService.setPostalcode(profile_obj.getString("postalcode"));
                        profileService.setCountry_code(profile_obj.getString("country_code"));
                        profileService.setPhone(profile_obj.getString("phone"));
                        // profileService.setProfile_pic(profile_obj.getString("profile_pic"));
                        profileService.setProfile_pic_url(profile_obj.getString("profile_pic_url"));
                        profileService.setStatus(profile_obj.getString("status"));
                        profileService.setApi_token(profile_obj.getString("api_token"));

                        // for service details
                        profileService.setId(service_obj.getString("id"));
                        profileService.setService_userid(service_obj.getString("userid"));
                        profileService.setService_type(service_obj.getString("service_type"));
                        profileService.setWhere_you_based(service_obj.getString("where_you_based"));
                        profileService.setTravel_date(service_obj.getString("travel_date"));
                        profileService.setOrigin_address(service_obj.getString("origin_address"));
                        profileService.setOrigin_city(service_obj.getString("origin_city"));
                        profileService.setOrigin_state(service_obj.getString("origin_state"));
                        profileService.setOrigin_country(service_obj.getString("origin_country"));
                        profileService.setOrigin_postalcode(service_obj.getString("origin_postalcode"));
                        profileService.setDestination_address(service_obj.getString("destination_address"));
                        profileService.setDestination_city(service_obj.getString("destination_city"));
                        profileService.setDestination_state(service_obj.getString("destination_state"));
                        profileService.setDestination_country(service_obj.getString("destination_country"));
                        profileService.setDestination_postalcode(service_obj.getString("destination_postalcode"));
                        profileService.setFee(service_obj.getString("fee"));
                        profileService.setOffer(service_obj.getString("offer"));

                        txt_distancepickup.setText(dist_from_pickup.toString());
                        txt_distancedelivery.setText(dist_from_delivery.toString());
                        txt_traveldate.setText(profileService.getTravel_date());
                        txt_name.setText(profileService.getFullname().toString());
                        //txt_creditvalues.setText(earnings.toString());
                        txt_fee.setText(profileService.getFee());

                        /*String profile_imge = profileService.getProfile_pic();

                        Bitmap bmp = StringToBitMap(profile_imge);
                        //CircleImageUtil graphicUtil = new CircleImageUtil();
                        img_profile.setImageBitmap(bmp);*/
                        /*Bitmap bm = BitmapFactory.decodeFile(profileService.getProfile_pic_url());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();*/
                        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);*/


                        img_profile.setImageUrl(profileService.getProfile_pic_url(), imageLoader);

                        new Thread(new Runnable() {
                            public void run() {
                                final int presentage = 0;
                                // while (mProgressStatus < 63) {
                                // mProgressStatus += 1;
                                // Update the progress bar
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        progBar.setProgress((int) Double.parseDouble(risk_score_fromapi));
                                        text.setText("" + risk_score_fromapi + "%");
                                    }
                                });
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //   }
                            }
                        }).start();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        /*if (risk_score == null) {
            Log.v("risk_score","null");
            ServiceCalls.CallAPI_togetbdDetails(this, Request.Method.POST, Constants.GET_USERDETAILS_FROM_LIST, listener, userId, serviceId, api_token);
        } else if (risk_score != null) {*/
        ServiceCalls.CallAPI_togetbduserDetails(this, Request.Method.POST, Constants.GET_USERDETAILS_FROM_LIST, listener, userId, serviceId, risk_score, api_token);
        //}

    }

    public void dosomething() {

       /* new Thread(new Runnable() {
            public void run() {
                final int presentage = 0;
                // while (mProgressStatus < 63) {
                // mProgressStatus += 1;
                // Update the progress bar
                mHandler.post(new Runnable() {
                    public void run() {
                        progBar.setProgress((int) Double.parseDouble(profileService.getRisk_score()));
                        text.setText("" + profileService.getRisk_score() + "%");
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //   }
            }
        }).start();*/
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
    public void onClick(View v) {
        if (v == button_view_profile) {
            Log.v("RISKKK", String.valueOf(profileService.getRisk_score()));
            Intent to_profile = new Intent(getApplicationContext(), DetailedProfileActivity.class);
            to_profile.putExtra(Constants.KEY_PROFILEPIC, profileService.getProfile_pic_url());
            to_profile.putExtra(Constants.KEY_FULLNAME, profileService.getFullname());
            to_profile.putExtra(Constants.KEY_DOB, profileService.getDob());
            to_profile.putExtra(Constants.KEY_GENDER, profileService.getGender());
            to_profile.putExtra(Constants.KEY_EDUCATION, profileService.getEducation());
            to_profile.putExtra(Constants.KEY_PROFESSION, profileService.getProfession());
            to_profile.putExtra(Constants.KEY_NATIONALITY, profileService.getNationality());
            to_profile.putExtra(Constants.KEY_PHONE, profileService.getPhone());
            to_profile.putExtra(Constants.KEY_COUNTRY, profileService.getCountry());
            to_profile.putExtra(Constants.KEY_CITY, profileService.getCity());
            to_profile.putExtra(Constants.KEY_STREET, profileService.getStreetaddress());
            to_profile.putExtra(Constants.KEY_POSTALCODE, profileService.getPostalcode());
            to_profile.putExtra(Constants.KEY_RISKSCORE, profileService.getRisk_score());
            to_profile.putExtra("ProfileService", profileService);

            startActivity(to_profile);
        }
        if (v == button_engage) {
            callAPI_toEngagePartner();
        }
        if (v == btn_decline) {
            callAPI_toDecline();
        }
    }

    private void callAPI_toDecline() {
        progressDialog = ProgressDialog.show(CheckPartnerDetailsActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT DECLINE--", response.toString());
                progressDialog.dismiss();
                // Toast.makeText(CheckPartnerDetailsActivity.this, response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    //JSONArray userarray = obj.getJSONArray("value");

                    //for (int i = 0; i < userarray.length(); i++) {
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        Intent to_list = new Intent(getApplicationContext(), FriendsMenuPartnerActivity.class);
                        startActivity(to_list);
                        // }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                    progressDialog.dismiss();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String type = "DECLINE";
        ServiceCalls.CallAPI_togetPartnerEngagement(this, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
    }

    private void callAPI_toEngagePartner() {
        progressDialog = ProgressDialog.show(CheckPartnerDetailsActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT ENGAGE--", response.toString());
                //  Toast.makeText(CheckPartnerDetailsActivity.this, response, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    //JSONArray userarray = obj.getJSONArray("value");

                    //for (int i = 0; i < userarray.length(); i++) {
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> user = session.getUserDetails();
                        // token
                        String u_name = user.get(SessionManager.KEY_USERNAME);
                        sendMessageToGCMAppServer(userId, u_name + " " + "has engaged for your service");
                        Intent to_list = new Intent(getApplicationContext(), FriendsMenuBuyDeliverActivity.class);
                        startActivity(to_list);
                    }
                    // }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                    progressDialog.dismiss();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String type = "ENGAGE";
        ServiceCalls.CallAPI_togetPartnerEngagement(this, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
    }

    private void sendMessageToGCMAppServer(final String toUserId,
                                           final String messageToSend) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                String result = appUtil.sendMessage(toUserId, messageToSend, api_token);
                Log.d("MainActivity", "Result: " + result);
                return result;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("MainActivity", "Result: " + msg);
                /*Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
                        .show();*/
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Check Partner Details Activity");
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
