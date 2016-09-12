package com.plantnurse.plantnurse.utils;
/**
 * create by Helois
 * 为了排序，对比每个植物名字的拼音，服务器不是排好了么？e
 */

import com.plantnurse.plantnurse.model.SortModel;

import java.util.Comparator;


public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
