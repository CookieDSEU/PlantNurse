package com.plantnurse.plantnurse.model;

/**
 * Created by Heloise on 2016/9/3.
 */

public class CollectPlantModel {


    private String name;
    private String url;
    private String addtime;
    private int id;

    public CollectPlantModel() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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
        this.name = name;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

}

