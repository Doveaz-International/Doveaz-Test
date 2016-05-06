package com.doveazapp.GettersSetters;

/**
 * Created by Karthik on 2016/01/05.
 */
public class UserDetails {
    private String name;
    private String profile_pic;
    private String travel_date;
    private String risk_score;
    private String id;
    private String service_id;
    private String delivery_date;
    private String category_name;
    private String service_b_id;

    public String getService_b_id() {
        return service_b_id;
    }

    public void setService_b_id(String service_b_id) {
        this.service_b_id = service_b_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public String getRisk_score() {
        return risk_score;
    }

    public void setRisk_score(String risk_score) {
        this.risk_score = risk_score;
    }

    public UserDetails(String name, String profile_pic, String travel_date, String risk_score) {
        this.name = name;
        this.profile_pic = profile_pic;
        this.travel_date = travel_date;
        this.risk_score = risk_score;
    }

    public UserDetails() {
    }

}
