package com.beepermessenger.model;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
public class PostDTO {
    private long post_id;
    private String title;
    private String description;
    private String post_media;
    private String post_media_type;
    private String status;
    private String date_added;
    private String date_updated;

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost_media() {
        return post_media;
    }

    public void setPost_media(String post_media) {
        this.post_media = post_media;
    }

    public String getPost_media_type() {
        return post_media_type;
    }

    public void setPost_media_type(String post_media_type) {
        this.post_media_type = post_media_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }
}
