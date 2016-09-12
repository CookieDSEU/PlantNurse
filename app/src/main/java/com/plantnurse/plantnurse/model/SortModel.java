package com.plantnurse.plantnurse.model;

import java.io.Serializable;

/**
 * create by Heloise
 * 排列条目的原型
 */

public class SortModel implements Serializable {

    private String name;
    private String sortLetters;
    private int id;
    private String url;

    public SortModel() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public int getId() {
        return id;
    }
}
