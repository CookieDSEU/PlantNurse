package com.plantnurse.plantnurse.model;

/**
 * Created by Yxuan on 2016/9/6.
 */
public class MusicInfo {
    private long id;
    private String title;
    private String artist;
    private String url;
    private long duration;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return this.duration;
    }

}
