package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
 * PartnerDetailActivity.java
 * Created by Karthik on 11/25/2015.
 */
public class PartnerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_view_profile;

    private Handler mHandler = new Handler();

    private ProgressBar progBar;

    private TextView text, txt_category, text_itemdesc, text_collectionaddress, text_deliveraddr, text_date;

    String userId, serviceId, risk_score, service_b_id;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    ProfileService profileService = null;

    String risk_score_fromapi = null;

    NetworkImageView img_profile, item_img;

    TextView txt_name, text_tipprice, txt_type, text_fee;

    Button btn_engage, btn_decline;

    //For GCM
    ShareExternalServer appUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_partner_desc);

        menuvisibilityinAlldevices();

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Partner Details Activity");
        appUtil = new ShareExternalServer();
        //btn_negotiate = (Button) findViewById(R.id.btn_negotiate);
        btn_engage = (Button) findViewById(R.id.btn_engage);
        btn_decline = (Button) findViewById(R.id.btn_decline);
        //btn_negotiate.setOnClickListener(this);
        btn_engage.setOnClickListener(this);
        btn_decline.setOnClickListener(this);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();

        // profile image
        img_profile = (NetworkImageView) findViewById(R.id.img_check_frnd);
        item_img = (NetworkImageView) findViewById(R.id.item_img);

        // textviews
        txt_name = (TextView) findViewById(R.id.txt_check_name);
        //txt_creditvalues = (TextView) findViewById(R.id.txt_check_credits_values);
        text_itemdesc = (TextView) findViewById(R.id.text_itemdesc);
        text_collectionaddress = (TextView) findViewById(R.id.text_collectionaddress);
        text_deliveraddr = (TextView) findViewById(R.id.text_deliveraddr);
        text_date = (TextView) findViewById(R.id.text_date);
        txt_type = (TextView) findViewById(R.id.txt_type);
        text_tipprice = (TextView) findViewById(R.id.text_tipprice);
        text_fee = (TextView) findViewById(R.id.text_fee);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getString(Constants.KEY_USERID);
        serviceId = bundle.getString(Constants.KEY_SERVICEID);
        risk_score = bundle.getString(Constants.KEY_RISKSCORE);
        service_b_id = bundle.getString(Constants.KEY_SERVICE_B_ID);


        progBar = (ProgressBar) findViewById(R.id.progress_checkrisk);
        text = (TextView) findViewById(R.id.prog_risk);

        button_view_profile = (Button) findViewById(R.id.button_view_pro);
        button_view_profile.setOnClickListener(this);
       /* button_engage = (Button) findViewById(R.id.btn_engage);
        button_engage.setOnClickListener(this);*/

        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        loadPercentage();
        callAPI_togetDetails();
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(PartnerDetailActivity.this);
    }


    private void loadPercentage() {

    }

    private void callAPI_togetDetails() {
        progressDialog = ProgressDialog.show(PartnerDetailActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE OF DETAILS--", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONObject profile_obj = value_obj.getJSONObject("profile");
                    JSONObject service_obj = value_obj.getJSONObject("service_details");
                    String dist_from_pickup = value_obj.getString("distace_from_pickup");
                    String dist_from_delivery = value_obj.getString("distace_from_delivery");
                    risk_score_fromapi = value_obj.getString("risk_score");
                    String earnings = value_obj.getString("total_eranings");
                    progressDialog.dismiss();
                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
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
                        //profileService.setProfile_pic(profile_obj.getString("profile_pic"));
                        profileService.setProfile_pic_url(profile_obj.getString("profile_pic_url"));
                        profileService.setStatus(profile_obj.getString("status"));
                        profileService.setApi_token(profile_obj.getString("api_token"));

                        // for service details
                        profileService.setId(service_obj.getString("id"));
                        profileService.setService_userid(service_obj.getString("userid"));
                        profileService.setService_type(service_obj.getString("service_type"));
                        profileService.setFee(service_obj.getString("fee"));
                        profileService.setItem_short_description(service_obj.getString("item_short_description"));
                        profileService.setCredits(service_obj.getString("credits"));
                        profileService.setPick_up_address(service_obj.getString("pick_up_address"));
                        profileService.setPick_up_city(service_obj.getString("pick_up_city"));
                        profileService.setPick_up_state(service_obj.getString("pick_up_state"));
                        profileService.setPick_up_country(service_obj.getString("pick_up_country"));
                        profileService.setPick_up_postalcode(service_obj.getString("pick_up_postalcode"));
                        profileService.setDelivery_address(service_obj.getString("delivery_address"));
                        profileService.setDelivery_city(service_obj.getString("delivery_city"));
                        profileService.setDelivery_state(service_obj.getString("delivery_state"));
                        profileService.setDelivery_country(service_obj.getString("delivery_country"));
                        profileService.setDelivery_postalcode(service_obj.getString("delivery_postalcode"));
                        profileService.setImage_url(service_obj.getString("image_url"));
                        profileService.setDate(service_obj.getString("date"));
                        profileService.setValue(service_obj.getString("value"));

                        txt_name.setText(profileService.getFullname().toString());
                        img_profile.setImageUrl(profileService.getProfile_pic_url(), imageLoader);
                        item_img.setImageUrl(profileService.getImage_url(), imageLoader);
                        //txt_creditvalues.setText(earnings.toString());
                        text_itemdesc.setText(profileService.getItem_short_description().toString());
                        text_date.setText(profileService.getDate());
                        text_collectionaddress.setText(profileService.getPick_up_address() + "," +
                                profileService.getPick_up_city() + "," + profileService.getPick_up_state() + "," + profileService.getPick_up_postalcode());
                        text_deliveraddr.setText(profileService.getDelivery_address() + "," +
                                profileService.getDelivery_city() + "," + profileService.getDelivery_state() + "," + profileService.getDelivery_postalcode());
                        if (profileService.getValue() != null)
                            text_tipprice.setText(profileService.getValue());
                        if (profileService.getService_type().equals("A")) {
                            txt_type.setText("Buy & Deliver");
                        } else if (profileService.getService_type().equals("B")) {
                            txt_type.setText("Collection");
                        } else if (profileService.getService_type().equals("C")) {
                            txt_type.setText("send");
                        }
                        text_fee.setText(profileService.getFee());

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
        Log.v("Calling API", Constants.GET_USERDETAILS_FROM_LIST);
        Log.v("jnvdfjbubgugbubv", "ifuvub");
        ServiceCalls.CallAPI_togetpartnerDetails(this, Request.Method.POST, Constants.GET_USERDETAILS_FROM_LIST, listener, userId, serviceId, service_b_id, risk_score, api_token);
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
    public void onClick(View view) {
        /*if (view == button_engage) {
            Intent to_congrats = new Intent(getApplicationContext(), CongratsFinalActivity.class);
            startActivity(to_congrats);
        }*/
        if (view == btn_engage) {
            callAPI_toEngage();
        }
        if (view == btn_decline) {
            callAPI_toDecline();
        }
       /* if (view == btn_negotiate) {
            Intent to_edit = new Intent(getApplicationContext(), EditDescription.class);
            to_edit.putExtra(Constants.KEY_USERID, userId);
            to_edit.putExtra(Constants.KEY_SERVICEID, serviceId);
            to_edit.putExtra(Constants.KEY_SERVICE_B_ID, service_b_id);
            to_edit.putExtra(Constants.KEY_COLLECTION_ADDRESS, profileService.getPick_up_address());
            to_edit.putExtra(Constants.KEY_COLLECTION_CITY, profileService.getPick_up_city());
            to_edit.putExtra(Constants.KEY_COLLECTION_COUNTRY, profileService.getPick_up_country());
            to_edit.putExtra(Constants.KEY_COLLECTION_STATE, profileService.getPick_up_state());
            to_edit.putExtra(Constants.KEY_COLLECTION_POSTAL, profileService.getPick_up_postalcode());
            to_edit.putExtra(Constants.KEY_DELIVERY_ADDRESS, profileService.getDelivery_address());
            to_edit.putExtra(Constants.KEY_DELIVERY_CITY, profileService.getDelivery_city());
            to_edit.putExtra(Constants.KEY_DELIVERY_COUNTRY, profileService.getDelivery_country());
            to_edit.putExtra(Constants.KEY_DELIVERY_STATE, profileService.getDelivery_state());
            to_edit.putExtra(Constants.KEY_DELIVERY_POSTAL, profileService.getDelivery_postalcode());
            to_edit.putExtra(Constants.KEY_TIP_FEE, profileService.getCredits());
            to_edit.putExtra("ProfileService", profileService);
            startActivity(to_edit);
        }*/
        if (view == button_view_profile) {
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
    }

    private void callAPI_toDecline() {
        progressDialog = ProgressDialog.show(PartnerDetailActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT DECLINE--", response.toString());
                progressDialog.dismiss();
                System.out.println(response.toString());
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        //}
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
        ServiceCalls.CallAPI_togetEngagement(this, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, service_b_id, api_token);
    }

    private void callAPI_toEngage() {
        progressDialog = ProgressDialog.show(PartnerDetailActivity.this, "Please wait ...", "Loading...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                progressDialog.dismiss();
                Log.v("--OUTPUT ENGAGE--", response.toString());
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        Intent to_credit = new Intent(getApplicationContext(), FriendsMenuPartnerActivity.class);
                        startActivity(to_credit);
                        // }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        String type = "ENGAGE";
        ServiceCalls.CallAPI_togetEngagement(this, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, service_b_id, api_token);
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
        MyApplication.getInstance().trackScreenView("Partner Details Activity");
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
