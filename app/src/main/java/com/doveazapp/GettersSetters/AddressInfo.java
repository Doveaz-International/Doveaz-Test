package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 2016/05/17.
 */
public class AddressInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String delivery_address;
    private String delivery_city;
    private String delivery_state;
    private String delivery_country;
    private String delivery_postalcode;
    private String item_details;

    public String getItem_details() {
        return item_details;
    }

    public void setItem_details(String item_details) {
        this.item_details = item_details;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_city() {
        return delivery_city;
    }

    public void setDelivery_city(String delivery_city) {
        this.delivery_city = delivery_city;
    }

    public String getDelivery_state() {
        return delivery_state;
    }

    public void setDelivery_state(String delivery_state) {
        this.delivery_state = delivery_state;
    }

    public String getDelivery_country() {
        return delivery_country;
    }

    public void setDelivery_country(String delivery_country) {
        this.delivery_country = delivery_country;
    }

    public String getDelivery_postalcode() {
        return delivery_postalcode;
    }

    public void setDelivery_postalcode(String delivery_postalcode) {
        this.delivery_postalcode = delivery_postalcode;
    }
}
