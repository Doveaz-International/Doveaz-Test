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
import com.doveazapp.Adapters.AgentDeliverByAreaAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.AgentDeliveryByArea;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karthik on 7/19/2016.
 */
public class AgentDeliveryByAreaFragment extends ListFragment implements AdapterView.OnItemClickListener {

    // Session Manager Class
    SessionManager session;

    private ArrayList<AgentDeliveryByArea> responseDetailsList = new ArrayList<AgentDeliveryByArea>();

    private ListView listView;

    private AgentDeliverByAreaAdapter adapter;

    String selected_area;

    public static AgentDeliveryByAreaFragment newInstance() {
        return new AgentDeliveryByAreaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("Oncreate", "Inside on create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.agent_delivery_byarea_fragment,
                container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);

        Bundle bundle = this.getArguments();
        selected_area = bundle.getString(Constants.KEY_AREA);
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
        CallApi_to_getdeliverybyareaList();

        /*adapter = new AgentPickupsAdapter(getActivity(), responseDetailsList);
        setListAdapter(adapter);*/

        adapter = new AgentDeliverByAreaAdapter(getActivity(), R.layout.agent_delivery_byarea_row, responseDetailsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    private void CallApi_to_getdeliverybyareaList() {
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--RESPONSE AGENT AREA LIST--", response.toString());
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");

                    if (status.equals("false")) {
                        String message_false = value_obj.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(), message_false, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        JSONArray sentJSONArray = value_obj.getJSONArray("orders");

                        for (int i = 0; i < sentJSONArray.length(); i++) {
                            //Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            AgentDeliveryByArea areaDetails = new AgentDeliveryByArea();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);

                            // adding user to user array
                            areaDetails.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                            areaDetails.setDelivery_name(jsonobject.getString(Constants.KEY_DELIVERY_NAME));
                            areaDetails.setDelivery_floor_no(jsonobject.getString(Constants.KEY_DELIVERY_FLOOR_NO));
                            areaDetails.setDelivery_street(jsonobject.getString(Constants.KEY_DELIVERY_STREET));
                            areaDetails.setDelivery_area(jsonobject.getString(Constants.KEY_DELIVERY_AREA));
                            areaDetails.setDelivery_city(jsonobject.getString(Constants.KEY_DELIVERY_CITY));
                            areaDetails.setDelivery_state(jsonobject.getString(Constants.KEY_DELIVERY_STATE));
                            areaDetails.setDelivery_country(jsonobject.getString(Constants.KEY_DELIVERY_COUNTRY));
                            areaDetails.setDelivery_zip(jsonobject.getString(Constants.KEY_DELIVERY_ZIP));
                            areaDetails.setDelivery_phone(jsonobject.getString(Constants.KEY_DELIVERY_PHONE));
                            areaDetails.setDelivery_name(jsonobject.getString(Constants.KEY_DELIVERY_ADDRESS));
                            areaDetails.setPayment_type(jsonobject.getString(Constants.KEY_PAYMENT_TYPE));
                            areaDetails.setCollection_amount(jsonobject.getString(Constants.KEY_COLLECTION_AMOUNT));

                            responseDetailsList.add(areaDetails);
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
        Log.v("Calling Api", Constants.DELIVERY_LIST_BY_AREA);
        ServiceCalls.CallAPI_to_getAgentPickUpList(getActivity(), Request.Method.POST, Constants.DELIVERY_LIST_BY_AREA, listener, selected_area, api_token);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
