package com.kot32.ksimplelibrary.fragment.t;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kot32.ksimplelibrary.util.tools.DisplayUtil;
import com.kot32.ksimplelibrary.widgets.view.KLoadingView;

/**
 * Created by kot32 on 15/11/4.
 */
@Deprecated
public class KLoadingFragment extends Fragment {

    private static KLoadingFragment instance;

    private KLoadingView loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingView = new KLoadingView(getActivity());
    }

    public static KLoadingFragment newInstance() {
        if (instance == null) {
            instance = new KLoadingFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout content=new RelativeLayout(getActivity());
        content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(DisplayUtil.dip2px(getActivity(),100),DisplayUtil.dip2px(getActivity(),100));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        loadingView.setLayoutParams(layoutParams);
        content.addView(loadingView);
        return content;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        loadingView.stop();
    }


}
