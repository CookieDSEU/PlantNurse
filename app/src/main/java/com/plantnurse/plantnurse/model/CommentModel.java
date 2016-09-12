package com.plantnurse.plantnurse.model;

/**
 * Created by Heloise on 2016/9/7.
 */

public class CommentModel {

    private String name;
    private String iconUrl;
    private String time;
    private String comment;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String date) {
        this.time = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
