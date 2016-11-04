package com.beepermessenger.model;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
public class StateDTO {
    private String state_name;
    private String country_code;
    private long state_id;

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public long getState_id() {
        return state_id;
    }

    public void setState_id(long state_id) {
        this.state_id = state_id;
    }
}
