package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 6/23/2016.
 */
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String owner_id;
    private String description;
    private String unit_price;
    private String max_order_qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getMax_order_qty() {
        return max_order_qty;
    }

    public void setMax_order_qty(String max_order_qty) {
        this.max_order_qty = max_order_qty;
    }
}
