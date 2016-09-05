package com.plantnurse.plantnurse.model;

import android.util.Log;

import java.util.Date;

/**
 * Created by Heloise on 2016/9/3.
 */

public class CollectModel {

    private String name;
    private String url;
    private String addtime;
    private int id;

    public CollectModel() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        Log.e("collectModel","setUrl");
        this.url = url;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Log.e("collectModel","setName");
        this.name = name;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

}

