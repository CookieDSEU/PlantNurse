package com.plantnurse.plantnurse.model;

import com.kot32.ksimplelibrary.model.domain.BaseUserModel;


public class WeatherInfo implements BaseUserModel {

    private String city;
    private String todayWeather;
    private int todayDayLowest;
    private int todayDayHighest;
    private int todayNightLowest;
    private int todayNightHighest;
    private String tomorrowWeather;
    private int tomorrowDayLowest;
    private int tomorrowDayHighest;
    private int tomorrowNightLowest;
    private int tomorrowNightHighest;

    public String getcity(){return city;}

    public String gettodayWeather(){return todayWeather;}
    public int gettodayDayHighest(){return todayDayHighest;}
    public int gettodayDayLowest(){return todayDayLowest;}
    public int gettodayNightHighest(){return todayNightHighest;}
    public int gettodayNightLowest(){return todayNightLowest;}

    public void settodayDayHighest(int t){ todayDayHighest = t;}
    public void settodayDayLowest(int t){ todayDayHighest = t;}
    public void settodayNightHighest(int t){ todayDayHighest = t;}
    public void settodayNightLowest(int t){ todayDayHighest = t;}

    public String gettomorrowWeather(){return tomorrowWeather;}
    public int gettomorrowDayHighest(){return tomorrowDayHighest;}
    public int gettomorrowDayLowest(){return tomorrowDayLowest;}
    public int gettomorrowNightHighest(){return tomorrowNightHighest;}
    public int gettomorrowNightLowest(){return tomorrowNightLowest;}

    public void settomorrowDayHighest(int t){ tomorrowDayHighest = t;}
    public void settomorrowDayLowest(int t){ tomorrowDayHighest = t;}
    public void settomorrowNightHighest(int t){ tomorrowDayHighest = t;}
    public void settomorrowNightLowest(int t){ tomorrowDayHighest = t;}



}