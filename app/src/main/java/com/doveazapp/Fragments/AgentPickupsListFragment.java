package com.doveazapp.Fragments;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
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
import com.doveazapp.Adapters.AgentPickupsAdapter;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.AgentPickups;
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
public class AgentPickupsListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    // Session Manager Class
    SessionManager session;

    private ArrayList<AgentPickups> responseDetailsList = new ArrayList<AgentPickups>();

    private ListView listView;

    private AgentPickupsAdapter adapter;

    String updated_area;

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
        View rootView = inflater.inflate(R.layout.agent_pickups_fragment,
                container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);

        Bundle bundle = this.getArguments();
        updated_area = bundle.getString(Constants.KEY_AREA);


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
        CallApi_to_getPickUpList();

        /*adapter = new AgentPickupsAdapter(getActivity(), responseDetailsList);
        setListAdapter(adapter);*/

        adapter = new AgentPickupsAdapter(getActivity(), R.layout.agent_pickups_row, responseDetailsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

    }

    private void CallApi_to_getPickUpList() {
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
                    JSONArray sentJSONArray = value_obj.getJSONArray("areas");

                    for (int i = 0; i < sentJSONArray.length(); i++) {
                        if (status.equals("false")) {
                            Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            //Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                            AgentPickups areaDetails = new AgentPickups();
                            JSONObject jsonobject = sentJSONArray.getJSONObject(i);

                            // adding user to user array
                            areaDetails.setPickup_area(jsonobject.getString(Constants.KEY_PICKUP_AREA));
                            areaDetails.setOrders(jsonobject.getString(Constants.KEY_ORDERS));

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
        Log.v("Calling Api", Constants.AGENT_PICKUP_AREAS);
        ServiceCalls.CallAPI_to_getAgentPickUpList(getActivity(), Request.Method.POST, Constants.AGENT_PICKUP_AREAS, listener, updated_area, api_token);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView area = (TextView) view.findViewById(R.id.areas);

        String selected_area = area.getText().toString();

        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.KEY_AREA, selected_area);
        AgentPickupsByAreaFragment DmenuListFragment = new AgentPickupsByAreaFragment();
        FragmentManager fm1 = getFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        DmenuListFragment.setArguments(bundle1);
        ft1.addToBackStack(null);
        ft1.replace(R.id.three_buttons_activity, DmenuListFragment);
        ft1.commit();
    }
}
