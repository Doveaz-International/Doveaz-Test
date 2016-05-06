package com.doveazapp.Activities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Karthik on 2015/12/23.
 */
public class ServiceCalls {
    static StringRequest mStringRequest;


    /* CALCULATE API FOR CREDITS */
    public static void CallAPI_tocalculate(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                           final String fee, final String token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                params.put(Constants.KEY_TIP_FEE, fee);
                Log.v("==INPUT CALCULATION==", params.toString());
                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, token);
                Log.v("==HEADERS CALCULATION==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /*
     *LOGIN API */
    public static void callAPI_toLogin(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                       final String username, final String password, final String gcm_id) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_EMAIL, username);
                params.put(Constants.KEY_PASSWORD, password);
                params.put(Constants.GCM_TOKEN, gcm_id);

                Log.v("--LOGIN INPUTS--", params.toString());
                return params;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    public static void callAPI_togetcategories(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                               final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("Out categories&milelist", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /*
     *SEND OTP API */
    public static void callAPI_toSendOTP(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                         final String mob_num, final String cc) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_PHONE, mob_num);
                params.put(Constants.KEY_COUNTRY_CODE, cc);

                Log.v("--LOGIN INPUTS--", params.toString());
                return params;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /* CALCULATE API FOR SAVE CONTACTS */
    public static void CallAPI_tosaveContact(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                             final String contacts, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_CONTACTS, contacts);
                Log.v("==INPUT FOR SAVE==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /*
    * SENDING TOKEN TO GET FRIENDS MENU
    * */
    public static void callAPI_togetfriendscount(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                 final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==HEADERS CALCULATION==", headers.toString());
                return headers;
            }

        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /* CALCULATE API FOR SAVE CONTACTS */
    public static void CallAPI_togetuserList(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                             final String type, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TYPE, type);
                Log.v("==INPUT FOR LIST==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /* FOR GET USER DETAILS */
    public static void CallAPI_togetbduserDetails(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String userId, final String serviceId, final String riskscore, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                if (riskscore != null) {
                    params.put(Constants.KEY_RISKSCORE, riskscore);
                }
                Log.v("==INPUT FOR DETAILS==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* FOR GET USER DETAILS */
    public static void CallAPI_togetbdDetails(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                              final String userId, final String serviceId, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                //params.put(Constants.KEY_SERVICE_B_ID, service_b_id);
                Log.v("==INPUT FOR DETAILS==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* FOR GET USER DETAILS */
    public static void CallAPI_togetDetails(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                            final String userId, final String serviceId, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                Log.v("==INPUT FOR DETAILS==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /* FOR GET USER DETAILS */
    public static void CallAPI_togetpartnerDetails(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                   final String userId, final String serviceId, final String service_bid, final String riskscore, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                if (riskscore != null) {
                    params.put(Constants.KEY_RISKSCORE, riskscore);
                }
                params.put(Constants.KEY_SERVICE_B_ID, service_bid);
                Log.v("==INPUT FOR DETAILS==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* FOR GET Engagement DELIVERY */
    public static void CallAPI_togetEngagement(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                               final String userId, final String serviceId, final String type, final String service_b_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                params.put(Constants.KEY_TYPE, type);
                params.put(Constants.KEY_SERVICE_B_ID, service_b_id);
                Log.v("==INPUT ENGAGEMENT==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* FOR GET Engagement DELIVERY */
    public static void CallAPI_togetPartnerEngagement(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                      final String userId, final String serviceId, final String type, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                params.put(Constants.KEY_TYPE, type);
                Log.v("==INPUT ENGAGEMENT==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* FOR NEGOTIATE FROM PARTNER TO DELIVER */
    public static void CallAPI_toNegotiate(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                           final String userId, final String serviceId, final String service_b_id,
                                           final String collection_address, final String collection_city, final String collection_state,
                                           final String collection_country, final String collection_postal, final String deliver_address,
                                           final String deliver_city, final String deliver_state, final String deliver_country,
                                           final String deliver_postal, final String fee, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                params.put(Constants.KEY_SERVICE_B_ID, service_b_id);
                params.put(Constants.KEY_NEGOTIATED_COL_ADDRESS, collection_address);
                params.put(Constants.KEY_NEGOTIATED_COL_CITY, collection_city);
                params.put(Constants.KEY_NEGOTIATED_COL_STATE, collection_state);
                params.put(Constants.KEY_NEGOTIATED_COL_COUNTRY, collection_country);
                params.put(Constants.KEY_NEGOTIATED_POSTAL, collection_postal);
                params.put(Constants.KEY_NEGOTIATED_D_ADDRESS, deliver_address);
                params.put(Constants.KEY_NEGOTIATED_D_CITY, deliver_city);
                params.put(Constants.KEY_NEGOTIATED_D_STATE, deliver_state);
                params.put(Constants.KEY_NEGOTIATED_D_COUNTRY, deliver_country);
                params.put(Constants.KEY_NEGOTIATED_D_POSTAL, deliver_postal);
                params.put(Constants.KEY_NEGOTIATED_FEE, fee);
                Log.v("==INPUT ENGAGEMENT==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /*
    * GET RESPONSE LIST*/
    public static void CallAPI_togetResponse(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                             final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==HEADERS RESPONSE==", headers.toString());
                return headers;
            }

        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* GET NEGOTIATION ACCEPT for partner*/
    public static void CallAPI_NegotiationAccept_forpartner(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                            final String userId, final String serviceId, final String service_b_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                params.put(Constants.KEY_SERVICE_B_ID, service_b_id);
                Log.v("==INPUT ENGAGEMENT==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* GET NEGOTIATION ACCEPT for deliver*/
    public static void CallAPI_NegotiationAccept_fordeliver(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                            final String userId, final String serviceId, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_SERVICEID, serviceId);
                Log.v("==INPUT ENGAGEMENT==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* TRANSFER CREDITS API*/
    public static void CallAPI_to_transfer_credit(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String total_credits, final String reference_id, final String service_type, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TOTAL_CREDITS, total_credits);
                params.put(Constants.KEY_REFERENCE, reference_id);
                params.put(Constants.KEY_SERVICE_TYPE, service_type);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* CHECK THE USER CURRENT CREDITS*/
    public static void CallAPI_to_check_credits(final Context context, final int method, final String url, final OnRequestCompletedListener listener, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                Log.v("INPUT CHECK CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* BUY PURCHASE CREDITS*/
    public static void CallAPI_to_purchase_credits(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                   final String total_credits, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TOTAL_CREDITS, total_credits);

                Log.v("INPUT PURCHASE CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /*CHECK ADMIN BALANCE*/
    public static void CallAPI_to_check_admin_balance(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                      final String total_credits, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TOTAL_CREDITS, total_credits);

                Log.v("INPUT PURCHASE CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /* MILESTONE UPLOAD IMAGES */
    public static void CallAPI_to_uploadmilestones(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                   final String stage, final String reference_id, final String image, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_STAGE, stage);
                params.put(Constants.KEY_REFERENCE_ID, reference_id);
                params.put(Constants.KEY_ITEM_UPLOAD, image);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* MILESTONE UPLOAD INVOICE */
    public static void CallAPI_to_uploadInvoice(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                final String stage, final String reference_id, final String image, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_STAGE, stage);
                params.put(Constants.KEY_REFERENCE_ID, reference_id);
                params.put(Constants.KEY_BILL_UPLOAD, image);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* MILESTONE UPLOAD INVOICE */
    public static void CallAPI_to_uploadInvoiceBC(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String stage, final String reference_id, final String image, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_STAGE, stage);
                params.put(Constants.KEY_REFERENCE_ID, reference_id);
                params.put(Constants.KEY_IMAGE, image);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* MILESTONE3 CHECK STATUS -- PARTNER */
    public static void CalLAPI_to_get_status(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                             final String stage, final String reference_id, final String message, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_STAGE, stage);
                params.put(Constants.KEY_REFERENCE_ID, reference_id);
                params.put(Constants.KEY_MILESTONE_THREE, message);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* MILESTONE VIEW STATUS -- DELIVER */
    public static void CalLAPI_to_milestone_details(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                    final String reference_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_REFERENCE_ID, reference_id);

                Log.v("INPUT MILESTONE", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* APPROVE MILESTONES API --- FOR DELIVER*/
    public static void CallAPI_to_Approve1(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                           final String reference_id, final String stage, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_REFERENCE, reference_id);
                params.put(Constants.KEY_STAGE, stage);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* MILESTONE1 CHECK STATUS -- PARTNER */
    public static void Call_api_to_checkMilestone(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String stage, final String reference_id, final String payment_status, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_STAGE, stage);
                params.put(Constants.KEY_REFERENCE_ID, reference_id);
                params.put(Constants.KEY_PAYMENT_STATUS, payment_status);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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
        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* USER RATING API --- FOR BOTH*/
    public static void CallAPI_to_rateUser(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                           final String reference_id, final String rate, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_REFERENCE, reference_id);
                params.put(Constants.KEY_RATING, rate);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /* APPROVE MILESTONES API --- FOR DELIVER*/
    public static void CallAPI_to_withdrawCredits(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String credits, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_CREDITS, credits);

                Log.v("INPUT WITHDRAW CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    //*******
    // *************************GROUPING*************//
    // *******************//
    //*************************//
    //************************************//

    public static void CallAPI_tocreateGroup(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                             final String group_name, final String group_slogan, final String image, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_GROUP_NAME, group_name);
                params.put(Constants.KEY_GROUP_SLOGAN, group_slogan);
                params.put(Constants.KEY_IMAGE, image);
                Log.v("==INPUT CREATE GROUP==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    public static void CallAPI_tolistcontacts_Groups(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                     final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==HEADERS RESPONSE==", headers.toString());
                return headers;
            }

        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    // ********* ADDING MEMBERS TO GROUP ******************** //
    public static void CallAPI_toAddMembers(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                            final String group_id, final String userid, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_GROUP_ID, group_id);
                params.put(Constants.KEY_USERID, userid);
                Log.v("==INPUT ADD MEMBER==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    //To get group details in Type-A
    public static void CallAPI_toGetGroupDetailsA(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String group_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_GROUP_ID, group_id);

                Log.v("INPUT GROUP DET- A", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    // ENGAGE SERVICE TO GROUP TYPE_A
    public static void CallAPI_service_addtoGroup(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String service_a_id, final String group_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_SERVICE_A_ID, service_a_id);
                params.put(Constants.KEY_GROUP_ID, group_id);

                Log.v("INPUT ADD TO GROUP", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    /****************
     * To get services list
     *****************/
    public static void CallAPI_tolistservice_group(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                   final String group_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_GROUP_ID, group_id);

                Log.v("INPUT SERVICE DET", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }


    public static void CallAPI_togetInfo(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                         final String service_id, final String service_type, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_SERVICEID, service_id);
                params.put(Constants.KEY_SERVICE_TYPE, service_type);

                Log.v("INPUT SERVICE DET", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    //ADD SERVICE FROM GROUP
     /* TRANSFER CREDITS API*/
    public static void CallAPI_to_AddservicefromGroup(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                      final String service_type, final String where_to_buy, final String trip, final String travelon,
                                                      final String returnon, final String country, final String address, final String city, final String state,
                                                      final String postal_code, final String tip, final String offer, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_SERVICETYPE, service_type);
                params.put(Constants.KEY_PARTNER_REGION, where_to_buy);
                params.put(Constants.KEY_TRAVEL_TYPE, trip);
                params.put(Constants.KEY_TRAVEL_DATE, travelon);
                params.put(Constants.KEY_RETURN_DATE, returnon);
                params.put(Constants.KEY_DESTINATION_COUNTRY, country);
                params.put(Constants.KEY_DESTINATION_ADDRESS, address);
                params.put(Constants.KEY_DESTINATION_CITY, city);
                params.put(Constants.KEY_DESTINATION_STATE, state);
                params.put(Constants.KEY_DESTINATION_POSTAL, postal_code);
                params.put(Constants.KEY_TIP_FEE, tip);
                params.put(Constants.KEY_OFFER, offer);

                Log.v("INPUT TRANSFER CREDIT", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    //DELETE MEMBERS FROM GROUP
    public static void CallAPI_toDeleteMembers(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                               final String userId, final String group_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_USERID, userId);
                params.put(Constants.KEY_GROUP_ID, group_id);

                Log.v("INPUT REMOVE", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /*FOR SESSION LOGIN*/
    /* APPROVE MILESTONES API --- FOR DELIVER*/
    public static void CallAPI_to_sessionLogin(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                               final String password, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_PASSWORD, password);

                Log.v("INPUT SESSION LOGIN", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    //*REGISTER*//
    public static void Call_api_toRegister(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                           final String fullname, final String username, final String email,
                                           final String password, final String gender, final String date,
                                           final String state, final String city, final String area,
                                           final String country, final String street_address, final String be_a_partner,
                                           final String nationality, final String edu, final String profession, final String prestent_address,
                                           final String img_proof1, final String img_proof2, final String postal_code,
                                           final String mobile_numfromnewuser, final String CC_fromnewuser, final String img_profile) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_FULLNAME, fullname);
                params.put(Constants.KEY_USERNAME, username);
                params.put(Constants.KEY_EMAIL, email);
                params.put(Constants.KEY_PASSWORD, password);
                params.put(Constants.KEY_GENDER, gender);
                params.put(Constants.KEY_DOB, date);
                params.put(Constants.KEY_STATE, state);
                params.put(Constants.KEY_CITY, city);
                params.put(Constants.KEY_AREA, area);
                params.put(Constants.KEY_COUNTRY, country);
                params.put(Constants.KEY_STREET, street_address);
                params.put(Constants.KEY_PARTNER, be_a_partner);
                params.put(Constants.KEY_NATIONALITY, nationality);
                params.put(Constants.KEY_EDUCATION, edu);
                params.put(Constants.KEY_PROFESSION, profession);
                params.put(Constants.KEY_PRESENTADDRESS, prestent_address);
                params.put(Constants.KEY_ID1, img_proof1);
                params.put(Constants.KEY_ID2, img_proof2);
                params.put(Constants.KEY_POSTALCODE, postal_code);
                params.put(Constants.KEY_PHONE, mobile_numfromnewuser);
                params.put(Constants.KEY_COUNTRY_CODE, CC_fromnewuser);
                params.put(Constants.KEY_PROFILEPIC, img_profile);
                Log.v("==INPUT REGISTER==", params.toString());

                //returning parameters
                return params;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }

    /*
    * GET TRANSACTION DETAILS FOR THE USER*/
    public static void CallAPI_to_getTransactionDetails(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                        final String transaction_id, final String api_token) {
        mStringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                listener.onRequestCompleted(new String(error.toString()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TRANSACTION_ID, transaction_id);

                Log.v("INPUT TRANSACTION DETAILS", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put(Constants.KEY_API_TOKEN, api_token);
                Log.v("==input headers==", headers.toString());
                return headers;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
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

        // Access the RequestQueue through your singleton class.
        VolleyProvider.getQueue(context).add(mStringRequest);
    }
}
