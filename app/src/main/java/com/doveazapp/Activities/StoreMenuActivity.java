package com.doveazapp.Activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Adapters.ProductMenuAdapter;
import com.doveazapp.Analytics.MyApplication;
import com.doveazapp.Constants;
import com.doveazapp.GettersSetters.ProductInfo;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.SqliteManager.AddedCartDBHelper;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Karthik on 6/23/2016.
 */
public class StoreMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = StoreMenuActivity.class.getName();
    public static final int REQUEST_CODE = 1;

    //session
    SessionManager session;

    String store_id, cart_quantity, address_type, deliver_st, deliver_area, deliver_city, deliver_state, deliver_country, deliver_zip,
            order_type, delivery_phone, deliver_st_manual, deliver_area_manual, delivery_address,
            deliver_city_manual, deliver_state_manual, delivery_country_manual, deliver_zip_manual, delivery_name, delivery_floor_number;

    private List<ProductInfo> productMenuList = new ArrayList<ProductInfo>();

    private ListView MenulistView;

    private ProductMenuAdapter menu_adapter;

    ImageView btn_close;

    LinearLayout checkout_layout;
    Button checkout_button;

    AddedCartDBHelper cartDBHelper;
    SQLiteDatabase sqldb;

    //Progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_menu_floating_button);

        menuvisibilityinAlldevices();

        //button
        btn_close = (ImageView) findViewById(R.id.btn_close);

        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Check Partner List");
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        checkout_layout = (LinearLayout) findViewById(R.id.checkout_layout);
        checkout_button = (Button) findViewById(R.id.checkout_button);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        store_id = bundle.getString(Constants.KEY_STOREID);
        address_type = bundle.getString(Constants.KEY_ADDRESS_TYPE);
        delivery_phone = bundle.getString(Constants.KEY_DELIVERY_PHONE);
        order_type = "0";

        if (address_type.equals("0")) {
            deliver_st = bundle.getString(Constants.KEY_DELIVERY_STREET);
            deliver_area = bundle.getString(Constants.KEY_DELIVERY_AREA);
            deliver_city = bundle.getString(Constants.KEY_DELIVERY_CITY);
            deliver_state = bundle.getString(Constants.KEY_DELIVERY_STATE);
            deliver_country = bundle.getString(Constants.KEY_DELIVERY_COUNTRY);
            deliver_zip = bundle.getString(Constants.KEY_DELIVERY_POSTAL);
            delivery_name = bundle.getString(Constants.KEY_NAME);
            delivery_floor_number = bundle.getString(Constants.KEY_FLOOR_NUMBER);
            delivery_address = bundle.getString(Constants.KEY_DELIVERY_ADDRESS);
        } else {
            delivery_address = bundle.getString(Constants.KEY_DELIVERY_ADDRESS);
            deliver_st = bundle.getString(Constants.KEY_DELIVERY_STREET);
            deliver_area = bundle.getString(Constants.KEY_DELIVERY_AREA);
            deliver_city = bundle.getString(Constants.KEY_DELIVERY_CITY);
            deliver_state = bundle.getString(Constants.KEY_DELIVERY_STATE);
            deliver_country = bundle.getString(Constants.KEY_DELIVERY_COUNTRY);
            deliver_zip = bundle.getString(Constants.KEY_DELIVERY_POSTAL);
            delivery_name = bundle.getString(Constants.KEY_NAME);
        }

        deleteAllData();

        callAPI_to_storeMenuList();

        btn_close.setOnClickListener(this);
        checkout_button.setOnClickListener(this);

        MenulistView = (ListView) findViewById(R.id.list_store_menu);
        menu_adapter = new ProductMenuAdapter(StoreMenuActivity.this, productMenuList);
        MenulistView.setAdapter(menu_adapter);

        cartDBHelper = new AddedCartDBHelper(this);
        sqldb = cartDBHelper.getWritableDatabase();

        MenulistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                Log.i("List View Clicked", "**********");

                TextView display_number = (TextView) v.findViewById(R.id.display_number);
                cart_quantity = display_number.getText().toString();

            }
        });
    }

    private void deleteAllData() {
        getApplicationContext().deleteDatabase(AddedCartDBHelper.DATABASE_NAME);
        //sqldb.delete("cart", null, null);

    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(StoreMenuActivity.this);
    }

    private void callAPI_to_storeMenuList() {
        progressDialog = ProgressDialog.show(StoreMenuActivity.this, "Please wait ...", "Requesting...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--OUTPUT STORE MENU--", response.toString());
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_array = obj.getJSONObject("value");
                    JSONArray product_array = value_array.getJSONArray("products");

                    for (int i = 0; i < product_array.length(); i++) {
                        if (status.equals("false")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        } else if (status.equals("true")) {
                            progressDialog.dismiss();
                            JSONObject jsonobject = product_array.getJSONObject(i);
                            ProductInfo product_info = new ProductInfo();
                            product_info.setId(jsonobject.getString(Constants.KEY_ID));
                            product_info.setDescription(jsonobject.getString(Constants.KEY_DESCRIPTION));
                            product_info.setOwner_id(jsonobject.getString(Constants.KEY_OWNER_ID));
                            product_info.setMax_order_qty(jsonobject.getString(Constants.KEY_ORDER_QTY));
                            product_info.setUnit_price(jsonobject.getString(Constants.KEY_UNIT_PRICE));

                            // adding user to user array
                            productMenuList.add(product_info);
                            menu_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException exception) {
                    progressDialog.dismiss();
                    Log.e("--JSON EXCEPTION--", exception.toString());
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);

        ServiceCalls.CallAPI_togetStoreMenu(this, Request.Method.POST, Constants.GET_STORE_PRODUCT_LIST, listener, store_id, api_token);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_close) {
            finish();
        }
        if (v == checkout_button) {
            /*sqldb.execSQL("delete from " + AddedCartDBHelper.AddedCartEntry.TABLE);
            sqldb.delete("cart", null, null);*/
            progressDialog = ProgressDialog.show(StoreMenuActivity.this, "Please wait ...", "Loading...", true);
            progressDialog.setCancelable(false);
            Cursor cursor = null;

            Set setOfKeys1 = menu_adapter.map_product_id.keySet();
            Iterator iterator1 = setOfKeys1.iterator();
            String result = null;

            /*Cursor cur = sqldb.rawQuery("SELECT count(*) FROM " + AddedCartDBHelper.AddedCartEntry.TABLE, null);
            if (cur != null && cur.moveToFirst() && cur.getInt(0) > 0) {
                Log.i(getClass().getName(), "table not empty");
                sqldb.execSQL("delete from " + AddedCartDBHelper.AddedCartEntry.TABLE);
                sqldb.delete("cart", null, null);
                getApplicationContext().deleteDatabase(AddedCartDBHelper.DATABASE_NAME);
                cur.close();
            } else {
                Log.i(getClass().getName(), "table is empty");
            }*/
            String js = "[";
            while (iterator1.hasNext()) {
                menu_adapter.prod_id = (String) iterator1.next();
                menu_adapter.product_quantity = menu_adapter.map_product_id.get(menu_adapter.prod_id);


                js = js + "{" + "\"product_id" + "\" " + ":" + menu_adapter.prod_id + "," + "\"quantity" + "\" " + ":" + menu_adapter.product_quantity + "},";
                // values.add("{" + "\"product_id" + "\" " + ":" + menu_adapter.prod_id + "," + "\"quantity" + "\" " + ":" + menu_adapter.product_quantity + "}");

                //System.out.println(Arrays.asList(hm));

                /* For inserting the values into database */
                ContentValues cv = new ContentValues();
                if (!menu_adapter.product_quantity.equals("0")) {
                    cv.put(AddedCartDBHelper.AddedCartEntry.PROD_ID, menu_adapter.prod_id);
                    cv.put(AddedCartDBHelper.AddedCartEntry.QUANTITY, menu_adapter.product_quantity);

                    Cursor c = sqldb.rawQuery("SELECT * FROM " + AddedCartDBHelper.AddedCartEntry.TABLE + " WHERE " + AddedCartDBHelper.AddedCartEntry.PROD_ID + " ='" + menu_adapter.prod_id + "'", null);
                    Log.v("CURSOR VALUE", c.toString());
                    if (c.moveToFirst()) {
                        // record exists
                        long temp;
                        temp = c.getLong(c.getColumnIndex(AddedCartDBHelper.AddedCartEntry._ID));
                        String rowID = String.valueOf(temp);
                        int x = sqldb.update(AddedCartDBHelper.AddedCartEntry.TABLE, cv, AddedCartDBHelper.AddedCartEntry._ID + "= ?", new String[]{rowID});
                        // Toast.makeText(this, "Record updated Generated id is : " + x, Toast.LENGTH_LONG).show();
                        Log.v("update", String.valueOf(x));
                    } else {
                        long id = sqldb.insert(AddedCartDBHelper.AddedCartEntry.TABLE, null, cv);
                        //sqldb.execSQL("INSERT INTO " + AddedCartDBHelper.AddedCartEntry.TABLE + " (" + AddedCartDBHelper.AddedCartEntry.PROD_ID + "," + AddedCartDBHelper.AddedCartEntry.QUANTITY + ") VALUES(" + menu_adapter.prod_id + ", " + menu_adapter.product_quantity + " )", null);
                        // Toast.makeText(this, "Record Added Generated id is : " + id, Toast.LENGTH_LONG).show();
                        Log.v("add", String.valueOf(id));
                    }
                }
                /*For showing the content in database*/
                cursor = sqldb.rawQuery("SELECT * FROM " + AddedCartDBHelper.AddedCartEntry.TABLE, null);
                ArrayList arraylst = new ArrayList();
                if (cursor.moveToFirst()) {
                    do {
                        arraylst.add(cursor.getLong(0));
                        arraylst.add(cursor.getString(1));
                        arraylst.add(cursor.getLong(2));

                    } while (cursor.moveToNext());
                }
                Log.v("Sqlite", arraylst.toString());

                ArrayList<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            map.put("\"" + cursor.getColumnName(i) + "\":", "\"" + cursor.getString(i) + "\"");
                        }
                        maplist.add(map);
                    } while (cursor.moveToNext());
                }
                result = maplist.toString().replaceAll("=", "");
                Log.v("MAP ARRAY", result.toString());
                //}
            }

            Map<String, List<String>> hm = new HashMap<String, List<String>>();
            List<String> values = new ArrayList<String>();
            /*js = js;
            //js.replaceAll(", $", "");
            *//*js.substring(0, js.length() - 1);
            js = js + "]";*/
            String jString = null;
            if (js != null && js.length() > 1) {
                jString = js.substring(0, js.length() - 1);
            }
            jString = jString + "]";
            Log.v("JSS", jString);
            values.add(jString);

            /*For checking table is empty or not*//*
            Cursor cur = sqldb.rawQuery("SELECT count(*) FROM " + AddedCartDBHelper.AddedCartEntry.TABLE, null);
            if (cur != null && cur.moveToFirst() && cur.getInt(0) > 0) {
                Log.i(getClass().getName(), "table not empty");
                *//*sqldb.execSQL("delete from " + AddedCartDBHelper.AddedCartEntry.TABLE);
                sqldb.delete("cart", null, null);*//*
            } else {
                Log.i(getClass().getName(), "table is empty");
            }*/
            progressDialog.dismiss();

            Intent review_order = new Intent(getApplicationContext(), OrderReviewActivity.class);
            review_order.putExtra(Constants.KEY_ORDER_TYPE, order_type);
            review_order.putExtra(Constants.KEY_DELIVERY_ADDRESS, delivery_address);
            review_order.putExtra(Constants.KEY_DELIVERY_STREET, deliver_st);
            review_order.putExtra(Constants.KEY_DELIVERY_AREA, deliver_area);
            review_order.putExtra(Constants.KEY_DELIVERY_CITY, deliver_city);
            review_order.putExtra(Constants.KEY_DELIVERY_STATE, deliver_state);
            review_order.putExtra(Constants.KEY_DELIVERY_ZIP, deliver_zip);
            review_order.putExtra(Constants.KEY_ADDRESS_TYPE, address_type);
            review_order.putExtra(Constants.KEY_RESULT, jString);
            review_order.putExtra(Constants.KEY_STOREID, store_id);
            review_order.putExtra(Constants.KEY_DELIVERY_PHONE, delivery_phone);
            review_order.putExtra(Constants.KEY_DELIVERY_NAME, delivery_name);
            startActivity(review_order);
        }

            /*for deleting whole values from table*/
        //sqldb.execSQL("delete from " + AddedCartDBHelper.AddedCartEntry.TABLE);
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
    public void onBackPressed() {

    }
}


