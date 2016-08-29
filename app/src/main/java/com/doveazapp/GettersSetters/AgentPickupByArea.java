package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/19/2016.
 */
public class AgentPickupByArea implements Serializable {
    private static final long serialVersionUID = 1L;

    private String order_id;
    private String pickup_name;
    private String pickup_floor_no;
    private String pickup_street;
    private String pickup_area;
    private String pickup_city;
    private String pickup_state;
    private String pickup_country;
    private String pickup_zip;
    private String pickup_phone;

    public String getPickup_phone() {
        return pickup_phone;
    }

    public void setPickup_phone(String pickup_phone) {
        this.pickup_phone = pickup_phone;
    }

    public String getPickup_zip() {
        return pickup_zip;
    }

    public void setPickup_zip(String pickup_zip) {
        this.pickup_zip = pickup_zip;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPickup_name() {
        return pickup_name;
    }

    public void setPickup_name(String pickup_name) {
        this.pickup_name = pickup_name;
    }

    public String getPickup_floor_no() {
        return pickup_floor_no;
    }

    public void setPickup_floor_no(String pickup_floor_no) {
        this.pickup_floor_no = pickup_floor_no;
    }

    public String getPickup_street() {
        return pickup_street;
    }

    public void setPickup_street(String pickup_street) {
        this.pickup_street = pickup_street;
    }

    public String getPickup_area() {
        return pickup_area;
    }

    public void setPickup_area(String pickup_area) {
        this.pickup_area = pickup_area;
    }

    public String getPickup_city() {
        return pickup_city;
    }

    public void setPickup_city(String pickup_city) {
        this.pickup_city = pickup_city;
    }

    public String getPickup_state() {
        return pickup_state;
    }

    public void setPickup_state(String pickup_state) {
        this.pickup_state = pickup_state;
    }

    public String getPickup_country() {
        return pickup_country;
    }

    public void setPickup_country(String pickup_country) {
        this.pickup_country = pickup_country;
    }
}
