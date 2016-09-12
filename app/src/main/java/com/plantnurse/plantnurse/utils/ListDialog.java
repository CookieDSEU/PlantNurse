package com.plantnurse.plantnurse.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.ListView;

import com.plantnurse.plantnurse.R;

/**
 * Created by Eason_Tao on 2016/8/26.
 */
public class ListDialog extends Dialog {
    private ListView listView;

    public ListDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_calender);
        listView = (ListView) findViewById(R.id.weather_listView);
    }

    public ListView getListView() {
        return listView;
    }
}
