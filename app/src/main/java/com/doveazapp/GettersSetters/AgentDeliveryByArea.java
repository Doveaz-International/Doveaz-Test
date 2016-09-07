package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/19/2016.
 */
public class AgentDeliveryByArea implements Serializable {
    private static final long serialVersionUID = 1L;

    private String order_id;
    private String delivery_name;
    private String delivery_floor_no;
    private String delivery_street;
    private String delivery_area;
    private String delivery_city;
    private String delivery_state;
    private String delivery_country;
    private String delivery_zip;
    private String delivery_phone;
    private String delivery_address;
    private String payment_type;
    private String collection_amount;

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getCollection_amount() {
        return collection_amount;
    }

    public void setCollection_amount(String collection_amount) {
        this.collection_amount = collection_amount;
    }

    public String getDelivery_phone() {
        return delivery_phone;
    }

    public void setDelivery_phone(String delivery_phone) {
        this.delivery_phone = delivery_phone;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getDelivery_zip() {
        return delivery_zip;
    }

    public void setDelivery_zip(String delivery_zip) {
        this.delivery_zip = delivery_zip;
    }
}
