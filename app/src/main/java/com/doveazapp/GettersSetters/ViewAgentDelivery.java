package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/28/2016.
 */
public class ViewAgentDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullname;
    private String profile_pic_url;
    private String order_id;
    private String delivery_status;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }
}
