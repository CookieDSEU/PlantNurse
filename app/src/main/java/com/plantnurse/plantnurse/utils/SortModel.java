package com.plantnurse.plantnurse.utils;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * create by Heloise
 * 排列条目的原型
 */

public class SortModel implements Serializable {

	private String name;
	private String sortLetters;
	private String iconUrl;
	private Bitmap iconBitmap;

	public SortModel(String name, String sortLetters, String iconUrl) {
		super();
		this.name = name;
		this.sortLetters = sortLetters;
		this.iconUrl = iconUrl;
	}

	public SortModel() {
		super();
	}

	//获取图片路径
	public String getIconUrl() {
		return iconUrl;
	}

	//设置路径
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
}
