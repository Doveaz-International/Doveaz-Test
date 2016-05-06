package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 2016/02/20.
 */
public class Moderator implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
