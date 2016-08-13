package com.kot32.ksimplelibrary.manager.task.base;

import android.content.Context;
import android.util.Log;

import com.kot32.ksimplelibrary.network.NetworkExecutor;

import java.util.HashMap;

/**
 * Created by kot32 on 15/11/11.
 * 负责接受服务器地址、参数、Response 类的class，返回经过服务器返回的JSON 转换后的 Response对象
 */
public abstract class NetworkTask extends SimpleTask {

    private Class responseClass;

    private HashMap requestParams;

    private String serverURL;

    private Context context;

    private int requestType;

    public static int GET = 0;
    public static int POST = 1;


    public NetworkTask(String tag, Context context, Class resultClass, HashMap requestParams, String serverURL, int requestType) {
        super(tag);
        this.responseClass = resultClass;
        this.requestParams = requestParams;
        this.serverURL = serverURL;
        this.context = context;
        this.requestType = requestType;
    }

    @Override
    protected NetworkExecutor.NetworkResult doInBackground(Object[] params) {
        return NetworkExecutor.doRequestAndReturnObject(context, responseClass, serverURL, requestParams, requestType);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        NetworkExecutor.NetworkResult result = (NetworkExecutor.NetworkResult) o;
        if (result.isEmpty()) {
            Log.e("警告", "网络请求失败,原因:" + result.tips);
            onExecutedFailed(result);
        } else {
            onExecutedMission(result);
        }
    }

    public abstract void onExecutedMission(NetworkExecutor.NetworkResult result);

    public abstract void onExecutedFailed(NetworkExecutor.NetworkResult result);

}
