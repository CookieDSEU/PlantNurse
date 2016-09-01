package com.plantnurse.plantnurse.utils;

import android.graphics.Bitmap;

import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;

import java.io.Serializable;

/**
 * create by Heloise
 * 排列条目的原型
 */

public class SortModel implements Serializable {

	private String name;
	private String sortLetters;
	private Bitmap iconBitmap;
	private int resid;
	private int id;

	public SortModel() {
		super();
	}

	public void seticonBitmap(Bitmap bitmap)
	{
		this.iconBitmap=bitmap;
	}

	public Bitmap getIconBitmap()
	{
		return iconBitmap;
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
	public void setResid(int id){this.resid=id;}
	public int getResid(){return resid;}
	public void setId(int _id){this.id=_id;}
	public int getId(){return id;}
}
