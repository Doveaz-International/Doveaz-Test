package com.doveazapp.Activities;

import android.annotation.SuppressLint;
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

    /*GET STATES FROM API*/
    public static void callAPI_togetStates(final Context context, final int method, final String url,
                                           final String country, final OnRequestCompletedListener listener) {
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
                params.put(Constants.KEY_COUNTRY, country);
                Log.v("==INPUT FOR SAVE==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
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

    /*GET CITIES FROM API*/
    public static void callAPI_togetCity(final Context context, final int method, final String url,
                                         final String country, final String state, final OnRequestCompletedListener listener) {
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
                params.put(Constants.KEY_COUNTRY, country);
                params.put(Constants.KEY_STATE, state);
                Log.v("==INPUT FOR SAVE==", params.toString());

                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
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

    /*GET AREA FROM GIVE CITY API*/
    public static void callAPI_togetAreafromcity(final Context context, final int method, final String url,
                                                 final String city, final OnRequestCompletedListener listener, final String api_token) {
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
                params.put(Constants.KEY_CITY, city);
                Log.v("==INPUT FOR AREA==", params.toString());

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

    /*GET AREA FROM GIVE CITY API*/
    public static void callAPI_togetlocalforArea(final Context context, final int method, final String url,
                                                 final String area, final OnRequestCompletedListener listener, final String api_token) {
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
                params.put(Constants.KEY_AREA, area);
                Log.v("==INPUT FOR AREA==", params.toString());

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

    /* INPUT MOBILE NUMBER - OUTPUT WILL BE PRIME OR NOT AND HOME, OFFICE ADDRESS*/
    public static void CallAPI_to_GetPrimeinfo(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                               final String phone, final String api_token) {
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
                params.put(Constants.KEY_PHONE, phone);

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

    // API REQUEST TO GET THE STORE LIST FROM CATEGORY ID
    public static void callAPI_togetStorefromcategory_id(final Context context, final int method, final String url,
                                                         final String category_id, final OnRequestCompletedListener listener, final String api_token) {
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
                params.put(Constants.KEY_CATEGORY, category_id);
                Log.v("==INPUT FOR STORES==", params.toString());

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
    * API REQUEST FOR CATEGORIES
    * */
    public static void callAPI_togetCategories(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
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

    /* API REQUEST TO GET THE MENU OF A STORE */
    public static void CallAPI_togetStoreMenu(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                              final String store_id, final String api_token) {
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
                params.put(Constants.KEY_STOREID, store_id);
                Log.v("==INPUT FOR MENU==", params.toString());

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
                                                  final String total_credits, final String reference_id, final String api_token) {
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
    public static void Call_api_toRegister(final Context context,
                                           final int method,
                                           final String url,
                                           final OnRequestCompletedListener listener,
                                           final String fullname,
                                           final String username,
                                           final String email,
                                           final String password,
                                           final String date,
                                           final String state,
                                           final String city,
                                           final String area,
                                           final String country,
                                           final String street_address,
                                           final String be_a_partner,
                                           final String prestent_address,
                                           final String postal_code,
                                           final String mobile_numfromnewuser,
                                           final String CC_fromnewuser,
                                           final String img_profile) {
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
                params.put(Constants.KEY_DOB, date);
                params.put(Constants.KEY_STATE, state);
                params.put(Constants.KEY_CITY, city);
                params.put(Constants.KEY_AREA, area);
                params.put(Constants.KEY_COUNTRY, country);
                params.put(Constants.KEY_STREET, street_address);
                params.put(Constants.KEY_PARTNER, be_a_partner);
                params.put(Constants.KEY_PRESENTADDRESS, prestent_address);
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

    public static void Call_api_toRegister1(final Context context,
                                            final int method,
                                            final String url,
                                            final OnRequestCompletedListener listener,
                                            final String fullname,
                                            final String username,
                                            final String email,
                                            final String password,
                                            final String date,
                                            final String state,
                                            final String city,
                                            final String area,
                                            final String country,
                                            final String street_address,
                                            final String be_a_partner,
                                            final String prestent_address,
                                            final String postal_code,
                                            final String mobile_numfromnewuser,
                                            final String CC_fromnewuser) {
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
                params.put(Constants.KEY_DOB, date);
                params.put(Constants.KEY_STATE, state);
                params.put(Constants.KEY_CITY, city);
                params.put(Constants.KEY_AREA, area);
                params.put(Constants.KEY_COUNTRY, country);
                params.put(Constants.KEY_STREET, street_address);
                params.put(Constants.KEY_PARTNER, be_a_partner);
                params.put(Constants.KEY_PRESENTADDRESS, prestent_address);
                params.put(Constants.KEY_POSTALCODE, postal_code);
                params.put(Constants.KEY_PHONE, mobile_numfromnewuser);
                params.put(Constants.KEY_COUNTRY_CODE, CC_fromnewuser);
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
            @SuppressLint("LongLogTag")
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

    public static void CallAPI_to_getDeliveryAddress(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
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
            @SuppressLint("LongLogTag")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_REFERENCE_ID, reference_id);

                Log.v("ADDRESS DETAILS", params.toString());

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

    /* ADD ORDER BUY AND DELIVER*/
    public static void Call_api_toAddOrderBD(final Context context,
                                             final int method,
                                             final String url,
                                             final OnRequestCompletedListener listener,
                                             final String order_type,
                                             final String delivery_address,
                                             final String deliver_st,
                                             final String delivery_area,
                                             final String deliver_city,
                                             final String deliver_state,
                                             final String deliver_country,
                                             final String deliver_zip,
                                             final String address_type,
                                             final String Order_details,
                                             final String store_id,
                                             final String delivery_phone,
                                             final String delivery_name,
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
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TYPE, order_type);
                params.put(Constants.KEY_DELIVERY_ADDRESS, delivery_address);
                params.put(Constants.KEY_DELIVERY_STREET, deliver_st);
                params.put(Constants.KEY_DELIVERY_AREA, delivery_area);
                params.put(Constants.KEY_DELIVERY_CITY, deliver_city);
                params.put(Constants.KEY_DELIVERY_STATE, deliver_state);
                params.put(Constants.KEY_DELIVERY_COUNTRY, deliver_country);
                params.put(Constants.KEY_DELIVERY_ZIP, deliver_zip);
                params.put(Constants.KEY_DELIVERY_ADDRESS_TYPE, address_type);
                params.put(Constants.KEY_ORDER_DETAILS, Order_details);
                params.put(Constants.KEY_STOREID, store_id);
                params.put(Constants.KEY_DELIVERY_PHONE, delivery_phone);
                params.put(Constants.KEY_DELIVERY_NAME, delivery_name);

                Log.v("INPUT B&D ORDER", params.toString());

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

    /*ADD ORDER COLLECTION*/
    public static void Call_api_toAddOrderCollection(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                     final String order_type,
                                                     final String image,
                                                     final String pick_street,
                                                     final String pick_area,
                                                     final String pick_city,
                                                     final String pick_state,
                                                     final String pick_country,
                                                     final String pick_pincode,
                                                     final String deliver_street,
                                                     final String deliver_area,
                                                     final String deliver_city,
                                                     final String deliver_state,
                                                     final String delivery_country,
                                                     final String deliver_pincode,
                                                     final String deliver_address_type,
                                                     final String pickup_address_type,
                                                     final String id_category,
                                                     final String delivery_phone,
                                                     final String pickup_phone,
                                                     final String pickup_name,
                                                     final String delivery_name,
                                                     final String input_price,
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
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TYPE, order_type);
                params.put(Constants.KEY_IMAGE, image);
                params.put(Constants.KEY_PICKUP_STREET, pick_street);
                params.put(Constants.KEY_PICKUP_AREA, pick_area);
                params.put(Constants.KEY_PICKUP_CITY, pick_city);
                params.put(Constants.KEY_PICKUP_STATE, pick_state);
                params.put(Constants.KEY_PICKUP_COUNTRY, pick_country);
                params.put(Constants.KEY_PICKUP_ZIPCODE, pick_pincode);
                params.put(Constants.KEY_DELIVERY_STREET, deliver_street);
                params.put(Constants.KEY_DELIVERY_AREA, deliver_area);
                params.put(Constants.KEY_DELIVERY_CITY, deliver_city);
                params.put(Constants.KEY_DELIVERY_STATE, deliver_state);
                params.put(Constants.KEY_DELIVERY_COUNTRY, delivery_country);
                params.put(Constants.KEY_DELIVERY_ZIP, deliver_pincode);
                params.put(Constants.KEY_DELIVERY_ADDRESS_TYPE, deliver_address_type);
                params.put(Constants.KEY_PICKUP_ADDR_TYPE, pickup_address_type);
                params.put(Constants.KEY_CATEGORY, id_category);
                params.put(Constants.KEY_DELIVERY_PHONE, delivery_phone);
                params.put(Constants.KEY_PICKUP_PHONE, pickup_phone);
                params.put(Constants.KEY_PICKUP_NAME, pickup_name);
                params.put(Constants.KEY_DELIVERY_NAME, delivery_name);
                params.put(Constants.KEY_PRICE, input_price);

                Log.v("INPUT B&D ORDER", params.toString());

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


    public static void Call_api_toAddOrderCollectionManual(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                           final String order_type,
                                                           final String image,
                                                           final String pick_address,
                                                           final String pick_street,
                                                           final String pick_area,
                                                           final String pick_city,
                                                           final String pick_state,
                                                           final String pick_country,
                                                           final String pick_pincode,
                                                           final String deliver_address,
                                                           final String deliver_street,
                                                           final String deliver_area,
                                                           final String deliver_city,
                                                           final String deliver_state,
                                                           final String delivery_country,
                                                           final String deliver_pincode,
                                                           final String deliver_address_type,
                                                           final String pickup_address_type,
                                                           final String id_category,
                                                           final String delivery_phone,
                                                           final String pickup_phone,
                                                           final String input_price,
                                                           final String name_manual,
                                                           final String name_manual_delivery,
                                                           final String floor_manual,
                                                           final String floor_manual_delivery,
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
            @SuppressLint("LongLogTag")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_TYPE, order_type);
                params.put(Constants.KEY_IMAGE, image);
                params.put(Constants.KEY_PICKUP_ADDRESS, pick_address);
                params.put(Constants.KEY_PICKUP_STREET, pick_street);
                params.put(Constants.KEY_PICKUP_AREA, pick_area);
                params.put(Constants.KEY_PICKUP_CITY, pick_city);
                params.put(Constants.KEY_PICKUP_STATE, pick_state);
                params.put(Constants.KEY_PICKUP_COUNTRY, pick_country);
                params.put(Constants.KEY_PICKUP_ZIPCODE, pick_pincode);
                params.put(Constants.KEY_DELIVERY_ADDRESS, deliver_address);
                params.put(Constants.KEY_DELIVERY_STREET, deliver_street);
                params.put(Constants.KEY_DELIVERY_AREA, deliver_area);
                params.put(Constants.KEY_DELIVERY_CITY, deliver_city);
                params.put(Constants.KEY_DELIVERY_STATE, deliver_state);
                params.put(Constants.KEY_DELIVERY_COUNTRY, delivery_country);
                params.put(Constants.KEY_DELIVERY_ZIP, deliver_pincode);
                params.put(Constants.KEY_DELIVERY_ADDRESS_TYPE, deliver_address_type);
                params.put(Constants.KEY_PICKUP_ADDR_TYPE, pickup_address_type);
                params.put(Constants.KEY_CATEGORY, id_category);
                params.put(Constants.KEY_DELIVERY_PHONE, delivery_phone);
                params.put(Constants.KEY_PICKUP_PHONE, pickup_phone);
                params.put(Constants.KEY_PRICE, input_price);
                if (name_manual != null)
                    params.put(Constants.KEY_PICKUP_NAME, name_manual);
                if (name_manual_delivery != null)
                    params.put(Constants.KEY_DELIVERY_NAME, name_manual_delivery);
                if (floor_manual != null)
                    params.put(Constants.KEY_PICKUP_FLOOR_NO, floor_manual);
                if (floor_manual_delivery != null)
                    params.put(Constants.KEY_DELIVERY_FLOOR_NO, floor_manual_delivery);

                Log.v("INPUT COLLECTION&SEND ORDER", params.toString());

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

    /*for list of areas agent has to deliver*/
    public static void CallAPI_to_getAgentPickUpList(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                     final String area, final String api_token) {
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
                params.put(Constants.KEY_AREA, area);
                Log.v("==INPUT FOR AREA==", params.toString());

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

    public static void CallAPI_to_UpdateArea(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                             final String updated_area, final String api_token) {
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
                params.put(Constants.KEY_AREA, updated_area);
                Log.v("==INPUT FOR AREA==", params.toString());

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

    public static void CallAPI_to_UpdateStatus(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                               final String order_id, final String status, final String api_token) {
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
                params.put(Constants.KEY_ORDER_ID, order_id);
                params.put(Constants.KEY_STATUS, status);
                Log.v("==INPUT FOR AREA==", params.toString());

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
    public static void CallAPI_to_CreateOrder(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                              final String order_id, final String fee, final String payment_type, final String api_token) {
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
                params.put(Constants.KEY_ORDER_ID, order_id);
                params.put(Constants.KEY_FEE, fee);
                params.put(Constants.KEY_PAYMENT_TYPE, payment_type);

                Log.v("INPUT CREATE ORDER", params.toString());

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

    public static void CallAPI_to_ViewOrder(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                            final String order_id, final String api_token) {
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
                params.put(Constants.KEY_ORDER_ID, order_id);

                Log.v("INPUT VIEW ORDER", params.toString());

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

    public static void CallAPI_to_Accept_Dispatch(final Context context, final int method, final String url, final OnRequestCompletedListener listener,
                                                  final String order_id, final String api_token) {
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
            @SuppressLint("LongLogTag")
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters --input params--
                params.put(Constants.KEY_ORDER_ID, order_id);
                Log.v("==INPUT FOR ACCEPT ORDER==", params.toString());

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
