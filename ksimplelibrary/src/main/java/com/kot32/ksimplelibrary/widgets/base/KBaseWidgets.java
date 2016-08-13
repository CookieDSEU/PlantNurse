package com.kot32.ksimplelibrary.widgets.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by kot32 on 15/11/3.
 */
public abstract class KBaseWidgets extends LinearLayout {

    protected Context mContext;

    public KBaseWidgets(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public KBaseWidgets(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        initData();
        initView();
        initController();
    }

    public abstract void initData();

    public abstract void initView();

    public abstract void initController();

}

