package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 2016/01/25.
 */
public class StatusDetails implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String service_a_id;
    private String service_b_id;
    private String receiver_id;
    private String fullname;
    private String date;
    private String travel_date;
    private String sender_id;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getService_b_id() {
        return service_b_id;
    }

    public void setService_b_id(String service_b_id) {
        this.service_b_id = service_b_id;
    }

    public String getService_a_id() {
        return service_a_id;
    }

    public void setService_a_id(String service_a_id) {
        this.service_a_id = service_a_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
