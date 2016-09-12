package com.plantnurse.plantnurse.utils;

/**
 * Created by Eason_Tao on 2016/9/7.
 */
public class DataAnalysis {
    //3day
    private int mcond_d[];//天气代码
    private int mcond_n[];
    private int mpop[];//降水概率
    private int mtmp_min;
    private int mtmp_max;
    private boolean needAlarm = false;
    //today
    private int todayDayCond;
    private int todayNightCond;
    private int todayPop;
    private int mspd[];//风速
    private int todaytem[];
    private int msize;
    private int hourlypop[];


    private String result = "";

    public DataAnalysis() {

        //未来三天
//        for (int i = 0 ; i<4 ; i++){
//            mcond_d[i] = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(i).cond.codeD);
//            mcond_n[i] = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(i).cond.codeN);
//            mpop[i] = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(i).pop);
//        }

        //当日每三小时
        msize = DataManager.getWeatherInfo().hourlyForecast.size();
        todayDayCond = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(0).cond.codeD);
        todayNightCond = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(0).cond.codeN);
        todayPop = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(0).pop);
//        for (int i = 0; i<msize ; i++){
//            mspd[i] =  Integer.parseInt(DataManager.getWeatherInfo().hourlyForecast.get(i).wind.spd);
//            hourlypop[i] = Integer.parseInt(DataManager.getWeatherInfo().hourlyForecast.get(i).pop);
//            todaytem[i] = Integer.parseInt(DataManager.getWeatherInfo().hourlyForecast.get(i).tmp);
//        }
        result = getTadayWeatgerTip(condAnalysis(todayDayCond), condAnalysis(todayNightCond), popAnalysis(todayPop));
    }


    public String getTadayWeatgerTip(int day, int night, int pop) {
        switch (day) {
            case 1:
                result += "今天是个好天气哟，\n带您的植物出去晒晒太阳吧！";
                break;
            case 2:
                result += "今天是阴天呢，";
                switch (pop) {
                    case 1:
                        result += "今天不会下雨，\n可以带您的植物去透透风。";
                        break;
                    case 2:
                        result += "今天有小概率下雨，\n可以带您的植物去透透风。";
                        break;
                    case 3:
                        result += "今天可能下雨，\n可以带您的植物去透透风。";
                        break;
                    case 4:
                        result += "今天很有可能下雨，\n可以带您的植物去透透风。";
                        break;
                }
                break;
            case 3:
                result += "今天有小雨，\n带您的植物去洗个雨水澡吧~";
                break;
            case 4:
                result += "今天是大雨，\n记得把您的植物收回来哦~";
                break;
            case 5:
                result += "今天会下雪，\n记得把您的植物收回来哦~";
                break;
        }
        switch (night) {
            case 1:
                result += "\n" + "今晚天气一切正常，\n放心将您的植物留在外边吧";
                break;
            case 2:
                result += "\n" + "今晚天气一切正常，\n放心将您的植物留在外边吧";
                break;
            case 3:
                result += "\n" + "今晚有小雨，\n带您的植物去外边淋淋雨吧~";
                break;
            case 4:
                result += "\n" + "今晚有大雨，\n记得把您的植物收回来哦~";
                break;
            case 5:
                result += "\n" + "今晚会下雪，\n记得把您的植物收回来哦~";
                break;
        }
        return result;
    }

    //    1 晴天 多云
//    2 阴天
//    3 小雨
//    4 大雨
//    5 雪
    public int condAnalysis(int cond) {
        int x = 1;
        if (cond >= 100 && cond <= 103)
            x = 1;
        else if (cond == 104)
            x = 2;
        else if (cond >= 300 && cond <= 313) {
            x = 4;
            if (cond == 305 || cond == 309)
                x = 3;
        } else if (cond >= 400 && cond <= 407)
            x = 5;
        return x;
    }

    //    1 轻风
//    2 微风
//    3 强风
//    4 暴风
//    5 飓风
    public int windAnalysis(int wind) {
        int x = 1;
        if (wind >= 12 && wind <= 28)
            x = 2;
        else if (wind >= 29 && wind <= 49)
            x = 3;
        else if (wind >= 50 && wind <= 74)
            x = 4;
        else
            x = 5;
        return x;
    }

    //    1 不下雨
//    2 有小概率下雨
//    3 有可能下雨
//    4 很有可能下雨
    public int popAnalysis(int pop) {
        int x = 1;
        if (pop >= 5 && pop < 30)
            x = 2;
        else if (pop >= 30 && pop < 60)
            x = 3;
        else
            x = 4;
        return x;
    }

    //    10  35+
//    9  30-35
//    8  25-30
//    7  20-25
//    6  15-20
//    5  10-15
//    4  5-10
//    3  0-5
//    2  -5-0
//    1  -5--
    public int tmpGraded(int tmp) {
        int x = 5;
        if (tmp < -5)
            x = 1;
        else if (tmp >= -5 && tmp < 0)
            x = 2;
        else if (tmp >= 0 && tmp < 5)
            x = 3;
        else if (tmp >= 5 && tmp < 10)
            x = 4;
        else if (tmp >= 10 && tmp < 15)
            x = 5;
        else if (tmp >= 15 && tmp < 20)
            x = 6;
        else if (tmp >= 20 && tmp < 25)
            x = 7;
        else if (tmp >= 25 && tmp < 30)
            x = 8;
        else if (tmp >= 30 && tmp < 35)
            x = 9;
        else
            x = 10;
        return x;
    }

    public int DiffTmpAnalysis(int max, int min) {
        return tmpGraded(max - min);
    }

    public String getResult() {
        return result;
    }
}
