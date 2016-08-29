package com.doveazapp.Fragments;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Adapters.AgentOrderListAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.AgentOrderHistory;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karthik on 7/18/2016.
 */
public class AgentOrderHistoryFragment extends ListFragment implements AdapterView.OnItemClickListener {
    // Session Manager Class
    SessionManager session;

    private ArrayList<AgentOrderHistory> responseDetailsList = new ArrayList<AgentOrderHistory>();

    private ListView listView;

    private AgentOrderListAdapter adapter;

    public static AgentDeliveriesListFragment newInstance() {
        return new AgentDeliveriesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("Oncreate", "Inside on create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.agent_order_fragment,
                container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.v("OncreateView", "Inside on onCreateView");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");

            }
        });
        session = new SessionManager(getActivity().getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        CallApi_to_getDeliveryList();

        adapter = new AgentOrderListAdapter(getActivity(), R.layout.agent_order_row, responseDetailsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    private void CallApi_to_getDeliveryList() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE AGENT ORDER LIST--", response.toString());
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    JSONArray sentJSONArray = value_obj.getJSONArray("orders");

                    for (int i = 0; i < sentJSONArray.length(); i++) {
                        if (status.equals("false")) {
                            Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            //Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            AgentOrderHistory orderHistory = new AgentOrderHistory();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);

                            // adding user to user array
                            orderHistory.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                            orderHistory.setPickup_name(jsonobject.getString(Constants.KEY_PICKUP_NAME));
                            orderHistory.setDelivery_name(jsonobject.getString(Constants.KEY_DELIVERY_NAME));
                            orderHistory.setStatus(jsonobject.getString(Constants.KEY_STATUS));

                            responseDetailsList.add(orderHistory);
                            adapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };

        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling Api", Constants.AGENT_ORDER_HISTORY);
        ServiceCalls.CallAPI_to_check_credits(getActivity(), Request.Method.POST, Constants.AGENT_ORDER_HISTORY, listener, api_token);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
