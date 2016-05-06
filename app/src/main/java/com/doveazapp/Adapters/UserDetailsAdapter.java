package com.doveazapp.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GcmClasses.ShareExternalServer;
import com.doveazapp.GettersSetters.UserDetails;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthik on 2016/01/05.
 */
public class UserDetailsAdapter extends BaseAdapter {

    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<UserDetails> userDetailsList;

    Context context = null;

    SessionManager session;

    //For GCM
    ShareExternalServer appUtil;

    //progress dialog
    ProgressDialog progressDialog;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public UserDetailsAdapter(Activity activity, List<UserDetails> userDetailsList) {
        this.activity = activity;
        this.userDetailsList = userDetailsList;
    }

    public UserDetailsAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return userDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    /*@Override
    public Object getItem(int position) {
        return null;
    }*/

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.checkpartner_row_activity, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.img_profilepic);

        TextView textName = (TextView) convertView.findViewById(R.id.list_txt_name);
        TextView traveldate = (TextView) convertView.findViewById(R.id.travel_date);
        TextView risk_value = (TextView) convertView.findViewById(R.id.txt_risk);
        ImageButton btnok = (ImageButton) convertView.findViewById(R.id.button_ok_row);
        ImageButton btnadd = (ImageButton) convertView.findViewById(R.id.button_add_row);
        ImageView img_risk = (ImageView) convertView.findViewById(R.id.img_risk);
        TextView userid = (TextView) convertView.findViewById(R.id.userid);
        TextView service_id = (TextView) convertView.findViewById(R.id.service_id);

        final UserDetails m = userDetailsList.get(position);

        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Ok Button Clicked" + position, "**********");
                progressDialog = ProgressDialog.show(activity, "Please wait ...", "Loading...", true);
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
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();
                            } else if (status.equals("true")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, value, Toast.LENGTH_SHORT).show();

                                appUtil = new ShareExternalServer();
                                HashMap<String, String> user = session.getUserDetails();
                                // token
                                String u_name = user.get(SessionManager.KEY_USERNAME);
                                String userId = m.getId();
                                sendMessageToGCMAppServer(userId, u_name + " " + "has engaged for your service");
                            }
                            // }
                        } catch (JSONException exception) {
                            progressDialog.dismiss();
                            Log.e("--JSON EXCEPTION--", exception.toString());
                        }
                    }
                };
                session = new SessionManager(activity);
                HashMap<String, String> user = session.getUserDetails();
                // token
                String api_token = user.get(SessionManager.KEY_APITOKEN);
                String type = "ENGAGE";
                String userId = m.getId();
                String serviceId = m.getService_id();
                ServiceCalls.CallAPI_togetPartnerEngagement(activity, Request.Method.POST, Constants.GET_ENGAGEMENT, listener, userId, serviceId, type, api_token);
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Add Button Clicked" + position, "**********");
                String name = m.getName();
                Log.d("NAME", name);

                ArrayList<ContentProviderOperation> ops =
                        new ArrayList<ContentProviderOperation>();

                int rawContactID = ops.size();

                // to insert a new raw contact in the table ContactsContract.RawContacts
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // to insert display name in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, m.getName())
                        .build());

                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "123456789")
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
                try {
                    // Executing all the insert operations as a single database transaction
                    parent.getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    Log.v("ADD CONTACT", "ADDED SUCCESSFULLY");
                    // Creating an intent to open Android's Contacts List
                    Intent contacts = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);

                    // Starting the activity
                    parent.getContext().startActivity(contacts);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
            }
        });


        userid.setText(m.getId());
        service_id.setText(m.getService_id());
        thumbNail.setImageUrl(m.getProfile_pic(), imageLoader);
        // title
        textName.setText(m.getName());
        // travel_date
        traveldate.setText(String.valueOf(m.getTravel_date()));
        // risk
        risk_value.setText(String.valueOf(m.getRisk_score()));

        double risk_score = Double.parseDouble(m.getRisk_score());
        if (risk_score <= 40) {
            img_risk.setImageResource(R.drawable.risk_no);
        } else if (risk_score > 40 && risk_score <= 65) {
            img_risk.setImageResource(R.drawable.risk_medium);
        } else if (risk_score > 65) {
            img_risk.setImageResource(R.drawable.risk_more);
        }

        /*int risk_score = 24;
        risk_value.setText(String.valueOf(risk_score));

        //double risk_score = Double.parseDouble(m.getRisk_score());
        if (risk_score <= 40) {
            img_risk.setImageResource(R.drawable.risk_no);
        } else if (risk_score > 40 && risk_score <= 65) {
            img_risk.setImageResource(R.drawable.risk_medium);
        } else if (risk_score > 65) {
            img_risk.setImageResource(R.drawable.risk_more);
        }*/

        return convertView;
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
}
