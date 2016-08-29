package com.doveazapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.doveazapp.GettersSetters.ProductInfo;
import com.doveazapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Karthik on 6/23/2016.
 */
public class ProductMenuAdapter extends BaseAdapter {
    int layoutResourceId;

    private Activity activity;

    private LayoutInflater inflater;

    private List<ProductInfo> productInfoList;

    Context context = null;

    Map<String, List<String>> hm = new HashMap<String, List<String>>();
    List<String> values = new ArrayList<String>();

    public String product_quantity, product_name, prod_id, prod_price, namee;

    public static HashMap<String, String> map = new HashMap<>();

    public static HashMap<String, String> map_product_id = new HashMap<>();

    public ProductMenuAdapter(Activity activity, List<ProductInfo> productInfo) {
        this.activity = activity;
        this.productInfoList = productInfo;
    }

    public ProductMenuAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return productInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.store_menu_row, null);


        final TextView product_name = (TextView) convertView.findViewById(R.id.txt_product_name);
        final TextView product_price = (TextView) convertView.findViewById(R.id.txt_product_price);
        final TextView display_number = (TextView) convertView.findViewById(R.id.display_number);
        Button decrease = (Button) convertView.findViewById(R.id.decrease);
        Button increase = (Button) convertView.findViewById(R.id.increase);
        final TextView product_id = (TextView) convertView.findViewById(R.id.txt_product_id);


        final ProductInfo productInfo = productInfoList.get(position);

        product_name.setText(productInfo.getDescription());
        product_id.setText(productInfo.getId());
        product_price.setText(productInfo.getUnit_price());

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minValue = 0;
                int currentVal = Integer.valueOf(display_number.getText().toString());
                if (currentVal > minValue) {
                    display_number.setText(String.valueOf(currentVal - 1));
               /* int minteger = 0;
                minteger--;

                display_number.setText(String.valueOf(minteger));*/
                    /*List<String> values = new ArrayList<String>();
                    values.add("" + product_name.getText().toString());
                    values.add(" " + display_number.getText().toString());
                    values.add(" " + product_id.getText().toString());
                    map.put("" + product_name.getText().toString(), values);*/
                    //map.put("" + product_name.getText().toString(), " " + display_number.getText().toString());
                    //map_product_id.put("" + product_price.getText().toString(), "" + product_id.getText().toString());
                    map_product_id.put("" + product_id.getText().toString(), "" + display_number.getText().toString());

                    //ShowHashMapValue();
                    Showproduct_id();
                } else if (display_number.getText().equals("0")) {
                    display_number.setText(String.valueOf(currentVal));
                }
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxValue = Integer.MAX_VALUE;
                int currentVal = Integer.valueOf(display_number.getText().toString());
                if (currentVal < maxValue) {
                    display_number.setText(String.valueOf(currentVal + 1));
                    /*int minteger = 0;
                    minteger++;

                    display_number.setText(String.valueOf(minteger));*/
                    /*List<String> values = new ArrayList<String>();
                    values.add("" + product_name.getText().toString());
                    values.add(" " + display_number.getText().toString());
                    values.add(" " + product_id.getText().toString());
                    map.put("" + product_name.getText().toString(), values);*/
                    //map.put("" + product_name.getText().toString(), " " + display_number.getText().toString());
                    //map_product_id.put("" + product_price.getText().toString(), "" + product_id.getText().toString());
                    /*values.add(product_name.getText().toString());
                    values.add(product_price.getText().toString());
                    values.add(display_number.getText().toString());
                    hm.put(product_id.getText().toString(), values);

                    Log.v("TEST", hm.toString());

                    // to get the arraylist
                    System.out.println("test" + hm.get(product_id.getText().toString();


                    Set setOfKeys = hm.keySet();

                    Iterator iterator = setOfKeys.iterator();


                    while (iterator.hasNext()) {

                        prod_id = (String) iterator.next();

                        product_quantity = String.valueOf(display_number.getText().toString());

                        System.out.println("Key: " + prod_id + ", Value: " + product_quantity);

                    }*/
                    //ShowHashMapValue();
                    map_product_id.put("" + product_id.getText().toString(), "" + display_number.getText().toString());

                    Map<String, List<String>> hm = new HashMap<String, List<String>>();
                    List<String> values = new ArrayList<String>();
                    values.add("{" + "\"product_id" + "\" " + ":" + product_id.getText().toString() + "," + "\"quantity" + "\" " + ":" + display_number.getText().toString() + "}");
                    hm.put("products", values);
                    System.out.println(hm.get("products"));

                    Showproduct_id();
                }
            }
        });

        return convertView;
    }

    private void showNew() {

    }

    public void Showproduct_id() {
        Set setOfKeys = map_product_id.keySet();
        Iterator iterator = setOfKeys.iterator();
        while (iterator.hasNext()) {
            prod_id = (String) iterator.next();
            product_quantity = map_product_id.get(prod_id);
            System.out.println("PRODUCT_ID: " + prod_id + "PRODUCT_PRICE:" + product_quantity);
        }
    }

    private void ShowHashMapValue() {

        Set setOfKeys = map.keySet();

        Iterator iterator = setOfKeys.iterator();


        while (iterator.hasNext()) {
/**
 * next() method returns the next key from Iterator instance.
 * return type of next() method is Object so we need to do DownCasting to String
 */
            product_name = (String) iterator.next();
/**
 * once we know the 'key', we can get the value from the HashMap
 * by calling get() method
 */
            product_quantity = map.get(product_name);

            System.out.println("Key: " + product_name + ", Value: " + product_quantity); //value = product_name key = quantity

        }
    }
}

