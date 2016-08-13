package com.kot32.ksimplelibrary.activity.i;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kot32 on 15/11/4.
 */
public interface IBaseAction {

    /**
     * 需要继续加载网络数据并继续loading
     */
    int LOAD_NETWORK_DATA_AND_SHOW = 0;

    /**
     * 取消loading，在后台加载网络数据
     */
    int LOAD_NETWORK_DATA_AND_DISMISS = 1;

    /**
     * 不加载网络数据
     */
    int DONT_LOAD_NETWORK_DATA = 2;

    /**
     * 初始化本地数据，可以直接在此函数访问数据库，文件，不适合在此初始化 UI
     * @return 返回值代表不同LOADING画面显示策略，可根据在此拿到缓存数据判断是否为最新来决定返回值
     */
    int initLocalData();

    /**
     * 可以在此使用view.findViewById 进行View 的初始化,并且可在此根据初始化之后的本地数据进行 UI 更新
     * @param view
     */
    void initView(ViewGroup view);


    /**
     * 设置监听器等逻辑业务
     */
    void initController();

    /**
     * 加载网络数据操作，可以直接在此函数访问网络
     */
    void onLoadingNetworkData();

    /**
     * 加载网络数据完毕
     */
    void onLoadedNetworkData(View contentView);

    /**
     *
     * @return 直接返回布局的layout id 即可完成 inflate
     */
    int getContentLayoutID();
}
