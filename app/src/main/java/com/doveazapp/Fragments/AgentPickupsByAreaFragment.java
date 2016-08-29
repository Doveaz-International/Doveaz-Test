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
import com.doveazapp.Adapters.AgentPickupByAreaAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.AgentPickupByArea;
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
public class AgentPickupsByAreaFragment extends ListFragment implements AdapterView.OnItemClickListener {

    // Session Manager Class
    SessionManager session;

    private ArrayList<AgentPickupByArea> responseDetailsList = new ArrayList<AgentPickupByArea>();

    private ListView listView;

    private AgentPickupByAreaAdapter adapter;

    String selected_area;

    public static AgentPickupsListFragment newInstance() {
        return new AgentPickupsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("Oncreate", "Inside on create");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.agent_pickup_byarea_fragment,
                container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);

        Bundle bundle = this.getArguments();
        if (bundle != null)
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
        CallApi_to_getPickUpbyareaList();

        /*adapter = new AgentPickupsAdapter(getActivity(), responseDetailsList);
        setListAdapter(adapter);*/

        adapter = new AgentPickupByAreaAdapter(getActivity(), R.layout.agent_pickup_byarea_row, responseDetailsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

    }

    private void CallApi_to_getPickUpbyareaList() {
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
                            AgentPickupByArea areaDetails = new AgentPickupByArea();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);

                            // adding user to user array
                            areaDetails.setOrder_id(jsonobject.getString(Constants.KEY_ORDER_ID));
                            areaDetails.setPickup_name(jsonobject.getString(Constants.KEY_PICKUP_NAME));
                            areaDetails.setPickup_floor_no(jsonobject.getString(Constants.KEY_PICKUP_FLOOR_NO));
                            areaDetails.setPickup_street(jsonobject.getString(Constants.KEY_PICKUP_STREET));
                            areaDetails.setPickup_area(jsonobject.getString(Constants.KEY_PICKUP_AREA));
                            areaDetails.setPickup_city(jsonobject.getString(Constants.KEY_PICKUP_CITY));
                            areaDetails.setPickup_state(jsonobject.getString(Constants.KEY_PICKUP_STATE));
                            areaDetails.setPickup_country(jsonobject.getString(Constants.KEY_PICKUP_COUNTRY));
                            areaDetails.setPickup_zip(jsonobject.getString(Constants.KEY_PICKUP_ZIPCODE));
                            areaDetails.setPickup_phone(jsonobject.getString(Constants.KEY_PICKUP_PHONE));

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
        Log.v("Calling Api", Constants.PICKUP_LIST_BY_AREA);
        ServiceCalls.CallAPI_to_getAgentPickUpList(getActivity(), Request.Method.POST, Constants.PICKUP_LIST_BY_AREA, listener, selected_area, api_token);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
