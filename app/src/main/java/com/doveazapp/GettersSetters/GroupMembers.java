package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 2016/02/17.
 */
public class GroupMembers implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userid;
    private String fullname;
    private String profile_pic_url;
    private String group_id;
    private String moderator_userid;
    private String my_user_id;
    private String admin;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getModerator_userid() {
        return moderator_userid;
    }

    public void setModerator_userid(String moderator_userid) {
        this.moderator_userid = moderator_userid;
    }

    public String getMy_user_id() {
        return my_user_id;
    }

    public void setMy_user_id(String my_user_id) {
        this.my_user_id = my_user_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

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
}
