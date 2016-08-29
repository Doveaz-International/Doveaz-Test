package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/18/2016.
 */
public class AgentOrderHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String order_id;
    private String pickup_name;
    private String pickup_floor_no;
    private String pickup_street;
    private String pickup_area;
    private String pickup_city;
    private String pickup_state;
    private String pickup_country;
    private String delivery_name;
    private String delivery_floor_no;
    private String delivery_street;
    private String delivery_area;
    private String delivery_city;
    private String delivery_state;
    private String delivery_country;
    private String status;

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

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_floor_no() {
        return delivery_floor_no;
    }

    public void setDelivery_floor_no(String delivery_floor_no) {
        this.delivery_floor_no = delivery_floor_no;
    }

    public String getDelivery_street() {
        return delivery_street;
    }

    public void setDelivery_street(String delivery_street) {
        this.delivery_street = delivery_street;
    }

    public String getDelivery_area() {
        return delivery_area;
    }

    public void setDelivery_area(String delivery_area) {
        this.delivery_area = delivery_area;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
