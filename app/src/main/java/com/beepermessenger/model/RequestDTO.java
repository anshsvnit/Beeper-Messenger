package com.beepermessenger.model;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
public class RequestDTO {
    private long request_id;
    private String sender_name;
    private String sender_image;
    private String request_time;

    public long getRequest_id() {
        return request_id;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }
}
