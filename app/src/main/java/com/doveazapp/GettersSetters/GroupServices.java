package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 2016/02/19.
 */
public class GroupServices implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type_a_userid;
    private String name;
    private String profile_pic;
    private String service_id;

    public String getType_a_userid() {
        return type_a_userid;
    }

    public void setType_a_userid(String type_a_userid) {
        this.type_a_userid = type_a_userid;
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

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }
}

