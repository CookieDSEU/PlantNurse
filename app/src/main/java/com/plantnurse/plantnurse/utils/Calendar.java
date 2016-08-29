package com.plantnurse.plantnurse.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.plantnurse.plantnurse.R;

/**
 * Created by Eason_Tao on 2016/8/25.
 */
public class Calendar extends Dialog{
    Context context;
    public Calendar(Context context) {
        super(context);
        this.context = context;
    }
    public  Calendar(Context context,int theme){
        super(context,theme);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_calender);
    }
}
