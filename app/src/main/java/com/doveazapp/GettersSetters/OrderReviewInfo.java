package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 7/29/2016.
 */
public class OrderReviewInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String product_id;
    private String product_name;
    private String unit_price;
    private String total_price;
    private String quantity;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
