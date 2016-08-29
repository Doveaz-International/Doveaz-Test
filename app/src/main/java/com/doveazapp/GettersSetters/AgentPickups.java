package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/18/2016.
 */
public class AgentPickups implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pickup_area;
    private String orders;

    public String getPickup_area() {
        return pickup_area;
    }

    public void setPickup_area(String pickup_area) {
        this.pickup_area = pickup_area;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }
}
