package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/22/2016.
 */
public class OrderDetailsInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String order_id;
    private String pickup_street;
    private String pickup_area;
    private String pickup_city;
    private String pickup_state;
    private String pickup_country;
    private String pickup_zip;
    private String pickup_phone;
    private String delivery_street;
    private String delivery_area;
    private String delivery_city;
    private String delivery_state;
    private String delivery_country;
    private String delivery_zip;
    private String delivery_phone;
    private String fee;
    private String delivery_name;
    private String delivery_status;

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getPickup_zip() {
        return pickup_zip;
    }

    public void setPickup_zip(String pickup_zip) {
        this.pickup_zip = pickup_zip;
    }

    public String getPickup_phone() {
        return pickup_phone;
    }

    public void setPickup_phone(String pickup_phone) {
        this.pickup_phone = pickup_phone;
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

    public String getDelivery_zip() {
        return delivery_zip;
    }

    public void setDelivery_zip(String delivery_zip) {
        this.delivery_zip = delivery_zip;
    }

    public String getDelivery_phone() {
        return delivery_phone;
    }

    public void setDelivery_phone(String delivery_phone) {
        this.delivery_phone = delivery_phone;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }
}
