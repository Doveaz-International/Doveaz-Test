package com.doveazapp.GettersSetters;

import java.io.Serializable;

/**
 * Created by Karthik on 2016/01/08.
 */
public class ProfileService implements Serializable {

    private static final long serialVersionUID = 1L;

    // PROFILE DETAILS
    private String userid;
    private String email;
    private String fullname;
    private String lastname;
    private String gender;
    private String dob;
    private String country;
    private String presentaddress;
    private String area;
    private String streetaddress;
    private String partner;
    private String idproof1;
    private String idproof2;
    private String nationality;
    private String state;
    private String education;
    private String profession;
    private String city;
    private String country_code;
    private String phone;
    private String profile_pic;
    private String profile_pic_url;
    private String status;
    private String api_token;
    private String postalcode;

    //SERVICE DETAILS
    private String id;
    private String service_userid;
    private String service_type;
    private String where_you_based;
    private String travel_date;
    private String origin_address;
    private String origin_city;
    private String origin_state;
    private String origin_country;
    private String origin_postalcode;
    private String destination_address;
    private String destination_city;
    private String destination_state;
    private String destination_country;
    private String destination_postalcode;
    private String fee;
    private String offer;
    private String distace_from_pickup;
    private String distace_from_delivery;
    private String risk_score;
    private String item_short_description;
    private String image;
    private String date;
    private String credits;
    private String pick_up_address;
    private String pick_up_city;
    private String pick_up_state;
    private String pick_up_country;
    private String pick_up_postalcode;
    private String delivery_address;
    private String delivery_city;
    private String delivery_state;
    private String delivery_country;
    private String delivery_postalcode;
    private String image_url;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_city() {
        return delivery_city;
    }

    public void setDelivery_city(String delivery_city) {
        this.delivery_city = delivery_city;
    }

    public String getDelivery_country() {
        return delivery_country;
    }

    public void setDelivery_country(String delivery_country) {
        this.delivery_country = delivery_country;
    }

    public String getDelivery_state() {
        return delivery_state;
    }

    public void setDelivery_state(String delivery_state) {
        this.delivery_state = delivery_state;
    }

    public String getDelivery_postalcode() {
        return delivery_postalcode;
    }

    public void setDelivery_postalcode(String delivery_postalcode) {
        this.delivery_postalcode = delivery_postalcode;
    }

    public String getItem_short_description() {
        return item_short_description;
    }

    public void setItem_short_description(String item_short_description) {
        this.item_short_description = item_short_description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getPick_up_address() {
        return pick_up_address;
    }

    public void setPick_up_address(String pick_up_address) {
        this.pick_up_address = pick_up_address;
    }

    public String getPick_up_city() {
        return pick_up_city;
    }

    public void setPick_up_city(String pick_up_city) {
        this.pick_up_city = pick_up_city;
    }

    public String getPick_up_state() {
        return pick_up_state;
    }

    public void setPick_up_state(String pick_up_state) {
        this.pick_up_state = pick_up_state;
    }

    public String getPick_up_country() {
        return pick_up_country;
    }

    public void setPick_up_country(String pick_up_country) {
        this.pick_up_country = pick_up_country;
    }

    public String getPick_up_postalcode() {
        return pick_up_postalcode;
    }

    public void setPick_up_postalcode(String pick_up_postalcode) {
        this.pick_up_postalcode = pick_up_postalcode;
    }

    public String getRisk_score() {
        return risk_score;
    }

    public void setRisk_score(String risk_score) {
        this.risk_score = risk_score;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public String getService_userid() {
        return service_userid;
    }

    public void setService_userid(String service_userid) {
        this.service_userid = service_userid;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getWhere_you_based() {
        return where_you_based;
    }

    public void setWhere_you_based(String where_you_based) {
        this.where_you_based = where_you_based;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public String getOrigin_address() {
        return origin_address;
    }

    public void setOrigin_address(String origin_address) {
        this.origin_address = origin_address;
    }

    public String getOrigin_city() {
        return origin_city;
    }

    public void setOrigin_city(String origin_city) {
        this.origin_city = origin_city;
    }

    public String getOrigin_state() {
        return origin_state;
    }

    public void setOrigin_state(String origin_state) {
        this.origin_state = origin_state;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }

    public String getOrigin_postalcode() {
        return origin_postalcode;
    }

    public void setOrigin_postalcode(String origin_postalcode) {
        this.origin_postalcode = origin_postalcode;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }

    public String getDestination_state() {
        return destination_state;
    }

    public void setDestination_state(String destination_state) {
        this.destination_state = destination_state;
    }

    public String getDestination_country() {
        return destination_country;
    }

    public void setDestination_country(String destination_country) {
        this.destination_country = destination_country;
    }

    public String getDestination_postalcode() {
        return destination_postalcode;
    }

    public void setDestination_postalcode(String destination_postalcode) {
        this.destination_postalcode = destination_postalcode;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getDistace_from_pickup() {
        return distace_from_pickup;
    }

    public void setDistace_from_pickup(String distace_from_pickup) {
        this.distace_from_pickup = distace_from_pickup;
    }

    public String getDistace_from_delivery() {
        return distace_from_delivery;
    }

    public void setDistace_from_delivery(String distace_from_delivery) {
        this.distace_from_delivery = distace_from_delivery;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPresentaddress() {
        return presentaddress;
    }

    public void setPresentaddress(String presentaddress) {
        this.presentaddress = presentaddress;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getIdproof1() {
        return idproof1;
    }

    public void setIdproof1(String idproof1) {
        this.idproof1 = idproof1;
    }

    public String getIdproof2() {
        return idproof2;
    }

    public void setIdproof2(String idproof2) {
        this.idproof2 = idproof2;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
