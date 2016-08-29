package com.doveazapp.Fragments;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.ViewMyOrdersAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.OrderDetailsCustomer;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karthik on 7/22/2016.
 */
public class ViewOrderFragment extends ListFragment implements AdapterView.OnItemClickListener {

    // Session Manager Class
    SessionManager session;

    ProgressDialog progressDialog;

    TextView status_txt, address;

    private ArrayList<OrderDetailsCustomer> responseDetailsList = new ArrayList<OrderDetailsCustomer>();

    private ListView listView;

    private ViewMyOrdersAdapter adapter;


    public static ViewOrderFragment newInstance() {
        return new ViewOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("Oncreate", "Inside on create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*View rootView = inflater.inflate(R.layout.view_order_deliver_layout,
                container, false);*/
        View rootView = inflater.inflate(R.layout.view_order_fragment,
                container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);

        Log.v("Oncreate", "Inside on create");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v("OncreateView", "Inside on onCreateView");

        session = new SessionManager(getActivity().getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        adapter = new ViewMyOrdersAdapter(getActivity(), R.layout.db_list_cell_review, responseDetailsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        Call_viewOrderAPI();
    }

    private void Call_viewOrderAPI() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("OUTPUT VIEW ORDER", response);
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_object = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        JSONArray order_array = value_object.getJSONArray("order");
                        for (int i = 0; i < order_array.length(); i++) {
                            JSONObject jsonobject = order_array.getJSONObject(i);
                            OrderDetailsCustomer OrderDetailsInfo = new OrderDetailsCustomer();
                            OrderDetailsInfo.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                            OrderDetailsInfo.setPickup_street(jsonobject.getString(Constants.KEY_PICKUP_STREET));
                            OrderDetailsInfo.setPickup_area(jsonobject.getString(Constants.KEY_PICKUP_AREA));
                            OrderDetailsInfo.setPickup_city(jsonobject.getString(Constants.KEY_PICKUP_CITY));
                            //OrderDetailsInfo.setPickup_zip(jsonobject.getString(Constants.KEY_PICKUP_ZIPCODE));
                            //OrderDetailsInfo.setPickup_phone(jsonobject.getString(Constants.KEY_PICKUP_PHONE));
                            OrderDetailsInfo.setDelivery_street(jsonobject.getString(Constants.KEY_DELIVERY_STREET));
                            OrderDetailsInfo.setDelivery_area(jsonobject.getString(Constants.KEY_DELIVERY_AREA));
                            OrderDetailsInfo.setDelivery_city(jsonobject.getString(Constants.KEY_DELIVERY_CITY));
                            //OrderDetailsInfo.setDelivery_zip(jsonobject.getString(Constants.KEY_DELIVERY_ZIP));
                            //OrderDetailsInfo.setDelivery_phone(jsonobject.getString(Constants.KEY_DELIVERY_PHONE));
                            OrderDetailsInfo.setDelivery_status(jsonobject.getString(Constants.KEY_ORDER_STATUS));
                            OrderDetailsInfo.setDelivery_name(jsonobject.getString(Constants.KEY_DELIVERY_NAME));

                            status_txt = (TextView) getActivity().findViewById(R.id.status);
                            address = (TextView) getActivity().findViewById(R.id.address);

                            address.setText(OrderDetailsInfo.getDelivery_street() + " , " +
                                    OrderDetailsInfo.getDelivery_area() + " , " + OrderDetailsInfo.getDelivery_city());

                            status_txt.setText(OrderDetailsInfo.getDelivery_status());
                        }

                        JSONArray order_items_array = value_object.getJSONArray("order_items");
                        for (int i = 0; i < order_items_array.length(); i++) {
                            JSONObject jsonobject = order_items_array.getJSONObject(i);
                            OrderDetailsCustomer order_items = new OrderDetailsCustomer();
                            order_items.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                            order_items.setDescription(jsonobject.getString(Constants.KEY_DESCRIPTION));
                            order_items.setPrice(jsonobject.getString(Constants.KEY_PRICE));
                            order_items.setQty(jsonobject.getString(Constants.KEY_QUANTITY));
                            responseDetailsList.add(order_items);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        progressDialog.dismiss();
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling API", Constants.VIEW_MY_ORDER);
        ServiceCalls.CallAPI_to_check_credits(getActivity(), Request.Method.POST, Constants.VIEW_MY_ORDER, listener, api_token);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
